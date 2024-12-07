package matal.store.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.store.dto.RestPage;
import matal.store.dto.SortTarget;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private static final int PAGE_SIZE = 10;
    private static final int TOP10_PAGE = 0;

    private final StoreRepository storeRepository;

    public Page<StoreListResponseDto> searchAndFilterStores(StoreSearchFilterRequestDto filterRequestDto) {

        Pageable pageable = PageRequest.of(filterRequestDto.page(), PAGE_SIZE);

        List<Long> fullTextResultIds = null;

        if (filterRequestDto.searchKeywords() != null && !filterRequestDto.searchKeywords().isBlank()) {
            fullTextResultIds = storeRepository
                    .searchByFullText(filterRequestDto.searchKeywords());
        }

        return storeRepository.searchAndFilterStores(
                fullTextResultIds,
                filterRequestDto.category(),
                filterRequestDto.addresses(),
                filterRequestDto.positiveKeyword(),
                filterRequestDto.rating(),
                filterRequestDto.positiveRatio(),
                filterRequestDto.reviewsCount(),
                filterRequestDto.isSoloDining(),
                filterRequestDto.isParking(),
                filterRequestDto.isWaiting(),
                filterRequestDto.isPetFriendly(),
                filterRequestDto.sortTarget(),
                pageable
        ).map(StoreListResponseDto::from);
    }

    @Cacheable(value = "store", key = "'store_' + #storeId")
    public StoreResponseDto findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID));

        return StoreResponseDto.from(store);
    }

    @Cacheable(value = "stores", key = "'stores_' + #sortTarget + '_' + #page")
    public RestPage<StoreListResponseDto> findAll(int page,
                                                  String sortTarget) {

        Sort sort = Sort.by(Direction.DESC, sortTarget);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        Page<StoreListResponseDto> responseDtoPage =
                storeRepository.findAll(pageable)
                        .map(StoreListResponseDto::from);

        return new RestPage<>(responseDtoPage);
    }

    @Cacheable(value = "top10Stores", key = "'topStores_10'")
    public RestPage<StoreListResponseDto> findTop10Stores() {

        Sort sortAll = Sort.by(SortTarget.RATING.getName()).descending()
                .and(Sort.by(SortTarget.POSITIVE_RATIO.getName()).descending());
        Pageable pageable = PageRequest.of(TOP10_PAGE, PAGE_SIZE, sortAll);

        Page<StoreListResponseDto> responseDtoPage =
                storeRepository.findAll(pageable).map(StoreListResponseDto::from);

        return new RestPage<>(responseDtoPage);
    }
}