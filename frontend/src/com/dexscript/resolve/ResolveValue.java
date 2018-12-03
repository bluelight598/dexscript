package com.dexscript.resolve;

final class ResolveValue {

//    private final DenotationTable<Value> builtin = new DenotationTable<>();
//    private final Resolve resolve;
//
//    public ResolveValue(Resolve resolve) {
//        this.resolve = resolve;
//    }
//
//    public Denotation resolveValue(DexReference ref) {
//        Denotation denotation = ref.attachmentOfType(Denotation.class);
//        if (denotation != null) {
//            return denotation;
//        }
//        denotation = _resolveValue(ref);
//        ref.attach(denotation);
//        return denotation;
//    }
//
//    private Denotation _resolveValue(DexReference ref) {
//        List<DexElement> prevElems = new ArrayList<>();
//        DexElement current = ref;
//        while (true) {
//            current = current.prev();
//            if (current == null) {
//                break;
//            }
//            prevElems.add(current);
//        }
//        DenotationTable<Value> parentDT = builtin;
//        for (int i = prevElems.size() - 1; i >= 0; i--) {
//            DexElement elem = prevElems.get(i);
//            parentDT = denotationTable(elem, parentDT);
//        }
//        String refName = ref.toString();
//        Denotation denotation = parentDT.get(refName);
//        if (denotation == null) {
//            return new Denotation.Error(refName, ref, "can not resolve " + refName + " to a value");
//        }
//        return denotation;
//    }
//
//    private DenotationTable denotationTable(DexElement elem, DenotationTable<Value> parentDT) {
//        DenotationTable<Value> denotationTable = elem.attachmentOfType(DenotationTable.class);
//        if (denotationTable != null) {
//            return denotationTable;
//        }
//        denotationTable = elem.attach(parentDT.copy());
//        if (elem instanceof DexFunction) {
//            return fillTable((DexFunction) elem, denotationTable);
//        }
//        if (elem instanceof DexShortVarDecl) {
//            return fillTable((DexShortVarDecl) elem, denotationTable);
//        }
//        return parentDT;
//    }
//
//    private DenotationTable fillTable(DexFunction function, DenotationTable<Value> denotationTable) {
//        for (DexParam param : function.sig().params()) {
//            String name = param.paramName().toString();
//            Type type = resolve.resolveType(param.paramType());
//            denotationTable.put(name, new Value(name, type, param));
//        }
//        return denotationTable;
//    }
//
//    private DenotationTable fillTable(DexShortVarDecl shortVarDecl, DenotationTable<Value> denotationTable) {
//        String name = shortVarDecl.decls().get(0).toString();
//        Type type = resolve.resolveType(shortVarDecl.expr());
//        denotationTable.put(name, new Value(name, type, shortVarDecl));
//        return denotationTable;
//    }
}
