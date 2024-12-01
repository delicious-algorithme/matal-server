package matal.store.domain.repository;

import matal.store.domain.Store;
import org.springframework.data.domain.Page;

public interface StoreRepositoryCustom {

    Page<Store> search();
}
