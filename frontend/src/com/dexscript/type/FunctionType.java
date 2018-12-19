package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Function DType: test type compatibility, more permissive than signature
// Function Signature: generic type constraint & where condition
public final class FunctionType implements DType {

    private String description;

    private final TypeSystem ts;

    @NotNull
    private final String name;

    @NotNull
    private final List<DType> params;

    @NotNull
    private final DType ret;

    @NotNull
    private final FunctionSig sig;

    private Object impl;

    private FunctionImplProvider implProvider;

    public FunctionType(TypeSystem ts, @NotNull String name, @NotNull List<DType> params, @NotNull DType ret) {
        this(ts, name, params, ret, null);
    }

    public FunctionType(TypeSystem ts, @NotNull String name, @NotNull List<DType> params, @NotNull DType ret, FunctionSig sig) {
        this.ts = ts;
        this.name = name;
        this.params = params;
        this.ret = ret;
        if (sig == null) {
            sig = new FunctionSig(ts, params, ret);
        }
        sig.reparent(this);
        this.sig = sig;
        ts.defineFunction(this);
    }

    public void setImplProvider(FunctionImplProvider implProvider) {
        this.implProvider = implProvider;
    }

    public final Object impl() {
        if (impl == null) {
            impl = implProvider.implOf(this);
        }
        return impl;
    }

    @NotNull
    public final String name() {
        return name;
    }

    @NotNull
    public List<DType> params() {
        return params;
    }

    @NotNull
    public DType ret() {
        return ret;
    }

    @NotNull
    public FunctionSig sig() {
        return sig;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType thatObj) {
        if (!(thatObj instanceof FunctionType)) {
            return false;
        }
        FunctionType that = (FunctionType) thatObj;
        if (!this.name.equals(that.name)) {
            ctx.addLog("function name not equal", "to", this.name, "from", that.name);
            return false;
        }
        if (this.params.size() != that.params.size()) {
            ctx.addLog("params count not equal", "to", this.params().size(), "from", that.params.size());
            return false;
        }
        for (int i = 0; i < params.size(); i++) {
            DType thisParam = this.params.get(i);
            DType thatParam = that.params.get(i);
            if (!new IsAssignable(ctx, "#" + i + " param", thatParam, thisParam).result()) {
                return false;
            }
        }
        if (!new IsAssignable(ctx, "ret", this.ret, that.ret).result()) {
            return false;
        }
        return true;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        if (description != null) {
            return description;
        }
        description = name + sig.toString();
        return description;
    }

    public FunctionImplProvider implProvider() {
        return implProvider;
    }

    public boolean hasImpl() {
        return implProvider != null;
    }
}
