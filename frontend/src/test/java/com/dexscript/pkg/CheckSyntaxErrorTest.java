package com.dexscript.pkg;

import com.dexscript.ast.DexFile;
import org.junit.Assert;
import org.junit.Test;

public class CheckSyntaxErrorTest {

    @Test
    public void no_error() {
        CheckSyntaxError result = new CheckSyntaxError(DexFile.$("function hello() {}"));
        Assert.assertFalse(result.hasError());
    }

    @Test
    public void has_error() {
        CheckSyntaxError result = new CheckSyntaxError(DexFile.$("function hello() }"));
        Assert.assertTrue(result.hasError());
    }
}
