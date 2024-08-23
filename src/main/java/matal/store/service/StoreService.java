package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreResponseDto> findStoresByName(String name, int page) {
        if (page < 0 ) throw new IllegalArgumentException("Invalid page number");

        Pageable pageable = PageRequest.of(page, 10);
        Page<Store> storePage = storeRepository.findByNameContaining(name, pageable);

        return storePage.getContent().stream()
                .map(StoreResponseDto::from)
                .toList();
    }

    public List<StoreResponseDto> findStoresByCategory(String category, int page) {
        if (page < 0 ) throw new IllegalArgumentException("Invalid page number");

        Pageable pageable = PageRequest.of(page, 10);
        Page<Store> storePage = storeRepository.findByCategoryContaining(category, pageable);

        return storePage.getContent().stream()
                .map(StoreResponseDto::from)
                .toList();
    }

    public List<StoreResponseDto> findStoresByStation(String stationName, int page) {
        if (page < 0 ) throw new IllegalArgumentException("Invalid page number");

        Pageable pageable = PageRequest.of(page, 10);
        Page<Store> storePage = storeRepository.findByNearbyStationContaining(stationName, pageable);

        return storePage.getContent().stream()
                .map(StoreResponseDto::from)
                .toList();
    }

    public StoreResponseDto findById(Long id) {
        return StoreResponseDto.from(storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Error")));
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