package com.hotdeal.discord.infrastructure.crawler.fm;

import static com.hotdeal.discord.common.exception.ErrorCode.DATABASE_OPERATION_FAILED;
import static com.hotdeal.discord.domain.hotdeal.CommunityType.FMKOREA;

import com.hotdeal.discord.common.exception.ErrorCode;
import com.hotdeal.discord.domain.hotdeal.HotDeal;
import com.hotdeal.discord.domain.hotdeal.HotDealStatus;
import com.hotdeal.discord.infrastructure.crawler.dto.CrawledHotDealDto;
import com.hotdeal.discord.infrastructure.persistence.hotdeal.HotDealRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FmKoreaCrawlManager {

    private final FmKoreaCrawler fmKoreaCrawler;
    private final HotDealRepository hotDealRepository;

    /**
     * FM_KOREA 핫딜 동기화 작업
     * 1. 크롤링한 데이터를 DB에 저장한다
     *    - 이미 저장되어 있을 경우 핫딜 상태를 업데이트한다.
     *    - 신규일경우 DB에 저장한다.
     */
    public void synchronizeFmKoreaDeals() {
        List<CrawledHotDealDto> crawledDeals;
        try {
            crawledDeals = fmKoreaCrawler.crawl();
        } catch (Exception e) {
            ErrorCode unexpectedErrorCode = ErrorCode.CRAWLER_UNEXPECTED_ERROR;
            log.error("[FM크롤링 매니저] Fmkorea 크롤링 중 예기치 않은 내부 오류 발생 (Code: {}): {}",
                unexpectedErrorCode.getCode(), e.getMessage(), e);
            return;
        }

        if (crawledDeals == null || crawledDeals.isEmpty()) {
            log.info("[FM크롤링 매니저] 새로 크롤링된 Fmkorea 핫딜 정보가 없습니다.");
            return;
        }

        log.info("[FM크롤링 매니저] 크롤링 완료. {}개의 핫딜 정보 동기화 시작.", crawledDeals.size());
        saveOrUpdateDeals(crawledDeals);
    }

    /**
     * 크롤링된 핫딜 목록을 DB와 동기화(저장 또는 업데이트)
     */
    public void saveOrUpdateDeals(List<CrawledHotDealDto> deals) {

        int successCount = 0;
        int failCount = 0;

        for (CrawledHotDealDto deal : deals) {
            try {
                saveOrUpdateDeal(deal);
                successCount++;
            } catch (DataAccessException e) {
                log.error("[FM크롤링 매니저] 데이터베이스 오류 발생 (PostId: {}, ERROR: {})", deal.postId(),
                    DATABASE_OPERATION_FAILED, e);
                failCount++;
            } catch (Exception e) {
                failCount++;
                ErrorCode unexpectedErrorCode = ErrorCode.CRAWLER_UNEXPECTED_ERROR;
                log.error("[FM크롤링 매니저] 예기치 않은 오류 발생 (PostId: {}, ERROR: {})", deal.postId(),
                    unexpectedErrorCode.getCode(), e);
            }
        }
        log.info("[FM크롤링 매니저] 동기화 최종 결과: 성공 {}, 실패 {}", successCount, failCount);
    }

    /**
     * 크롤링된 핫딜을 DB에 저장하거나 상태 업데이트
     */
    @Transactional
    public void saveOrUpdateDeal(CrawledHotDealDto deal) {
        var optDeal = hotDealRepository.findByCommunityCodeAndPostId(FMKOREA, deal.postId());

        optDeal.ifPresentOrElse(
            existsDeal -> existsDeal.updateStatusIfEnded(deal.status()),
            () -> insertNewDeal(deal)
        );
    }

    /**
    * ACTIVE 상태의 핫딜만 새로 추가합니다.
    * 종료된 핫딜은 새로 추가하지 않습니다.
    */
    private void insertNewDeal(CrawledHotDealDto deal) {
        if (deal.status() == HotDealStatus.ACTIVE) {
            var newDeal = HotDeal.builder()
                .communityCode(FMKOREA)
                .postId(deal.postId())
                .postUrl(deal.url())
                .title(deal.title())
                .status(deal.status())
                .build();
            hotDealRepository.save(newDeal);
        }
    }

}
