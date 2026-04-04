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

	public static String sub(String text, String prefix, String suffix) {
		return sub(text, prefix, suffix, false);
	}

	public static String sub(String text, String prefix, String suffix, boolean max) {
		int pos1 = text.indexOf(prefix);
		if (pos1 == -1) return null;

		int pos2 = max ? text.lastIndexOf(suffix) : text.indexOf(suffix, pos1 + prefix.length());
		if (pos2 == -1 || pos2 < pos1) return null;

		return text.substring(pos1 + prefix.length(), pos2);
	}
}
