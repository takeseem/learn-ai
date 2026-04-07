package com.takeseem.learn.ai.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class UtilJson {
	private static final ObjectMapper mapper = init(new ObjectMapper());
	private static final ObjectWriter pretty = mapper.writerWithDefaultPrettyPrinter();

	/**
	 * 基本配置初始化(允许name双引号单引号无引号, 不输出null值，而content.includeAlways), 遇到unknown属性不fail
	 */
	public static ObjectMapper init(ObjectMapper mapper) {
		// 允许不带引号
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 允许单引号
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 输出非空
		mapper.setSerializationInclusion(Include.NON_NULL);
		// 缺省属性？
		mapper.setDefaultPropertyInclusion(Include.NON_NULL);
		// 不知道的属性，不异常
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 禁用empty bean时异常
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return mapper;
	}

	public static String write(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String writePretty(Object value) {
		try {
			return pretty.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static <T> T convert(Object src, Class<T> clazz) {
		return mapper.convertValue(src, clazz);
	}

	public static JsonNode readTree(String json) {
		try {
			return mapper.readTree(json);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("json = " + json, e);
		}
	}

	public static boolean isObjectNode(String text) {
		if (text == null) return false;

		String json = text.trim();
		if (json.length() < 2 || json.charAt(0) != '{' || json.charAt(json.length() - 1) != '}') {
			return false;
		}

		try {
			mapper.readTree(json);
			return true;
		} catch (JsonProcessingException e) {
			return false;
		}
	}

	public static ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}
}
