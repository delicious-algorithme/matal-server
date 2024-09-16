package matal.store.repository;

import matal.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT * FROM store WHERE " +
            "(:searchKeywords IS NULL OR MATCH(keyword, name, category, address, nearby_station, main_menu, positive_keywords, review_summary, recommended_menu) AGAINST (:searchKeywords IN BOOLEAN MODE)) AND " +
            "(:category IS NULL OR category = :category) AND " +
            "(:addresses IS NULL OR address REGEXP :addresses) AND " +
            "(:positiveKeyword IS NULL OR positive_keywords LIKE %:positiveKeyword%) AND " +
            "(:positiveRatio IS NULL OR positive_ratio >= :positiveRatio) AND " +
            "(:reviewsCount IS NULL OR reviews_count >= :reviewsCount) AND " +
            "(:rating IS NULL OR rating >= :rating) AND " +
            "(:soloDining IS NULL OR solo_dining = :soloDining) AND " +
            "(:parking IS NULL OR parking = :parking) AND " +
            "(:waiting IS NULL OR waiting = :waiting) AND " +
            "(:petFriendly IS NULL OR pet_friendly = :petFriendly) " +
            "ORDER BY " +
            "IF(:orderByRating = 'ASC', rating, NULL) ASC, " +
            "IF(:orderByRating = 'DESC', rating, NULL) DESC, " +
            "IF(:orderByPositiveRatio = 'ASC', positive_ratio, NULL) ASC, " +
            "IF(:orderByPositiveRatio = 'DESC', positive_ratio, NULL) DESC",
            nativeQuery = true)
    Page<Store> searchAndFilterStores(
            @Param("searchKeywords") String searchKeywords,
            @Param("category") String category,
            @Param("addresses") String addresses,
            @Param("positiveKeyword") String positiveKeyword,
            @Param("positiveRatio") Double positiveRatio,
            @Param("reviewsCount") Long reviewsCount,
            @Param("rating") Double rating,
            @Param("soloDining") Boolean soloDining,
            @Param("parking") Boolean parking,
            @Param("waiting") Boolean waiting,
            @Param("petFriendly") Boolean petFriendly,
            @Param("orderByRating") String orderByRating,
            @Param("orderByPositiveRatio") String orderByPositiveRatio,
            Pageable pageable);
}
