package matal.store.dto;

import matal.store.entity.Store;

public record StoreListResponseDto(
        Long storeId,
        String name,
        String address,
        String storeLink,
        Double rating,
        String imageUrls,
        Double positiveRatio,
        String positiveKeywords,
        Double latitude,
        Double longitude
) {
    public static StoreListResponseDto from(Store store) {
        return new StoreListResponseDto(
                store.getStoreId(),
                store.getName(),
                store.getAddress(),
                store.getStoreLink(),
                store.getRating(),
                store.getImageUrls(),
                store.getPositiveRatio(),
                store.getPositiveKeywords(),
                store.getLatitude(),
                store.getLongitude()
        );
    }
}
