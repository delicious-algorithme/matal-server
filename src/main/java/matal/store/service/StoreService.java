package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreInfoResponseDto;
import matal.store.dto.StoreRequestDto;
import matal.store.dto.StoreResponseDto;
import matal.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
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

    public StoreInfoResponseDto getStoreInfo(Long id) {
        return storeRepository.findById(id).map(StoreInfoResponseDto::fromInfo)
                .orElseThrow(() -> new IllegalArgumentException("Error"));
    }

    public List<StoreResponseDto> sortStores(List<StoreResponseDto> storeResponseDtoList, String sortBy, String sortOrder) {

        Comparator <StoreResponseDto> storeResponseDtoComparator = switch (sortBy) {
            case "positive_ratio" -> Comparator.comparing(StoreResponseDto::positive_ratio);
            case "rating" -> Comparator.comparing(StoreResponseDto::rating);
            case "reviews_count" -> Comparator.comparing(StoreResponseDto::reviews_count);
            default -> throw new IllegalStateException("Unexpected value: " + sortBy);
        };

        if (sortOrder.equalsIgnoreCase("upper")) { //오름차순
            storeResponseDtoComparator = storeResponseDtoComparator;
        } else if (sortOrder.equalsIgnoreCase("lower")) { //내림차순
            storeResponseDtoComparator = storeResponseDtoComparator.reversed();
        } else {
            throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
        }

        storeResponseDtoList.sort(storeResponseDtoComparator);
        return storeResponseDtoList;
    }
}