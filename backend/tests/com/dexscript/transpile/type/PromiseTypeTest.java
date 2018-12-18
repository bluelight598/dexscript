package com.dexscript.transpile.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PromiseTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new OutShim(new TypeSystem()).typeSystem();
    }

    @Test
    public void consume_any() {
        DType promiseType = ResolveType.$(ts, "Promise");
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        IsAssignable isAssignable = new IsAssignable(promiseType, inf);
        Assert.assertTrue(isAssignable.result());
    }

    @Test
    public void consume_string() {
        DType promiseType = ResolveType.$(ts, "Promise<string>");
        InterfaceType consumeString = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(consumeString, promiseType));
        InterfaceType consumeInt64 = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Consume__(): int64\n" +
                "}"));
        Assert.assertFalse(IsAssignable.$(consumeInt64, promiseType));
    }
}