package matal.store.repository;

import matal.store.entity.StoreInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreInfoRepository extends JpaRepository<StoreInfo, Long> {

    @Query("SELECT s FROM StoreInfo s " +
            "WHERE (:name IS NULL OR s.name LIKE %:name%) " +
            "AND (:category IS NULL OR s.category LIKE %:category%) " +
            "AND (:nearby_station IS NULL OR s.nearByStation LIKE %:nearby_station%)"+
            "AND (:keywords Is NULL OR s.keyword LIKE %:keywords%)")
    Page<StoreInfo> findStoresByCriteria(@Param("name") String name,
                                         @Param("category") String category,
                                         @Param("nearby_station") String nearby_station,
                                         @Param("keywords") String keywords,
                                         Pageable pageable);
}