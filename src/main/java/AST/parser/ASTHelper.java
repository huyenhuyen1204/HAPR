package AST.parser;

import org.eclipse.jdt.core.dom.*;

import java.util.Arrays;
import java.util.List;

public class ASTHelper {

    public static String getFullyQualifiedName(Type type, CompilationUnit cu) {
        if (type.isParameterizedType()) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return getFullyQualifiedTypeName(parameterizedType, cu);
        } else if (type.isArrayType()) {
            ArrayType arrayType = (ArrayType) type;
            return getFullyQualifiedTypeName(arrayType, cu);
        } else
            return getFullyQualifiedTypeName(type.toString(), cu);
    }

    protected static String getFullyQualifiedTypeName(ParameterizedType parameterizedType, CompilationUnit cu) {
        String result = "";
        String type = parameterizedType.getType().toString();
        result += getFullyQualifiedTypeName(type, cu) + "<";

        List args = parameterizedType.typeArguments();
        if (args.size() == 1) {
            String argQualifiedName = getFullyQualifiedTypeName(args.get(0).toString(), cu);
            result += argQualifiedName;
        } else {
            for (Object arg : args) {
                String argQualifiedName = getFullyQualifiedTypeName(arg.toString(), cu);
                result += argQualifiedName + ",";
            }
            result = result.substring(0, result.length() - 1);
        }
        result += ">";
        return result;
    }

    protected static String getFullyQualifiedTypeName(ArrayType arrayType, CompilationUnit cu) {
        String result = "";
        result += getFullyQualifiedTypeName(arrayType.getElementType().toString(), cu);
        for (Object dimen : arrayType.dimensions()) {
            result += dimen.toString();
        }
        return result;
    }

    protected static String getFullyQualifiedTypeName(String typeName, CompilationUnit cu) {
        // input is null or input is already a fully qualified type
        if (typeName == null || typeName.contains(".")) return typeName;
        //TODO: edit int -> Integer
        // is primitive type?
        if (primitiveTypes.contains(typeName))
            return typeName;

        // find in import statements
        for (Object o : cu.imports()) {
            if (o instanceof ImportDeclaration) {
                ImportDeclaration id = (ImportDeclaration) o;
                String idStr = id.getName().getFullyQualifiedName();
                if (idStr.endsWith("." + typeName)) {
                    return idStr;
                }
            }
        }

        // find in java.lang package
        if (javaLangTypes.contains(typeName)) {
            return "java.lang." + typeName;
        }

        PackageDeclaration packageDeclaration = cu.getPackage();
        if (packageDeclaration == null) {
            return typeName;
        }
        else {
            return packageDeclaration.getName() + "." + typeName;
        }
    }

    protected static final String[] PRIMITIVE_TYPES = {
            "boolean", "short", "int", "long", "float", "double", "void"
    };

    protected static final String[] JAVA_LANG_TYPES = {
            "Boolean", "Byte", "Character.Subset", "Character.UnicodeBlock", "ClassLoader", "Double",
            "Float", "Integer", "Long", "Math", "Number", "Object", "Package", "Process", "Runtime",
            "Short", "String", "StringBuffer", "StringBuilder", "System", "Thread", "ThreadGroup",
            "Throwable", "Void"
    };

    public static String getType (String type) {

        if (type.equals("boolean")) {
            return "Boolean";
        } else if (type.equals("short")) {
            return "Short";
        } else if (type.equals("int")) {
            return "Integer";
        } else if (type.equals("long")) {
            return "Long";
        } else if (type.equals("float")) {
            return "Float";
        } else if (type.equals("double")) {
            return "Double";
        } else {
            return type;
        }
    }

    protected static List<String> primitiveTypes = Arrays.asList(PRIMITIVE_TYPES);
    protected static List<String> javaLangTypes = Arrays.asList(JAVA_LANG_TYPES);
}
