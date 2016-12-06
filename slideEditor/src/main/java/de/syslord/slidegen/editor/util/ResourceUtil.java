package de.syslord.slidegen.editor.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

public class ResourceUtil {

	public static ByteArrayInputStream getResourceAsStream(String resourcePath) {
		InputStream stream = ResourceUtil.class.getClassLoader().getResourceAsStream(resourcePath);
		if (stream == null) {
			stream = ResourceUtil.class.getResourceAsStream(resourcePath);
		}

		try {
			return new ByteArrayInputStream(ByteStreams.toByteArray(stream));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
