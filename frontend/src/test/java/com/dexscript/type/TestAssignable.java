package com.dexscript.type;

import com.dexscript.infer.ResolvePosArgs;
import com.dexscript.test.framework.Row;
import com.dexscript.test.framework.Table;
import org.junit.Assert;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public interface TestAssignable {

    static void $(boolean expected, DType to, DType from) {
        IsAssignable isAssignable = new IsAssignable(to, from);
        if (expected != isAssignable.result()) {
            isAssignable.dump();
            Assert.fail();
        }
    }

    static void $() {
        TypeSystem ts = new TypeSystem();
        Table table = testDataFromMySection().table();
        for (Row row : table.body) {
            DType to = ResolvePosArgs.$(ts, stripQuote(row.get(1))).get(0);
            DType from = ResolvePosArgs.$(ts, stripQuote(row.get(2))).get(0);
            TestAssignable.$("true".equals(row.get(0)), to, from);
        }
    }
}
