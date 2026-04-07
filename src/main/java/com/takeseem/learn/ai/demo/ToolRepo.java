package com.takeseem.learn.ai.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.openai.models.FunctionDefinition;
import com.openai.models.chat.completions.ChatCompletionMessageFunctionToolCall;
import com.takeseem.learn.ai.util.UtilJson;
import com.takeseem.learn.ai.util.UtilOpenAI;
import com.takeseem.learn.ai.util.UtilSys;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class ToolRepo {

	public static Object invokeTool(ChatCompletionMessageFunctionToolCall c) {
		var fun = c.function();
		ObjectNode n = UtilJson.createObjectNode().put("name", fun.name());
		n.set("arguments", UtilJson.readTree(fun.arguments()));
		return invokeTool(n);
	}

	/** @param n {name, arguments} */
	public static Object invokeTool(ObjectNode n) {
		try {
			String name = n.path("name").asText();
			switch (name) {
			case "UtilFile.mkdirs":
				return mkdirs(n);
			case "UtilFile.readText":
				return readText(n);
			case "UtilFile.writeFile":
				return writeFile(n);
			}
			return "unknown tool: " + name + ", " + n;
		} catch (Exception e) {
			return "执行异常：" + e.getMessage();
		}
	}

	public static String writeFile(ObjectNode n) throws IOException {
		JsonNode args = n.path("arguments");
		boolean append = args.path("append").asBoolean(false);
		var file = new File(args.path("file").asText());
		if (file.getName().isEmpty()) throw new IllegalArgumentException("file = " + file);

		File dir = file.getParentFile();
		if (dir != null && !dir.exists()) {
			if (args.path("mkdirs").asBoolean()) {
				dir.mkdirs();
			} else {
				throw new IllegalArgumentException("目录不存在：" + dir);
			}
		}

		String content = args.path("content").asText();
		try (var out = new FileOutputStream(file, append)) {
			out.write(content.getBytes(StandardCharsets.UTF_8));
		}
		return "写入文件 " + file + " 成功";
	}

	public static String mkdirs(ObjectNode n) {
		JsonNode args = n.path("arguments");
		var dir = new File(args.path("dir").asText());
		if (dir.exists()) return "目录已经存在：" + dir;

		boolean ret = dir.mkdirs();
		return "创建目录：" + dir + "，结果：" + ret;
	}

	public static String readText(ObjectNode n) {
		String file = n.path("arguments").path("file").asText();
		return UtilSys.readText(file);
	}

	/**
	 * <a href=
	 * "https://developers.openai.com/api/docs/guides/function-calling?lang=python#function-tool-example">openai
	 * 示例</a><br/>
	 * <a href="https://json-schema.org/understanding-json-schema/about">json schema
	 * 标准</a><br/>
	 * <a href=
	 * "https://github.com/openai/openai-java/blob/main/openai-java-example/src/main/java/com/openai/example/FunctionCallingRawExample.java#L36">FunctionCallingRawExample</a>
	 */
	public static FunctionDefinition getWriteFileFuncDef() {
		String paramsSchemal = """
				{
					"type": "object",
					"required": ["file", "content"],
					"properties": {
						"file": {
							"type": "string",
							"description": "文件路径"
						},
						"conent": {
							"type": "string",
							"description": "文件内容"
						},
						"append": {
							"type": "boolean",
							"description": "默认 false=覆盖文件内容，true=追加内容"
						},
						"mkdirs": {
							"type": "boolean",
							"description": "默认 true=自动创建父目录，false=不创建父目录父目录不存在时异常"
						}
					}
				}
				""";
		return FunctionDefinition.builder().name("UtilFile.writeFile")
				.parameters(UtilOpenAI.buildFuncParams(paramsSchemal)).build();
	}

}
