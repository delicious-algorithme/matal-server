package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreListResponseDto;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Page<StoreListResponseDto> searchAndFilterStores(
            String searchKeywords,
            String category,
            String addresse,
            String positiveKeywords,
            Double minPositiveRatio,
            Long reviewsCount,
            Double rating,
            Boolean soloDining,
            Boolean parking,
            Boolean waiting,
            Boolean petFriendly,
            String orderByRating,
            String orderByPositiveRatio,
            int page) {

        Pageable pageable = PageRequest.of(page, 10);

        return storeRepository.searchAndFilterStores(
                searchKeywords,
                category,
                addresse,
                positiveKeywords,
                minPositiveRatio,
                reviewsCount,
                rating,
                soloDining,
                parking,
                waiting,
                petFriendly,
                orderByRating,
                orderByPositiveRatio,
                pageable).map(StoreListResponseDto::from);
    }

    public StoreResponseDto findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 가게 입니다."));

        return StoreResponseDto.from(store);
    }

    public Page<StoreListResponseDto> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return storeRepository.findAll(pageable).map(StoreListResponseDto::from);
    }

    public Page<StoreListResponseDto> findTop() {
        Sort sortAll = Sort.by("rating").descending()
                .and(Sort.by("positiveRatio").descending());
        Pageable pageable = PageRequest.of(0, 10, sortAll);
        return storeRepository.findAll(pageable).map(StoreListResponseDto::from);
    }
}