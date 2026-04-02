package com.takeseem.learn.ai.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class UtilOpenAI {

	/**
	 * 根据 application.properties 中的 openai.* 配置客户端
	 * <ul>
	 * <li>openai.maxRetries = 0</li>
	 * <li>openai.proxy = http://127.0.0.1:8888</li>
	 * <li>openai.proxy.ca = 证书路径</li>
	 * <li>openai.baseUrl = 中转URL https://openai.zz.com</li>
	 * </ul>
	 */
	public static OpenAIOkHttpClient.Builder setup(OpenAIOkHttpClient.Builder builder) {
		Properties props = UtilSys.loadProperties("application.properties");

		String maxRetries = props.getProperty("openai.maxRetries");
		if (UtilString.isNotEmpty(maxRetries)) builder.maxRetries(Integer.parseInt(maxRetries));

		String proxy = props.getProperty("openai.proxy");
		if (UtilString.isNotEmpty(proxy)) {
			URI url = URI.create(proxy);
			builder.proxy(new Proxy(Type.HTTP, new InetSocketAddress(url.getHost(), url.getPort())));

			String ca = props.getProperty("openai.proxy.ca");
			if (UtilString.isNotEmpty(ca)) {
				X509TrustManager trustManager = UtilSys.getX509TrustManager(ca);
				SSLContext sslContext = UtilSys.getSSLContext("TLS", trustManager);
				builder.trustManager(trustManager).sslSocketFactory(sslContext.getSocketFactory());
			}
		}

		String baseUrl = props.getProperty("openai.baseUrl");
		if (UtilString.isNotEmpty(baseUrl)) builder.baseUrl(baseUrl);

		return builder;
	}

	public static String toStr(ChatCompletion c) {
		String str = "";

		var n = UtilJson.convert(c, ObjectNode.class);
		JsonNode message = n.path("choices").get(0).path("message");
		String reasoningContent = message.path("reasoning_content").asText();
		if (UtilString.isNotEmpty(reasoningContent))
			str += "Reasoning >\n----\n" + reasoningContent.trim() + "\n----\n\n";

		str += message.path("role").asText() + " >\n----\n" + message.path("content").asText().trim() + "\n----\n";

		str += n.path("model").asText() + '\n';
		JsonNode usage = n.path("usage");
		int compTokens = usage.path("completion_tokens").asInt();
		int reasTokens = usage.path("completion_tokens_details").path("reasoning_tokens").asInt();
		str += "Tokens: " + usage.path("total_tokens") + " (prompt: " + usage.path("prompt_tokens") + ", completion: "
				+ compTokens
				+ (reasTokens > 0 ? " (reasoning: " + reasTokens + ", content: " + (compTokens - reasTokens) + ")" : "")
				+ ")\n----\n";

		return str;
	}
}
