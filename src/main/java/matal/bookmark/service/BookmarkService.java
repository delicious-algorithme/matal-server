package matal.bookmark.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matal.bookmark.domain.Bookmark;
import matal.bookmark.domain.repository.BookmarkRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

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
    public List<BookmarkResponseDto> getBookmarks(AuthMember authMember) {
        validateMember(authMember.memberId());
        return bookmarkRepository.findBookmarksByMemberId(authMember.memberId())
                .stream().map(BookmarkResponseDto::from).toList();
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
