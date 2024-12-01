package matal.store.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import matal.store.domain.Store;
import org.springframework.data.domain.Page;
import static matal.store.domain.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Store> search() {
        jpaQueryFactory.select(store).from(store)
                .where()
    }
}
