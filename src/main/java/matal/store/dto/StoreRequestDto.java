package matal.store.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StoreRequestDto {
    //검색 키워드 필드 : 가게이름, 카테고리, 지하철역
    private final String name;
    private final String category;
    private final String nearby_station;

    public StoreRequestDto(String name, String category, String nearby_station) {
        this.name = name;
        this.category = category;
        this.nearby_station = nearby_station;
    }
}
