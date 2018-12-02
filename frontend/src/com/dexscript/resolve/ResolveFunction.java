package com.dexscript.resolve;

import com.dexscript.ast.*;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.inf.DexInfFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dexscript.resolve.Denotation.FunctionType.returnTypeOf;

final class ResolveFunction {

    private final Map<String, List<Denotation.FunctionType>> defined = new HashMap<>();
    private Resolve resolve;

    void setResolve(Resolve resolve) {
        this.resolve = resolve;
    }

    public void declare(DexFunction function) {
        String functionName = function.identifier().toString();
        List<Denotation.FunctionType> types = defined.computeIfAbsent(functionName, k -> new ArrayList<>());
        List<Denotation.Type> args = new ArrayList<>();
        for (DexParam param : function.sig().params()) {
            args.add(resolve.resolveType(param.paramType()));
        }
        Denotation.Type ret = resolve.resolveType(function.sig().ret());
        types.add(new Denotation.FunctionType(functionName, function, args, ret));
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexNewExpr newExpr) {
        DexReference ref = newExpr.target().asRef();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : newExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunctions(newExpr, functionName, argTypes);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexFunctionCallExpr callExpr) {
        DexReference ref = callExpr.target().asRef();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : callExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunctions(callExpr, functionName, argTypes);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexMethodCallExpr callExpr) {
        DexReference ref = callExpr.method();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(callExpr.obj()));
        for (DexExpr arg : callExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunctions(callExpr, functionName, argTypes);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexAddExpr addExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(addExpr.left()));
        argTypes.add(resolve.resolveType(addExpr.right()));
        return resolveFunctions(addExpr, "Add__", argTypes);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexConsumeExpr consumeExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(consumeExpr.right()));
        return resolveFunctions(consumeExpr, "Consume__", argTypes);
    }

    private List<Denotation.FunctionType> resolveFunctions(DexElement elem, String functionName, List<Denotation.Type> paramTypes) {
        List<Denotation.FunctionType> candidates = defined.get(functionName);
        if (candidates == null) {
            return new ArrayList<>();
        }
        List<Denotation.FunctionType> resolved = new ArrayList<>();
        for (Denotation.FunctionType candidate : candidates) {
            if (signatureMatch(paramTypes, candidate.params())) {
                resolved.add(candidate);
            }
        }
        return resolved;
    }

    private boolean signatureMatch(List<Denotation.Type> argTypes, List<Denotation.Type> paramTypes) {
        if (paramTypes.size() != argTypes.size()) {
            return false;
        }
        for (int i = 0; i < paramTypes.size(); i++) {
            Denotation.Type arg = paramTypes.get(i);
            if (!arg.isAssignableFrom(argTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean canProvide(String functionName, List<Denotation.Type> params, Denotation.Type ret) {
        List<Denotation.FunctionType> candidates = defined.get(functionName);
        if (candidates == null) {
            return false;
        }
        for (Denotation.FunctionType candidate : candidates) {
            if (candidate.canProvide(functionName, params, ret)) {
                return true;
            }
        }
        return false;
    }

    public void declare(Denotation.FunctionType functionType) {
        List<Denotation.FunctionType> functionTypes = defined.computeIfAbsent(
                functionType.name(), k -> new ArrayList<>());
        functionTypes.add(functionType);
    }

    public List<Denotation.FunctionType> resolveFunctions(DexInfFunction infFunction) {
        List<Denotation.FunctionType> candidates = defined.get(infFunction.identifier().toString());
        List<Denotation.Type> paramTypes = new ArrayList<>();
        for (DexParam param : infFunction.sig().params()) {
            Denotation.Type paramType = resolve.resolveType(param.paramType());
            paramTypes.add(paramType);
        }
        List<Denotation.FunctionType> impls = new ArrayList<>();
        for (Denotation.FunctionType candidate : candidates) {
            if (!candidate.isImpl()){
                continue;
            }
            if (signatureMatch(candidate.params(), paramTypes)) {
                impls.add(candidate);
            }
        }
        return impls;
    }

    public Denotation.Type resolveType(DexFunction function) {
        Denotation.FunctionInterfaceType type = function.attachmentOfType(Denotation.FunctionInterfaceType.class);
        if (type != null) {
            return type;
        }
        type = new Denotation.FunctionInterfaceType(resolve, function);
        function.attach(type);
        for (Denotation.FunctionType member : type.members()) {
            declare(member);
        }
        return type;
    }

    public Denotation.Type resolveType(DexExpr expr) {
        Denotation.Type denotation = expr.attachmentOfType(Denotation.Type.class);
        if (denotation != null) {
            return denotation;
        }
        denotation = _resolveType(expr);
        expr.attach(denotation);
        return denotation;
    }

    private Denotation.Type _resolveType(DexExpr expr) {
        if (expr instanceof DexIntegerLiteral) {
            return BuiltinTypes.INT64_TYPE;
        }
        if (expr instanceof DexFloatLiteral) {
            return BuiltinTypes.FLOAT64_TYPE;
        }
        if (expr instanceof DexStringLiteral) {
            return BuiltinTypes.STRING_TYPE;
        }
        if (expr instanceof DexReference) {
            return eval((DexReference)expr);
        }
        if (expr instanceof DexParenExpr) {
            return resolveType(((DexParenExpr)expr).body());
        }
        if (expr instanceof DexFunctionCallExpr) {
            return returnTypeOf(resolveFunctions((DexFunctionCallExpr)expr));
        }
        if (expr instanceof DexMethodCallExpr) {
            return returnTypeOf(resolveFunctions((DexMethodCallExpr)expr));
        }
        if (expr instanceof DexAddExpr) {
            return returnTypeOf(resolveFunctions((DexAddExpr)expr));
        }
        if (expr instanceof DexNewExpr) {
            return eval((DexNewExpr)expr);
        }
        if (expr instanceof DexConsumeExpr) {
            return returnTypeOf(resolveFunctions((DexConsumeExpr)expr));
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexReference expr) {
        Denotation refObj = resolve.resolveValue(expr);
        if (refObj instanceof Denotation.Value) {
            Denotation.Value ref = (Denotation.Value) refObj;
            return (Denotation.Type) ref.type();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexNewExpr newExpr) {
        List<Denotation.FunctionType> functions = resolveFunctions(newExpr);
        if (functions.size() == 0) {
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        if (functions.size() == 1) {
            DexFunction definedBy = (DexFunction) functions.get(0).definedBy();
            return resolveType(definedBy);
        }
        throw new UnsupportedOperationException("not implemented: intersection type of multiple functions");
    }
}