package com.dexscript.infer;

import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.stmt.DexShortVarDecl;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.TypeSystem;

public class InferShortVarDecl implements InferValue<DexShortVarDecl> {

    @Override
    public void handle(TypeSystem ts, DexShortVarDecl shortVarDecl, ValueTable table) {
        if (shortVarDecl.decls().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        DexIdentifier decl = shortVarDecl.decls().get(0);
        String valueName = decl.toString();
        DType valueType = InferType.$(ts, shortVarDecl.expr());
        // widen const types
        valueType = ts.widenConst(valueType);
        table.define(new Value(valueName, valueType, decl));
    }
}
