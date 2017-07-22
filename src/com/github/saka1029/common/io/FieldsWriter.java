package com.github.saka1029.common.io;

import java.io.IOException;

public interface FieldsWriter extends AutoCloseable {

	void writeFields(Fields fields) throws IOException;

}
