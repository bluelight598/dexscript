package com.dexscript.pkg;

import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.TypeSystem;

class CheckReturn implements CheckSemanticError.Handler<DexReturnStmt> {
    @Override
    public void handle(CheckSemanticError cse, DexReturnStmt elem) {
        elem.walkDown(cse);
        TypeSystem ts = cse.typeSystem();
        DType from = InferType.$(ts, elem.expr());
        if (ts.UNDEFINED.equals(from)) {
            cse.report(elem.expr(), "referenced value not found: " + elem);
            return;
        }
        DType to = ResolveType.$(ts, cse.localTypeTable(), elem.enclosingSig().ret());
        CheckAssignment.checkTypeAssignable(cse, elem, from, to);
    }
}
