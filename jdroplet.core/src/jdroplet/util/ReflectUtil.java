package jdroplet.util;

import jdroplet.annotation.db.DataField;
import jdroplet.data.model.Meta;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2017/11/7.
 */
public class ReflectUtil {

    public static <T extends Annotation> T getFieldAnnotation(Class clazz, String filedName, Class<T> clazz2) {
        try {
            return clazz.getDeclaredField(filedName).getAnnotation(clazz2);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static String[] getAnnotationFields(Class<?> clazz) {
        Field[] fields = null;
        DataField df = null;
        List<String> list = null;
        String[] field_list = null;

        list = new ArrayList<>();
        fields = clazz.getDeclaredFields();
        for(Field field:fields) {
            df = field.getAnnotation(DataField.class);

            if (df != null)
                list.add(df.name());
        }
        field_list = new String[list.size()];
        list.toArray(field_list);
        return field_list;
    }

    public static Field[] getStaticFields(Class<?> clazz) {
        Field[] fields = null;
        List<Field> newFields = null;

        newFields = new ArrayList<>();
        fields = clazz.getDeclaredFields();
        for (Field field:fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                newFields.add(field);
            }
        }
        fields = new Field[newFields.size()];
        newFields.toArray(fields);
        return fields;
    }

    public static Field[] getNotStaticFields(Class<?> clazz) {
        Field[] fields = null;
        List<Field> newFields = null;

        newFields = new ArrayList<>();
        fields = clazz.getDeclaredFields();
        for (Field field:fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                newFields.add(field);
            }
        }
        fields = new Field[newFields.size()];
        newFields.toArray(fields);
        return fields;
    }

    public static Object getFieldValue(Object obj, Field field) {
        String name = field.getName();
        String c = name.substring(0, 1);
        Method m = null;

        name = name.replaceFirst(c, c.toUpperCase());
        try {
            m = obj.getClass().getMethod("get" + name, new Class<?>[]{});
            return m.invoke(obj, new Object[]{});
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public static void setField(Object obj, String fieldName, Object val) {
        Field field = null;

        try {
            field = obj.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);
            field.set(obj, val);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public static void setFieldInt(Object obj, String fieldName, int val) {
        Field field = null;

        try {
            field = obj.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);
            field.setInt(obj, val);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
