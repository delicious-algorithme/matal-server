package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreResponseDto;
import matal.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreResponseDto> findStoresByName(String name) {
        return storeRepository.findByNameContaining(name)
                .orElseThrow(() -> new IllegalArgumentException("Error"))
                .stream().map(StoreResponseDto::from)
                .toList();
    }

    public List<StoreResponseDto> findStoresByCategory(String category) {
        return storeRepository.findByCategoryContaining(category)
                .orElseThrow(() -> new IllegalArgumentException("Error"))
                .stream().map(StoreResponseDto::from)
                .toList();
    }

    public List<StoreResponseDto> findStoresByStation(String stationName) {
        return storeRepository.findByNearbyStationContaining(stationName)
                .orElseThrow(() -> new IllegalArgumentException("Error"))
                .stream().map(StoreResponseDto::from)
                .toList();
    }

    public StoreResponseDto findById(Long id) {
        return StoreResponseDto.from(storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Error")));
    }
}
