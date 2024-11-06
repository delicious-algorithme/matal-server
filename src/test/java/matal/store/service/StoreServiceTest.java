package matal.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import matal.global.exception.NotFoundException;
import matal.store.dto.request.StoreSearchFilterRequestDto;
import matal.store.dto.response.StoreListResponseDto;
import matal.store.dto.response.StoreResponseDto;
import matal.store.domain.Store;
import matal.store.domain.repository.StoreRepository;
import matal.store.domain.repository.originStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private originStoreRepository originRepository;
    @InjectMocks
    private StoreService storeService;
    @InjectMocks
    private originStoreService originStoreService;

    private Store store1;
    private Store store2;
    private Store store3;
    private Store store4;
    private Store store5;
    private Store store6;
    private Store store7;
    private Store store8;
    private Store store9;
    private Store store10;
    private StoreSearchFilterRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new StoreSearchFilterRequestDto(
                "커피",
                List.of("카페"),
                List.of("서울"),
                List.of("맛있는 커피"),
                80.0,
                100L,
                4.0,
                true,
                true,
                true,
                true,
                "desc",
                "asc",
                0
        );


        store1 = createStoreWithCustomValues(
                1L,
                "베스트 커피숍",
                "서울시 커피거리 123",
                250L,
                4.5,
                85.0
        );
        store2 = createStoreWithCustomValues(
                2L,
                "버거킹",
                "서울시 버거로 456",
                500L,
                4.2,
                75.0
        );
        store3 = createStoreWithCustomValues(
                3L,
                "파스타 레스토랑",
                "서울시 맛있는 거리 789",
                300L,
                4.8,
                90.0
        );
        store4 = createStoreWithCustomValues(
                4L,
                "도넛 가게",
                "서울시 단 거리 321",
                100L,
                4.1,
                70.0
        );
        store5 = createStoreWithCustomValues(
                5L,
                "스테이크 하우스",
                "서울시 고기 거리 654",
                450L,
                4.7,
                88.0
        );
        store6 = createStoreWithCustomValues(
                6L,
                "초밥 레스토랑",
                "서울시 신선한 거리 111",
                600L,
                4.6,
                82.0
        );
        store7 = createStoreWithCustomValues(
                7L,
                "베이커리",
                "서울시 빵 거리 222",
                200L,
                4.3,
                76.0
        );
        store8 = createStoreWithCustomValues(
                8L,
                "치킨 전문점",
                "서울시 바삭한 거리 333",
                350L,
                4.9,
                92.0
        );
        store9 = createStoreWithCustomValues(
                9L,
                "피자 전문점",
                "서울시 치즈 거리 444",
                400L,
                4.4,
                78.0
        );
        store10 = createStoreWithCustomValues(
                10L,
                "라면 전문점",
                "서울시 면 거리 555",
                150L,
                4.0,
                68.0
        );
    }


    public Store createStoreWithCustomValues(
            Long storeId,
            String name,
            String address,
            Long reviewsCount,
            double rating,
            double positiveRatio
    ) {
        return Store.builder()
                .storeId(storeId)
                .name(name)
                .category("카페")
                .reviewsCount(reviewsCount)
                .address(address)
                .latitude(37.5665)
                .longitude(126.9780)
                .rating(rating)
                .positiveRatio(positiveRatio)
                .negativeRatio(100.0 - positiveRatio)
                .neutralRatio(0.0)
                .isSoloDining(true)
                .isParking(true)
                .isWaiting(true)
                .isPetFriendly(true)
                .build();
    }

    @Test
    @DisplayName("고유 ID값을 이용한 가게 상세 정보 조회 테스트")
    void testFindById() {
        // given
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store1));

        // when
        StoreResponseDto responseDto = storeService.findById(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(responseDto.address(), store1.getAddress());
        assertEquals(responseDto.rating(), store1.getRating());
    }

    @Test
    @DisplayName("가게 모든 정보 조회 테스트")
    void testFindAll() {
        // given
        List<Store> stores = List.of(store1, store2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Store> storePage = new PageImpl<>(stores, pageable, stores.size());

        // when
        when(originRepository.findAllOrderByRatingOrPositiveRatio("DESC", "DESC", pageable)).thenReturn(storePage);

        Page<StoreListResponseDto> responseDtos = storeService.findAll(0, "DESC", "DESC");

        // then
        assertNotNull(responseDtos);
        assertEquals(stores.size(), responseDtos.getTotalElements());
        assertEquals(responseDtos.getContent().get(0).address(), store1.getAddress());
        assertEquals(responseDtos.getContent().get(0).name(), store1.getName());
        assertEquals(responseDtos.getContent().get(0).positiveKeywords(), store1.getPositiveKeywords());
        assertEquals(responseDtos.getContent().get(1).address(), store2.getAddress());
        assertEquals(responseDtos.getContent().get(1).name(), store2.getName());
        assertEquals(responseDtos.getContent().get(1).positiveKeywords(), store2.getPositiveKeywords());
    }

    @Test
    @DisplayName("기존 가게 검색 및 필터링 테스트")
    void testSearchAndFilterStores() {
        // given
        String searchKeywords = "커피";
        String category = "카페";
        String address = "서울";
        String positiveKeywords = "맛있는 커피";
        Double minPositiveRatio = 80.0;
        Long reviewsCount = 100L;
        Double rating = 4.0;
        Boolean soloDining = true;
        Boolean parking = true;
        Boolean waiting = true;
        Boolean petFriendly = true;
        String orderByRating = "desc";
        String orderByPositiveRatio = "asc";

        Pageable pageable = PageRequest.of(0, 10);

        List<Store> filteredStores = List.of(
                store1,
                store2,
                store3,
                store4,
                store5,
                store6,
                store7,
                store8,
                store9,
                store10);

        Page<Store> storePage = new PageImpl<>(
                filteredStores,
                pageable,
                filteredStores.size());

        // when
        when(originRepository.searchAndFilterStores(
                searchKeywords,
                category,
                address,
                positiveKeywords,
                minPositiveRatio,
                reviewsCount,
                rating,
                soloDining,
                parking,
                waiting,
                petFriendly,
                orderByRating,
                orderByPositiveRatio,
                pageable
        )).thenReturn(storePage);

        Page<StoreListResponseDto> responseDtos = originStoreService.searchAndFilterStores(requestDto);

        // then
        assertNotNull(responseDtos);
        assertEquals(10, responseDtos.getTotalElements());
        assertEquals(responseDtos.getContent().get(0).name(), store1.getName());
        assertEquals(responseDtos.getContent().get(0).address(), store1.getAddress());

        assertEquals(responseDtos.getContent().get(1).storeLink(), store2.getStoreLink());
        assertEquals(responseDtos.getContent().get(1).positiveKeywords(), store2.getPositiveKeywords());

        assertEquals(responseDtos.getContent().get(3).latitude(), store2.getLatitude());
        assertEquals(responseDtos.getContent().get(3).longitude(), store3.getLongitude());
    }
    @Test
    @DisplayName("동적 쿼리 가게 검색 및 필터링 테스트")
    void dynamictestSearchAndFilterStores() {
        // given
        String searchKeywords = "커피";
        List<String> category = List.of("카페"); // 카테고리를 List로 수정
        List<String> addresses = List.of("서울"); // 주소를 List로 수정
        List<String> positiveKeywords = List.of("맛있는 커피"); // 긍정 키워드를 List로 수정
        Double minPositiveRatio = 80.0;
        Long reviewsCount = 100L;
        Double rating = 4.0;
        Boolean soloDining = true;
        Boolean parking = true;
        Boolean waiting = true;
        Boolean petFriendly = true;
        String orderByRating = "desc";
        String orderByPositiveRatio = "asc";
        int page = 0;

        Pageable pageable = PageRequest.of(page, 10);

        List<Store> filteredStores = List.of(store1, store2, store3, store4, store5, store6, store7, store8, store9, store10);
        Page<Store> storePage = new PageImpl<>(filteredStores, pageable, filteredStores.size());

        // when
        when(storeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(storePage);

        StoreSearchFilterRequestDto filterRequestDto = new StoreSearchFilterRequestDto(
                searchKeywords,
                category,
                addresses,
                positiveKeywords,
                minPositiveRatio,
                reviewsCount,
                rating,
                soloDining,
                parking,
                waiting,
                petFriendly,
                orderByRating,
                orderByPositiveRatio,
                page
        );

        // 서비스 메서드 호출
        Page<StoreListResponseDto> responseDtos = storeService.searchAndFilterStores(filterRequestDto);

        //then
        assertNotNull(responseDtos);
        assertEquals(10, responseDtos.getTotalElements());
        assertEquals(responseDtos.getContent().get(0).name(), store1.getName());
        assertEquals(responseDtos.getContent().get(0).address(), store1.getAddress());

        assertEquals(responseDtos.getContent().get(1).storeLink(), store2.getStoreLink());
        assertEquals(responseDtos.getContent().get(1).positiveKeywords(), store2.getPositiveKeywords());

        assertEquals(responseDtos.getContent().get(3).latitude(), store2.getLatitude());
        assertEquals(responseDtos.getContent().get(3).longitude(), store3.getLongitude());

    }

    @Test
    @DisplayName("별점순, 긍정비율순 top 10 가게 목록 조회 테스트")
    void testFindTop() {
        //given
        List<Store> topStores = List.of(
                store8,
                store3,
                store5,
                store6,
                store1,
                store9,
                store7,
                store2,
                store4,
                store10);

        Sort sortAll = Sort.by("rating").descending()
                .and(Sort.by("positiveRatio").descending());
        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Store> topStorePage = new PageImpl<>(topStores, pageable, topStores.size());

        //when
        when(storeRepository.findAll(pageable)).thenReturn(topStorePage);

        Page<StoreListResponseDto> responseDtos = storeService.findTop();

        //then
        assertNotNull(responseDtos);
        assertEquals(topStores.size(), responseDtos.getTotalElements());
        assertEquals(responseDtos.getContent().get(0).address(), store8.getAddress());
        assertEquals(responseDtos.getContent().get(0).name(), store8.getName());
        assertEquals(responseDtos.getContent().get(2).address(), store5.getAddress());
        assertEquals(responseDtos.getContent().get(2).name(), store5.getName());
    }

    @Test
    @DisplayName("존재하지 않는 고유 ID 값으로 가게 조회 시 발생하는 예외 테스트")
    void testNotFoundException() {
        //when
        when(storeRepository.findById(1L)).thenThrow(NotFoundException.class);

        //then
        assertThrows(NotFoundException.class, () -> storeService.findById(1L));
    }
}