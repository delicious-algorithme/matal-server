package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreRequestDto;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    //가게 이름 검색 조회 리스트
    public Optional<List<StoreResponseDto>> StoreNameSearch(StoreRequestDto storeRequestDto) {
        return storeRepository.findByNameContaining(storeRequestDto.name())
                .map(stores -> stores.stream()
                        .map(StoreResponseDto::from)
                        .toList());
    }

    public Optional<List<StoreResponseDto>> StoreCategorySearch(StoreRequestDto storeRequestDto) {
        return storeRepository.findByCategoryContaining(storeRequestDto.category())
                .map(stores -> stores.stream()
                        .map(StoreResponseDto::from)
                        .toList());
    }

    public Optional<List<StoreResponseDto>> StoreStationSearch(StoreRequestDto storeRequestDto) {
        return storeRepository.findByNearbyStationContaining(storeRequestDto.nearby_station())
                .map(stores -> stores.stream()
                        .map(StoreResponseDto::from)
                        .toList());
    }
}
