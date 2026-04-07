package com.takeseem.learn.ai.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletion.Choice;
import com.openai.models.chat.completions.ChatCompletion.Choice.FinishReason;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionToolMessageParam;
import com.openai.services.blocking.chat.ChatCompletionService;
import com.takeseem.learn.ai.util.UtilOpenAI;
import com.takeseem.learn.ai.util.UtilString;
import com.takeseem.learn.ai.util.UtilSys;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class ReActToolsDemo {
	private static PrintStream out = System.out;
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

	private static String readLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * <a href="https://lmstudio.ai/docs/developer/openai-compat/tools">LM Studio OpenAI tools </a><br/>
	 * <a href="https://developers.openai.com/api/reference/resources/chat/subresources/completions/methods/create">OpenAI API</a><br/>
	 * <a href="https://developers.openai.com/api/docs/guides/function-calling">OpenAI Function calling</a>
	 */
	public static void main(String[] args) {
		String model = UtilSys.loadProperties("application.properties").getProperty("openai.model");
		out.println("model: " + model);
		ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder().model(ChatModel.of(model));

		builder.addFunctionTool(ToolRepo.getWriteFileFuncDef());

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
			}
			demo = null;
			out.println("----");
			if (UtilString.isNotEmpty(prompt)) builder.addUserMessage(prompt);

			ChatCompletion completion = service.create(builder.build());
			out.println(UtilOpenAI.toStr(completion));

			Choice c = completion.choices().getFirst();
			FinishReason finishReason = c.finishReason();
			if (UtilOpenAI.isStop(finishReason)) {
				String content = c.message().content().get();
				if (content.length() <= 4) break;
				builder.addAssistantMessage(content);
			} else if (UtilOpenAI.isToolCalls(finishReason)) {
				builder.addMessage(c.message());
				c.message().toolCalls().get().forEach(v -> {
					if (v.isFunction()) {
						var fun = v.asFunction();
						out.println("tool call id: " + fun.id() + ", name: " + fun.function().name() + ": \n----\n");
						Object result = ToolRepo.invokeTool(fun);
						out.println("result: " + result + "\n----");
						builder.addMessage(ChatCompletionToolMessageParam.builder().toolCallId(fun.id())
								.content(result == null ? "task completed success." : result.toString()).build());
					}
				});
			}
			out.print("回车继续：");
		}
	}

	private static ChatCompletionService service = UtilOpenAI.setup(OpenAIOkHttpClient.builder()).build().chat()
			.completions();

}
