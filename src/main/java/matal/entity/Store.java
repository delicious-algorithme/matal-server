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
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String naver_map_url;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private String opening_hours;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer review_count;

    @Column(nullable = false)
    private String near_station;

    @Column(nullable = true)
    private String phone_number;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String menu;

    @OneToMany(mappedBy = "store")
    private List<Review> Reviews = new ArrayList<>();

    @Builder
    public Store(Long id, String name, String category,
                 String naver_map_url, Double rating,
                 String opening_hours, String address,
                 Integer review_count, String near_station,
                 String phone_number, Double latitude,
                 Double longitude, String menu) {
        this.id = id;
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
        this.menu = menu;
    }
}
