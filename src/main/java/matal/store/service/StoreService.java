package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreRequestDto;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    //가게 이름 검색 조회 리스트
    public List<StoreResponseDto> StoreNameSearch(StoreRequestDto storeRequestDto) {
        List<Store> storeList = storeRepository.findByNameContaining(storeRequestDto.name());

        return storeList.stream()
                .map(StoreResponseDto::from)
                .collect(Collectors.toList());
    }

    //가게 카테고리 검색 조회 리스트
    public List<StoreResponseDto> StoreCategorySearch(StoreRequestDto storeRequestDto) {
        List<Store> storeList = storeRepository.findByCategoryContaining(storeRequestDto.category());

        return storeList.stream()
                .map(StoreResponseDto::from)
                .collect(Collectors.toList());
    }

    //가게 지하철 역 검색 조회 리스트
    public List<StoreResponseDto> StoreStationSearch(StoreRequestDto storeRequestDto) {
        List<Store> storeList = storeRepository.findByNearbyStationContaining(storeRequestDto.nearby_station());

        return storeList.stream()
                .map(StoreResponseDto::from)
                .collect(Collectors.toList());
    }
}
