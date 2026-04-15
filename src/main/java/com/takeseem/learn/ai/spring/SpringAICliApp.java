package com.takeseem.learn.ai.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.takeseem.learn.ai.util.UtilSpring;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
@SpringBootApplication
public class SpringAICliApp {

	public static void main(String[] args) {
		UtilSpring.run(SpringAICliApp.class, args);
	}

}
