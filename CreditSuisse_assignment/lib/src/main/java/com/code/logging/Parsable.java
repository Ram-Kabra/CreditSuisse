package com.code.logging;

import java.io.File;
import java.util.List;

public interface Parsable<T> {
	
	    List<ServerLog> parseFile(File f);
}
