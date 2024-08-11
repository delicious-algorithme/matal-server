package matal.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import matal.store.entity.Review;
import matal.store.entity.Store;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class StoreResponseDto {

    //가게 리스트 데이터 컬럼
    private Long id;
    private String name;
    private String category;
    private Long reviews_count;
    private String address;
    private String nearby_station;
    private String phone;
    private String business_hours;
    private Double rating;

    @Builder
    public StoreResponseDto(Long id, String name, String category, Long reviews_count,
                            String address, String nearby_station, String phone,
                            String business_hours, Double rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.reviews_count = reviews_count;
        this.address = address;
        this.nearby_station = nearby_station;
        this.phone = phone;
        this.business_hours = business_hours;
        this.rating = rating;
    }
}
