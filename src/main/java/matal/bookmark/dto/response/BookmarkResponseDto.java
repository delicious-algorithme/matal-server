package matal.bookmark.dto.response;

import matal.bookmark.domain.Bookmark;
import matal.store.dto.response.StoreListResponseDto;

public record BookmarkResponseDto(Long bookmarkId,
                                  StoreListResponseDto storeResponseDto) {

    public static BookmarkResponseDto from(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getBookmarkId(),
                StoreListResponseDto.from(bookmark.getStore())
        );
    }
}
