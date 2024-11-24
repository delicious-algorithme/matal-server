package matal.store.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.store.dto.request.StoreSearchFilterRequestDto;
import matal.store.dto.response.StoreListResponseDto;
import matal.store.dto.response.StoreResponseDto;
import matal.store.domain.Store;
import matal.store.domain.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private static final int PAGE_SIZE = 10;

    private final StoreRepository storeRepository;
    private final MeterRegistry meterRegistry;

    private double lastMemoryUsage = 0;

    public Page<StoreListResponseDto> searchAndFilterStores(StoreSearchFilterRequestDto filterRequestDto) {

        measureCount("store.search.count", "searchAndFilterStores");
        measureMemory("store.search.memory", "searchAndFilterStores");
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            Pageable pageable = PageRequest.of(filterRequestDto.getPage(), PAGE_SIZE);

            return storeRepository.searchAndFilterStores(
                    filterRequestDto.getSearchKeywords(),
                    filterRequestDto.convertCategoryToString(),
                    filterRequestDto.convertAddressesToString(),
                    filterRequestDto.convertPositiverKeywordsToString(),
                    filterRequestDto.getPositiveRatio(),
                    filterRequestDto.getReviewsCount(),
                    filterRequestDto.getRating(),
                    filterRequestDto.getIsSoloDining(),
                    filterRequestDto.getIsParking(),
                    filterRequestDto.getIsWaiting(),
                    filterRequestDto.getIsPetFriendly(),
                    filterRequestDto.getOrderByRating(),
                    filterRequestDto.getOrderByPositiveRatio(),
                    pageable
            ).map(StoreListResponseDto::from);
        } finally {
            stopTimer(timer, "store.search.time", "searchAndFilterStores");
        }
    }

    public StoreResponseDto findById(Long storeId) {

        measureCount("store.Id.count", "findById");
        measureMemory("store.Id.memory", "findById");
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID));

            return StoreResponseDto.from(store);
        } finally {
            stopTimer(timer, "store.Id.time", "findById");
        }
    }

    public Page<StoreListResponseDto> findAll(int page,
                                              String orderByRatio,
                                              String orderByPositiveRatio) {

        measureCount("store.all.count", "findAll");
        measureMemory("store.all.memory", "findAll");
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            Pageable pageable = PageRequest.of(page, PAGE_SIZE);

            return storeRepository.findAllOrderByRatingOrPositiveRatio(orderByRatio, orderByPositiveRatio, pageable).map(StoreListResponseDto::from);
        } finally {
            stopTimer(timer, "store.all.time", "findAll");
        }
    }

    public Page<StoreListResponseDto> findTop() {

        measureCount("store.top.count", "findTop");
        measureMemory("store.top.memory", "findTop");
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            Sort sortAll = Sort.by("rating").descending()
                    .and(Sort.by("positiveRatio").descending());
            Pageable pageable = PageRequest.of(0, PAGE_SIZE, sortAll);

            return storeRepository.findAll(pageable).map(StoreListResponseDto::from);
        } finally {
            stopTimer(timer, "store.top.time", "findTop");
        }
    }

    private void measureMemory(String metricName, String methodName) {

        Runtime runtime = Runtime.getRuntime();
        lastMemoryUsage = (runtime.totalMemory() - runtime.freeMemory()) / (1024.0 * 1024.0);

        Gauge.builder(metricName, () -> lastMemoryUsage)
                .tag("class", this.getClass().getName())
                .tag("metric", methodName)
                .description("Memory usage during " + methodName)
                .baseUnit("MB")
                .register(meterRegistry);
    }

    private void measureCount(String metricName, String methodName) {
        Counter.builder(metricName)
                .tag("class", this.getClass().getName())
                .tag("metric", methodName)
                .description("Count of " + methodName)
                .register(meterRegistry)
                .increment();
    }

    private void stopTimer(Timer.Sample timer, String metricName, String methodName) {
        timer.stop(Timer.builder(metricName)
                .tag("class", this.getClass().getName())
                .tag("method", methodName)
                .description("Execution time of " + methodName)
                .register(meterRegistry));
    }
}