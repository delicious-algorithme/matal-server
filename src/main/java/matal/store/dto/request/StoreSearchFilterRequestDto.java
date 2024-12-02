package matal.store.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import matal.global.exception.BadRequestException;
import matal.global.exception.ResponseCode;

public record StoreSearchFilterRequestDto(String searchKeywords,
                                          List<String> category,
                                          List<String> addresses,
                                          List<String> positiveKeyword,
                                          Double positiveRatio,
                                          Long reviewsCount,
                                          Double rating,
                                          Boolean isSoloDining,
                                          Boolean isParking,
                                          Boolean isWaiting,
                                          Boolean isPetFriendly,
                                          @JsonProperty(defaultValue = "rating") String sortTarget,
                                          int page) {

    public void validateFields() {
        if (searchKeywords == null &&
                category == null &&
                addresses == null &&
                positiveKeyword == null &&
                positiveRatio == null &&
                reviewsCount == null &&
                rating == null &&
                isSoloDining == null &&
                isParking == null &&
                isWaiting == null &&
                isPetFriendly == null &&
                sortTarget == null) {
            throw new BadRequestException(ResponseCode.STORE_BAD_REQUEST);
        }
        validateSort(sortTarget);
        validatePage();
    }

    private void validatePage() {
        if (page < 0) {
            throw new BadRequestException(ResponseCode.STORE_PAGE_INVALID);
        }
    }

    private void validateSort(String sortTarget) {
        List<String> validatedString = List.of("rating", "positiveRatio");
        if(!validatedString.contains(sortTarget))
            throw new BadRequestException(ResponseCode.STORE_BAD_REQUEST);
    }
}
