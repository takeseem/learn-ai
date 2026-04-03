# learn-ai

## 编码规范

- 缩进：Tab
- All respond in Chinese.

## 功能

1. [OpenAIDemo](src/main/java/com/takeseem/learn/ai/demo/OpenAIDemo.java)：调用大模型，openai 兼容的都支持
1. [ChatToolsDemo](src/main/java/com/takeseem/learn/ai/demo/ChatToolsDemo.java)：通过系统提示词 [tools-demo.md](src/main/resources/demo/tools-demo.md) 引导 LLM 支持调用工具
1. [ReActDemo](src/main/java/com/takeseem/learn/ai/demo/ReActDemo.java)：简单 [ReAct](https://java2ai.com/docs/frameworks/agent-framework/tutorials/agents/#%E4%BB%80%E4%B9%88%E6%98%AF-react) 模型实验，配套提示词 [react-demo.md](src/main/resources/demo/react-demo.md)
    - 思考（Reasoning）：分析当前情况，决定下一步该做什么
    - 行动（Acting）：执行工具调用或生成最终答案
    - 观察（Observation）：接收工具执行的结果
    - 迭代：基于观察结果继续思考和行动，直到完成任务
