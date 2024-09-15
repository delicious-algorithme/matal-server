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
            + "AND (:nameOrMenuOrStation IS NULL "
            + "OR s.name LIKE %:nameOrMenuOrStation% "
            + "OR s.keyword LIKE %:nameOrMenuOrStation% "
            + "OR s.mainMenu LIKE %:nameOrMenuOrStation% "
            + "OR s.nearByStation LIKE %:nameOrMenuOrStation%) "

            + "AND (:category IS NULL OR s.category LIKE %:category%) "
            + "AND (:address IS NULL OR s.address LIKE %:address%) "
            + "AND (:reviewsCount IS NULL OR s.reviewsCount >= :reviewsCount) ")
    Page<StoreInfo> filterStoresByCriteria(@Param("storeIds") List<Long> storeIds,
                                           @Param("nameOrMenuOrStation") String nameOrMenuOrStation,

                                           @Param("category") String category,
                                           @Param("address") String address,
                                           @Param("reviewsCount") Long reviewsCount,
                                           Pageable pageable);

}