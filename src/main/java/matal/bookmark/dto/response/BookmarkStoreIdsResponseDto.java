package matal.bookmark.dto.response;

import matal.bookmark.domain.Bookmark;

public record BookmarkStoreIdsResponseDto(Long bookmarkId,
                                          Long storeId) {

    public static BookmarkStoreIdsResponseDto from(Bookmark bookmark) {
        return new BookmarkStoreIdsResponseDto(
                bookmark.getBookmarkId(),
                bookmark.getStore().getStoreId()
        );
    }
}
