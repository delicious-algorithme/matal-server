package matal.store.dto;

import matal.store.entity.Store;

public record StoreResponseDto(Long id, String name, String category, Long reviews_count, String address,
                               String nearby_station, String phone, String business_hours, Double rating,
                               Double positive_ratio, String image_urls){

    public static StoreResponseDto from(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                store.getCategory(),
                (long) store.getReviews_count(), // 리뷰의 수를 계산하는 로직
                store.getAddress(),
                store.getNearby_station(),
                store.getPhone(),
                store.getBusiness_hours(),
                store.getRating(),
                store.getPositive_ratio(),
                store.getImage_urls()
        );
    }
}