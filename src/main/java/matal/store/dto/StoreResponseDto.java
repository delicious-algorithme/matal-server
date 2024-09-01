package matal.store.dto;

import matal.store.entity.StoreInfo;
import matal.store.entity.StoreReviewInsight;

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

    public static StoreResponseDto from(StoreInfo storeInfo, StoreReviewInsight reviewInsight) {
        return new StoreResponseDto(
                storeInfo.getStoreId(),
                storeInfo.getKeyword(),
                storeInfo.getName(),
                storeInfo.getStoreLink(),
                storeInfo.getCategory(),
                storeInfo.getReviewsCount(),
                storeInfo.getAddress(),
                storeInfo.getNearByStation(),
                storeInfo.getPhone(),
                storeInfo.getBusinessHours(),
                storeInfo.getLatitude(),
                storeInfo.getLongitude(),
                storeInfo.getMenuAndPrice(),
                storeInfo.getImageUrls(),
                reviewInsight.getPositiveKeywords(),
                reviewInsight.getNegativeKeywords(),
                reviewInsight.getReviewSummary(),
                reviewInsight.getRating(),
                reviewInsight.getPositiveRatio(),
                reviewInsight.getNegativeRatio(),
                reviewInsight.getNeutralRatio(),
                reviewInsight.getIsSoloDining(),
                reviewInsight.getIsParking(),
                reviewInsight.getParkingTip(),
                reviewInsight.getIsWaiting(),
                reviewInsight.getWaitingTip(),
                reviewInsight.getIsPetFriendly(),
                reviewInsight.getRecommendMenu()
        );
    }
}

