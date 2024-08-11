package matal.store.repository;

import matal.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByNameContaining(String searchKeyword);
    List<Store> findByCategoryContaining(String searchKeyword);

    @Query("SELECT s FROM Store s WHERE s.nearby_station LIKE %:nearby_station%")
    List<Store> findByNearbyStationContaining(@Param("nearby_station") String nearby_station);
}