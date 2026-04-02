package com.takeseem.learn.ai.demo;

import java.util.Properties;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.takeseem.learn.ai.util.UtilJson;
import com.takeseem.learn.ai.util.UtilOpenAI;
import com.takeseem.learn.ai.util.UtilSys;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class OpenAIDemo {

	public static void main(String[] args) throws Exception {
		OpenAIClient client = UtilOpenAI.setup(OpenAIOkHttpClient.builder()).fromEnv().build();

		Properties props = UtilSys.loadProperties("application.properties");
		String chatModel = props.getProperty("openai.model");
		var params = ChatCompletionCreateParams.builder().model(ChatModel.of(chatModel)).addUserMessage("你好").build();

		ChatCompletion chatCompletion = client.chat().completions().create(params);
		System.out.println(UtilJson.writePretty(chatCompletion));
	}

}
