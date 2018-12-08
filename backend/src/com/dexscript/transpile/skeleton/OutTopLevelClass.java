package com.dexscript.transpile.skeleton;

import com.dexscript.ast.DexFunction;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class OutTopLevelClass implements OutClass {

    private final TypeSystem ts;
    private final DexFunction iFunc;
    private final Gen g = new Gen();
    private final OutFields oFields = new OutFields();
    private final OutShim oShim;
    private final OutStateMachine stateMachine = new OutStateMachine();
    private OutMethod oMethod;

    public OutTopLevelClass(TypeSystem ts, OutShim oShim, DexFunction iFunc) {
        this.ts = ts;
        this.oShim = oShim;
        this.iFunc = iFunc;
        g.__("package "
        ).__(packageName()
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__(new Line("import com.dexscript.runtime.gen__.*;"));
        g.__("public class "
        ).__(className()
        ).__(" extends Actor {"
        ).__(new Indent(() -> {
            new OutInitMethod(this, iFunc);
            g.__(oMethod.finish());
            stateMachine.genResumeMethods(g);
            genFields();
        }));
        g.__(new Line("}"));
    }

    public static String qualifiedClassNameOf(DexFunction iFunction) {
        String packageName = iFunction.file().packageClause().identifier().toString();
        return packageName + "." + iFunction.actorName();
    }

    private void genFields() {
        for (OutField oField : oFields) {
            g.__("private "
            ).__(oField.type().javaClassName()
            ).__(' '
            ).__(oField.value()
            ).__(new Line(";"));
        }
    }

    public String packageName() {
        return iFunc.file().packageClause().identifier().toString();
    }

    public String className() {
        return iFunc.actorName();
    }

    public String qualifiedClassName() {
        return packageName() + "." + className();
    }

    @Override
    public String toString() {
        return g.toString();
    }

    public OutField allocateField(String name, Type type) {
        return oFields.allocate(name, type);
    }

    public String indention() {
        return g.indention();
    }

    public TypeSystem typeSystem() {
        return ts;
    }

    public DexFunction iFunc() {
        return iFunc;
    }

    public Gen g() {
        return oMethod.g();
    }

    @Override
    public OutStateMachine oStateMachine() {
        return stateMachine;
    }

    public void changeMethod(OutMethod oMethod) {
        if (this.oMethod != null) {
            g.__(this.oMethod.finish());
        }
        this.oMethod = oMethod;
    }

    public OutShim oShim() { return oShim; }
}
