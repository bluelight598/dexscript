package com.dexscript.type;

import com.dexscript.ast.DexActor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class FunctionTableTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    private FunctionType func(String actorSrc) {
        DexActor actor = new DexActor("function " + actorSrc);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType funcType = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
        return funcType;
    }

    public Invoked invoke(String funcName, String argsStr) {
        return invoke(funcName, null, argsStr);
    }

    public Invoked invoke(String funcName, String typeArgsStr, String argsStr) {
        List<DType> typeArgs = Collections.emptyList();
        if (typeArgsStr != null) {
            typeArgs = ResolveType.resolveTypes(ts, typeArgsStr.split(","));
        }
        List<DType> args = ResolveType.resolveTypes(ts, argsStr.split(","));
        return ts.invoke(new Invocation(funcName, typeArgs, args, null));
    }

    @Test
    public void match_one() {
        FunctionType func1 = func("Hello(arg0: string)");
        FunctionType func2 = func("Hello(arg0: int64)");
        Invoked invoked = invoke("Hello", "string");
        Assert.assertEquals(1, invoked.successes().size());
        Assert.assertEquals(func1, invoked.successes().get(0).function());
    }

    @Test
    public void match_two() {
        func("Hello(arg0: 'a')");
        func("Hello(arg0: string)");
        Invoked invoked = invoke("Hello", "string");
        Assert.assertEquals(2, invoked.successes().size());
    }

    @Test
    public void if_static_checked_then_following_candidates_will_be_ignored() {
        func("Hello(arg0: 'a')");
        func("Hello(arg0: string)");
        Invoked invoked = invoke("Hello", "string");
        Assert.assertEquals(1, invoked.successes().size());
    }
}
