package com.takeseem.learn.ai.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.takeseem.learn.ai.util.UtilSys;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class ToolRepo {

	public static Object invokeTool(ObjectNode n) {
		try {
			String tool = n.path("_tool").asText();
			switch (tool) {
			case "UtilFile.mkdirs":
				return mkdirs(n);
			case "UtilFile.readText":
				return readText(n);
			case "UtilFile.writeFile":
				return writeFile(n);
			}
			return "unknown tool: " + tool + ", " + n;
		} catch (Exception e) {
			return "执行异常：" + e.getMessage();
		}
	}

	public static String writeFile(ObjectNode n) throws IOException {
		JsonNode args = n.path("args");
		boolean append = args.path("append").asBoolean(false);
		var file = new File(args.path("file").asText());

		File dir = file.getParentFile();
		if (dir != null && !dir.exists()) throw new IllegalArgumentException("目录不存在：" + dir);

		String content = args.path("content").asText();
		try (var out = new FileOutputStream(file, append)) {
			out.write(content.getBytes(StandardCharsets.UTF_8));
		}
		return "写入文件 " + file + " 成功";
	}

	public static String mkdirs(ObjectNode n) {
		JsonNode args = n.path("args");
		var file = new File(args.path("file").asText());
		if (file.exists()) return "目录已经存在：" + file;

		boolean ret = file.mkdirs();
		return "创建目录：" + file + "，结果：" + ret;
	}

	public static String readText(ObjectNode n) {
		String file = n.path("args").path("file").asText();
		return UtilSys.readText(file);
	}

}
