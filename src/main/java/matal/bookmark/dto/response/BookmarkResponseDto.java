package matal.bookmark.dto.response;

import matal.bookmark.domain.Bookmark;
import matal.store.domain.Store;

public record BookmarkResponseDto(Long bookmarkId,
                                  Store store) {

    public static BookmarkResponseDto from(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getBookmarkId(),
                bookmark.getStore()
        );
    }
}
