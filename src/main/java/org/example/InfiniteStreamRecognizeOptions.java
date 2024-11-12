package org.example;

public class InfiniteStreamRecognizeOptions {
    private final String langCode;

    public InfiniteStreamRecognizeOptions(String langCode) {
        this.langCode = langCode;
    }

    public static InfiniteStreamRecognizeOptions fromFlags(String[] args) {
        // 这里简化处理：假设语言代码总是第一个参数
        if (args.length > 0) {
            return new InfiniteStreamRecognizeOptions(args[0]);
        } else {
            return null;
        }
    }

    public String getLangCode() {
        return langCode;
    }
}