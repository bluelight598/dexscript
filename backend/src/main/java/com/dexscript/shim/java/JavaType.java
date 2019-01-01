package com.dexscript.shim.java;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.type.DexType;
import com.dexscript.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class JavaType implements NamedType, FunctionsType, GenericType {

    private final OutShim oShim;
    private final Class clazz;
    private TypeTable localTypeTable;
    private List<FunctionType> functions;
    private List<DType> dTypeParams;
    private List<DType> dTypeArgs;
    private TypeSystem ts;
    private String description;

    public JavaType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JavaType(OutShim oShim, Class clazz, List<DType> dTypeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.ts = oShim.typeSystem();
        dTypeParams = new ArrayList<>();
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            dTypeParams.add(ts.ANY);
        }
        if (dTypeArgs == null) {
            oShim.javaTypes().add(clazz, this);
            ts.defineType(this);
            this.dTypeArgs = dTypeParams;
        } else {
            this.dTypeArgs = dTypeArgs;
        }
        ts.lazyDefineFunctions(this);
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            javaMethodToDexFunc(functions, method);
        }
//        arrayGetToDexFunc(functions);
        return functions;
    }

    private void arrayGetToDexFunc(List<FunctionType> collector) {
        if (!clazz.isArray()) {
            return;
        }
        ArrayList<FunctionParam> params = new ArrayList<>();
        params.add(new FunctionParam("self", this));
        params.add(new FunctionParam("index", ts.INT32));
        DType ret = oShim.javaTypes().resolve(clazz.getComponentType());
        FunctionType func = new FunctionType(ts, "get", params, ret);
        func.implProvider(expandedFunc -> new CallJavaArrayGet(oShim, expandedFunc, clazz));
        collector.add(func);
    }

    private void javaMethodToDexFunc(List<FunctionType> collector, Method method) {
        Type jRet = method.getGenericReturnType();
        DType dRet = resolve(jRet);
        if (dRet == null) {
            return;
        }
        List<FunctionParam> dParams = new ArrayList<>();
        dParams.add(new FunctionParam("self", this));
        for (Parameter jParam : method.getParameters()) {
            DType dParam = resolve(jParam.getType());
            if (dParam == null) {
                return;
            }
            dParams.add(new FunctionParam(jParam.getName(), dParam));
        }
        FunctionType func = new FunctionType(ts, method.getName(), dParams, dRet);
        func.implProvider(expandedFunc -> new CallJavaMethod(oShim, expandedFunc, method));
        collector.add(func);
    }

    private DType resolve(Type jTypeObj) {
        String src = TranslateJavaCtor.translateType(oShim.javaTypes(), jTypeObj);
        TypeTable localTypeTable = localTypeTable();
        DexType type = DexType.parse(new Text(src));
        type.attach(oShim.pkg(jTypeObj));
        return ResolveType.$(ts, localTypeTable, type);
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return this.equals(that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        if (description == null) {
            description = describe(dTypeArgs);
        }
        return description;
    }

    @Override
    public @NotNull String name() {
        String className = clazz.getCanonicalName();
        int dotPos = className.lastIndexOf('.');
        if (dotPos == -1) {
            return className;
        }
        return className.substring(dotPos + 1).replace("$", "__");
    }

    @Override
    public DexPackage pkg() {
        return oShim.pkg(clazz);
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new JavaType(oShim, clazz, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        if (dTypeParams != null) {
            return dTypeParams;
        }
        dTypeParams = new ArrayList<>();
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            dTypeParams.add(ts.ANY);
        }
        return dTypeParams;
    }

    private TypeTable localTypeTable() {
        if (localTypeTable != null) {
            return localTypeTable;
        }
        localTypeTable = new TypeTable(ts);
        List<DType> dTypeArgs = this.dTypeArgs;
        if (dTypeArgs == null) {
            dTypeArgs = typeParameters();
        }
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            TypeVariable jTypeParam = clazz.getTypeParameters()[i];
            localTypeTable.define(oShim.pkg(clazz), jTypeParam.getName(), dTypeArgs.get(i));
        }
        return localTypeTable;
    }
}
