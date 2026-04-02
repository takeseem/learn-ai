package com.takeseem.learn.ai.demo;

import java.util.Properties;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.takeseem.learn.ai.util.UtilOpenAI;
import com.takeseem.learn.ai.util.UtilSys;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class ChatToolsDemo {

	public static void main(String[] args) {
		String prompt = "把1到10之间的整数写入 numbers.txt 文件";
		String sysPrompt = UtilSys.readText("demo/tools-demo.md");

		OpenAIClient client = UtilOpenAI.setup(OpenAIOkHttpClient.builder()).fromEnv().build();

		Properties props = UtilSys.loadProperties("application.properties");
		String model = props.getProperty("openai.model");

		System.out.println("model: " + model);
		System.out.println("system prompt: \n----\n" + sysPrompt + "\n----");
		System.out.println("user prompt: \n----\n" + prompt + "\n----");
		var params = ChatCompletionCreateParams.builder().model(ChatModel.of(model))
				.addSystemMessage(sysPrompt)
				.addUserMessage(prompt).build();

		ChatCompletion chatCompletion = client.chat().completions().create(params);
		System.out.println(UtilOpenAI.toStr(chatCompletion));

	}

}
