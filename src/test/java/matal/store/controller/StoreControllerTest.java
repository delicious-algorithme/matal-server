package matal.store.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.store.dto.request.StoreSearchFilterRequestDto;
import matal.store.dto.response.StoreListResponseDto;
import matal.store.dto.response.StoreResponseDto;
import matal.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StoreController.class)
@ActiveProfiles("local")
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreService storeService;

    private StoreResponseDto storeResponse;
    private List<StoreListResponseDto> stores;
    private StoreSearchFilterRequestDto validRequestDto;
    private StoreSearchFilterRequestDto invalidRequestDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new StoreSearchFilterRequestDto(
                "커피",
                List.of("카페"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0
        );

        invalidRequestDto = new StoreSearchFilterRequestDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0
        );

        storeResponse = new StoreResponseDto(
                1L,
                "커피, 카페",
                "베스트 커피숍",
                "https://bestcoffeeshop.example.com",
                "카페",
                250L,
                "서울시 커피거리 123",
                "중앙역",
                "010-1234-5678",
                "08:00-22:00",
                37.5665,
                126.9780,
                "에스프레소, 라떼, 카푸치노",
                "https://example.com/images/coffee1.jpg",
                "친절한 직원, 맛있는 커피",
                "느린 서비스",
                "커피 한잔 하며 휴식을 취하기 좋은 곳.",
                4.5,
                85.0,
                10.0,
                5.0,
                true,
                true,
                "건물 뒤쪽에 무료 주차 가능",
                true,
                "주말에는 대기 시간이 길어요",
                true,
                "에스프레소, 라떼"
        );

        stores = List.of(
                new StoreListResponseDto(
                        1L,
                        "베스트 커피숍",
                        "서울시 커피거리 123",
                        "https://bestcoffeeshop.example.com",
                        4.5,
                        "https://example.com/images/coffee1.jpg",
                        85.0,
                        "친절한 직원, 맛있는 커피",
                        37.5665,
                        126.9780
                ),
                new StoreListResponseDto(
                        2L,
                        "버거킹",
                        "서울시 버거로 456",
                        "https://burgerking.example.com",
                        4.2,
                        "https://example.com/images/burger1.jpg",
                        75.0,
                        "빠른 서비스, 맛있는 버거",
                        37.5670,
                        126.9785
                ),
                new StoreListResponseDto(
                        3L,
                        "파스타 하우스",
                        "서울시 맛집 거리 789",
                        "https://pastahouse.example.com",
                        4.7,
                        "https://example.com/images/pasta1.jpg",
                        90.0,
                        "맛있는 파스타, 친절한 서비스",
                        37.5640,
                        126.9765
                ),
                new StoreListResponseDto(
                        4L,
                        "피자헛",
                        "서울시 피자거리 111",
                        "https://pizzahut.example.com",
                        4.0,
                        "https://example.com/images/pizza1.jpg",
                        65.0,
                        "넉넉한 양, 저렴한 가격",
                        37.5668,
                        126.9783
                ),
                new StoreListResponseDto(
                        5L,
                        "치킨마루",
                        "서울시 치킨거리 222",
                        "https://chickenmaru.example.com",
                        4.9,
                        "https://example.com/images/chicken1.jpg",
                        95.0,
                        "바삭한 치킨, 맛있는 소스",
                        37.5680,
                        126.9795
                ),
                new StoreListResponseDto(
                        6L,
                        "카페베네",
                        "서울시 카페거리 333",
                        "https://cafebene.example.com",
                        4.3,
                        "https://example.com/images/cafe1.jpg",
                        80.0,
                        "아늑한 분위기, 맛있는 커피",
                        37.5655,
                        126.9760
                ),
                new StoreListResponseDto(
                        7L,
                        "스타벅스",
                        "서울시 커피거리 456",
                        "https://starbucks.example.com",
                        4.5,
                        "https://example.com/images/starbucks1.jpg",
                        85.0,
                        "프리미엄 커피, 편안한 좌석",
                        37.5675,
                        126.9805
                ),
                new StoreListResponseDto(
                        8L,
                        "맥도날드",
                        "서울시 패스트푸드 거리 123",
                        "https://mcdonalds.example.com",
                        4.1,
                        "https://example.com/images/mcd1.jpg",
                        70.0,
                        "빠른 서비스, 저렴한 가격",
                        37.5662,
                        126.9778
                ),
                new StoreListResponseDto(
                        9L,
                        "KFC",
                        "서울시 패스트푸드 거리 789",
                        "https://kfc.example.com",
                        4.0,
                        "https://example.com/images/kfc1.jpg",
                        68.0,
                        "맛있는 치킨, 다양한 메뉴",
                        37.5685,
                        126.9810
                ),
                new StoreListResponseDto(
                        10L,
                        "던킨도너츠",
                        "서울시 도넛거리 123",
                        "https://dunkindonuts.example.com",
                        3.9,
                        "https://example.com/images/dunkin1.jpg",
                        60.0,
                        "달콤한 도넛, 다양한 음료",
                        37.5660,
                        126.9768
                )
        );
    }

    @Test
    @DisplayName("검색 및 필터링 기능 - 특정 카테고리 및 키워드 요청 테스트")
    @WithMockUser(username = "test", roles = "USER")
    void testSearchAndFilterStores() throws Exception {
        // given
        List<StoreListResponseDto> filteredStores = List.of(stores.get(0), stores.get(6), stores.get(5));
        Page<StoreListResponseDto> storePage = new PageImpl<>(filteredStores);

        // when
        when(storeService.searchAndFilterStores(any(StoreSearchFilterRequestDto.class))).thenReturn(storePage);

        // then
        mockMvc.perform(post("/api/stores/searchAndFilter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(stores.get(0).name()))
                .andExpect(jsonPath("$.content[1].name").value(stores.get(6).name()))
                .andExpect(jsonPath("$.content[2].name").value(stores.get(5).name()))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 및 필터링 기능 - 특정 카테고리 및 키워드 요청 테스트 시 발생하는 예외 처리")
    @WithMockUser(username = "test", roles = "USER")
    void testSearchAndFilterStoresException() throws Exception {

        // then
        mockMvc.perform(post("/api/stores/searchAndFilter")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("모든 가게 조회 기능 테스트")
    @WithMockUser(username = "test", roles = "USER")
    void testGetAllStores() throws Exception {
        // given
        List<StoreListResponseDto> sortedStores = stores.stream()
                .sorted(Comparator.comparingDouble(StoreListResponseDto::rating).reversed())
                .sorted(Comparator.comparingDouble(StoreListResponseDto::positiveRatio).reversed())
                .collect(Collectors.toList());
        Page<StoreListResponseDto> storePage = new PageImpl<>(sortedStores);


        // when
        when(storeService.findAll(0, "DESC", "DESC")).thenReturn(storePage);

        // then
        mockMvc.perform(get("/api/stores/all")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .param("page", "0")
                        .param("orderByRating", "DESC")
                        .param("orderByPositiveRatio", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].storeId").value(sortedStores.get(0).storeId()))
                .andExpect(jsonPath("$.content[1].storeId").value(sortedStores.get(1).storeId()))
                .andExpect(jsonPath("$.content[2].storeId").value(sortedStores.get(2).storeId()))
                .andExpect(jsonPath("$.content[9].storeId").value(sortedStores.get(9).storeId()))
                .andDo(print());
    }

    @Test
    @DisplayName("가게 상세 정보 조회 - 성공 테스트")
    @WithMockUser(username = "test", roles = "USER")
    void testGetStoreDetail_Success() throws Exception {
        // given
        StoreListResponseDto storeDetail = stores.get(0);

        // when
        when(storeService.findById(1L)).thenReturn(storeResponse);

        // then
        mockMvc.perform(get("/api/stores/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(storeDetail.storeId()))
                .andExpect(jsonPath("$.name").value(storeDetail.name()))
                .andExpect(jsonPath("$.address").value(storeDetail.address()))
                .andDo(print());
    }

    @Test
    @DisplayName("top10 가게 조회 기능 테스트")
    @WithMockUser(username = "test", roles = "USER")
    void testGetStoreTop() throws Exception {
        //given
        List<StoreListResponseDto> topStores = List.of(
                stores.get(4),
                stores.get(2),
                stores.get(0),
                stores.get(6),
                stores.get(5),
                stores.get(1),
                stores.get(7),
                stores.get(8),
                stores.get(3),
                stores.get(9));

        Page<StoreListResponseDto> storePage = new PageImpl<>(topStores);

        // when
        when(storeService.findTop()).thenReturn(storePage);

        // then
        mockMvc.perform(get("/api/stores/top")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(stores.get(4).name()))
                .andExpect(jsonPath("$.content[1].name").value(stores.get(2).name()))
                .andExpect(jsonPath("$.content[2].name").value(stores.get(0).name()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 고유 ID 값으로 가게 조회 시 발생하는 API 예외 테스트")
    @WithMockUser(username = "test", roles = "USER")
    void testNotFoundException() throws Exception {
        // when
        when(storeService.findById(1L)).thenThrow(new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID));

        // then
        mockMvc.perform(get("/api/stores/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
