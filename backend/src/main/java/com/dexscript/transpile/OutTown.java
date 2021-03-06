package com.dexscript.transpile;

import com.dexscript.pkg.ImportPackage;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.shim.OutShim;
import com.dexscript.type.composite.ActorType;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.type.core.FunctionType;
import com.dexscript.type.core.TypeSystem;
import org.mdkt.compiler.InMemoryJavaCompiler;

public class OutTown {

    public interface OnSourceAdded {
        void handle(String className, String classSrc);
    }

    public static OnSourceAdded ON_SOURCE_ADDED = (className, classSrc) -> {
    };

    private final InMemoryJavaCompiler compiler = InMemoryJavaCompiler
            .newInstance()
            .ignoreWarnings();
    private final TypeSystem ts = new TypeSystem();
    private final OutShim oShim = new OutShim(ts);

    public OutTown importPackage(String path) {
        oShim.importPackage(path);
        return this;
    }

    public Class transpile() {
        for (ActorType actor : oShim.actors()) {
            OutTopLevelClass oClass = new OutTopLevelClass(ts, oShim, actor.elem());
            addSource(oClass.qualifiedClassName(), oClass.toString());
            ensureActorImplLoaded(actor);
        }
        try {
            addSource(OutShim.QUALIFIED_CLASSNAME, oShim.finish());
            return compiler.compileAll().get(OutShim.QUALIFIED_CLASSNAME);
        } catch (Exception e) {
            throw new DexRuntimeException(e);
        }
    }

    private static void ensureActorImplLoaded(ActorType actor) {
        for (FunctionType func : actor.functions()) {
            if (!func.hasImpl()) {
                continue;
            }
            func.impl();
        }
    }

    public void addSource(String className, String classSrc) {
        try {
            compiler.addSource(className, classSrc);
            ON_SOURCE_ADDED.handle(className, classSrc);
        } catch (Exception e) {
            throw new DexRuntimeException(e);
        }
    }

    public OutShim oShim() {
        return oShim;
    }
}
