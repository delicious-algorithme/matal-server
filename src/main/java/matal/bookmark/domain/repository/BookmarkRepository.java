package matal.bookmark.domain.repository;

import java.util.List;
import matal.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findBookmarksByMemberId(Long id);
}
