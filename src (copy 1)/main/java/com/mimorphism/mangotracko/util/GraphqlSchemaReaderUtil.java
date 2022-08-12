package com.mimorphism.mangotracko.util;

import java.io.IOException;

public final class GraphqlSchemaReaderUtil {

	public static final String getSchemaFromFileName(String filename) throws IOException {
	    return new String(
	        GraphqlSchemaReaderUtil.class.getClassLoader().getResourceAsStream("schema/" + filename + ".graphql").readAllBytes());
	  }
}
