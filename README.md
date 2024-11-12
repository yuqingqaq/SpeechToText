
---

## Step 1: 

在命令行中设置 Google 应用程序凭据环境变量

```bash
export GOOGLE_APPLICATION_CREDENTIALS="/path/to/your/service-account-file.json"
```

## Step 2: Java 代码

替换 `"YOUR APIKEY"` 为实际 API 密钥。

```java
public class InfiniteStreamRecognize {
    private static OpenAIGPT openAIGPT = new OpenAIGPT("gpt-4", "YOUR APIKEY");

}
```


---

