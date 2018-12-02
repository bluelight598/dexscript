package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexBlockTest {

    @Test
    public void empty() {
        Assert.assertEquals("{}", new DexBlock("{}").toString());
    }

    @Test
    public void one_statement() {
        DexBlock blk = new DexBlock("{hello()}");
        Assert.assertEquals("{hello()}", blk.toString());
        Assert.assertEquals(1, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
    }

    @Test
    public void statements_separated_by_new_line() {
        DexBlock blk = new DexBlock("{hello()\nworld()}");
        Assert.assertEquals("{hello()\nworld()}", blk.toString());
        Assert.assertEquals(2, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
        Assert.assertEquals("world()", blk.stmts().get(1).toString());
    }

    @Test
    public void statements_separated_by_semicolon() {
        DexBlock blk = new DexBlock("{hello();world()}");
        Assert.assertEquals("{hello();world()}", blk.toString());
        Assert.assertEquals(2, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
        Assert.assertEquals("world()", blk.stmts().get(1).toString());
    }

    @Test
    public void walk_up() {
        DexBlock blk = new DexBlock("{return abc; return def}");
        Assert.assertEquals("{return abc; return def}", blk.stmts().get(0).prev().toString());
        Assert.assertEquals("return abc", blk.stmts().get(1).prev().toString());
    }

    @Test
    public void recover_from_invalid_statement_by_line_end() {
        String src = "" +
                "{\n" +
                "??\n" +
                "return abc\n" +
                "}";
        DexBlock blk = new DexBlock(src);
        Assert.assertEquals("{\n" +
                "<error/>??\n" +
                "return abc\n" +
                "}", blk.toString());
        Assert.assertEquals("<error/>", blk.stmts().get(0).toString());
        Assert.assertEquals("return abc", blk.stmts().get(1).toString());
    }

    @Test
    public void recover_from_last_invalid_statement() {
        String src = "" +
                "{\n" +
                "return abc;??" +
                "}xyz";
        DexBlock blk = new DexBlock(src);
        Assert.assertEquals("{\n" +
                "return abc;<error/>??}", blk.toString());
        Assert.assertEquals("return abc", blk.stmts().get(0).toString());
        Assert.assertEquals("<error/>", blk.stmts().get(1).toString());
    }
}