package com.hotdeal.discord.infrastructure.crawler.fetcher;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class SystemJsoupConnection implements JsoupConnection {

    @Override
    public Document get(String url, String userAgent, int timeoutMs) throws IOException {
        return Jsoup.connect(url)
            .userAgent(userAgent)
            .timeout(timeoutMs)
            .get();
    }

}
