package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class FunctionTypeTest {

    @Test
    public void assignable() {
        FunctionType hello1 = new FunctionType("hello", new ArrayList<>(), BuiltinTypes.STRING);
        FunctionType hello2 = new FunctionType("hello", new ArrayList<>(), BuiltinTypes.STRING);
        Assert.assertTrue(hello1.isAssignableFrom(hello2));
        Assert.assertTrue(hello2.isAssignableFrom(hello1));
    }

    @Test
    public void name_not_assignable() {
        Assert.assertFalse(new FunctionType("hello", new ArrayList<>(), BuiltinTypes.STRING).isAssignableFrom(
                new FunctionType("world", new ArrayList<>(), BuiltinTypes.STRING)));
    }

    @Test
    public void params_count_not_assignable() {
        Assert.assertFalse(new FunctionType("hello", new ArrayList<>(), BuiltinTypes.STRING).isAssignableFrom(
                new FunctionType("hello", new ArrayList<>() {{
                    add(BuiltinTypes.STRING);
                }}, BuiltinTypes.STRING)));
    }

    @Test
    public void params_not_assignable() {
        Assert.assertFalse(new FunctionType("hello", new ArrayList<>() {{
            add(BuiltinTypes.VOID);
        }}, BuiltinTypes.STRING).isAssignableFrom(
                new FunctionType("hello", new ArrayList<>() {{
                    add(BuiltinTypes.STRING);
                }}, BuiltinTypes.STRING)));
    }

    @Test
    public void ret_not_assignable() {
        Assert.assertFalse(new FunctionType("hello", new ArrayList<>(), BuiltinTypes.STRING).isAssignableFrom(
                new FunctionType("hello", new ArrayList<>(), BuiltinTypes.VOID)));
    }

    @Test
    public void param_is_sub_type() {
        FunctionType hello1 = new FunctionType("hello", new ArrayList<>() {{
            add(BuiltinTypes.STRING);
        }}, BuiltinTypes.VOID);
        FunctionType hello2 = new FunctionType("hello", new ArrayList<>() {{
            add(new StringLiteralType("abc"));
        }}, BuiltinTypes.VOID);
        Assert.assertFalse(hello1.isAssignableFrom(hello2));
        Assert.assertTrue(hello2.isAssignableFrom(hello1));
    }

    @Test
    public void ret_is_sub_type() {
        FunctionType hello1 = new FunctionType("hello", new ArrayList<>(), BuiltinTypes.STRING);
        FunctionType hello2 = new FunctionType("hello", new ArrayList<>(), new StringLiteralType("abc"));
        Assert.assertTrue(hello1.isAssignableFrom(hello2));
        Assert.assertFalse(hello2.isAssignableFrom(hello1));
    }
}