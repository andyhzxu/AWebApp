package com.anh.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CacheLockInterceptor implements InvocationHandler {

    public static int ERROR_COUNT = 0;
    private Object proxied;

    public CacheLockInterceptor(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CacheLock cacheLock = method.getAnnotation(CacheLock.class);
        if (null == cacheLock) {
            System.out.println("not cachelock anotation");
            return method.invoke(proxied, args);
        }

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object lockedObject = getLockedObject(parameterAnnotations, args);
        String objectValue = lockedObject.toString();


        return null;
    }

    private Object getLockedObject(Annotation[][] annotations, Object[] args) throws CacheLockException {
        if (null == args || args.length == 0) {
            throw new CacheLockException("no args");
        }

        if (null == annotations || annotations.length == 0) {
            throw new CacheLockException("no anotations");
        }

        int index = -1;
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof LockedComplexObject) {
                    index = i;
                    try {
                        return args[i].getClass().getField(((LockedComplexObject) annotations[i][j]).field());
                    } catch (Exception e) {
                        throw new CacheLockException(e.getMessage());
                    }
                }

                if (annotations[i][j] instanceof LockedObject) {
                    index = 1;
                    break;
                }
            }

            if (index != -1) {
                break;
            }
        }

        if (index == -1) {
            throw new CacheLockException("aaa");
        }

        return null;

    }
}
