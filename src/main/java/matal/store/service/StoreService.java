package matal.store.service;

import io.micrometer.core.instrument.Counter;
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

import java.util.function.Supplier;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private static final int PAGE_SIZE = 10;

    private final StoreRepository storeRepository;
    private final MeterRegistry meterRegistry;


    public Page<StoreListResponseDto> searchAndFilterStores(StoreSearchFilterRequestDto filterRequestDto) {

        return executeWithMetricsAndMemory("store.filter", "searchAndFilterStores", () -> {
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
        });
    }

    public StoreResponseDto findById(Long storeId) {

        return executeWithMetricsAndMemory("store.detail", "findById", () -> {
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID));

            return StoreResponseDto.from(store);
        });
    }

    public Page<StoreListResponseDto> findAll(int page,
                                              String orderByRatio,
                                              String orderByPositiveRatio) {

        return executeWithMetricsAndMemory("store.all", "findAll", () -> {
            Pageable pageable = PageRequest.of(page, PAGE_SIZE);

            return storeRepository.findAllOrderByRatingOrPositiveRatio(orderByRatio, orderByPositiveRatio, pageable).map(StoreListResponseDto::from);
        });
    }

    public Page<StoreListResponseDto> findTop() {

            return executeWithMetricsAndMemory("store.top", "findTop", () -> {
                Sort sortAll = Sort.by("rating").descending()
                        .and(Sort.by("positiveRatio").descending());
                Pageable pageable = PageRequest.of(0, PAGE_SIZE, sortAll);

                return storeRepository.findAll(pageable).map(StoreListResponseDto::from);
            });
    }

    private <T> T executeWithMetricsAndMemory(String metricPrefix, String methodName, Supplier<T> action) {
        measureCount(metricPrefix + ".count", methodName);
        Timer.Sample timer = Timer.start(meterRegistry);

        Runtime runtime = Runtime.getRuntime();
        double startMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024.0 * 1024.0);

        try {
            return action.get();
        } finally {
            double endMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024.0 * 1024.0);
            double memoryUsed = endMemory - startMemory;

            Counter.builder(metricPrefix + ".memory")
                    .tag("class", this.getClass().getName())
                    .tag("method", methodName)
                    .description("Memory usage during " + methodName)
                    .register(meterRegistry)
                    .increment(memoryUsed);

            stopTimer(timer, metricPrefix + ".time", methodName);
        }
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