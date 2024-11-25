package matal.bookmark.domain.repository;

import java.util.List;
import matal.bookmark.domain.Bookmark;
import matal.bookmark.dto.response.BookmarkStoreIdsResponseDto;
import matal.member.domain.Member;
import matal.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @EntityGraph(attributePaths = {"store"}, type = EntityGraphType.FETCH)
    @Query("SELECT DISTINCT b FROM Bookmark b LEFT JOIN b.store WHERE b.member.id = :memberId")
    Page<Bookmark> findBookmarksByMemberId(@Param("memberId") Long id, Pageable pageable);

    @Query("SELECT new matal.bookmark.dto.response.BookmarkStoreIdsResponseDto(b.bookmarkId, b.store.storeId) " +
            "FROM Bookmark b WHERE b.member.id = :memberId")
    List<BookmarkStoreIdsResponseDto> findBookmarkStoreIdsByMemberId(@Param("memberId") Long memberId);


    Boolean existsBookmarkByStoreAndMember(Store store, Member member);
}
