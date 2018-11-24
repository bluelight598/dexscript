package com.dexscript.ast.stmt;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexStatement extends DexElement {

    protected DexStatement prev;

    public DexStatement(Text src) {
        super(src);
    }

    public abstract void reparent(DexElement parent, DexStatement prev);

    @Override
    public final DexElement prev() {
        if (prev != null) {
            return prev;
        } else {
            return parent();
        }
    }

    public static DexStatement parse(Text src) {
        DexStatement stmt = new DexReturnStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexBlock(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexShortVarDecl(src);
        if (stmt.matched()) {
            return stmt;
        }
        return new DexExprStmt(src);
    }

    public static DexStatement parse(String src) {
        return parse(new Text(src));
    }

    public DexReturnStmt asReturn() {
        return (DexReturnStmt) this;
    }
}