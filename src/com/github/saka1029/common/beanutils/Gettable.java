package com.github.saka1029.common.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface Gettable {

	/**
	 * 指定された名前のプロパティ値を取得する。
	 * 名前が定義されていない場合はNoSuchNameExceptionをスローする。
	 */
	public Object getValue(String name)
		throws NoSuchMethodException,
			IllegalAccessException,
			InvocationTargetException,
			SQLException;

	/**
	 * 指定された名前のプロパティクラスを取得する。
	 * 名前が定義されていない場合はnullを返す。
	 */
	public Class<?> getterClass(String name)
		throws NoSuchMethodException,
			IllegalAccessException,
			InvocationTargetException,
			SQLException;

	/**
	 * プロパティ名称の一覧を取得する。
	 */
	public Iterable<String> getterNames()
		throws NoSuchMethodException,
			IllegalAccessException,
			InvocationTargetException,
			SQLException;

}
