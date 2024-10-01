package matal.store.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import matal.global.exception.BadRequestException;
import matal.global.exception.ResponseCode;

@AllArgsConstructor
@Getter
@Setter
public class StoreSearchFilterRequestDto {

    private final String searchKeywords;

    private final  List<String> category;

    private final List<String> addresses;

    private final List<String> positiveKeyword;

    private final Double positiveRatio;

    private final Long reviewsCount;

    private final Double rating;

    private final Boolean isSoloDining;

    private final Boolean isParking;

    private final Boolean isWaiting;

    private final Boolean isPetFriendly;

    private final String orderByRating;

    private final String orderByPositiveRatio;

    private final int page;

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
                isPetFriendly == null) {
            throw new BadRequestException(ResponseCode.STORE_BAD_REQUEST);
        }
        validatePage();
    }

    private void validatePage() {
        if (page < 0) {
            throw new BadRequestException(ResponseCode.STORE_PAGE_INVALID);
        }
    }

    public String convertCategoryToString() {
        if (this.category == null || this.category.isEmpty()) {
            return null;
        }
        return String.join("|", this.category);
    }

    public String convertAddressesToString() {
        if (this.addresses.isEmpty()) {
            return null;
        }
        return String.join("|", this.addresses);
    }

    public String convertPositiverKeywordsToString() {
        if (this.positiveKeyword.isEmpty()) {
            return null;
        }
        return String.join("|", this.positiveKeyword);
    }
}
