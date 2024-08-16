package matal.store.repository;

import matal.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<List<Store>> findByNameContaining(String searchKeyword);
    Optional<List<Store>> findByCategoryContaining(String searchKeyword);

    @Query("SELECT s FROM Store s WHERE s.nearby_station LIKE %:nearby_station%")
    Optional<List<Store>> findByNearbyStationContaining(@Param("nearby_station") String nearby_station);
}