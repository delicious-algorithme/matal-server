package matal.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store_review_insight")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReviewInsight {

    @Id
    private Long storeId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_info_id")
    private StoreInfo storeInfo;

    @Column(name = "recommended_menu", nullable = false, length = 500)
    private String recommendMenu;

    @Column(name = "positive_keywords", nullable = false)
    private String positiveKeywords;

    @Column(name = "negative_keywords", nullable = false)
    private String negativeKeywords;

    @Column(name = "review_summary", nullable = false, length = 1000)
    private String reviewSummary;

    @Column(nullable = true)
    private Double rating;

    @Column(name = "positive_ratio", nullable = false)
    private Double positiveRatio;

    @Column(name = "negative_ratio", nullable = false)
    private Double negativeRatio;

    @Column(name = "neutral_ratio", nullable = false)
    private Double neutralRatio;

    @Column(name = "solo_dining", nullable = false)
    private Boolean isSoloDining;

    @Column(name = "parking", nullable = false)
    private Boolean isParking;

    @Column(name = "parking_tip", nullable = false, length = 500)
    private String parkingTip;

    @Column(name = "waiting", nullable = false)
    private Boolean isWaiting;

    @Column(name = "waiting_tip", nullable = false, length = 500)
    private String waitingTip;

    @Column(name = "pet_friendly", nullable = false)
    private Boolean isPetFriendly;

    @Builder
    public StoreReviewInsight(Long storeId,
                              StoreInfo storeInfo,
                              String recommendMenu,
                              String positiveKeywords,
                              String negativeKeywords,
                              String reviewSummary,
                              Double rating,
                              Double positiveRatio,
                              Double negativeRatio,
                              Double neutralRatio,
                              Boolean isSoloDining,
                              Boolean isParking,
                              String parkingTip,
                              Boolean isWaiting,
                              String waitingTip,
                              Boolean isPetFriendly) {
        this.storeId = storeId;
        this.storeInfo = storeInfo;
        this.recommendMenu = recommendMenu;
        this.positiveKeywords = positiveKeywords;
        this.negativeKeywords = negativeKeywords;
        this.reviewSummary = reviewSummary;
        this.rating = rating;
        this.positiveRatio = positiveRatio;
        this.negativeRatio = negativeRatio;
        this.neutralRatio = neutralRatio;
        this.isSoloDining = isSoloDining;
        this.isParking = isParking;
        this.parkingTip = parkingTip;
        this.isWaiting = isWaiting;
        this.waitingTip = waitingTip;
        this.isPetFriendly = isPetFriendly;
    }
}


