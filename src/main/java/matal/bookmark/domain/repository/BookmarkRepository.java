package matal.bookmark.domain.repository;

import java.util.List;
import matal.bookmark.domain.Bookmark;
import matal.member.domain.Member;
import matal.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findBookmarksByMemberId(Long id);

    Boolean existsBookmarkByStoreAndMember(Store store, Member member);
}
