package com.dexscript.transpile.skeleton;

import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class OutInnerClass implements OutClass {

    private final OutShim oShim;
    private final OutClass oOuterClass;
    private final Gen g;
    private final OutStateMachine oStateMachine = new OutStateMachine();
    private OutMethod oMethod;

    public OutInnerClass(OutClass oOuterClass, DexAwaitConsumer iAwaitConsumer) {
        this.oShim = oOuterClass.oShim();
        this.oOuterClass = oOuterClass;
        g = new Gen(oOuterClass.indention());
        g.__("public class "
        ).__(iAwaitConsumer.identifier().toString()
        ).__(" extends Actor {");
        g.__(new Indent(() -> {
            new OutInitMethod(this, iAwaitConsumer);
            g.__(oMethod.finish());
            oStateMachine.genResumeMethods(g);
        }));
        g.__(new Line("}"));
    }

    @Override
    public TypeSystem typeSystem() {
        return null;
    }

    @Override
    public void changeMethod(OutMethod oMethod) {
        if (this.oMethod != null) {
            g.__(this.oMethod.finish());
        }
        this.oMethod = oMethod;
    }

    @Override
    public String indention() {
        return g.indention();
    }

    @Override
    public OutField allocateField(String fieldName, Type fieldType) {
        return oOuterClass.allocateField(fieldName, fieldType);
    }

    @Override
    public OutShim oShim() {
        return oShim;
    }

    @Override
    public Gen g() {
        return oMethod.g();
    }

    @Override
    public OutStateMachine oStateMachine() {
        return oStateMachine;
    }

    @Override
    public String toString() {
        return g.toString();
    }
}
