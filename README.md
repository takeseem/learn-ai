# learn-ai

## 编码规范

- 缩进：Tab
- All respond in Chinese.

## 注意

- 模型能力：不同模型对 prompt 的理解是完全不同的，模型的智力就如同小学生和大学生的区别，不要期望一个低等模型能理解你的需求。
- 参数量越低越不能处理复杂的问题，token 越少越好。

## 大模型 LLM Demo

1. [OpenAIDemo](src/main/java/com/takeseem/learn/ai/demo/OpenAIDemo.java)：调用大模型，openai 兼容的都支持
1. [ChatToolsDemo](src/main/java/com/takeseem/learn/ai/demo/ChatToolsDemo.java)：通过系统提示词 [tools-demo.md](src/main/resources/demo/tools-demo.md) 引导 LLM 支持调用工具
1. [ReActDemo](src/main/java/com/takeseem/learn/ai/demo/ReActDemo.java)：简单 [ReAct](https://java2ai.com/docs/frameworks/agent-framework/tutorials/agents/#%E4%BB%80%E4%B9%88%E6%98%AF-react) 模型实验，配套提示词 [react-demo.md](src/main/resources/demo/react-demo.md)
    - 思考（Reasoning）：分析当前情况，决定下一步该做什么
    - 行动（Acting）：执行工具调用或生成最终答案
    - 观察（Observation）：接收工具执行的结果
    - 迭代：基于观察结果继续思考和行动，直到完成任务
1. [ReActToolsDemo](src/main/java/com/takeseem/learn/ai/demo/ReActToolsDemo.java)：LLM tools 能力。参考：[LM Studio OpenAI tools](https://lmstudio.ai/docs/developer/openai-compat/tools)，[OpenAI API](https://developers.openai.com/api/reference/resources/chat/subresources/completions/methods/create)，[Function calling](https://developers.openai.com/api/docs/guides/function-calling)

## Spring AI Demo

- [Spring AI](https://spring.io/projects/spring-ai) 最强 AI 框架，优势：设计和扩展能力，[文档](https://docs.spring.io/spring-ai/reference/concepts.html)清晰易懂。

### 贪吃蛇网页游戏

- [ ] 基于 Spring AI 搭建 ReAct Agent 框架。评测：输入任务、使用 ReAct 范式完成任务。
- [ ] 单模型完成：实现一个简单的贪吃蛇网页游戏。
- [ ] 加入自动评测能力，最大重试次数：2次。
- [ ] 多模型完成：实现一个简单的贪吃蛇网页游戏。
- [ ] 模型路由、自动评测、数据反馈训练策略。

