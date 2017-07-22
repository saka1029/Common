package com.github.saka1029.common.beanutils;

import java.lang.reflect.InvocationTargetException;

public interface Settable {
	/**
	 * 指定された名前のプロパティ値を設定する。
	 * 名前が定義されていない場合はNoSuchMethodExceptionをスローする。
	 */
	public void setValue(String name, Object value)
		throws NoSuchMethodException,
			IllegalAccessException,
			InvocationTargetException;

	/**
	 * 指定された名前のプロパティクラスを取得する。
	 * 名前が定義されていない場合はnullを返す。
	 */
	public Class<?> setterClass(String name)
		throws NoSuchMethodException,
			IllegalAccessException,
			InvocationTargetException;

	/**
	 * プロパティ名称の一覧を取得する。
	 */
	public Iterable<String> setterNames()
		throws NoSuchMethodException,
			IllegalAccessException,
			InvocationTargetException;

}
