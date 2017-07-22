package com.github.saka1029.common.io;

import java.io.IOException;

/**
 * Fieldsの列を保持するオブジェクトからFieldsを読み出すインタフェースです。
 * Fieldsの列を保持するオブジェクトの例としては
 * CSVファイル、
 * RDBのテーブル、
 * Excelファイルのシートなどが考えられます。
 *
 */
public interface FieldsReader extends AutoCloseable {

	Fields readFields() throws IOException;

}
