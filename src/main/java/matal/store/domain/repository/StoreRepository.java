package matal.store.domain.repository;

import java.util.List;
import matal.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    @Query(value = "SELECT store_id "
            + "FROM store "
            + "WHERE MATCH("
            + "keyword, "
            + "name, "
            + "category, "
            + "address, "
            + "nearby_station, "
            + "main_menu, "
            + "positive_keywords, "
            + "review_summary, "
            + "recommended_menu) "
            + "AGAINST (:searchKeywords)", nativeQuery = true)
    List<Long> searchByFullText(@Param("searchKeywords") String searchKeywords);
}
