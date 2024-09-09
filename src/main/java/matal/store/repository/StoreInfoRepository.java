package matal.store.repository;

import matal.store.entity.StoreInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoreInfoRepository extends JpaRepository<StoreInfo, Long> {

    @Query("SELECT s FROM StoreInfo s WHERE "
            + "(:storeIds IS NULL OR s.storeId IN :storeIds) "
            + "AND (:name IS NULL OR s.name LIKE %:name%) "
            + "AND (:category IS NULL OR s.category LIKE %:category%) "
            + "AND (:nearby_station IS NULL OR s.nearByStation LIKE %:nearby_station%)")
    Page<StoreInfo> findStoresByCriteria(@Param("storeIds") List<Long> storeIds,
                                         @Param("name") String name,
                                         @Param("category") String category,
                                         @Param("nearby_station") String nearby_station,
                                         Pageable pageable);

    @Query("SELECT s FROM StoreInfo s WHERE "
            + "(:storeIds IS NULL OR s.storeId IN :storeIds) "
            + "AND (:name IS NULL OR s.name LIKE %:name%) "
            + "AND (:nearby_station IS NULL OR s.nearByStation LIKE %:nearby_station%) "
            + "AND (:keyword IS NULL OR s.keyword LIKE %:keyword%) "
            + "AND (:address IS NULL OR s.address LIKE %:address%) "
            + "AND (:reviewsCount IS NULL OR s.reviewsCount >= :reviewsCount) ")
    Page<StoreInfo> filterStoresByCriteria(@Param("storeIds") List<Long> storeIds,
                                           @Param("name") String name,
                                           @Param("nearby_station") String nearby_station,
                                           @Param("keyword") String keyword,
                                           @Param("address") String address,
                                           @Param("reviewsCount") Long reviewsCount,
                                           Pageable pageable);

}