package com.takeseem.learn.ai.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.ReasoningEffort;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.services.blocking.chat.ChatCompletionService;
import com.takeseem.learn.ai.util.UtilJson;
import com.takeseem.learn.ai.util.UtilOpenAI;
import com.takeseem.learn.ai.util.UtilString;
import com.takeseem.learn.ai.util.UtilSys;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class ReActDemo {
	private static PrintStream out = System.out;
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

	private static String readLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void main(String[] args) {
		String model = UtilSys.loadProperties("application.properties").getProperty("openai.model");
		out.println("model: " + model);

		String sysPrompt = UtilSys.readText("demo/react-demo.md");
		out.println("system prompt: \n----\n" + sysPrompt + "\n----");
		ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder().model(ChatModel.of(model))
				.addSystemMessage(sysPrompt).reasoningEffort(ReasoningEffort.LOW);

		var demo = "把1到10的整数写入文件中";
		for (String tips = "ReAct Demo\n----\n请输入你的任务：";;) {
			if (tips != null) {
				out.print(tips);
				tips = null;
			}
			String prompt = readLine();
			if (prompt.equalsIgnoreCase("exit")) break;

			if (prompt.trim().isEmpty() && demo != null) {
				out.println("示例任务：" + demo);
				prompt = demo;
				demo = null;
			}
			out.println("----");
			if (UtilString.isNotEmpty(prompt)) builder.addUserMessage(prompt);

			ChatCompletion completion = doChat(builder.build());
			String content = UtilOpenAI.getContent(completion);
			if (content.length() <= 4) break;
			builder.addAssistantMessage(content);

			try {
				String actionResult = doAction(content);
				if (actionResult != null) builder.addAssistantMessage(actionResult);
			} catch (Exception e) {
				builder.addAssistantMessage("调用工具：\n```json\n" + content + "\n```\n 异常：" + e.getMessage());
			}
			out.print("回车继续：");
		}
	}

	private static ChatCompletionService service = UtilOpenAI.setup(OpenAIOkHttpClient.builder()).build().chat()
			.completions();

	private static ChatCompletion doChat(ChatCompletionCreateParams params) {
		ChatCompletion completion = service.create(params);
		out.println(UtilOpenAI.toStr(completion));
		return completion;
	}

	private static String doAction(String content) {
		String json = UtilJson.isObjectNode(content) ? content : UtilString.sub(content, "```json", "```");
		if (json == null) return null;

		out.println("Action: \n----\n" + json.trim() + "\n----");
		var result = ToolRepo.invokeTool((ObjectNode) UtilJson.readTree(json));
		out.println(result + "\n----");
		return result.toString();
	}

}
