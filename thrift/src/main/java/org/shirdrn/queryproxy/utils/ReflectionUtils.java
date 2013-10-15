package org.shirdrn.queryproxy.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

	private static final ClassLoader DEFAULT_CLASSLOADER = ReflectionUtils.class.getClassLoader();

	private static ClassLoader getClassLoader(ClassLoader classLoader) {
		if (classLoader == null) {
			classLoader = DEFAULT_CLASSLOADER;
		}
		return classLoader;
	}

	public static Object getInstance(String className) {
		return getInstance(className, DEFAULT_CLASSLOADER);
	}

	public static Object getInstance(String className, ClassLoader classLoader) {
		Object instance = null;
		try {
			Class<?> clazz = Class.forName(className, true, getClassLoader(classLoader));
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	public static <T> T getInstance(String className, Class<T> baseClass, ClassLoader classLoader) {
		return getInstance(className, baseClass, classLoader, new Object[] {});
	}

	public static <T> T getInstance(String className, Class<T> baseClass, ClassLoader classLoader, Object... parameters) {
		T instance = null;
		try {
			Class<T> clazz = getClass(className, baseClass, classLoader);
			instance = construct(clazz, parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	public static <T> T getInstance(Class<T> clazz, Object... parameters) {
		T instance = null;
		try {
			instance = construct(clazz, parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	private static <T> T construct(Class<T> clazz, Object... parameters)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		T instance = null;
		Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();
		for (Constructor<T> c : constructors) {
			if (c.getParameterTypes().length == parameters.length) {
				instance = c.newInstance(parameters);
				break;
			}
		}
		return instance;
	}

	public static <T> T getInstance(Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	public static Object getInstance(String className, Object... parameters) {
		return getInstance(className, getClassLoader(null), parameters);
	}

	public static Object getInstance(String className, ClassLoader classLoader, Object... parameters) {
		Object instance = null;
		try {
			Class<?> clazz = Class.forName(className, true, getClassLoader(classLoader));
			instance = getInstance(clazz, parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(String className, Class<T> baseClass) {
		Class<T> clazz = null;
		try {
			clazz = (Class<T>) Class.forName(className, true,
					getClassLoader(DEFAULT_CLASSLOADER));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return clazz;
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(String className, Class<T> baseClass, ClassLoader classLoader) {
		Class<T> clazz = null;
		try {
			clazz = (Class<T>) Class.forName(className, true, getClassLoader(classLoader));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return clazz;
	}

	public static Object getClass(String className, ClassLoader classLoader) {
		Object clazz = null;
		try {
			clazz = Class.forName(className, true, getClassLoader(classLoader));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return clazz;
	}
	
}