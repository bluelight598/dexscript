package com.dexscript.ast.func;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexAwaitStmt extends DexStatement {

    private int awaitEnd = -1;
    private List<DexAwaitCase> cases;

    public DexAwaitStmt(Text src) {
        super(src);
        new Parser();
    }

    public DexAwaitStmt(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return src.begin;
    }

    @Override
    public int end() {
        return awaitEnd;
    }

    @Override
    public boolean matched() {
        return awaitEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }

    public List<DexAwaitCase> cases() {
        return cases;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::await);
        }

        @Expect("await")
        State await() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (Keyword.__(src, i, 'a', 'w', 'a', 'i', 't')) {
                    i += 6;
                    return this::leftBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("{")
        State leftBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '{') {
                    i += 1;
                    cases = new ArrayList<>();
                    return this::caseOrRightBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("case")
        @Expect("}")
        private State caseOrRightBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ';'){
                    continue;
                }
                if (b == '}') {
                    awaitEnd = i + 1;
                    return null;
                }
                break;
            }
            DexAwaitCase stmt = DexAwaitCase.parse(src.slice(i));
            stmt.reparent(DexAwaitStmt.this, null);
            cases.add(stmt);
            if (stmt.matched()) {
                i = stmt.end();
                return this::caseOrRightBrace;
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
