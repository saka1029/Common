package com.github.saka1029.common.io;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.github.saka1029.common.beanutils.BeanHelper;

/**
 * FieldsReaderで読み取った内容をJavaBeanにマップします。
 */
public class FieldsMapper {

	private String[] fieldNames = null;

	public FieldsMapper() {
	}

	public FieldsMapper(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public void map(Fields fields, Object bean)
			throws InvocationTargetException, NoSuchMethodException,
			InstantiationException, IllegalAccessException {
		for (int i = 0; i < fieldNames.length; ++i) {
			Class<?> type = BeanHelper.setterClass(bean, fieldNames[i]);
			Object value = BeanHelper.convert(fields.get(i), type);
			BeanHelper.setValue(bean, fieldNames[i], value);
		}
	}

	public <T> T map(Fields fields, Class<T> beanClass)
			throws InvocationTargetException, NoSuchMethodException,
			InstantiationException, IllegalAccessException {
		T bean = beanClass.newInstance();
		map(fields, bean);
		return bean;
	}

	public <T> List<T> readAll(Class<T> beanClass, FieldsReader reader)
			throws IOException, InvocationTargetException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException {
		List<T> list = new ArrayList<T>();
		while (true) {
			Fields fields = reader.readFields();
			if (fields == null) break;
			list.add(map(fields, beanClass));
		}
		return list;
	}
}
