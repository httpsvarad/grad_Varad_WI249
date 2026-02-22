package io.github.httpsvarad.support.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "support")
public class SupportProperties {

    private boolean includeExceptionMessage = false;

    public boolean isIncludeExceptionMessage() {
        return includeExceptionMessage;
    }

    public void setIncludeExceptionMessage(boolean includeExceptionMessage) {
        this.includeExceptionMessage = includeExceptionMessage;
    }
}