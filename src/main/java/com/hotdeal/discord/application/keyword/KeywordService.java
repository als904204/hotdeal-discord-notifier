package com.hotdeal.discord.application.keyword;

import java.util.List;

public interface KeywordService {

    void registerKeyword(String keyword, String userId);

    void deleteKeyword(String keyword, String userId);

    List<String> getKeywordListByUserId(String userId);
}
