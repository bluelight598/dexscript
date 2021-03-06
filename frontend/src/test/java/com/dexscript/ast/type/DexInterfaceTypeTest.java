package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexInterfaceTypeTest {

    @Test
    public void empty_interface() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }

    @Test
    public void compact_empty_interface() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }

    @Test
    public void one_function() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }

    @Test
    public void one_method() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }

    @Test
    public void function_and_method() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }

    @Test
    public void invalid_method_name() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }

    @Test
    public void interface_with_name() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }

    @Test
    public void one_field() {
        TestFramework.assertParsedAST(DexAmsInfType::$);
    }
}
