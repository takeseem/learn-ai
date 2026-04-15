package com.takeseem.learn.ai.util;

import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class UtilSpring {

	/**
	 * {@link Banner.Mode#OFF}
	 * @see SpringApplicationBuilder#run(String...)
	 */
	public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
		var builder = new SpringApplicationBuilder(primarySource);
		builder.bannerMode(Banner.Mode.OFF);
		return builder.run(args);
	}
}
