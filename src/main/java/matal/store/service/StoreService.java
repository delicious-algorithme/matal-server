package matal.store.service;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.store.domain.Store;
import matal.store.domain.repository.StoreRepository;
import matal.store.domain.repository.StoreSpecification;
import matal.store.dto.request.StoreSearchFilterRequestDto;
import matal.store.dto.response.StoreListResponseDto;
import matal.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Page<StoreListResponseDto> searchAndFilterStores(StoreSearchFilterRequestDto filterRequestDto) {
        Pageable pageable = PageRequest.of(filterRequestDto.getPage(), 10);

        Specification<Store> spec = StoreSpecification.withFilters(filterRequestDto);

        return storeRepository.findAll(spec, pageable).map(StoreListResponseDto::from);
    }

    public StoreResponseDto findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID));
        return StoreResponseDto.from(store);
    }

    public Page<StoreListResponseDto> findAll(int page, String orderByRating, String orderByPositiveRatio) {
        return null;
    }

    public Page<StoreListResponseDto> findTop() {
        return null;
    }
}

