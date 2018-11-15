package com.dexscript.transpiler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TranspilerTest {

    private Transpiler transpiler;

    @Before
    public void setup() {
        transpiler = new Transpiler();
    }

    @After
    public void teardown() {
        transpiler.close();
    }

    @Test
    public void testReturnStringLiteral() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "function hello(): string {\n" +
                "   return 'hello'\n" +
                "}\n");
    }

    @Test
    public void testFunctionCall() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "function hello(): string {\n" +
                "   return world()\n" +
                "}\n" +
                "function world(): string {\n" +
                "   return 'hello'\n" +
                "}\n");
    }

    @Test
    public void testPlus() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "function hello(): int64 {\n" +
                "   return 1+2\n" +
                "}\n");
    }

    @Test
    public void testAssignment() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "function hello() {\n" +
                "   val := 'hello'\n" +
                "}\n");
    }

    @Test
    public void testGetResult() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "\n" +
                "function hello(): string {\n" +
                "    w := world{}\n" +
                "    return <- w\n" +
                "}\n" +
                "\n" +
                "function world(): string {\n" +
                "    return 'hello'\n" +
                "}");
    }

    @Test
    public void testAwait() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "\n" +
                "function Hello(): string {\n" +
                "    w := World{}\n" +
                "    return w.Say()\n" +
                "}\n" +
                "\n" +
                "function World() {\n" +
                "    await {\n" +
                "    -> Say(): string {\n" +
                "        return 'hello'\n" +
                "    }}\n" +
                "}");
    }
}