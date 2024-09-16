package matal.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "keyword", length = 1000)
    private String keyword;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "store_link", length = 1000)
    private String storeLink;

    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "reviews_count")
    private Long reviewsCount;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "nearby_station", length = 1000)
    private String nearbyStation;

    @Column(name = "phone", length = 255)
    private String phone;

    @Column(name = "business_hours", length = 1000)
    private String businessHours;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "main_menu", length = 1000)
    private String mainMenu;

    @Column(name = "image_urls", length = 1000)
    private String imageUrls;

    @Column(name = "positive_keywords", length = 255)
    private String positiveKeywords;

    @Column(name = "negative_keywords", length = 255)
    private String negativeKeywords;

    @Column(name = "review_summary", length = 1000)
    private String reviewSummary;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "positive_ratio")
    private Double positiveRatio;

    @Column(name = "negative_ratio")
    private Double negativeRatio;

    @Column(name = "neutral_ratio")
    private Double neutralRatio;

    @Column(name = "solo_dining")
    private Boolean isSoloDining;

    @Column(name = "parking")
    private Boolean isParking;

    @Column(name = "parking_tip", length = 1000)
    private String parkingTip;

    @Column(name = "waiting")
    private Boolean isWaiting;

    @Column(name = "waiting_tip", length = 1000)
    private String waitingTip;

    @Column(name = "pet_friendly")
    private Boolean isPetFriendly;

    @Column(name = "recommended_menu", length = 1000)
    private String recommendedMenu;

    @Builder
    public Store(Long storeId,
                 String keyword,
                 String name,
                 String storeLink,
                 String category,
                 Long reviewsCount,
                 String address,
                 String nearbyStation,
                 String phone,
                 String businessHours,
                 Double latitude,
                 Double longitude,
                 String mainMenu,
                 String imageUrls,
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
                 Boolean isPetFriendly,
                 String recommendedMenu) {
        this.storeId = storeId;
        this.keyword = keyword;
        this.name = name;
        this.storeLink = storeLink;
        this.category = category;
        this.reviewsCount = reviewsCount;
        this.address = address;
        this.nearbyStation = nearbyStation;
        this.phone = phone;
        this.businessHours = businessHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mainMenu = mainMenu;
        this.imageUrls = imageUrls;
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
        this.recommendedMenu = recommendedMenu;
    }
}

