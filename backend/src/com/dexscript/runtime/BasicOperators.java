package com.dexscript.runtime;

public class BasicOperators {

    public static Long Add__(Long arg0, Long arg1) {
        return arg0 + arg1;
    }

    public static Boolean Equal__(Long arg0, Long arg1) {
        return arg0.equals(arg1);
    }

    public static Boolean LessThan__(Long arg0, Long arg1) {
        return arg0 < arg1;
    }
}
