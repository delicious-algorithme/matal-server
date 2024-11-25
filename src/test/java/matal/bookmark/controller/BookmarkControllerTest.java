package matal.bookmark.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import matal.bookmark.dto.response.BookmarkResponseDto;
import matal.bookmark.dto.response.BookmarkStoreIdsResponseDto;
import matal.bookmark.service.BookmarkService;
import matal.global.exception.AuthException;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.member.domain.Role;
import matal.member.dto.request.AuthMember;
import matal.store.dto.response.StoreListResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookmarkController.class)
@ActiveProfiles("local")
public class BookmarkControllerTest {

    private static final String SESSION_KEY = "member";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookmarkService bookmarkService;

    private MockHttpSession mockSession;
    private AuthMember mockMember;
    private List<BookmarkResponseDto> bookmarkResponseDtos;
    private Page<BookmarkResponseDto> bookmarkPage;

    @BeforeEach
    void beforeEach() {
        mockMember = new AuthMember(
                1L,
                "test@test.com",
                "test",
                Role.MEMBER
                );

        mockSession = new MockHttpSession();

        mockSession.setAttribute(SESSION_KEY, mockMember);

        bookmarkResponseDtos = List.of(
                new BookmarkResponseDto(
                        1L,
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
                        )
                ),
                new BookmarkResponseDto(
                        2L,
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
                        )
                )
        );
    }

    @Test
    @DisplayName("북마크 성공 테스트")
    void testCreateBookmarkSuccess() throws Exception {
        // given
        Long storeId = 1L;

        // when
        doNothing().when(bookmarkService).saveBookmark(any(AuthMember.class), anyLong());

        // then
        mockMvc.perform(post("/api/bookmarks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(storeId))
                        .session(mockSession))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 생성 시 회원 세션 정보 없음으로 인한 실패 테스트")
    void testCreateBookmarkFailure() throws Exception {
        // given
        Long storeId = 1L;

        // when
        doNothing().when(bookmarkService).saveBookmark(any(AuthMember.class), anyLong());

        // then
        mockMvc.perform(post("/api/bookmarks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(storeId)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 생성 시 가게 정보 없음으로 인한 실패 테스트")
    void testCreateBookmarkFailure2() throws Exception {
        // given
        Long storeId = 1L;

        // when
        doThrow(new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID))
                .when(bookmarkService).saveBookmark(any(AuthMember.class), anyLong());

        // then
        mockMvc.perform(post("/api/bookmarks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(storeId))
                        .session(mockSession))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 페이지 조회 성공 테스트")
    void testGetBookmarksSuccess() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookmarkResponseDto> bookmarks = new PageImpl<>(bookmarkResponseDtos, pageable, bookmarkResponseDtos.size());


        // when
        when(bookmarkService.getBookmarks(mockMember, 0)).thenReturn(bookmarks);

        // then
        mockMvc.perform(get("/api/bookmarks?page=0")
                        .contentType("application/json")
                        .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookmarkId").value(bookmarkResponseDtos.get(0).bookmarkId()))
                .andExpect(jsonPath("$.content[0].storeResponseDto.name").value(bookmarkResponseDtos.get(0).storeResponseDto().name()))
                .andExpect(jsonPath("$.content[1].storeResponseDto.name").value(bookmarkResponseDtos.get(1).storeResponseDto().name()))
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 페이지 조회 시 세션 정보 없음으로 인한 실패 테스트")
    void testGetBookmarksFailure() throws Exception {
        // given

        // when
        when(bookmarkService.getBookmarks(mockMember, 0))
                .thenThrow(new AuthException(ResponseCode.SESSION_VALUE_EXCEPTION));

        // then
        mockMvc.perform(get("/api/bookmarks?page=0")
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 페이지 조회 시 회원 정보를 찾지 못해 발생하는 실패 테스트")
    void testGetBookmarksFailure2() throws Exception {
        // given

        // when
        doThrow(new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID))
                .when(bookmarkService).getBookmarks(mockMember, 0);

        // then
        mockMvc.perform(get("/api/bookmarks")
                        .contentType("application/json")
                        .session(mockSession))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크, 가게 아이디 조회 성공 테스트")
    void testGetBookmarkStoreIdsSuccess() throws Exception {
        // given
        List<BookmarkStoreIdsResponseDto> bookmarks = List.of(
                new BookmarkStoreIdsResponseDto(
                        1L, 1L
                ),
                new BookmarkStoreIdsResponseDto(
                        1L, 2L
                )
        );


        // when
        when(bookmarkService.getBookmarkStoreIds(mockMember)).thenReturn(bookmarks);

        // then
        mockMvc.perform(get("/api/bookmarks/ids")
                        .contentType("application/json")
                        .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookmarkId").value(1L))
                .andExpect(jsonPath("$[0].storeId").value(1L))
                .andExpect(jsonPath("$[1].bookmarkId").value(1L))
                .andExpect(jsonPath("$[1].storeId").value(2L))
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 가게 아이디 조회 시 세션 정보 없음으로 인한 실패 테스트")
    void testGetBookmarkStoreIdsFailure() throws Exception {
        // given

        // when
        when(bookmarkService.getBookmarkStoreIds(mockMember))
                .thenThrow(new AuthException(ResponseCode.SESSION_VALUE_EXCEPTION));

        // then
        mockMvc.perform(get("/api/bookmarks/ids")
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 가게 아이디 조회 시 회원 정보를 찾지 못해 발생하는 실패 테스트")
    void testGetBookmarkStoreIdsFailure2() throws Exception {
        // given

        // when
        doThrow(new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID))
                .when(bookmarkService).getBookmarkStoreIds(mockMember);

        // then
        mockMvc.perform(get("/api/bookmarks/ids")
                        .contentType("application/json")
                        .session(mockSession))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 삭제 성공 테스트")
    void testDelteBookmarkSuccess() throws Exception {
        // given

        // when
        doNothing().when(bookmarkService).deleteBookmark(any(), any());

        // then
        mockMvc.perform(delete("/api/bookmarks/1")
                        .contentType("application/json")
                        .session(mockSession))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 삭제 시 세션 정보 없음으로 인한 실패 테스트")
    void testDelteBookmarkFailure() throws Exception {
        // given

        // when
        doNothing().when(bookmarkService).deleteBookmark(any(), any());

        // then
        mockMvc.perform(delete("/api/bookmarks/1")
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 삭제 시 북마크 정보 없음으로 인한 실패 테스트")
    void testDelteBookmarkFailure2() throws Exception {
        // given

        // when
        doThrow(new NotFoundException(ResponseCode.BOOKMARK_NOT_FOUND))
                .when(bookmarkService).deleteBookmark(any(), any());

        // then
        mockMvc.perform(delete("/api/bookmarks/1")
                        .contentType("application/json")
                        .session(mockSession))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
