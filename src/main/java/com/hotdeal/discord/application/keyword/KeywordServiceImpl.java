package com.hotdeal.discord.application.keyword;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeywordServiceImpl implements KeywordService{


    @Override
    public void registerKeyword(String keyword, String userId) {
        log.info("키워드 등록 keyword: {}, userId: {}", keyword, userId);
    }

    @Override
    public void deleteKeyword(String keyword, String userId) {
        log.info("키워드 삭제 userId: {}, keyword: {}", userId, keyword);
    }

    @Override
    public List<String> getKeywordListByUserId(String userId) {
        log.info("키워드 목록 조회 userId: {}", userId);
        return List.of("제로콜라","사이다","환타");
    }

}
