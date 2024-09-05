package matal.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import matal.store.dto.StoreResponseDto;
import matal.store.entity.StoreInfo;
import matal.store.entity.StoreReviewInsight;
import matal.store.repository.StoreInfoRepository;
import matal.store.repository.StoreReviewInsightRepository;
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
    private StoreInfoRepository storeInfoRepository;

    @Mock
    private StoreReviewInsightRepository storeReviewInsightRepository;

    @InjectMocks
    private StoreService storeService;

    private StoreInfo storeInfo1;
    private StoreInfo storeInfo2;
    private StoreReviewInsight storeReviewInsight1;
    private StoreReviewInsight storeReviewInsight2;

    @BeforeEach
    void setUp() {
        storeInfo1 = createStore(1L, "Test Store1", "Address 1", "Station 1");
        storeInfo2 = createStore(2L, "Test Store2", "Address 2", "Station 2");

        storeReviewInsight1 = createReviewInsight(storeInfo1, "good keywords", "good summary", 4.5);
        storeReviewInsight2 = createReviewInsight(storeInfo2, "bad keywords", "bad summary", 4.0);
    }

    private StoreInfo createStore(Long id, String name, String address, String nearbyStation) {
        return StoreInfo.builder()
                .storeId(id)
                .keyword("Test")
                .name(name)
                .storeLink("https://example.com")
                .reviewsCount(10L)
                .category("Food")
                .address(address)
                .nearByStation(nearbyStation)
                .phone("123-456-789")
                .businessHours("9-23")
                .latitude(12.4)
                .longitude(11.11)
                .mainMenu("계절 숙성 사시미 (소) - 50,000원, 홍가리비찜 - 25,000원, 미나리조개탕 - 23,000원")
                .imageUrls("https://example.com/image.jpg")
                .build();
    }

    private StoreReviewInsight createReviewInsight(StoreInfo storeInfo, String positiveKeywords, String reviewSummary, double rating) {
        return StoreReviewInsight.builder()
                .storeId(storeInfo.getStoreId())
                .storeInfo(storeInfo)
                .positiveKeywords(positiveKeywords)
                .negativeKeywords("No negative keywords")
                .reviewSummary(reviewSummary)
                .rating(rating)
                .positiveRatio(80.0)
                .negativeRatio(10.0)
                .neutralRatio(10.0)
                .isSoloDining(true)
                .isParking(true)
                .parkingTip("주차장이 넓어요")
                .isWaiting(false)
                .waitingTip("대기 없음")
                .isPetFriendly(true)
                .recommendedMenu("홍가리비찜")
                .build();
    }

    @Test
    @DisplayName("가게 이름을 이용해 목록 조회 테스트 - 별점 오름차순")
    void testStoreNameSearch() {
        // given
        List<StoreInfo> storeInfos = List.of(storeInfo1, storeInfo2);
        Pageable pageable = PageRequest.of(1, 10, Sort.by("rating").ascending());
        Page<StoreInfo> storePage = new PageImpl<>(List.of(), pageable, storeInfos.size());

        // when
        when(storeInfoRepository.findStoresByCriteria("Test", null, null, null, pageable)).thenReturn(storePage);
        Page<StoreResponseDto> responses = storeService.findStores("Test", null, null, null, 1, "rating", "asc");

        // then
        assertNotNull(responses);
        assertTrue(responses.isEmpty()); // 페이지 번호가 1이므로 비어 있음
    }

    @Test
    @DisplayName("가게 카테고리 & 키워드를 이용해 목록 조회 테스트 - 별점 오름차순")
    void testStoreCategorySearch() {
        // given
        List<StoreInfo> storeInfos = List.of(storeInfo1, storeInfo2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rating").ascending());
        Page<StoreInfo> storePage = new PageImpl<>(storeInfos, pageable, storeInfos.size());

        // when
        when(storeInfoRepository.findStoresByCriteria(null, "Food", null, "keywords", pageable)).thenReturn(storePage);
        when(storeReviewInsightRepository.findById(storeInfo1.getStoreId())).thenReturn(Optional.of(storeReviewInsight1));
        when(storeReviewInsightRepository.findById(storeInfo2.getStoreId())).thenReturn(Optional.of(storeReviewInsight2));

        Page<StoreResponseDto> responses = storeService.findStores(null, "Food", null, "keywords", 0, "rating", "asc");

        // then
        assertNotNull(responses);
        assertEquals(storeInfos.size(), responses.getTotalElements());
        assertEquals(responses.getContent().get(0).address(), storeInfo1.getAddress());
        assertEquals(responses.getContent().get(1).address(), storeInfo2.getAddress());
        assertEquals(responses.getContent().get(0).negativeKeywords(), storeReviewInsight1.getNegativeKeywords());
        assertEquals(responses.getContent().get(1).negativeKeywords(), storeReviewInsight2.getNegativeKeywords());
    }

    @Test
    @DisplayName("가게 주변 역 & 키워드를 이용한 목록 조회 및 정렬 테스트 - 별점 내림차순")
    void testStoreStationSearchWithSort() {
        // given
        List<StoreInfo> storeInfos = List.of(storeInfo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rating").descending());
        Page<StoreInfo> storePage = new PageImpl<>(storeInfos, pageable, storeInfos.size());

        // when
        when(storeInfoRepository.findStoresByCriteria(null, null, "Station 1", "good", pageable)).thenReturn(storePage);
        when(storeReviewInsightRepository.findById(storeInfo1.getStoreId())).thenReturn(Optional.of(storeReviewInsight2));

        Page<StoreResponseDto> responses = storeService.findStores(null, null, "Station 1", "good", 0, "rating", "desc");

        // then
        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        assertEquals(responses.getContent().get(0).address(), storeInfo1.getAddress());
    }

    @Test
    @DisplayName("고유 ID값을 이용한 가게 상세 정보 조회 테스트")
    void testFindById() {
        // given
        when(storeInfoRepository.findById(1L)).thenReturn(Optional.of(storeInfo1));
        when(storeReviewInsightRepository.findById(1L)).thenReturn(Optional.of(storeReviewInsight1));

        // when
        StoreResponseDto responseDto = storeService.findById(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(responseDto.address(), storeInfo1.getAddress());
        assertEquals(responseDto.rating(), storeReviewInsight1.getRating());
    }

    @Test
    @DisplayName("가게 모든 정보 조회 테스트")
    void testFindAll() {
        // given
        List<StoreInfo> storeInfos = List.of(storeInfo1, storeInfo2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<StoreInfo> storePage = new PageImpl<>(storeInfos, pageable, storeInfos.size());

        // when
        when(storeInfoRepository.findAll(pageable)).thenReturn(storePage);
        when(storeReviewInsightRepository.findById(storeInfo1.getStoreId())).thenReturn(Optional.of(storeReviewInsight1));
        when(storeReviewInsightRepository.findById(storeInfo2.getStoreId())).thenReturn(Optional.of(storeReviewInsight2));

        Page<StoreResponseDto> responseDtos = storeService.findAll(0);

        // then
        assertNotNull(responseDtos);
        assertEquals(storeInfos.size(), responseDtos.getTotalElements());
        assertEquals(responseDtos.getContent().get(0).address(), storeInfo1.getAddress());
        assertEquals(responseDtos.getContent().get(0).name(), storeInfo1.getName());
        assertEquals(responseDtos.getContent().get(1).address(), storeInfo2.getAddress());
        assertEquals(responseDtos.getContent().get(1).name(), storeInfo2.getName());

        assertEquals(responseDtos.getContent().get(0).negativeKeywords(), storeReviewInsight1.getNegativeKeywords());
        assertEquals(responseDtos.getContent().get(1).negativeKeywords(), storeReviewInsight2.getNegativeKeywords());
        assertEquals(responseDtos.getContent().get(0).positiveKeywords(), storeReviewInsight1.getPositiveKeywords());
        assertEquals(responseDtos.getContent().get(1).positiveKeywords(), storeReviewInsight2.getPositiveKeywords());
    }
}
