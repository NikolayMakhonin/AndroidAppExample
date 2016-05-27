package com.github.nikolaymakhonin.utils;

import com.github.nikolaymakhonin.logger.Log;

import java.lang.reflect.Field;

public class ClassUtils {
    public static boolean IsSubClassOrInterface(final Class subClass, final Class baseClass){
        if (subClass == baseClass || baseClass.isAssignableFrom(subClass)) {
            return true;
        }
        if (!baseClass.isInterface()) {
            return false;
        }
        final Class[] interfaces = subClass.getInterfaces();
        for (Class anInterface : interfaces) {
            if (IsSubClassOrInterface(anInterface, baseClass)) {
                return true;
            }
        }
        return false;
    }
    
    public static Field getPrivateField(Class type, final String fieldName) {        
        while (type != null) {
            final Field[] fields = type.getDeclaredFields();
            for (final Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            type = type.getSuperclass();
        }
        Log.e("ClassUtils", "getPrivateField, field not found: " + fieldName + " in class: " + type.getName());
        return null;
    }
}
