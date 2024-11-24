package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.store.dto.request.StoreSearchFilterRequestDto;
import matal.store.dto.response.StoreListResponseDto;
import matal.store.dto.response.StoreResponseDto;
import matal.store.domain.Store;
import matal.store.domain.repository.StoreRepository;
import org.springframework.cache.annotation.Cacheable;
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

    private static final int PAGE_SIZE = 10;

    private final StoreRepository storeRepository;

    public Page<StoreListResponseDto> searchAndFilterStores(StoreSearchFilterRequestDto filterRequestDto) {

        Pageable pageable = PageRequest.of(filterRequestDto.getPage(), PAGE_SIZE);

        return storeRepository.searchAndFilterStores(
                filterRequestDto.getSearchKeywords(),
                filterRequestDto.convertCategoryToString(),
                filterRequestDto.convertAddressesToString(),
                filterRequestDto.convertPositiverKeywordsToString(),
                filterRequestDto.getPositiveRatio(),
                filterRequestDto.getReviewsCount(),
                filterRequestDto.getRating(),
                filterRequestDto.getIsSoloDining(),
                filterRequestDto.getIsParking(),
                filterRequestDto.getIsWaiting(),
                filterRequestDto.getIsPetFriendly(),
                filterRequestDto.getOrderByRating(),
                filterRequestDto.getOrderByPositiveRatio(),
                pageable
        ).map(StoreListResponseDto::from);
    }

    @Cacheable(value = "store", key = "'store_' + #storeId")
    public StoreResponseDto findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID));

        return StoreResponseDto.from(store);
    }

    @Cacheable(value = "stores", key = "'stores_' + #page")
    public Page<StoreListResponseDto> findAll(int page,
                                              String orderByRatio,
                                              String orderByPositiveRatio) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return storeRepository.findAllOrderByRatingOrPositiveRatio(orderByRatio,orderByPositiveRatio, pageable).map(StoreListResponseDto::from);
    }

    @Cacheable(value = "top10Stores", key = "'topStores_10'")
    public Page<StoreListResponseDto> findTop() {
        Sort sortAll = Sort.by("rating").descending()
                .and(Sort.by("positiveRatio").descending());
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, sortAll);
        return storeRepository.findAll(pageable).map(StoreListResponseDto::from);
    }
}