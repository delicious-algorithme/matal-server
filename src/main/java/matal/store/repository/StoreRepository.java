package matal.store.repository;

import matal.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s " +
            "WHERE (:name IS NULL OR s.name LIKE %:name%) " +
            "AND (:category IS NULL OR s.category LIKE %:category%) " +
            "AND (:nearby_station IS NULL OR s.nearby_station LIKE %:nearby_station%)"+
            "AND (:keywords Is NULL OR s.positive_keywords LIKE %:keywords% OR s.review_summary LIKE %:keywords%)")
    Page<Store> findStoresByCriteria(@Param("name") String name,
                                     @Param("category") String category,
                                     @Param("nearby_station") String nearby_station,
                                     @Param("keywords") String keywords,
                                     Pageable pageable);
}