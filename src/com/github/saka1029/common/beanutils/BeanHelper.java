package com.github.saka1029.common.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class BeanHelper {

	private BeanHelper() {
	}

	private static Map<Class<?>, Map<String, Method>> getters = null;
	private static Map<Class<?>, Map<String, Method>> setters = null;

	public static final String GETTER_PREFIX = "get";
	public static final String PREDIC_PREFIX = "is";
	public static final String SETTER_PREFIX = "set";

	private static String uncapitalize(String s) {
		if (s == null || "".equals(s))
			return s;
		return "" + Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	private static String getterName(Method method)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		String name = method.getName();
		if (name.startsWith(GETTER_PREFIX)) {
			if (method.getParameterTypes().length != 0)
				return null;
			if (method.getReturnType() == Void.TYPE)
				return null;
			return uncapitalize(name.substring(GETTER_PREFIX.length()));
		} else if (name.startsWith(PREDIC_PREFIX)) {
			if (method.getParameterTypes().length != 0)
				return null;
			if (method.getReturnType() != Boolean.TYPE)
				return null;
			return uncapitalize(name.substring(PREDIC_PREFIX.length()));
		} else
			return null;
	}

	private static String setterName(Method method)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		String name = method.getName();
		if (!name.startsWith(SETTER_PREFIX))
			return null;
		if (method.getParameterTypes().length != 1)
			return null;
		if (method.getReturnType() != Void.TYPE)
			return null;
		return uncapitalize(name.substring(SETTER_PREFIX.length()));
	}

	private static void ensureCache(Class<?> cls) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		if (getters == null)
			getters = new HashMap<Class<?>, Map<String, Method>>();
		if (setters == null)
			setters = new HashMap<Class<?>, Map<String, Method>>();
		Map<String, Method> g = getters.get(cls);
		Map<String, Method> s = setters.get(cls);
		synchronized (BeanHelper.class) {
			if (g != null && s != null)
				return;
			getters.put(cls, g = new HashMap<String, Method>());
			setters.put(cls, s = new HashMap<String, Method>());
			Method[] methods = cls.getMethods();
			for (int i = 0; i < methods.length; ++i) {
				String gn = getterName(methods[i]);
				if (gn != null) {
					g.put(gn, methods[i]);
					// System.out.println("add getter: " + gn + " " +
					// methods[i]);
				}
				String sn = setterName(methods[i]);
				if (sn != null) {
					s.put(sn, methods[i]);
					// System.out.println("add setter: " + sn + " " +
					// methods[i]);
				}
			}
		}
	}

	private static Map<Class<?>, Map<String, Method>> getGetters(Class<?> cls)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		ensureCache(cls);
		return getters;
	}

	private static Map<Class<?>, Map<String, Method>> getSetters(Class<?> cls)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		ensureCache(cls);
		return setters;
	}

	private static Map<String, Method> getGettersClass(Class<?> cls)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Map<Class<?>, Map<String, Method>> ret = getGetters(cls);
		return ret.get(cls);
	}

	private static Map<String, Method> getSettersClass(Class<?> cls)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Map<Class<?>, Map<String, Method>> ret = getSetters(cls);
		return ret.get(cls);
	}

	private static Method getterMethod(Object obj, String name)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		return (Method) getGettersClass(obj.getClass()).get(name);
	}

	private static Method setterMethod(Object obj, String name)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		return (Method) getSettersClass(obj.getClass()).get(name);
	}

	public static Iterable<String> getterNames(Object obj)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		return getGettersClass(obj.getClass()).keySet();
	}

	public static Iterable<String> setterNames(Object obj)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		return getSettersClass(obj.getClass()).keySet();
	}

	public static Class<?> getterClass(Object obj, String name)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method m = getterMethod(obj, name);
		if (m == null)
			return null;
		return m.getReturnType();
	}

	public static Class<?> setterClass(Object obj, String name)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method m = setterMethod(obj, name);
		if (m == null)
			return null;
		return m.getParameterTypes()[0];
	}

	public static Object getValue(Object obj, String name)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method m = getterMethod(obj, name);
		if (m == null)
			throw new NoSuchMethodException(name);
		return m.invoke(obj);
	}

	public static void setValue(Object obj, String name, Object value)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method m = setterMethod(obj, name);
		if (m == null)
			throw new NoSuchMethodException(name);
		m.invoke(obj, value);
	}

	public static void copyValues(Gettable from, Settable to)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, SQLException {
		for (String name : from.getterNames()) {
			// System.out.println("name=" + name);
			// System.out.println("from.class=" + from.getterClass(name));
			// System.out.println("to.class=" + to.setterClass(name));
			if (from.getterClass(name) == to.setterClass(name))
				to.setValue(name, from.getValue(name));
		}
	}

	public static void copyValues(Gettable from, Object to)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, SQLException {
		copyValues(from, new ObjectProxy(to));
	}

	public static void copyValues(Object from, Settable to)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, SQLException {
		copyValues(new ObjectProxy(from), to);
	}

	public static void copyValues(Object from, Object to)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, SQLException {
		copyValues(new ObjectProxy(from), new ObjectProxy(to));
	}

	public static void copyValues(ResultSet from, Object to)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, SQLException {
		for (String name : setterNames(to)) {
			Object value = from.getObject(name);
			if (from.wasNull()) value = null;
			setValue(to, name, value);
		}
	}

	public static String toString(Gettable obj, Class<?> cls)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append(cls).append("[");
		String conj = "[";
		for (String name : obj.getterNames()) {
			sb.append(conj).append(name).append("=").append(obj.getValue(name));
			conj = ", ";
		}
		sb.append("]");
		return sb.toString();
	}

	public static boolean isNull(String s) {
		if (s == null || "".equals(s))
			return true;
		return false;
	}

	public static Object convert(String s, Class<?> cls) {
		if (cls == String.class)
			return s;
		else if (cls == Integer.TYPE)
			return isNull(s) ? new Integer(0) : Integer.valueOf(s);
		else if (cls == Long.TYPE)
			return isNull(s) ? new Long(0) : Long.valueOf(s);
		else if (cls == Short.TYPE)
			return isNull(s) ? new Short((short) 0) : Short.valueOf(s);
		else if (cls == BigDecimal.class)
			return isNull(s) ? new BigDecimal(0) : new BigDecimal(s);
		else if (cls == BigInteger.class)
			return isNull(s) ? new BigInteger("0") : new BigInteger(s);
		else if (cls == Boolean.TYPE)
			return isNull(s) ? Boolean.FALSE : Boolean.valueOf(s);
		else if (cls == Short.TYPE)
			return isNull(s) ? new Short((short) 0) : Short.valueOf(s);
		else if (cls == Float.TYPE)
			return isNull(s) ? new Float(0) : Float.valueOf(s);
		else if (cls == Double.TYPE)
			return isNull(s) ? new Double(0) : Double.valueOf(s);
		else if (cls == Character.TYPE) {
			return isNull(s) ? new Character((char) 0) : new Character(s
					.charAt(0));
		} else
			return s;
	}

	public static String toString(Object obj) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, SQLException {
		return toString(new ObjectProxy(obj), obj.getClass());
	}
}
