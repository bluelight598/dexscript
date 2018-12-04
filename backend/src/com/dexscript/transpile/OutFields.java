package com.dexscript.transpile;

import com.dexscript.ast.core.DexElement;
import com.dexscript.type.Type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class OutFields implements Iterable<OutField> {

    private static Map<String, OutField> fields = new HashMap<>();

    public OutField allocate(DexElement iElem, Type type) {
        String suggestedName = iElem.toString();
        OutField oField = tryAllocate(suggestedName, iElem, type);
        if (oField != null) {
            return oField;
        }
        int i = 2;
        while(true) {
            String newName = suggestedName + i;
            oField = tryAllocate(newName, iElem, type);
            if (oField != null) {
                return oField;
            }
            i += 1;
        }
    }

    private OutField tryAllocate(String name, DexElement iElem, Type type) {
        if (fields.containsKey(name)) {
            return null;
        }
        OutField field = new OutField(iElem, name, type);
        fields.put(name, field);
        return field;
    }

    @Override
    public Iterator<OutField> iterator() {
        return fields.values().iterator();
    }
}
