package matal.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "Review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String review_summary;

    @Column(nullable = false)
    private String review_keywords;

    @Column(nullable = false)
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Review(Long id, String review_summary,
                  String review_keywords, Double rating) {
        this.id = id;
        this.review_summary = review_summary;
        this.review_keywords = review_keywords;
        this.rating = rating;
    }
}
