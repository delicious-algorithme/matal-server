package matal.store.domain.repository;

import java.util.List;
import matal.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

    Page<Store> searchAndFilterStores(List<Long> fullTextResultIds,
                                      List<String> category,
                                      List<String> addresses,
                                      List<String> positiveKeyword,
                                      Double rating,
                                      Double positiveRatio,
                                      Long reviewsCount,
                                      Boolean soloDining,
                                      Boolean parking,
                                      Boolean waiting,
                                      Boolean petFriendly,
                                      Pageable pageable);
}
