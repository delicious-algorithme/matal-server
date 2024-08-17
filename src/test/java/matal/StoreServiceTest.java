package matal;

import matal.store.dto.StoreRequestDto;
import matal.store.dto.StoreResponseDto;
import matal.store.entity.Store;
import matal.store.repository.StoreRepository;
import matal.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
public class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @BeforeEach
    void setUp() {
        storeRepository.deleteAll();

        Store store1 = Store.builder()
                .keyword("Test")
                .name("Test Store1")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 1")
                .nearby_station("Station 1")
                .phone("123-456-789")
                .business_hours("9-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.5)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

        Store store2 = Store.builder()
                .keyword("Test")
                .name("Test Store2")
                .store_link("ssss")
                .reviews_count(10L)
                .category("Food")
                .address("Address 2")
                .nearby_station("Station 2")
                .phone("12345666")
                .business_hours("8-23")
                .latitude(12.4)
                .longitude(11.11)
                .positive_keywords("good")
                .review_summary("summary")
                .rating(4.0)
                .positive_ratio(93.0)
                .negative_ratio(7.0)
                .build();

         storeRepository.save(store1);
         storeRepository.save(store2);
    }

    @Test
    void StoreNameSearchTest() {
        // given
        StoreRequestDto requestDto = new StoreRequestDto("Test Store1", null, null);

        // when
        Optional<List<StoreResponseDto>> storeResponseDtoList = storeService.StoreNameSearch(requestDto);

        // then
        assertThat(storeResponseDtoList).isPresent();
        assertThat(storeResponseDtoList.get()).hasSize(1);
        assertThat(storeResponseDtoList.get().get(0).name()).isEqualTo("Test Store1");
    }

    @Test
    void StoreCategorySearchTest() {
        // given
        StoreRequestDto requestDto = new StoreRequestDto(null, "Food", null);

        // when
        Optional<List<StoreResponseDto>> storeResponseDtoList = storeService.StoreCategorySearch(requestDto);

        // then
        assertThat(storeResponseDtoList).isPresent();
        assertThat(storeResponseDtoList.get()).hasSize(2);
        assertThat(storeResponseDtoList.get().get(0).category()).isEqualTo("Food");
    }

    @Test
    void StoreStationSearchTest() {
        // given
        StoreRequestDto requestDto = new StoreRequestDto(null, null, "FakeStation");

        // when
        Optional<List<StoreResponseDto>> storeResponseDtoList = storeService.StoreStationSearch(requestDto);

        // then
        assertThat(storeResponseDtoList).isPresent();
        assertThat(storeResponseDtoList.get()).isEmpty();
    }
}