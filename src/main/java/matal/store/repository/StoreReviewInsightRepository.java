package matal.store.repository;

import matal.store.entity.StoreReviewInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreReviewInsightRepository extends JpaRepository<StoreReviewInsight, Long> {

    @Query("SELECT sr.storeInfo.storeId FROM StoreReviewInsight sr "
            + "WHERE (:rating IS NULL OR sr.rating >= :rating) "
            + "AND (:positiveRatio IS NULL OR sr.positiveRatio >= :positiveRatio) "
            + "AND (:reviewword IS NULL OR sr.positiveKeywords LIKE %:reviewword% OR sr.reviewSummary LIKE %:reviewword%) "

            + "AND (:isSoloDining IS NULL OR sr.isSoloDining = :isSoloDining) "
            + "AND (:isParking IS NULL OR sr.isParking = :isParking)"
            + "AND (:isWaiting IS NULL OR sr.isWaiting = :isWaiting) "
            + "AND (:isPetFriendly IS NULL OR sr.isPetFriendly = :isPetFriendly)"

            + "ORDER BY CASE WHEN :sortBy = 'rating' THEN sr.rating "
            + "WHEN :sortBy = 'positiveRatio' THEN sr.positiveRatio "
            + "ELSE sr.rating END ASC")
    List<Long> findStoreIdsByReviewCriteriaRatingASC(@Param("rating") Double rating,
                                            @Param("positiveRatio") Double positiveRatio,
                                            @Param("reviewword") String reviewword,
                                            @Param("isSoloDining") Boolean isSoloDining,
                                            @Param("isParking") Boolean isParking,
                                            @Param("isWaiting") Boolean isWaiting,
                                            @Param("isPetFriendly") Boolean isPetFriendly,
                                            @Param("sortBy") String sortBy);


    @Query("SELECT sr.storeInfo.storeId FROM StoreReviewInsight sr "
            + "WHERE (:rating IS NULL OR sr.rating >= :rating) "
            + "AND (:positiveRatio IS NULL OR sr.positiveRatio >= :positiveRatio) "
            + "AND (:reviewword IS NULL OR sr.positiveKeywords LIKE %:reviewword% OR sr.reviewSummary LIKE %:reviewword%) "

            + "AND (:isSoloDining IS NULL OR sr.isSoloDining = :isSoloDining) "
            + "AND (:isParking IS NULL OR sr.isParking = :isParking)"
            + "AND (:isWaiting IS NULL OR sr.isWaiting = :isWaiting) "
            + "AND (:isPetFriendly IS NULL OR sr.isPetFriendly = :isPetFriendly)"

            + "ORDER BY CASE WHEN :sortBy = 'rating' THEN sr.rating "
            + "WHEN :sortBy = 'positiveRatio' THEN sr.positiveRatio "
            + "ELSE sr.rating END DESC")
    List<Long> findStoreIdsByReviewCriteriaRatingDESC(@Param("rating") Double rating,
                                                     @Param("positiveRatio") Double positiveRatio,
                                                     @Param("reviewword") String reviewword,
                                                     @Param("isSoloDining") Boolean isSoloDining,
                                                     @Param("isParking") Boolean isParking,
                                                     @Param("isWaiting") Boolean isWaiting,
                                                     @Param("isPetFriendly") Boolean isPetFriendly,
                                                     @Param("sortBy") String sortBy);
}