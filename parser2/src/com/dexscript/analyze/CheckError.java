package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;

public class CheckError implements DexElement.Visitor {

    private boolean hasError;

    public CheckError(DexElement elem) {
        visit(elem);
    }

    @Override
    public void visit(DexElement elem) {
        if (elem.err() != null) {
            hasError = true;
        }
        elem.walkDown(this);
    }

    public boolean hasError() {
        return hasError;
    }
}