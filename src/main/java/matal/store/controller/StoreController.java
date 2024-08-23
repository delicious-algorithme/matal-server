package matal.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreResponseDto;
import matal.store.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "store", description = "store API")
public class StoreController {

    private final StoreService storeService;

    //가게 리스트 조회 이름 ANd 카테고리 AND 지하철역 + 정렬 기능
    @GetMapping("/search")
    @Operation(summary = "가게명 & 카테고리 & 지하철역으로 가게 리스트를 페이지에 따라 조회 및 정렬", description = "사용자가 가게명 & 카테고리 & 지하철역에 관련된 가게를 검색할 때 가게 리스트를 조회하고 원하는 기준에 따라 정렬하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                content = {@Content(mediaType ="application/json",
                        array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public List<StoreResponseDto> getStoreListByNmae(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "nearby_station", required = false) String nearby_station,
            @RequestParam(name = "pahe", required = false, defaultValue = "0") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "rating") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "upper") String sortOrder
    ) {
        if(name == null && category == null && nearby_station == null)
            throw new IllegalArgumentException("최소한 하나의 검색 조건은 입력해야 합니다.");

        List<StoreResponseDto> results = new ArrayList<>();

        Set<StoreResponseDto> nameResults = new HashSet<>();
        Set<StoreResponseDto> categoryResults = new HashSet<>();
        Set<StoreResponseDto> stationResults = new HashSet<>();

        if (name != null) {
            nameResults.addAll(storeService.findStoresByName(name, page));
        }
        if (category != null) {
            categoryResults.addAll(storeService.findStoresByCategory(category, page));
        }
        if (nearby_station != null) {
            stationResults.addAll(storeService.findStoresByStation(nearby_station, page));
        }

        // 각 조건별 결과 리스트를 통합
        if (nameResults.isEmpty() && categoryResults.isEmpty() && stationResults.isEmpty()) {
            return new ArrayList<>();
        }

        // 결과 리스트가 비어있지 않다면, 교집합을 구한다.
        if (!nameResults.isEmpty()) {
            results.addAll(nameResults);
            if (!categoryResults.isEmpty()) {
                results.retainAll(categoryResults);
            }
            if (!stationResults.isEmpty()) {
                results.retainAll(stationResults);
            }
        } else if (!categoryResults.isEmpty()) {
            results.addAll(categoryResults);
            if (!stationResults.isEmpty()) {
                results.retainAll(stationResults);
            }
        } else if (!stationResults.isEmpty()) {
            results.addAll(stationResults);
        }

        // 결과를 정렬한다.
        return storeService.sortStores(new ArrayList<>(results), sortBy, sortOrder);
    }

    //가게 상세 정보 조회
    @GetMapping("/{id}")
    @Operation(summary = "고유 ID 값으로 가게 상세 조회", description = "사용자가 가게 리스트 중 하나를 선택할 때 가게의 상세 정보를 조회하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = StoreResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public StoreResponseDto getStoreDetail(@PathVariable Long id) {
        return storeService.findById(id);
    }
}
