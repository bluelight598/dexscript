package com.dexscript.parser2.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexReturnStmtTest {

    @Test
    public void matched() {
        Assert.assertEquals("return abc", new DexReturnStmt(" return abc").toString());
    }

    @Test
    public void missing_expr_recover_by_file_end() {
        Assert.assertEquals("return <error/>", new DexReturnStmt("return ").toString());
    }

    @Test
    public void missing_expr_recover_by_line_end() {
        Assert.assertEquals("return <error/>", new DexReturnStmt("return ; abc").toString());
    }

    @Test
    public void return_without_space() {
        Assert.assertEquals("<unmatched>returnabc</unmatched>", new DexReturnStmt("returnabc").toString());
    }
}
