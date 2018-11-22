package com.dexscript.transpiler2;

import com.dexscript.runtime.Result;

import java.util.Map;

public interface TranspileOne {
    static Result __(String src) {
        try {
            Map<String, Class<?>> transpiled = new Town()
                    .addFile("hello.ds", "package abc\n" + src)
                    .transpile();
            return (Result) transpiled.get("abc.Hello").getConstructor().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
