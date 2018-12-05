package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class MultiDispatchTest {

    @Test
    public void dispatch_to_compatible_type() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return World('hello world')\n" +
                "}\n" +
                "function World(msg: int64): int64 {\n" +
                "   return msg\n" +
                "}\n" +
                "function World(msg: string): string {\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello world", result);
    }

    @Test
    public void dispatch_to_runtime_matched_type() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return World('world')\n" +
                "}\n" +
                "function World(msg: 'world'): string {\n" +
                "   return 'yes, world'\n" +
                "}\n" +
                "function World(msg: string): string {\n" +
                "   return 'no, no'\n" +
                "}");
        Assert.assertEquals("yes, world", result);
    }
}
