package com.dexscript.type;

public class AnyType implements DType {

    private final TypeSystem ts;

    public AnyType(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return true;
    }

    @Override
    public boolean _isSubType(IsAssignable ctx, DType that) {
        return true;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "interface{}";
    }
}
