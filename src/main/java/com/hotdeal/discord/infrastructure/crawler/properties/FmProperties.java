package com.hotdeal.discord.infrastructure.crawler.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "community.fmkorea")
@Validated
public class FmProperties {

    @NotBlank
    @URL
    private String url;

    @NotBlank(message = "List item selector must not be blank")
    private String listItemSelector;

    @NotBlank(message = "Link selector must not be blank")
    private String linkSelector;

    @NotBlank(message = "Ended hotDeal class must not be blank")
    private String endedDealClass;

    @Positive
    private int pagesToCrawl;

}
