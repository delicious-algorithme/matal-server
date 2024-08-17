package matal.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private Store store1;
    private Store store2;

    @BeforeEach
    void setUp() {
        store1 = createStore(1L, "Test Store1", "Address 1", "Station 1");
        store2 = createStore(2L, "Test Store2", "Address 2", "Station 2");
    }

    private Store createStore(Long id, String name, String address, String nearbyStation) {
        return Store.builder()
                .id(id)
                .keyword("Test")
                .name(name)
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address(address)
                .nearby_station(nearbyStation)
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
    }

    @Test
    @DisplayName("가게 이름을 이용해 목록 조회 테스트")
    void testStoreNameSearch() {
        // given
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
        store1 = createStore(1L, "Test Store1", "Address 1", "부산역 3번 출구로 부터 10m");
        store2 = createStore(2L, "Test Store2", "Address 2", "부산역 2번 출구로부터 30m");
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
