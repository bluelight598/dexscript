package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexAddExpr;
import com.dexscript.ast.expr.DexCallExpr;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ResolveFunction {

    private final Map<String, List<Denotation.Type>> defined = new HashMap<>();
    private ResolveType resolveType;

    void setResolveType(ResolveType resolveType) {
        this.resolveType = resolveType;
    }

    public void define(DexFile file) {
        for (DexRootDecl rootDecl : file.rootDecls()) {
            if (rootDecl instanceof DexFunction) {
                define((DexFunction) rootDecl);
            }
        }
    }

    public void define(DexFunction function) {
        String functionName = function.identifier().toString();
        List<Denotation.Type> types = defined.computeIfAbsent(functionName, k -> new ArrayList<>());
        List<Denotation.Type> args = new ArrayList<>();
        for (DexParam param : function.sig().params()) {
            Denotation typeObj = resolveType.resolveType(param.paramType());
            if (!(typeObj instanceof Denotation.Type)) {
                return;
            }
            Denotation.Type arg = (Denotation.Type) typeObj;
            args.add(arg);
        }
        Denotation.Type ret = (Denotation.Type) resolveType.resolveType(function.sig().ret());
        types.add(Denotation.function(functionName, function, args, ret));
    }

    @NotNull
    public Denotation resolveFunction(DexCallExpr callExpr) {
        DexReference ref = callExpr.target().asRef();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : callExpr.args()) {
            argTypes.add((Denotation.Type) resolveType.resolveType(arg));
        }
        return resolveFunction(callExpr, functionName, argTypes);
    }

    @NotNull
    public Denotation resolveFunction(DexAddExpr addExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add((Denotation.Type) resolveType.resolveType(addExpr.left()));
        argTypes.add((Denotation.Type) resolveType.resolveType(addExpr.right()));
        return resolveFunction(addExpr, "Add__", argTypes);
    }

    private Denotation resolveFunction(DexElement elem, String functionName, List<Denotation.Type> argTypes) {
        List<Denotation.Type> candidates = defined.get(functionName);
        if (candidates == null) {
            return new Denotation.Error(functionName, elem, "can not resolve " + functionName + " to a function");
        }
        for (Denotation.Type candidate : candidates) {
            if (signatureMatch(argTypes, candidate)) {
                return candidate;
            }
        }
        return new Denotation.Error(functionName, elem, "can not resolve " + functionName + " to a function");
    }

    private boolean signatureMatch(List<Denotation.Type> argTypes, Denotation.Type candidate) {
        if (candidate.args().size() != argTypes.size()) {
            return false;
        }
        for (int i = 0; i < candidate.args().size(); i++) {
            Denotation.Type arg = candidate.args().get(i);
            if (!arg.isAssignableFrom(argTypes.get(i))) {
                return false;
            }
        }
        return true;
    }
}
