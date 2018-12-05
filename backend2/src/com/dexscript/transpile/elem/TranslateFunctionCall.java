package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutField;
import com.dexscript.transpile.OutValue;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.*;

import java.util.List;

public class TranslateFunctionCall implements Translate {

    @Override
    public void handle(OutClass oClass, DexElement iElem) {
        DexFunctionCallExpr iCallExpr = (DexFunctionCallExpr) iElem;
        List<DexExpr> iArgs = iCallExpr.args();
        for (DexExpr iArg : iArgs) {
            Translate.$(oClass, iArg);
        }

        String funcName = iCallExpr.target().asRef().toString();
        TypeSystem ts = oClass.typeSystem();

        StringLiteralType arg1 = new StringLiteralType(funcName);
        Type actorType = ResolveReturnType.$(ts, "New__", InferType.inferTypes(ts, arg1, iCallExpr.args()));

        List<FunctionType> funcTypes = ts.resolveFunctions(funcName, InferType.inferTypes(ts, iArgs));
        String newF = oClass.oShim().newF(funcTypes);
        OutField oActorField = oClass.allocateField(funcName, actorType);
        Gen g = oClass.g();
        g.__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__('(');
        for (int i = 0; i < iArgs.size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexExpr iArg = iArgs.get(i);
            OutValue oValue = iArg.attachmentOfType(OutValue.class);
            g.__(oValue.value());
        }
        g.__(new Line(");"));

        Type resultType = InferType.inferType(ts, iCallExpr);
        OutField oResultField = oClass.allocateField(funcName + "Result", resultType);
        g.__(oResultField.value()
        ).__(" = (("
        ).__(resultType.javaClassName()
        ).__(")("
        ).__(oActorField.value()
        ).__(new Line(".value()));"));
        iElem.attach(oResultField);
    }
}