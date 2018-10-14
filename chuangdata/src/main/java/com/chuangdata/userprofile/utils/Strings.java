package com.chuangdata.userprofile.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Strings {

    public static String[] split(String line, String separator) {
        String[] result = line.split(separator);
        if (line.endsWith(separator)) {
            String[] temp = new String[result.length + 1];
            System.arraycopy(result, 0, temp, 0, result.length);
            temp[result.length] = "";
            result = temp;
        }
        return result;
    }

    public static boolean isNotEmpty(String str) {
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return false;
        }
        if (str.equals("\\N") || str.equals("null")) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    /**
     * Join collections by '\t'
     *
     * @param collections
     * @return
     */
    public static <T> String collectionToString(Collection<T> collections) {
        return collectionToString(collections, "\t");
    }

    public static <T> String collectionToString(Collection<T> collections, String separator) {
        StringBuilder builder = new StringBuilder();
        if (collections == null || collections.isEmpty()) {
            return builder.toString();
        }
        for (T t : collections) {
            builder.append(t.toString()).append(separator);
        }
        return builder.toString().substring(0, builder.toString().length() - 1);
    }

    public static <T> String toStringProperties(T obj) {
        if (obj == null) {
            return "<null>";
        }
        StringBuilder s = new StringBuilder();
        for (Method m : obj.getClass().getMethods()) {
            if (m.getParameterTypes().length != 0) {
                continue;
            }
            String name = m.getName();
            if (name.equals("getClass")) {
                continue;
            }
            if (name.startsWith("get")) {
                name = name.substring("get".length());
            } else if (name.startsWith("is")) {
                name = name.substring("is".length());
            } else {
                continue;
            }
            if (name.isEmpty() || !Character.isUpperCase(name.charAt(0))) {
                continue;
            }
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            if (s.length() != 0) {
                s.append(", ");
            }
            s.append(name);
            s.append("=");
            try {
                Object v = m.invoke(obj);
                if (v == null) {
                    s.append("<null>");
                } else if (v.getClass().isArray()) {
                    int len = Array.getLength(v);
                    List<Object> a = new ArrayList<Object>(len);
                    for (int i = 0; i < len; i++) {
                        a.add(Array.get(v, i));
                    }
                    s.append(a.toString());
                } else {
                    s.append(v.toString());
                }
            } catch (IllegalAccessException ex) {
            } catch (InvocationTargetException ex) {
            }
        }
        return s.toString();
    }

    public static <T> String toStringFields(T obj) {
        if (obj == null) {
            return "<null>";
        }
        StringBuilder s = new StringBuilder();
        for (Field f : obj.getClass().getFields()) {
            if (s.length() != 0) {
                s.append(", ");
            }
            s.append(f.getName());
            s.append("=");
            try {
                Object v = f.get(obj);
                if (v == null) {
                    s.append("<null>");
                } else if (v.getClass().isArray()) {
                    int len = Array.getLength(v);
                    List<Object> a = new ArrayList<Object>(len);
                    for (int i = 0; i < len; i++) {
                        a.add(Array.get(v, i));
                    }
                    s.append(a.toString());
                } else {
                    s.append(v.toString());
                }
            } catch (IllegalAccessException ex) {
            }
        }
        return s.toString();
    }
}
