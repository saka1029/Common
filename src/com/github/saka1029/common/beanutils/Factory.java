package com.github.saka1029.common.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Factory {

	public static Map<String, Factory> instances = new HashMap<String, Factory>();

	private String name;
	private Class<?> interfaceClass;
	private Class<?> implementClass;
//	private List constructorArgs = new ArrayList();
	private Map<String, Object> properties = new HashMap<String, Object>();

	public Factory(String name) {
		this.name = name;
		instances.put(name, this);
	}

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public String getName() {
		return name;
	}

	public void setInterfaceClass(Class<?> cls) {
		interfaceClass = cls;
	}

	public Class<?> getImplementClass() {
		return implementClass;
	}

	public void setImplementClass(Class<?> cls) {
		implementClass = cls;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public Factory getFactory(String name) {
		return (Factory)instances.get(name);
	}

	private void setProperties(Object obj)
		throws
			IllegalAccessException,
			NoSuchMethodException,
			InvocationTargetException
	{
		for (Map.Entry<String, Object> e : properties.entrySet())
			BeanHelper.setValue(obj, e.getKey(), e.getValue());
	}

	public Object getObject(String name)
		throws
			InstantiationException,
			IllegalAccessException,
			NoSuchMethodException,
			InvocationTargetException
	{
		Object obj = getFactory(name).getImplementClass().newInstance();
		setProperties(obj);
		return obj;
	}
}
