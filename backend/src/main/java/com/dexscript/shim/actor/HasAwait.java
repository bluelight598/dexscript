package com.dexscript.shim.actor;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexConsumeExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexProduceStmt;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.type.composite.InnerActorType;
import com.dexscript.type.core.Dispatched;
import com.dexscript.type.core.FunctionSig;
import com.dexscript.type.core.Invocation;
import com.dexscript.type.core.TypeSystem;

public class HasAwait implements DexElement.Visitor {

    private boolean result;
    private final TypeSystem ts;

    public HasAwait(TypeSystem ts, DexElement func) {
        this.ts = ts;
        visit(func);
    }

    @Override
    public void visit(DexElement elem) {
        if (elem instanceof DexAwaitConsumer) {
            result = true;
            return;
        }
        if (elem instanceof DexConsumeExpr) {
            result = true;
            return;
        }
        if (elem instanceof DexProduceStmt) {
            // do not visit DexValueRef inside Produce
            return;
        }
        if (elem instanceof DexValueRef) {
            Value val = InferValue.$(ts, (DexValueRef) elem);
            if (val != null && val.type() instanceof InnerActorType) {
                result = true;
                return;
            }
        }
        if (elem instanceof DexInvocationExpr && ((DexInvocationExpr) elem).isInvokable()) {
            DexInvocation dexIvc = ((DexInvocationExpr) elem).invocation();
            Invocation ivc = Invocation.ivc(ts, dexIvc).requireImpl(true);
            Dispatched dispatched = ts.dispatch(ivc);
            for (FunctionSig.Invoked candidate : dispatched.candidates) {
                FunctionImpl impl = (FunctionImpl) candidate.func().impl();
                if (impl.hasAwait()) {
                    result = true;
                    return;
                }
            }
        }
        elem.walkDown(this);
    }

    public boolean result() {
        return result;
    }
}
