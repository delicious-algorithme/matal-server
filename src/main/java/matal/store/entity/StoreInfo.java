package matal.store.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "store_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private String name;

    @Column(name = "store_link", nullable = true, length = 1000)
    private String storeLink;

    @Column(nullable = false)
    private String category;

    @Column(name = "reviews_count", nullable = false)
    private Long reviewsCount;

    @Column(nullable = false)
    private String address;

    @Column(name = "nearby_station", nullable = false)
    private String nearByStation;

    @Column(nullable = true)
    private String phone;

    @Column(name = "business_hours", nullable = true, length = 1000)
    private String businessHours;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "menu_and_price", nullable = false)
    private String menuAndPrice;

    @Column(name = "image_urls", nullable = false, length = 1000)
    private String imageUrls;

    @Builder
    public StoreInfo(Long storeId,
                     String keyword,
                     String name,
                     String storeLink,
                     String category,
                     Long reviewsCount,
                     String address,
                     String nearByStation,
                     String phone,
                     String businessHours,
                     Double latitude,
                     Double longitude,
                     String menuAndPrice,
                     String imageUrls) {
        this.storeId = storeId;
        this.keyword = keyword;
        this.name = name;
        this.storeLink = storeLink;
        this.category = category;
        this.reviewsCount = reviewsCount;
        this.address = address;
        this.nearByStation = nearByStation;
        this.phone = phone;
        this.businessHours = businessHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.menuAndPrice = menuAndPrice;
        this.imageUrls = imageUrls;
    }
}
