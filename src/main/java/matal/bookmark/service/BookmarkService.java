package matal.bookmark.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matal.bookmark.domain.Bookmark;
import matal.bookmark.domain.repository.BookmarkRepository;
import matal.bookmark.dto.response.BookmarkStoreIdsResponseDto;
import matal.bookmark.dto.response.BookmarkResponseDto;
import matal.global.exception.AlreadyExistException;
import matal.global.exception.AuthException;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.member.domain.Member;
import matal.member.domain.repository.MemberRepository;
import matal.member.dto.request.AuthMember;
import matal.store.domain.Store;
import matal.store.domain.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private static final int PAGE_SIZE = 10;

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void saveBookmark(AuthMember authMember, Long storeId) {

        Member member = memberRepository.findById(authMember.memberId())
                .orElseThrow(() -> new NotFoundException(ResponseCode.MEMBER_NOT_FOUND_ID));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.STORE_NOT_FOUND_ID));

        if(bookmarkRepository.existsBookmarkByStoreAndMember(store, member))
            throw new AlreadyExistException(ResponseCode.BOOKMARK_ALREADY_EXIST);

        bookmarkRepository.save(createBookmark(member, store));
    }

    @Transactional(readOnly = true)
    public Page<BookmarkResponseDto> getBookmarks(AuthMember authMember, int page) {
        validateMember(authMember.memberId());
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return bookmarkRepository.findBookmarksByMemberId(authMember.memberId(), pageable).map(BookmarkResponseDto::from);
    }

    @Transactional(readOnly = true)
    public List<BookmarkStoreIdsResponseDto> getBookmarkAndStoreIds(AuthMember authMember) {
        validateMember(authMember.memberId());
        return bookmarkRepository.findBookmarkStoreIdsByMemberId(authMember.memberId());
    }

    @Transactional
    public void deleteBookmark(AuthMember authMember, Long bookmarkId) {
        validateMember(authMember.memberId());
        bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.BOOKMARK_NOT_FOUND));
        bookmarkRepository.deleteById(bookmarkId);
    }

    private Bookmark createBookmark(Member member, Store store) {
        return Bookmark.builder()
                .member(member)
                .store(store)
                .build();
    }

    private void validateMember(Long memberId) {
        if(memberRepository.findById(memberId).isEmpty())
            throw new AuthException(ResponseCode.MEMBER_NOT_FOUND_ID);
    }
}
