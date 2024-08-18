package matal.store.dto;

import matal.store.entity.Store;

public record StoreInfoResponseDto(
        Long id, String name, String category, Long reviews_count, String address,
        String nearby_station, String phone, String business_hours, Double latitude,
        Double longitude, String positive_keywords, String review_summary, Double rating,
        Double positive_ratio, Double negative_ratio) {

    public static StoreInfoResponseDto fromInfo(Store store) {
        return new StoreInfoResponseDto(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getReviews_count(),
                store.getAddress(),
                store.getNearby_station(),
                store.getPhone(),
                store.getBusiness_hours(),
                store.getLatitude(),
                store.getLongitude(),
                store.getPositive_keywords(),
                store.getReview_summary(),
                store.getRating(),
                store.getPositive_ratio(),
                store.getNegative_ratio()
        );
    }
}
