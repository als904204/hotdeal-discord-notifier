package com.hotdeal.discord.application.keyword;

import com.hotdeal.discord.domain.keyword.KeywordSubscription;
import com.hotdeal.discord.domain.keyword.exception.KeywordDuplicateException;
import com.hotdeal.discord.domain.keyword.exception.KeywordNotFoundException;
import com.hotdeal.discord.infrastructure.persistence.keyword.KeywordSubscriptionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeywordServiceImpl implements KeywordService{

    private final KeywordSubscriptionRepository keywordRepository;

    /**
     * @param kw     사용자가 등록한 키워드
     * @param userId 사용자 고유 Discord ID
     */
    @Override
    public void registerKeyword(String userId, String kw) {

        keywordRepository.findByDiscordUserIdAndKeyword(userId, kw).ifPresent(
            k -> {
                log.warn("중복 키워드 등록 시도: userId={}, keyword={}", userId, kw);
                throw new KeywordDuplicateException(kw);
            }
        );

        var keyword = KeywordSubscription.builder()
            .discordUserId(userId)
            .keyword(kw)
            .build();

        keywordRepository.save(keyword);

        log.info("키워드 등록 keyword: {}, userId: {}", kw, userId);
    }

    @Transactional
    @Override
    public void deleteKeyword(String userId, String kw) {
        var keyword = keywordRepository.findByDiscordUserIdAndKeyword(userId, kw)
            .orElseThrow(() -> {
                log.warn("삭제할 키워드를 찾을 수 없음: userId={}, keyword={}", userId, kw);
                return new KeywordNotFoundException(kw);
            });

        keywordRepository.delete(keyword);
        log.info("키워드 삭제 userId: {}, keyword: {}", userId, keyword);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getKeywordListByUserId(String userId) {
        List<KeywordSubscription> subscriptions = keywordRepository.findAllByDiscordUserId(userId);

        return subscriptions.stream()
            .map(KeywordSubscription::getKeyword)
            .collect(Collectors.toList());
    }

}
