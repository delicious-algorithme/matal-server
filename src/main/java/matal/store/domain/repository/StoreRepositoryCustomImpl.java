package matal.store.domain.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matal.store.domain.Store;
import matal.store.dto.SortTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static matal.store.domain.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Store> searchAndFilterStores(List<Long> fullTextResultIds,
                                             List<String> categories,
                                             List<String> addresses,
                                             List<String> positiveKeyword,
                                             Double rating,
                                             Double positiveRatio,
                                             Long reviewsCount,
                                             Boolean soloDining,
                                             Boolean parking,
                                             Boolean waiting,
                                             Boolean petFriendly,
                                             String sortTarget,
                                             Pageable pageable)
    {
        QueryResults<Store> result =
                jpaQueryFactory
                        .select(store)
                        .from(store)
                        .where(
                                searchResultIdsIn(fullTextResultIds),
                                categoriesLike(categories),
                                addressesLike(addresses),
                                positiveKeywordsLike(positiveKeyword),
                                positiveRatioGoe(positiveRatio),
                                reviewsCountGoe(reviewsCount),
                                ratingGoe(rating),
                                soloDiningEq(soloDining),
                                parkingEq(parking),
                                waitingEq(waiting),
                                petFriendlyEq(petFriendly))
                        .orderBy(sortTargetDesc(sortTarget))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();


        List<Store> stores = result.getResults();
        long totalSize = result.getTotal();

        return new PageImpl<>(stores, pageable, totalSize);
    }

    private BooleanExpression searchResultIdsIn(List<Long> fullTextResultIds) {
        return fullTextResultIds == null ? null : store.storeId.in(fullTextResultIds);
    }

    private BooleanExpression categoriesLike(List<String> categories) {
        return categories == null ? null
                : Expressions.anyOf(categories.stream().map(this::categoryLike).toArray(BooleanExpression[]::new));
    }

    private BooleanExpression categoryLike(String category) {
        return category == null ? null : store.category.contains(category);
    }

    private BooleanExpression addressesLike(List<String> addresses) {
        return addresses == null ? null
                : Expressions.anyOf(addresses.stream().map(this::addressLike).toArray(BooleanExpression[]::new));
    }

    private BooleanExpression addressLike(String address) {
        return address == null ? null : store.address.contains(address);
    }

    private BooleanExpression positiveKeywordsLike(List<String> positiveKeywords) {
        return positiveKeywords == null ? null
                : Expressions.anyOf(positiveKeywords.stream().map(this::positiveKeywordLike).toArray(BooleanExpression[]::new));
    }

    private BooleanExpression positiveKeywordLike(String positiveKeyword) {
        return positiveKeyword == null ? null : store.positiveKeywords.contains(positiveKeyword);
    }


    private BooleanExpression positiveRatioGoe(Double positiveRatio) {
        return positiveRatio == null ? null : store.positiveRatio.goe(positiveRatio);
    }

    private BooleanExpression reviewsCountGoe(Long reviewsCount) {
        return reviewsCount == null ? null : store.reviewsCount.goe(reviewsCount);
    }

    private BooleanExpression ratingGoe(Double rating) {
        return rating == null ? null : store.rating.goe(rating);
    }

    private BooleanExpression soloDiningEq(Boolean soloDining) {
        return soloDining == null ? null : store.isSoloDining.eq(soloDining);
    }

    private BooleanExpression parkingEq(Boolean parking) {
        return parking == null ? null : store.isParking.eq(parking);
    }

    private BooleanExpression waitingEq(Boolean waiting) {
        return waiting == null ? null : store.isWaiting.eq(waiting);
    }

    private BooleanExpression petFriendlyEq(Boolean petFriendly) {
        return petFriendly == null ? null : store.isPetFriendly.eq(petFriendly);
    }

    private OrderSpecifier<Double> sortTargetDesc(String sortTarget) {
        if(sortTarget.equals(SortTarget.POSITIVE_RATIO.getName()))
            return store.positiveRatio.desc();
        return store.rating.desc();
    }
}
