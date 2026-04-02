package com.takeseem.learn.ai.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class ToolRepo {

	public static Object invokeTool(ObjectNode n) {
		String tool = n.path("_tool").asText();
		switch (tool) {
		case "UtilFile.writeFile":
			return writeFile(n);
		}
		return "unknown tool: " + tool + ", " + n;
	}

	public static String writeFile(ObjectNode n) {
		JsonNode args = n.path("args");
		boolean append = args.path("append").asBoolean(false);
		var file = new File(args.path("file").asText());

		File dir = file.getParentFile();
		if (dir != null && !dir.exists()) dir.mkdirs();

		String content = args.path("content").asText();
		try (var out = new FileOutputStream(file, append)) {
			out.write(content.getBytes(StandardCharsets.UTF_8));
			return "写入文件 " + file + " 成功";
		} catch (IOException e) {
			return "写入文件 " + file + " 失败，exception: " + e.getMessage();
		}
	}
}
