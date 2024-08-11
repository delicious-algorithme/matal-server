package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreRequestDto;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    //가게 이름 검색 조회 리스트
    public List<StoreResponseDto> StoreNameSearch(StoreRequestDto storeRequestDto) {
        List<Store> storeList = storeRepository.findByNameContaining(storeRequestDto.getName());
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

        for(Store store : storeList) {
            StoreResponseDto dto = StoreResponseDto.builder()
                    .id(store.getId())
                    .name(store.getName())
                    .category(store.getCategory())
                    .reviews_count(store.getReviews_count())
                    .address(store.getAddress())
                    .nearby_station(store.getNearby_station())
                    .phone(store.getPhone())
                    .business_hours(store.getBusiness_hours())
                    .rating(store.getRating())
                    .build();
            storeResponseDtoList.add(dto);
        }
        return storeResponseDtoList;
    }

    public List<StoreResponseDto> StoreCategorySearch(StoreRequestDto storeRequestDto) {
        List<Store> storeList = storeRepository.findByCategoryContaining(storeRequestDto.getCategory());
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

        for(Store store : storeList) {
            StoreResponseDto dto = StoreResponseDto.builder()
                    .id(store.getId())
                    .name(store.getName())
                    .category(store.getCategory())
                    .reviews_count(store.getReviews_count())
                    .address(store.getAddress())
                    .nearby_station(store.getNearby_station())
                    .phone(store.getPhone())
                    .business_hours(store.getBusiness_hours())
                    .rating(store.getRating())
                    .build();
            storeResponseDtoList.add(dto);
        }
        return storeResponseDtoList;
    }

    public List<StoreResponseDto> StoreStationSearch(StoreRequestDto storeRequestDto) {
        List<Store> storeList = storeRepository.findByNearbyStationContaining(storeRequestDto.getNearby_station());
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

        for(Store store : storeList) {
            StoreResponseDto dto = StoreResponseDto.builder()
                    .id(store.getId())
                    .name(store.getName())
                    .category(store.getCategory())
                    .reviews_count(store.getReviews_count())
                    .address(store.getAddress())
                    .nearby_station(store.getNearby_station())
                    .phone(store.getPhone())
                    .business_hours(store.getBusiness_hours())
                    .rating(store.getRating())
                    .build();
            storeResponseDtoList.add(dto);
        }
        return storeResponseDtoList;
    }
}
