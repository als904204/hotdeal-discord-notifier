package com.hotdeal.discord.infrastructure.crawler.jsoup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jsoup")
@Validated
public class JsoupProperties {

    @NotBlank
    private String userAgent;

    @Positive
    private int timeoutMs;

}
