package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAssignStmtTest {

    @Test
    public void one_target() {
        TestFramework.assertParsedAST(DexAssignStmt::$);
    }

    @Test
    public void two_targets() {
        TestFramework.assertParsedAST(DexAssignStmt::$);
    }

    @Test
    public void missing_expr() {
        TestFramework.assertParsedAST(DexAssignStmt::$);
    }

    @Test
    public void target_is_index_expr() {
        TestFramework.assertParsedAST(DexAssignStmt::$);
    }
}
