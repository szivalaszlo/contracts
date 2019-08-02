package com.szivalaszlo.contracts.landon.business.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class PatchMethodInvoker {
    private static Logger logger = LogManager.getLogger();
    private Object instanceToPatch;
    private Class classOfInstance;
    private Map<String, Object> fieldsToPatch;
    private Method[] allMethods;


    public PatchMethodInvoker(Object instanceToPatch, Map<String, Object> fieldsToPatch) {
        this.instanceToPatch = instanceToPatch;
        this.classOfInstance = instanceToPatch.getClass();
        this.allMethods = classOfInstance.getDeclaredMethods();
        this.fieldsToPatch = fieldsToPatch;
        logger.debug("fieldsToPatch: " + fieldsToPatch.toString());
    }

    public boolean updateFields() {
        for (Method m : allMethods) {
            String mName = m.getName();
            for (String f : fieldsToPatch.keySet()) {
                if (mName.toLowerCase().contains("set") &&
                        mName.toLowerCase().contains(f.toLowerCase()) &&
                        mName.length() == f.length() + 3) {
                    m.setAccessible(true);
                    try {
                        Object o = m.invoke(instanceToPatch, fieldsToPatch.get(f));
                        logger.debug("invoking method: " + m.toString() + " with argument: " + fieldsToPatch.get(f).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        logger.debug("cannot invoke method: " + m + " with argument: " + fieldsToPatch.get(f).toString());
                        return false;
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        logger.debug("cannot invoke method: " + m + " with argument: " + fieldsToPatch.get(f).toString());
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
