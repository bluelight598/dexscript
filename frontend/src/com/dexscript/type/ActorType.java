package com.dexscript.type;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorType extends TopLevelType implements FunctionsProvider {

    private final TopLevelTypeTable typeTable;
    private final FunctionTable functionTable;
    private final DexFunction func;
    private List<FunctionType> members;

    public ActorType(ActorTable actorTable, FunctionTable functionTable, DexFunction func) {
        super(func.identifier().toString(), "Result");
        this.func = func;
        this.typeTable = actorTable.typeTable();
        this.functionTable = functionTable;
        actorTable.define(this);
        functionTable.lazyDefine(this);
    }

    @Override
    public List<FunctionType> functions() {
        if (members != null) {
            return members;
        }
        members = new ArrayList<>();
        members.add(consumeFunc());
        List<FunctionType> functions = new ArrayList<>(members);
        functions.add(callFunc());
        functions.add(newFunc());
        return functions;
    }

    private FunctionType callFunc() {
        Type ret = ResolveType.$(typeTable, func.sig().ret());
        ArrayList<Type> params = new ArrayList<>();
        for (DexParam param : func.sig().params()) {
            params.add(ResolveType.$(typeTable, param.paramType()));
        }
        return new FunctionType(func, name(), params, ret);
    }

    private FunctionType newFunc() {
        ArrayList<Type> params = new ArrayList<>();
        params.add(new StringLiteralType(name()));
        for (DexParam param : func.sig().params()) {
            params.add(ResolveType.$(typeTable, param.paramType()));
        }
        return new FunctionType(func, "New__", params, this);
    }

    private FunctionType consumeFunc() {
        Type ret = ResolveType.$(typeTable, func.sig().ret());
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        return new FunctionType(func,"Consume__", params, ret);
    }

    @Override
    public boolean isAssignableFrom(Type thatObj) {
        if (super.isAssignableFrom(thatObj)) {
            return true;
        }
        if (thatObj instanceof SameType) {
            return false;
        }
        Map<Type, Type> lookup = new HashMap<>();
        lookup.put(this, new SameType(thatObj));
        for (FunctionType member : functions()) {
            FunctionType expandedMember = (FunctionType) member.expand(lookup);
            if (!functionTable.isDefined(expandedMember)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return name();
    }
}
