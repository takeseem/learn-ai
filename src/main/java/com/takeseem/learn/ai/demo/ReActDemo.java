package com.takeseem.learn.ai.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.services.blocking.chat.ChatCompletionService;
import com.takeseem.learn.ai.util.UtilOpenAI;
import com.takeseem.learn.ai.util.UtilString;
import com.takeseem.learn.ai.util.UtilSys;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class ReActDemo {

	public static void main(String[] args) {
		ChatCompletionService service = UtilOpenAI.setup(OpenAIOkHttpClient.builder()).build().chat().completions();

		PrintStream out = System.out;
		String model = UtilSys.loadProperties("application.properties").getProperty("openai.model");
		out.println("model: " + model);

		String sysPrompt = UtilSys.readText("demo/react-demo.md");
		out.println("system prompt: \n----\n" + sysPrompt + "\n----");
		ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder().model(ChatModel.of(model))
				.addSystemMessage(sysPrompt);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		var demo = "把1到10的整数写入文件中";
		for (String tips = "ReAct Demo\n----\n请输入你的任务：";;) {
			out.println(tips);
			String prompt;
			try {
				prompt = reader.readLine();
				if (UtilString.isEmpty(prompt) && demo != null) {
					out.println("示例任务：" + demo);
					prompt = demo;
					demo = null;
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			out.println("----");
			ChatCompletion completion = service.create(builder.addUserMessage(prompt).build());
			out.println(UtilOpenAI.toStr(completion));
		}
	}

}
