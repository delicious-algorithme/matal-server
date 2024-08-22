package matal.store.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "Store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, length = 1000)
    private String store_link;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long reviews_count;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String nearby_station;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true, length = 1000)
    private String business_hours;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String positive_keywords;

    @Column(nullable = false, length = 1000)
    private String review_summary;

    @Column(nullable = true)
    private Double rating;

    @Column(nullable = false)
    private Double positive_ratio;

    @Column(nullable = false)
    private Double negative_ratio;

    @Builder
    public Store(Long id, String keyword, String name, String store_link,
                 String category, Long reviews_count, String address,
                 String nearby_station, String phone, String business_hours,
                 Double latitude, Double longitude, String positive_keywords,
                 String review_summary, Double rating,
                 Double positive_ratio, Double negative_ratio) {
        this.id = id;
        this.keyword = keyword;
        this.name = name;
        this.store_link = store_link;
        this.category = category;
        this.reviews_count = reviews_count;
        this.address = address;
        this.nearby_station = nearby_station;
        this.phone = phone;
        this.business_hours = business_hours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.positive_keywords = positive_keywords;
        this.review_summary = review_summary;
        this.rating = rating;
        this.positive_ratio = positive_ratio;
        this.negative_ratio = negative_ratio;
    }
}
