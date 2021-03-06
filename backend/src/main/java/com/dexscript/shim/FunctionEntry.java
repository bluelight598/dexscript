package com.dexscript.shim;

import com.dexscript.gen.*;
import com.dexscript.shim.gen.DeclareParams;
import com.dexscript.shim.gen.InvokeParams;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.shim.java.JavaTypes;

import java.util.List;
import java.util.Objects;

public class FunctionEntry {

    private final String funcName;
    private final int paramsCount;

    public FunctionEntry(String funcName, int paramsCount) {
        this.funcName = funcName;
        this.paramsCount = paramsCount;
    }

    public void gen(Gen g, List<FunctionImpl> impls, JavaTypes javaTypes) {
        for (FunctionImpl impl : impls) {
            impl.canF(javaTypes);
            impl.callF();
        }
        g.__("public static Object "
        ).__(funcName);
        DeclareParams.$(g, paramsCount, false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__(new Line("Scheduler scheduler = new Scheduler();"));
            g.__("Object arg"
            ).__(paramsCount // the context
            ).__(new Line(" = null;"));
            for (FunctionImpl impl : impls) {
                g.__("if ("
                ).__(impl.canF(javaTypes));
                InvokeParams.$(g, paramsCount + 1, false);
                g.__(") {");
                g.__(new Indent(() -> {
                    g.__("Promise result = (Promise)"
                    ).__(impl.callF());
                    InvokeParams.$(g, paramsCount + 1, true);
                    g.__(new Line(";"));
                    g.__(new Line("scheduler.schedule();"));
                    g.__(new Line("if (!result.finished()) {"));
                    g.__(new Line("  throw new DexRuntimeException(\"function never return\");"));
                    g.__(new Line("}"));
                    g.__(new Line("return result.value();"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new DexRuntimeException();"));
        }));
        g.__(new Line("}"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionEntry that = (FunctionEntry) o;
        return paramsCount == that.paramsCount &&
                Objects.equals(funcName, that.funcName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, paramsCount);
    }

    @Override
    public String toString() {
        return "FunctionEntry{" +
                "funcName='" + funcName + '\'' +
                ", paramsCount=" + paramsCount +
                '}';
    }
}
