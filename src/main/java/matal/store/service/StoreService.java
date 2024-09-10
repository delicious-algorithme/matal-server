package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.StoreInfo;
import matal.store.entity.StoreReviewInsight;
import matal.store.repository.StoreInfoRepository;
import matal.store.repository.StoreReviewInsightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreInfoRepository storeInfoRepository;
    private final StoreReviewInsightRepository storeReviewInsightRepository;

    public Page<StoreResponseDto> categoryStores(String name, String category, String stationName,
                                                 int page, String sortBy, String sortOrder) {
        if (page < 0) throw new IllegalArgumentException("Invalid page number");

        Pageable pageable = PageRequest.of(page, 10);

        List<Long> storeIds;
        if(sortOrder.equals("ASC")) {
            storeIds = storeReviewInsightRepository.findStoreIdsByReviewCriteriaRatingASC(
                    null, null, null,
                    null, null, null,
                    null, sortBy);
        } else {
            storeIds = storeReviewInsightRepository.findStoreIdsByReviewCriteriaRatingDESC(
                    null, null, null,
                    null, null, null,
                    null, sortBy);
        }

        Page<StoreInfo> storePage = storeInfoRepository.findStoresByCriteria(storeIds, name, category,
                stationName, pageable);

        List<StoreInfo> filteredStores = new ArrayList<>(storePage.getContent());
        filteredStores.sort(Comparator.comparing(store -> storeIds.indexOf(store.getStoreId())));

        return new PageImpl<>(filteredStores, pageable,
                storePage.getTotalElements())
                .map(this::convertToStoreResponseDto);

    }

    public Page<StoreResponseDto> filterStores(String name, String stationName, String keyword,
                                               String address, Long reviewsCount, Double rating,
                                               Double positiveRatio, String reviewword, Boolean isSoloDining,
                                               Boolean isParking, Boolean isWaiting, Boolean isPetFriendly,
                                               int page, String sortBy, String sortOrder) {
        if (page < 0) throw new IllegalArgumentException("Invalid page number");

        Pageable pageable = PageRequest.of(page, 10);
        List<Long> storeIds;
        if(sortOrder.equals("ASC")) {
            storeIds = storeReviewInsightRepository.findStoreIdsByReviewCriteriaRatingASC(
                    rating, positiveRatio, reviewword,
                    isSoloDining, isParking, isWaiting,
                    isPetFriendly, sortBy);
        } else {
            storeIds = storeReviewInsightRepository.findStoreIdsByReviewCriteriaRatingDESC(
                    rating, positiveRatio, reviewword,
                    isSoloDining, isParking, isWaiting,
                    isPetFriendly, sortBy);
        }

        Page<StoreInfo> storePage = storeInfoRepository.filterStoresByCriteria(
                storeIds, name, stationName,
                keyword, address, reviewsCount, pageable);

        List<StoreInfo> filteredStores = new ArrayList<>(storePage.getContent());
        filteredStores.sort(Comparator.comparing(
                store -> storeIds.indexOf(store.getStoreId())));

        return new PageImpl<>(filteredStores, pageable,
                storePage.getTotalElements())
                .map(this::convertToStoreResponseDto);
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