package com.dexscript.ast.expr;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

import java.util.List;

public class DexMethodCallExpr extends DexExpr {

    private static final int LEFT_RANK = 1;
    private static final int RIGHT_RANK = 0;

    private DexExpr obj;
    private DexReference method;
    private DexFunctionCallExpr functionCallExpr;

    public DexMethodCallExpr(Text src, DexExpr obj) {
        super(src);
        this.obj = obj;
        new Parser();
    }

    public DexExpr obj() {
        return obj;
    }

    public DexReference method() {
        return method;
    }

    public List<DexExpr> args() {
        return functionCallExpr.args();
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public int begin() {
        return obj.begin();
    }

    @Override
    public int end() {
        return functionCallExpr.end();
    }

    @Override
    public boolean matched() {
        return functionCallExpr != null && functionCallExpr.matched();
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(obj);
        visitor.visit(method);
        for (DexExpr arg : args()) {
            visitor.visit(arg);
        }
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::dot);
        }

        @Expect(".")
        State dot() {
            for (;i<src.end;i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '.') {
                    i += 1;
                    return this::method;
                }
                return null;
            }
            return null;
        }

        @Expect("reference")
        State method() {
            method = new DexReference(src.slice(i));
            if (!method.matched()) {
                return null;
            }
            i = method.end();
            return this::functionCallExpr;
        }

        @Expect("call")
        State functionCallExpr() {
            functionCallExpr = new DexFunctionCallExpr(src.slice(i), DexMethodCallExpr.this);
            return null;
        }
    }
}