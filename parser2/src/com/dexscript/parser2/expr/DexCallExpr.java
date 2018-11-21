package com.dexscript.parser2.expr;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Expect;
import com.dexscript.parser2.core.State;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.token.Blank;
import com.dexscript.parser2.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexCallExpr implements DexExpr {

    private static final int LEFT_RANK = 1;
    private static final int RIGHT_RANK = 0;

    private final Text src;
    private final DexExpr target;
    private List<DexExpr> args;
    private int callExprEnd = -1;
    private DexError err;

    public DexCallExpr(Text src, DexExpr target) {
        this.target = target;
        this.src = src;
        new Parser();
    }

    public DexExpr target() {
        return target;
    }

    public List<DexExpr> args() {
        return args;
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        return target().begin();
    }

    @Override
    public int end() {
        if (!matched()) {
            throw new IllegalStateException();
        }
        return callExprEnd;
    }

    @Override
    public boolean matched() {
        return callExprEnd != -1;
    }

    @Override
    public DexError err() {
        return err;
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftParen);
        }

        @Expect("(")
        State leftParen() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '(') {
                    i += 1;
                    args = new ArrayList<>();
                    return this::argument;
                }
                break;
            }
            return null;
        }

        @Expect("expression")
        State argument() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
                break;
            }
            DexExpr arg = DexExpr.parse(new Text(src.bytes, i, src.end), RIGHT_RANK);
            args.add(arg);
            if (arg.matched()) {
                i = arg.end();
                return this::commaOrRightParen;
            }
            reportError();
            // try to recover from invalid argument
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == ',') {
                    i += 1;
                    return this::argument;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
                if (LineEnd.__(b)) {
                    callExprEnd = i;
                    return null;
                }
            }
            callExprEnd = i;
            return null;
        }

        @Expect(",")
        @Expect(")")
        State commaOrRightParen() {
            int originalCursor = i;
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::argument;
                }
                if (b == ')') {
                    callExprEnd = i + 1;
                    return null;
                }
                break;
            }
            i = originalCursor;
            return this::missingRightParen;
        }

        private State missingRightParen() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.__(b)) {
                    callExprEnd = i;
                    return null;
                }
            }
            callExprEnd = i;
            return null;
        }

        void reportError() {
            if (err == null) {
                err = new DexError(src, i);
            }
        }
    }
}
