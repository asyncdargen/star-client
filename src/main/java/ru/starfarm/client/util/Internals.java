package ru.starfarm.client.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@UtilityClass
public class Internals {

    public final Unsafe UNSAFE = findUnsafe();
    public final MethodHandles.Lookup LOOKUP = findLookup();


    @SneakyThrows
    public <T> T getFieldValue(Class<?> declaredClass, Object object, String fieldName) {
        val field = declaredClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(object);
    }

    @SneakyThrows
    public <T> T getFieldValue(Object object, String fieldName) {
        return getFieldValue(object.getClass(), object, fieldName);
    }

    @SneakyThrows
    public <T> T allocateInstance(Class<T> declaredClass) {
        return (T) UNSAFE.allocateInstance(declaredClass);
    }

    @SneakyThrows
    public <T> T newInstance(Class<T> declaredClass) {
        val constructor = declaredClass.getConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    @SneakyThrows
    public MethodHandle unreflectMethod(Method method) {
        return LOOKUP.unreflect(method);
    }

    @SneakyThrows
    public MethodHandle findGetter(Class<?> declaredClass, String fieldName, Class<?> fieldType) {
        return LOOKUP.findGetter(declaredClass, fieldName, fieldType);
    }

    @SneakyThrows
    public MethodHandle findStaticGetter(Class<?> declaredClass, String fieldName, Class<?> fieldType) {
        return LOOKUP.findStaticGetter(declaredClass, fieldName, fieldType);
    }

    @SneakyThrows
    public MethodHandle findGetter(Class<?> declaredClass, String fieldName) {
        return LOOKUP.unreflectGetter(declaredClass.getDeclaredField(fieldName));
    }


    @SneakyThrows
    public MethodHandle findSetter(Class<?> declaredClass, String fieldName, Class<?> fieldType) {
        return LOOKUP.findSetter(declaredClass, fieldName, fieldType);
    }

    @SneakyThrows
    public MethodHandle findSetter(Class<?> declaredClass, String fieldName) {
        return LOOKUP.unreflectSetter(declaredClass.getDeclaredField(fieldName));
    }

    @SneakyThrows
    public <I> I asInterface(Class<I> interfaceClass, MethodHandle methodHandle) {
        return MethodHandleProxies.asInterfaceInstance(interfaceClass, methodHandle);
    }

    @SneakyThrows
    private MethodHandles.Lookup findLookup() {
        return getFieldValue(MethodHandles.Lookup.class, null, "IMPL_LOOKUP");
    }

    @SneakyThrows
    private Unsafe findUnsafe() {
        return getFieldValue(Unsafe.class, null, "theUnsafe");
    }

}
