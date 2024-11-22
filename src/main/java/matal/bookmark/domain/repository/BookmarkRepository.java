package matal.bookmark.domain.repository;

import matal.bookmark.domain.Bookmark;
import matal.member.domain.Member;
import matal.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Page<Bookmark> findBookmarksByMemberId(Long id, Pageable pageable);

    Boolean existsBookmarkByStoreAndMember(Store store, Member member);
}
