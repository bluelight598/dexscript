package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {

    private final Map<String, List<FunctionType>> defined = new HashMap<>();
    private final List<FunctionsProvider> providers = new ArrayList<>();

    public void define(FunctionType func) {
        List<FunctionType> functions = defined.computeIfAbsent(func.name(), k -> new ArrayList<>());
        functions.add(func);
    }

    public List<FunctionType> resolve(String funcName, List<Type> args) {
        pullFromProviders();
        List<FunctionType> functions = defined.get(funcName);
        if (functions == null) {
            return new ArrayList<>();
        }
        List<FunctionType> resolved = new ArrayList<>();
        for (FunctionType function : functions) {
            if (function.params().size() != args.size()) {
                continue;
            }
            if (matchSig(args, function.params())) {
                resolved.add(function);
            }
        }
        return resolved;
    }

    private boolean matchSig(@NotNull List<Type> args, @NotNull List<Type> params) {
        for (int i = 0; i < params.size(); i++) {
            Type param = params.get(i);
            Type arg = args.get(i);
            boolean argMatched = arg.isAssignableFrom(param) || param.isAssignableFrom(arg);
            if (!argMatched) {
                return false;
            }
        }
        return true;
    }

    public void lazyDefine(FunctionsProvider provider) {
        providers.add(provider);
    }

    public boolean isDefined(Substituted substituted, FunctionType that) {
        pullFromProviders();
        List<FunctionType> functions = defined.get(that.name());
        if (functions == null) {
            return false;
        }
        Substituted staging = new Substituted(substituted);
        for (FunctionType function : functions) {
            if (that.isAssignableFrom(staging, function)) {
                staging.commit();
                return true;
            } else {
                staging.rollback();
            }
        }
        return false;
    }

    private void pullFromProviders() {
        while (!(providers.isEmpty())) {
            ArrayList<FunctionsProvider> toPull = new ArrayList<>(providers);
            providers.clear();
            for (FunctionsProvider provider : toPull) {
                for (FunctionType function : provider.functions()) {
                    define(function);
                }
            }
        }
    }

    public boolean isAssignableFrom(Substituted substituted, FunctionsProvider superType, Type subType) {
        if (substituted.get((NamedType) superType) != null) {
            boolean result = substituted.get((NamedType) superType).equals(subType);
            return result;
        }
        substituted.put((NamedType) superType, subType);
        for (FunctionType member : superType.functions()) {
            if (!isDefined(substituted, member)) {
                return false;
            }
        }
        return true;
    }
}
