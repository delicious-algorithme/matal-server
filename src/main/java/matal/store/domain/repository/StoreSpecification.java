package matal.store.domain.repository;

import jakarta.persistence.criteria.Predicate;
import matal.store.domain.Store;
import matal.store.dto.request.StoreSearchFilterRequestDto;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class StoreSpecification {
    public static Specification<Store> withFilters(StoreSearchFilterRequestDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSearchKeywords() != null) {
                predicates.add(cb.like(root.get("keyword"), "%" + filter.getSearchKeywords() + "%"));
            }

            if (filter.getCategory() != null) {
                predicates.add(cb.equal(root.get("category"), filter.getCategory()));
            }

            if (filter.getAddresses() != null) {
                predicates.add(cb.like(root.get("address"), "%" + filter.getAddresses() + "%"));
            }

            if (filter.getPositiveKeyword() != null) {
                predicates.add(cb.like(root.get("positive_keywords"), "%" + filter.getPositiveKeyword() + "%"));
            }

            if (filter.getPositiveRatio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("positive_ratio"), filter.getPositiveRatio()));
            }

            if (filter.getReviewsCount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("reviews_count"), filter.getReviewsCount()));
            }

            if (filter.getRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), filter.getRating()));
            }

            if (filter.getIsSoloDining() != null) {
                predicates.add(cb.equal(root.get("solodining"), filter.getIsSoloDining()));
            }

            if (filter.getIsParking() != null) {
                predicates.add(cb.equal(root.get("parking"), filter.getIsParking()));
            }

            if (filter.getIsWaiting() != null) {
                predicates.add(cb.equal(root.get("waiting"), filter.getIsWaiting()));
            }

            if (filter.getIsPetFriendly() != null) {
                predicates.add(cb.equal(root.get("petfriendly"), filter.getIsPetFriendly()));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
