package com.dexscript.transpile;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class AssignTest {

    @Test
    public void assign_string_const_type_to_literal_type() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_string_const_type_to_string_type() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_bool_const_type_to_literal_type() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_bool_const_type_to_bool() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_integer_const_type_to_literal_type() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_integer_const_type_to_int64() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_integer_const_type_to_int32() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_integer_const_type_to_float64() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_integer_const_type_to_float32() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_float_const_type_to_float64() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void assign_float_const_type_to_float32() {
        TestFramework.assertByList(TestTranspile::$);
    }
}
