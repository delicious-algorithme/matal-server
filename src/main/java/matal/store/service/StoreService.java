package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.StoreInfo;
import matal.store.entity.StoreReviewInsight;
import matal.store.repository.StoreInfoRepository;
import matal.store.repository.StoreReviewInsightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreInfoRepository storeInfoRepository;
    private final StoreReviewInsightRepository storeReviewInsightRepository;

    public Page<StoreResponseDto> findStores(String name, String category, String stationName, String keywords, int page, String sortBy, String sortOrder) {
        if (page < 0) throw new IllegalArgumentException("Invalid page number");

        Sort.Direction direction = Sort.Direction.fromString(sortOrder.toUpperCase());
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sortBy));
        Page<StoreInfo> storePage = storeInfoRepository.findStoresByCriteria(name, category, stationName, keywords, pageable);

        return storePage.map(this::convertToStoreResponseDto);
    }

    public StoreResponseDto findById(Long id) {
        StoreInfo storeInfo = storeInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게 상세 정보를 조회할 수 없습니다."));

        StoreReviewInsight storeReviewInsight = storeReviewInsightRepository.findById(storeInfo.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게 분석 정보를 조회할 수 없습니다."));

        return StoreResponseDto.from(storeInfo, storeReviewInsight);
    }

    public Page<StoreResponseDto> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<StoreInfo> storePage = storeInfoRepository.findAll(pageable);

        return storePage.map(this::convertToStoreResponseDto);
    }

    private StoreResponseDto convertToStoreResponseDto(StoreInfo storeInfo) {
        StoreReviewInsight storeReviewInsight = storeReviewInsightRepository.findById(storeInfo.getStoreId())
                .orElse(null);
        return StoreResponseDto.from(storeInfo, storeReviewInsight);
    }
}