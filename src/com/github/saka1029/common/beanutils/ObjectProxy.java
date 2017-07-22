package com.github.saka1029.common.beanutils;

import java.lang.reflect.InvocationTargetException;

public class ObjectProxy implements Gettable, Settable {

	private Object origin;

	public ObjectProxy(Object origin) {
		this.origin = origin;
	}

	public Object getOrigin() {
		return this.origin;
	}

	/**
	 * 指定された名前のプロパティ値を取得する。 名前が定義されていない場合はNoSuchMethodExceptionをスローする。
	 */
	public Object getValue(String name) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return BeanHelper.getValue(origin, name);
	}

	/**
	 * 指定された名前のプロパティ値を設定する。 名前が定義されていない場合はNoSuchMethodExceptionをスローする。
	 */
	public void setValue(String name, Object value)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		BeanHelper.setValue(origin, name, value);
	}

	/**
	 * 指定された名前のプロパティクラスを取得する。 名前が定義されていない場合はnullを返す。
	 */
	public Class<?> getterClass(String name) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return BeanHelper.getterClass(origin, name);
	}

	/**
	 * 指定された名前のプロパティクラスを取得する。 名前が定義されていない場合はnullを返す。
	 */
	public Class<?> setterClass(String name) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return BeanHelper.setterClass(origin, name);
	}

	/**
	 * プロパティ名称の一覧を取得する。
	 */
	public Iterable<String> getterNames() throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return BeanHelper.getterNames(origin);
	}

	/**
	 * プロパティ名称の一覧を取得する。
	 */
	public Iterable<String> setterNames() throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return BeanHelper.setterNames(origin);
	}

	@Override
	public String toString() {
		try {
			return BeanHelper.toString(this, origin.getClass());
		} catch (Exception e) {
			return getClass().getName();
		}
	}

}
