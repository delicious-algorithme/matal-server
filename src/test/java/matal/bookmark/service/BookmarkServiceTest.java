package matal.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import matal.bookmark.domain.Bookmark;
import matal.bookmark.domain.repository.BookmarkRepository;
import matal.bookmark.dto.response.BookmarkResponseDto;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.member.domain.Member;
import matal.member.domain.Role;
import matal.member.domain.repository.MemberRepository;
import matal.member.dto.request.AuthMember;
import matal.store.domain.Store;
import matal.store.domain.repository.StoreRepository;
import matal.store.dto.response.StoreListResponseDto;
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
public class BookmarkServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private Member member;
    private AuthMember mockMember;
    private Store store1;
    private Store store2;

    @BeforeEach
    void beforeEach() {
        member = Member.builder()
                .email("test@test.com")
                .password("encryptedTest")
                .nickname("test")
                .role(Role.MEMBER)
                .build();

        store1 = Store.builder()
                .storeId(1L)
                .name("testCafe")
                .category("카페")
                .reviewsCount(999L)
                .address("test address")
                .latitude(37.5665)
                .longitude(126.9780)
                .rating(4.5)
                .positiveRatio(30.0)
                .negativeRatio(100.0 - 30.0)
                .neutralRatio(0.0)
                .isSoloDining(true)
                .isParking(true)
                .isWaiting(true)
                .isPetFriendly(true)
                .build();

        store2 = Store.builder()
                .storeId(2L)
                .name("testCafe2")
                .category("카페2")
                .reviewsCount(999L)
                .address("test address2")
                .latitude(37.5665)
                .longitude(126.9780)
                .rating(4.5)
                .positiveRatio(30.0)
                .negativeRatio(100.0 - 30.0)
                .neutralRatio(0.0)
                .isSoloDining(true)
                .isParking(true)
                .isWaiting(true)
                .isPetFriendly(true)
                .build();

        mockMember = new AuthMember(
                1L,
                "test@test.com",
                "test",
                Role.MEMBER
        );
    }

    @Test
    @DisplayName("북마크 저장 성공 테스트")
    void testSaveBookmarkSuccess() {
        // given
        Long storeId = 1L;

        // when
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(storeRepository.findById(any())).thenReturn(Optional.of(store1));

        // then
        assertThatCode(
                () -> bookmarkService.saveBookmark(mockMember, storeId)).doesNotThrowAnyException();
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 저장 시 회원 정보 없음으로 인한 실패 테스트")
    void testSaveBookmarkFailure1() {
        // given
        Long storeId = 1L;

        // when
        when(memberRepository.findById(any()))
                .thenThrow(new NotFoundException(ResponseCode.MEMBER_NOT_FOUND_ID));

        // then
        assertThrows(NotFoundException.class, () -> bookmarkService.saveBookmark(mockMember, storeId));
    }

    @Test
    @DisplayName("북마크 저장 시 가게 정보 없음으로 인한 실패 테스트")
    void testSaveBookmarkFailure3() {
        // given
        Long storeId = 1L;

        // when
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(storeRepository.findById(any()))
                .thenThrow(new NotFoundException(ResponseCode.BOOKMARK_NOT_FOUND));

        // then
        assertThrows(NotFoundException.class, () -> bookmarkService.saveBookmark(mockMember, storeId));
    }

    @Test
    @DisplayName("북마크 리스트 조회 성공 테스트")
    void testGetBookmarksSuccess() {
        // given
        List<Bookmark> bookmarks = List.of(
                new Bookmark(
                        member,
                        store1
                ),
                new Bookmark(
                        member,
                        store2
                )
        );

        // when
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(bookmarkRepository.findBookmarksByMemberId(any())).thenReturn(bookmarks);

        // then
        List<BookmarkResponseDto> response = bookmarkService.getBookmarks(mockMember);
        assertThat(response.get(0).storeResponseDto().address()).isEqualTo(bookmarks.get(0).getStore().getAddress());
        assertThat(response.get(1).storeResponseDto().address()).isEqualTo(bookmarks.get(1).getStore().getAddress());
        assertThat(response.get(0).storeResponseDto().name()).isEqualTo(bookmarks.get(0).getStore().getName());
    }

    @Test
    @DisplayName("북마크 리스트 조회 시 회원 정보 없음으로 인한 실패 테스트")
    void testGetBookmarksFailure() {
        // given

        // when
        when(memberRepository.findById(any()))
                .thenThrow(new NotFoundException(ResponseCode.MEMBER_NOT_FOUND_ID));

        // then
        assertThrows(NotFoundException.class, () -> bookmarkService.getBookmarks(mockMember));
    }

    @Test
    @DisplayName("북마크 삭제 성공 테스트")
    void testDeleteBookmarkSuccess() {
        // given
        Bookmark mockBookmark = new Bookmark(member, store1);
        Long bookmarkId = 1L;

        // when
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(mockBookmark));

        // then
        assertThatCode(
                () -> bookmarkService.deleteBookmark(mockMember, bookmarkId)).doesNotThrowAnyException();
        verify(bookmarkRepository, times(1)).deleteById(bookmarkId);
    }

    @Test
    @DisplayName("북마크 삭제 시 회원 정보 없음으로 인한 실패 테스트")
    void testDeleteBookmarkFailure1() {
        // given

        // when
        when(memberRepository.findById(any()))
                .thenThrow(new NotFoundException(ResponseCode.MEMBER_NOT_FOUND_ID));

        // then
        assertThrows(NotFoundException.class, () -> bookmarkService.deleteBookmark(mockMember, any()));
    }

    @Test
    @DisplayName("북마크 삭제 시 북마크 정보 없음으로 인한 실패 테스트")
    void testDeleteBookmarkFailure2() {
        // given

        // when
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(bookmarkRepository.findById(any()))
                .thenThrow(new NotFoundException(ResponseCode.BOOKMARK_NOT_FOUND));

        // then
        assertThrows(NotFoundException.class, () -> bookmarkService.deleteBookmark(mockMember, any()));
    }
}
