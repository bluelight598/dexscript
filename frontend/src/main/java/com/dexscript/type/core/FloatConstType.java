package com.dexscript.type.core;

import com.dexscript.ast.expr.DexFloatConst;

import java.util.Objects;

class FloatConstType implements DType {

    static {
        InferType.register(DexFloatConst.class, (ts, localTypeTable, elem) -> ts.constOfFloat(elem.toString()));
    }

    private final TypeSystem ts;
    private final String val;

    public FloatConstType(TypeSystem ts, String val) {
        this.ts = ts;
        this.val = val;
    }

    public static void init() {
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "(const)" + val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatConstType that = (FloatConstType) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, val);
    }

    public String constValue() {
        return val;
    }
}
