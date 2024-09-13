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
    @DisplayName("필터링 + 검색하여 목록 조회 테스트 - 메뉴(String) & 별점 내림차순")
    void testStoreKeywordFilter() {
        // given
        List<StoreInfo> storeInfos = List.of(storeInfo1, storeInfo2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<StoreInfo> storePage = new PageImpl<>(storeInfos, pageable, storeInfos.size());

        //when
        when(storeInfoRepository.filterStoresByCriteria(List.of(storeInfo1.getStoreId(), storeInfo2.getStoreId()), null, null, "Address", null, pageable)).thenReturn(storePage);
        when(storeReviewInsightRepository.findStoreIdsByReviewCriteriaRatingDESC(null, null, null, null, null, null, null, "rating")).thenReturn(List.of(storeInfo1.getStoreId(), storeInfo2.getStoreId()));
        when(storeReviewInsightRepository.findById(storeInfo1.getStoreId())).thenReturn(Optional.of(storeReviewInsight1));
        when(storeReviewInsightRepository.findById(storeInfo2.getStoreId())).thenReturn(Optional.of(storeReviewInsight2));
        Page<StoreResponseDto> responses = storeService.filterStores(null, null, "Address", null, null, null, null, null,
                null, null, null, 0, "rating", "DESC");

        //then
        assertNotNull(responses);
        assertFalse(responses.getContent().isEmpty());
        assertEquals(storeInfos.size(), responses.getTotalElements());
        assertEquals(responses.getContent().get(0).address(), storeInfo1.getAddress());
        assertEquals(responses.getContent().get(1).address(), storeInfo2.getAddress());
    }

    @Test
    @DisplayName("필터링 + 검색하여 목록 조회 테스트 - 모든 조건 & 별점 내림차순")
    void testStoreAllilter() {
        // given
        List<StoreInfo> storeInfos = List.of(storeInfo1, storeInfo2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<StoreInfo> storePage = new PageImpl<>(storeInfos, pageable, storeInfos.size());

        //when
        when(storeInfoRepository.filterStoresByCriteria(List.of(storeInfo1.getStoreId(), storeInfo2.getStoreId()), null, "Food", "Address", 9L ,pageable)).thenReturn(storePage);
        when(storeReviewInsightRepository.findStoreIdsByReviewCriteriaRatingDESC(75.0, 4.0, "summary", true, true, false, true, "rating")).thenReturn(List.of(storeInfo1.getStoreId(), storeInfo2.getStoreId()));
        when(storeReviewInsightRepository.findById(storeInfo1.getStoreId())).thenReturn(Optional.of(storeReviewInsight1));
        when(storeReviewInsightRepository.findById(storeInfo2.getStoreId())).thenReturn(Optional.of(storeReviewInsight2));
        Page<StoreResponseDto> responses = storeService.filterStores(null, "Food", "Address", 9L, 75.0, 4.0, "summary",
                true, true, false, true, 0, "rating", "DESC");

        //then
        assertNotNull(responses);
        assertFalse(responses.getContent().isEmpty());
        assertEquals(storeInfos.size(), responses.getTotalElements());
        assertEquals(responses.getContent().get(0).address(), storeInfo1.getAddress());
        assertEquals(responses.getContent().get(1).address(), storeInfo2.getAddress());
    }

    @Test
    @DisplayName("필터링 + 검색하여 목록 조회 테스트 - 모든 조건 & 검색(이름, 지하철역) & 별점 내림차순")
    void testStoreFilterSearch() {
        // given
        List<StoreInfo> storeInfos = List.of(storeInfo1, storeInfo2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<StoreInfo> storePage = new PageImpl<>(storeInfos, pageable, storeInfos.size());

        //when
        when(storeInfoRepository.filterStoresByCriteria(List.of(storeInfo1.getStoreId(), storeInfo2.getStoreId()), "Test", "Food", "Address", 9L, pageable)).thenReturn(storePage);
        when(storeReviewInsightRepository.findStoreIdsByReviewCriteriaRatingDESC(75.0, 4.0, "keywords", true, true, false, true, "rating")).thenReturn(List.of(storeInfo1.getStoreId(), storeInfo2.getStoreId()));
        when(storeReviewInsightRepository.findById(storeInfo1.getStoreId())).thenReturn(Optional.of(storeReviewInsight1));
        when(storeReviewInsightRepository.findById(storeInfo2.getStoreId())).thenReturn(Optional.of(storeReviewInsight2));

        Page<StoreResponseDto> responses = storeService.filterStores("Test", "Food", "Address", 9L, 75.0, 4.0, "keywords",
                true, true, false, true, 0, "rating", "DESC");

        //then
        assertNotNull(responses);
        assertFalse(responses.getContent().isEmpty());
        assertEquals(storeInfos.size(), responses.getTotalElements());
        assertEquals(responses.getContent().get(0).address(), storeInfo1.getAddress());
        assertEquals(responses.getContent().get(1).address(), storeInfo2.getAddress());
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
