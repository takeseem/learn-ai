package com.takeseem.learn.ai.demo;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.takeseem.learn.ai.util.UtilOpenAI;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class OpenAIDemo {

	public static void main(String[] args) throws Exception {
		OpenAIClient client = UtilOpenAI.setup(OpenAIOkHttpClient.builder()).fromEnv().build();
		var params = ChatCompletionCreateParams.builder().model(ChatModel.GPT_3_5_TURBO).addUserMessage("你好").build();
		ChatCompletion chatCompletion = client.chat().completions().create(params);
		System.out.println(chatCompletion._object_().toString());
	}

}
