package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.inf.DexInfFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Resolve {

    private final ResolveType resolveType;
    private final ResolveValue resolveValue;
    private final ResolveFunction resolveFunction;

    public Resolve() {
        resolveValue = new ResolveValue(this);
        resolveFunction = new ResolveFunction(this);
        resolveType = new ResolveType(this);
    }

    public void define(DexFile file) {
        for (DexRootDecl rootDecl : file.rootDecls()) {
            if (rootDecl.function() != null) {
                define(rootDecl.function());
            } else if (rootDecl.inf() != null) {
                define(rootDecl.inf());
            }
        }
    }

    public void define(DexFunction function) {
        resolveFunction.define(function);
        resolveType.define(function);
    }


    public void define(DexInterface inf) {
        Denotation.InterfaceType infType = new Denotation.InterfaceType(this, inf);
        resolveType.define(infType);
        for (Denotation.FunctionType functionType : infType.members()) {
            resolveFunction.define(functionType);
        }
    }

    public void define(Denotation.FunctionType functionType) {
        resolveFunction.define(functionType);
    }

    @NotNull
    public List<Denotation.Function> resolveFunctions(DexNewExpr newExpr) {
        return resolveFunction.resolveFunctions(newExpr);
    }

    @NotNull
    public List<Denotation.Function> resolveFunctions(DexInfFunction infFunction) {
        return resolveFunction.resolveFunctions(infFunction);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexFunctionCallExpr callExpr) {
        return resolveFunction.resolveFunctions(callExpr);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexMethodCallExpr callExpr) {
        return resolveFunction.resolveFunctions(callExpr);
    }

    @NotNull
    public Denotation resolveValue(DexReference ref) {
        return resolveValue.resolveValue(ref);
    }

    @NotNull
    public Denotation.Type resolveType(DexExpr expr) {
        return resolveFunction.resolveType(expr);
    }

    @NotNull
    public Denotation.Type resolveType(String name) {
        return resolveType(new DexReference(name));
    }

    @NotNull
    public Denotation.Type resolveType(DexReference ref) {
        return resolveType.resolveType(ref);
    }

    @NotNull
    public Denotation.Type resolveType(DexFunction function) {
        return resolveFunction.resolveType(function);
    }

    public boolean canProvide(String functionName, List<Denotation.Type> params, Denotation.Type ret) {
        return resolveFunction.canProvide(functionName, params, ret);
    }
}
