package matal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private String keyword;  // 'Keyword' 컬럼

    @Column(nullable = false)
    private String name;  // 'Name' 컬럼

    @Column(nullable = false, length = 500)
    private String naver_map_url;  // 'Store link' 컬럼

    @Column(nullable = false)
    private String category;  // 'Category' 컬럼

    @Column(nullable = false)
    private Integer review_count;  // 'Reviews Count' 컬럼

    @Column(nullable = false)
    private String address;  // 'Address' 컬럼

    @Column(nullable = false)
    private String near_station;  // 'Nearby Station' 컬럼

    @Column(nullable = true)
    private String phone_number;  // 'Phone' 컬럼

    @Column(nullable = false, length = 500)
    private String opening_hours;  // 'Business Hours' 컬럼

    @Column(nullable = false)
    private Double latitude;  // 'Latitude' 컬럼

    @Column(nullable = false)
    private Double longitude;  // 'Longitude' 컬럼

    @Column(nullable = true)
    private String positive_keywords;  // 'Positive Keywords' 컬럼

    @OneToMany(mappedBy = "store")
    private  List<Review> review_summary=new ArrayList<>();;  // 'Review Summary' 컬럼

    @Column(nullable = false)
    private Double rating;  // 'Rating' 컬럼

    @Column(nullable = true)
    private Double positive_ratio;  // 'Positive Ratio' 컬럼

    @Column(nullable = true)
    private Double negative_ratio;  // 'Negative Ratio' 컬럼

    @Builder
    public Store(Long id, String keyword, String name, String category, String naver_map_url, Double rating,
                 String opening_hours, String address, Integer review_count, String near_station,
                 String phone_number, Double latitude, Double longitude, String positive_keywords,
                  Double positive_ratio, Double negative_ratio) {
        this.id = id;
        this.keyword = keyword;
        this.name = name;
        this.category = category;
        this.naver_map_url = naver_map_url;
        this.rating = rating;
        this.opening_hours = opening_hours;
        this.address = address;
        this.review_count = review_count;
        this.near_station = near_station;
        this.phone_number = phone_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.positive_keywords = positive_keywords;
        this.positive_ratio = positive_ratio;
        this.negative_ratio = negative_ratio;
    }

}
