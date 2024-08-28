package matal.store.service;

import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreResponseDto> findStores(String name, String category, String stationName, String keywords, int page, String sortBy, String sortOrder) {
        if (page < 0) throw new IllegalArgumentException("Invalid page number");

        Sort.Direction direction = Sort.Direction.fromString(sortOrder.toUpperCase());
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sortBy));
        Page<Store> storePage = storeRepository.findStoresByCriteria(name, category, stationName, keywords, pageable);

        return storePage.getContent().stream()
                .map(StoreResponseDto::from)
                .toList();
    }

    public StoreResponseDto findById(Long id) {
        return StoreResponseDto.from(storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Error")));
    }

    public List<StoreResponseDto> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return storeRepository.findAll(pageable).stream()
                .map(StoreResponseDto::from)
                .toList();
    }
}