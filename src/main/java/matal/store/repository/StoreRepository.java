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

    Page <Store> findByNameContaining(String searchKeyword, Pageable pageable);
    Page <Store> findByCategoryContaining(String searchKeyword, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.nearby_station LIKE %:nearby_station%")
    Page <Store> findByNearbyStationContaining(@Param("nearby_station") String nearby_station, Pageable pageable);
}