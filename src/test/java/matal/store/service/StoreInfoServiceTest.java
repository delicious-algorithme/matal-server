package matal.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import matal.store.dto.StoreListResponseDto;
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
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
public class StoreInfoServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private Store store1;
    private Store store2;

    @BeforeEach
    void setUp() {
        store1 = Store.builder()
                .storeId(1L)
                .keyword("커피, 카페")
                .name("베스트 커피숍")
                .storeLink("https://bestcoffeeshop.example.com")
                .category("카페")
                .reviewsCount(250L)
                .address("서울시 커피거리 123")
                .nearbyStation("중앙역")
                .phone("010-1234-5678")
                .businessHours("08:00-22:00")
                .latitude(37.5665)
                .longitude(126.9780)
                .mainMenu("에스프레소, 라떼, 카푸치노")
                .imageUrls("https://example.com/images/coffee1.jpg, https://example.com/images/coffee2.jpg")
                .positiveKeywords("친절한 직원, 맛있는 커피")
                .negativeKeywords("느린 서비스")
                .reviewSummary("커피 한잔 하며 휴식을 취하기 좋은 곳.")
                .rating(4.5)
                .positiveRatio(85.0)
                .negativeRatio(10.0)
                .neutralRatio(5.0)
                .isSoloDining(true)
                .isParking(true)
                .parkingTip("건물 뒤쪽에 무료 주차 가능")
                .isWaiting(true)
                .waitingTip("주말에는 대기 시간이 길어요")
                .isPetFriendly(true)
                .recommendedMenu("에스프레소, 라떼")
                .build();

        store2 = Store.builder()
                .storeId(2L)
                .keyword("햄버거, 패스트푸드")
                .name("버거킹")
                .storeLink("https://burgerking.example.com")
                .category("패스트푸드")
                .reviewsCount(500L)
                .address("서울시 버거로 456")
                .nearbyStation("버거역")
                .phone("010-9876-5432")
                .businessHours("09:00-23:00")
                .latitude(37.5670)
                .longitude(126.9785)
                .mainMenu("치즈버거, 와퍼, 감자튀김")
                .imageUrls("https://example.com/images/burger1.jpg, https://example.com/images/burger2.jpg")
                .positiveKeywords("빠른 서비스, 맛있는 버거")
                .negativeKeywords("혼잡함")
                .reviewSummary("빠르게 식사할 수 있는 좋은 곳.")
                .rating(4.2)
                .positiveRatio(75.0)
                .negativeRatio(15.0)
                .neutralRatio(10.0)
                .isSoloDining(true)
                .isParking(false)
                .parkingTip(null)
                .isWaiting(true)
                .waitingTip("점심 시간에는 대기가 필요합니다")
                .isPetFriendly(false)
                .recommendedMenu("와퍼, 치즈버거")
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
        when(storeRepository.findAll(pageable)).thenReturn(storePage);

        Page<StoreListResponseDto> responseDtos = storeService.findAll(0);

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
}
