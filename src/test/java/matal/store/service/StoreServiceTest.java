package matal.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    @DisplayName("가게 이름을 이용해 목록 조회 테스트")
    void testStoreNameSearch() {
        // given
        Store store1 = Store.builder()
                .keyword("Test")
                .name("Test Store1")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 1")
                .nearby_station("Station 1")
                .phone("123-456-789")
                .business_hours("9-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.5)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

        Store store2 = Store.builder()
                .keyword("Test")
                .name("Test Store2")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 2")
                .nearby_station("Station 2")
                .phone("12345666")
                .business_hours("8-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.0)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

        List<Store> stores = List.of(store1, store2);

        // when
        when(storeRepository.findByNameContaining("Test")).thenReturn(Optional.of(stores));
        List<StoreResponseDto> responses = storeService.findStoresByName("Test");

        // then
        assertNotNull(responses);
        assertEquals(responses.get(0).address(), store1.getAddress());
        assertEquals(responses.get(1).address(), store2.getAddress());
    }

    @Test
    @DisplayName("가게 카테고리를 이용해 목록 조회 테스트")
    void testStoreCategorySearch() {
        // given
        Store store1 = Store.builder()
                .keyword("Test")
                .name("Test Store1")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 1")
                .nearby_station("Station 1")
                .phone("123-456-789")
                .business_hours("9-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.5)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

        Store store2 = Store.builder()
                .keyword("Test")
                .name("Test Store2")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 2")
                .nearby_station("Station 2")
                .phone("12345666")
                .business_hours("8-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.0)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

        List<Store> stores = List.of(store1, store2);

        // when
        when(storeRepository.findByCategoryContaining("Food")).thenReturn(Optional.of(stores));
        List<StoreResponseDto> responses = storeService.findStoresByCategory("Food");

        // then
        assertNotNull(responses);
        assertEquals(responses.get(0).address(), store1.getAddress());
        assertEquals(responses.get(1).address(), store2.getAddress());
    }

    @Test
    @DisplayName("가게 주변 역을 이용한 목록 조회 테스트")
    void testStoreStationSearch() {
        // given
        Store store1 = Store.builder()
                .keyword("Test")
                .name("Test Store1")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 1")
                .nearby_station("부산역 3번 출구로 부터 10m")
                .phone("123-456-789")
                .business_hours("9-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.5)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

        Store store2 = Store.builder()
                .keyword("Test")
                .name("Test Store2")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 2")
                .nearby_station("부산역 2번 출구로부터 30m")
                .phone("12345666")
                .business_hours("8-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.0)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

        List<Store> stores = List.of(store1, store2);

        // when
        when(storeRepository.findByNearbyStationContaining("부산역")).thenReturn(Optional.of(stores));
        List<StoreResponseDto> responses = storeService.findStoresByStation("부산역");

        // then
        assertNotNull(responses);
        assertEquals(responses.get(0).address(), store1.getAddress());
        assertEquals(responses.get(1).address(), store2.getAddress());
    }
}