package AST.assign;


import AST.node.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class SettingAST {
    public static void initAST(Object object, Node folderNode) throws Exception {
        Class aClass = object.getClass();
        List<Node> nodes = folderNode.getChildren();
        for (Node clasNode : nodes) {
            if (clasNode instanceof ClassNode) {
                if (clasNode.getName().equals(aClass.getName())) {
//                if (aClass.getName().contains(clasNode.getName())) {
                    List<Node> nodeFields = clasNode.getChildren().stream().filter(node -> node instanceof FieldNode)
                            .collect(Collectors.toList());
                    Field[] fields = aClass.getDeclaredFields();
                    for (Field f : fields) {
                        for (Node nodeField : nodeFields) {
                            if (nodeField instanceof FieldNode) {
                                if (f.getName().equals(nodeField.getName())) {
//                                    ((FieldNode) nodeField).setValue((String) SettingAST.getValueOf(aClass, f.getName()));
                                    if (SettingAST.getValueOf(aClass, f.getName()) != null)
                                        System.out.println(SettingAST.getValueOf(aClass, f.getName()));
                                }
                            }
                        }
                    }
                }
            } else if (clasNode instanceof FieldNode || clasNode instanceof MethodNode
                    || clasNode instanceof ParameterNode) {
                continue;
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static Object getValueOf(Class clazz, String lookingForValue) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class clazzType = field.getType();
            if (clazzType.toString().equals("double") || clazzType.toString().equals("Double")) {
                System.out.println(field.getDouble(clazz));
            } else if (clazzType.toString().equals("int") || clazzType.toString().equals("Integer")) {
                System.out.println(field.getInt(clazz));
            } else if (clazzType.toString().equals("boolean") || clazzType.toString().equals("Boolean")) {
                System.out.println(field.getBoolean(clazz));
            } else if (clazzType.toString().equals("long") || clazzType.toString().equals("Long")) {
                System.out.println(field.getBoolean(clazz));
            } else if (clazzType.toString().equals("short") || clazzType.toString().equals("Short")) {
                System.out.println(field.getShort(clazz));
            } else if (clazzType.toString().equals("float") || clazzType.toString().equals("Float")) {
                System.out.println(field.getFloat(clazz));
            } else if (clazzType.toString().equals("byte") || clazzType.toString().equals("Byte")) {
                System.out.println(field.getByte(clazz));
            } else if (clazzType.toString().equals("char") || clazzType.toString().equals("Char")) {
                System.out.println( field.getChar(clazz));
            }
            System.out.println(field.get(clazz));
        }
        try {
            Field field = clazz.getField(lookingForValue);
            Class clazzType = field.getType();
            if (clazzType.toString().equals("double") || clazzType.toString().equals("Double")) {
                return field.getDouble(clazz);
            } else if (clazzType.toString().equals("int") || clazzType.toString().equals("Integer")) {
                return field.getInt(clazz);
            } else if (clazzType.toString().equals("boolean") || clazzType.toString().equals("Boolean")) {
                return field.getBoolean(clazz);
            } else if (clazzType.toString().equals("long") || clazzType.toString().equals("Long")) {
                return field.getBoolean(clazz);
            } else if (clazzType.toString().equals("short") || clazzType.toString().equals("Short")) {
                return field.getShort(clazz);
            } else if (clazzType.toString().equals("float") || clazzType.toString().equals("Float")) {
                return field.getFloat(clazz);
            } else if (clazzType.toString().equals("byte") || clazzType.toString().equals("Byte")) {
                return field.getByte(clazz);
            } else if (clazzType.toString().equals("char") || clazzType.toString().equals("Char")) {
                return field.getChar(clazz);
            }
            return field.get(clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
//            return new ObjectNotFound(404, lookingForValue);
            return null;
        }
    }


}
