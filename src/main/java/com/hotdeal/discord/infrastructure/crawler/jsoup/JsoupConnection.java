package com.hotdeal.discord.infrastructure.crawler.jsoup;

import java.io.IOException;
import org.jsoup.nodes.Document;

public interface JsoupConnection {
    Document get(String url, String userAgent, int timeoutMs) throws IOException;
}
