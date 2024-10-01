package matal.store.dto.response;

import matal.store.domain.Store;

public record StoreResponseDto(Long storeId,
                               String keyword,
                               String name,
                               String storeLink,
                               String category,
                               Long reviewsCount,
                               String address,
                               String nearbyStation,
                               String phone,
                               String businessHours,
                               Double latitude,
                               Double longitude,
                               String menuAndPrice,
                               String imageUrl,
                               String positiveKeywords,
                               String negativeKeywords,
                               String reviewSummary,
                               Double rating,
                               Double positiveRatio,
                               Double negativeRatio,
                               Double neutralRatio,
                               Boolean isSoloDining,
                               Boolean isParking,
                               String parkingTip,
                               Boolean isWaiting,
                               String waitingTip,
                               Boolean isPetFriendly,
                               String recommendMenu) {

    public static StoreResponseDto from(Store store) {
        return new StoreResponseDto(
                store.getStoreId(),
                store.getKeyword(),
                store.getName(),
                store.getStoreLink(),
                store.getCategory(),
                store.getReviewsCount(),
                store.getAddress(),
                store.getNearbyStation(),
                store.getPhone(),
                store.getBusinessHours(),
                store.getLatitude(),
                store.getLongitude(),
                store.getMainMenu(),
                store.getImageUrls(),
                store.getPositiveKeywords(),
                store.getNegativeKeywords(),
                store.getReviewSummary(),
                store.getRating(),
                store.getPositiveRatio(),
                store.getNegativeRatio(),
                store.getNeutralRatio(),
                store.getIsSoloDining(),
                store.getIsParking(),
                store.getParkingTip(),
                store.getIsWaiting(),
                store.getWaitingTip(),
                store.getIsPetFriendly(),
                store.getRecommendedMenu()
        );
    }
}

