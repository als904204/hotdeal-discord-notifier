package com.hotdeal.discord.domain.hotdeal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/*
  @UniqueConstraint
 * 핫딜 정보를 크롤링할 때
 * 동일한 커뮤니티의 동일한 게시글(동일한 postId)이 실수로 여러 번 DB에 저장되는 것을 방지
 */
@Table(name = "hot_deal", uniqueConstraints = {
    @UniqueConstraint(name = "uk_hot_deal_community_post",
        columnNames = {"communityCode", "postId"})
})
public class HotDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CommunityType communityCode;

    @Column(nullable = false, length = 100)
    private String postId;

    @Column(nullable = false, length = 512)
    private String postUrl;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HotDealStatus status;

    @Builder
    public HotDeal(CommunityType communityCode, String postId, String postUrl, String title, HotDealStatus status) {
        this.communityCode = communityCode;
        this.postId = postId;
        this.postUrl = postUrl;
        this.title = title;
        this.status = (status != null) ? status : HotDealStatus.ACTIVE;
    }

}
