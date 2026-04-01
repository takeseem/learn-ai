package com.takeseem.learn.ai.util;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class UtilString {

	public static boolean isEmpty(String v) {
		return v == null || v.isEmpty();
	}

	public static boolean isNotEmpty(String v) {
		return !isEmpty(v);
	}

	public static String getFileName(String path) {
		int pos = path.lastIndexOf('/');
		if (pos != -1) return path.substring(pos + 1);

		pos = path.lastIndexOf('\\');
		if (pos != -1) return path.substring(pos + 1);

		pos = path.indexOf(':');
		if (pos != -1) return path.substring(pos + 1);

		return path;
	}
}
