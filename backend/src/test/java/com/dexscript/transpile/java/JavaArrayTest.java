package com.dexscript.transpile.java;

import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.Transpile;
import org.junit.Assert;
import org.junit.Test;

public class JavaArrayTest {

    public static class Class1 {
        public static String[] newArray() {
            return new String[10];
        }
    }

    @Test
    public void return_array() {
        OutTown oTown = new OutTown();
        oTown.oShim().importJavaFunctions(Class1.class);
        Object ret = Transpile.$(oTown, "", "" +
                "function Hello(): interface{} {\n" +
                "   return newArray()\n" +
                "}");
        Assert.assertEquals(10, ((String[]) ret).length);
    }

    public static class Class2 {
        public static String[] newArray() {
            return new String[]{"hello"};
        }
    }

    @Test
    public void get_element_from_array() {
        OutTown oTown = new OutTown();
        oTown.oShim().importJavaFunctions(Class2.class);
        String ret = (String) Transpile.$(oTown, "", "" +
                "function Hello(): string {\n" +
                "   arr := newArray()\n" +
                "   return arr[0]\n" +
                "}");
        Assert.assertEquals("hello", ret);
    }
}