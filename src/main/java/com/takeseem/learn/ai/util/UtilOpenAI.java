package com.takeseem.learn.ai.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import com.openai.client.okhttp.OpenAIOkHttpClient;

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
}
