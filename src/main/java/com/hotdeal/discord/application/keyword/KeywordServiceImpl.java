package com.hotdeal.discord.application.keyword;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeywordServiceImpl implements KeywordService{


    /**
     * RegisterCommandHandler 클래스에서 넘어온 값을 이용합니다.
     * 사용자가 요청한 키워드를 DB에 저장하는 비즈니스 로직 작성
     * @param keyword 사용자가 등록한 키워드
     * @param userId 사용자 고유 Discord ID
     */
    @Override
    public void registerKeyword(String keyword, String userId) {
        /**
         * DB 저장하기 전, 해당 사용자가 이미 저장한 키워드인지 검증
         * - 중복 저장이라면 커스텀 예외 발생
         */

        /**
         * Keyword 엔티티 생성
         */

        /**
         * 생성된 Keyword 엔티티 DB저장
         */

        /**
         * 저장된 Keyword 로깅
         */
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
