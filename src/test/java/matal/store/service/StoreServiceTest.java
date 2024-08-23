package matal.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        store1 = createStore(1L, "Test Store1", "Address 1", "Station 1", 4.5);
        store2 = createStore(2L, "Test Store2", "Address 2", "Station 2", 4.0);
    }

    private Store createStore(Long id, String name, String address, String nearbyStation, Double rating) {
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
                .rating(rating)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();
    }

    @Test
    @DisplayName("가게 이름을 이용해 목록 조회 테스트")
    void testStoreNameSearch() {
        // given
        List<Store> stores = List.of(store1, store2);
        Pageable pageable = PageRequest.of(1, 10);
            //페이지 번호 0이상 -> 비어있음 -> List.of()
        Page<Store> storePage = new PageImpl<>(List.of(), pageable, stores.size());

        // when
        when(storeRepository.findByNameContaining("Test", pageable)).thenReturn(storePage);
        List<StoreResponseDto> responses = storeService.findStoresByName("Test", 1);

        // then
        assertNotNull(responses);
        assertTrue(responses.isEmpty()); //페이지 번호 0이상 -> 비어있음
        //assertEquals(2, responses.size());
        //assertEquals(responses.get(0).address(), store1.getAddress());
        //assertEquals(responses.get(1).address(), store2.getAddress());
    }

    @Test
    @DisplayName("가게 카테고리를 이용해 목록 조회 테스트")
    void testStoreCategorySearch() {
        // given
        List<Store> stores = List.of(store1, store2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Store> storePage = new PageImpl<>(stores, pageable, stores.size());

        // when
        when(storeRepository.findByCategoryContaining("Food", pageable)).thenReturn(storePage);
        List<StoreResponseDto> responses = storeService.findStoresByCategory("Food", 0);

        // then
        assertNotNull(responses);
        assertEquals(responses.get(0).address(), store1.getAddress());
        assertEquals(responses.get(1).address(), store2.getAddress());
    }

    @Test
    @DisplayName("가게 주변 역을 이용한 목록 조회 테스트")
    void testStoreStationSearch() {
        // given
        store1 = createStore(1L, "Test Store1", "Address 1", "부산역 3번 출구로 부터 10m", 4.5);
        store2 = createStore(2L, "Test Store2", "Address 2", "부산역 2번 출구로부터 30m", 4.0);
        List<Store> stores = List.of(store1, store2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Store> storePage = new PageImpl<>(stores, pageable, stores.size());

        // when
        when(storeRepository.findByNearbyStationContaining("부산역", pageable)).thenReturn(storePage);
        List<StoreResponseDto> responses = storeService.findStoresByStation("부산역", 0);

        // then
        assertNotNull(responses);
        assertEquals(responses.get(0).address(), store1.getAddress());
        assertEquals(responses.get(1).address(), store2.getAddress());
    }

    @Test
    @DisplayName("고유 ID값을 이용한 가게 상세 정보 조회 테스트")
    void testFindById() {
        // given
        store1 = createStore(1L, "Test Store1", "Address 1", "부산역 3번 출구로 부터 10m", 4.3);

        // when
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store1));
        StoreResponseDto responseDto = storeService.findById(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(responseDto.address(), store1.getAddress());
    }

    @Test
    @DisplayName("별점을 기준으로 오름차순 혹은 내림차순으로 정렬하는 테스트")
    void testStoreSort() {
        //given
        List<Store> stores = List.of(store1, store2);
        List<StoreResponseDto> storeResponseDto = stores.stream().map(StoreResponseDto::from).collect(Collectors.toList());

        //when
        List<StoreResponseDto> responses = storeService.sortStores(storeResponseDto, "rating", "upper");

        //then
        assertNotNull(responses);
        assertEquals(responses.get(0).id(), 2L);
        assertTrue(responses.get(0).rating() <= responses.get(1).rating());
    }
}
