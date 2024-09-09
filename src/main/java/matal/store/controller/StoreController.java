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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "store", description = "store API")
public class StoreController {

    private final StoreService storeService;

    // 필터링 + 검색 (가게명 & 지하철역 & 키워드) + 정렬
    @GetMapping("/filtersearch")
    @Operation(summary = "필터링과 검색(가게명 & 지하철역 & 키워드) 로 가게 리스트를 페이지에 따라 조회 및 정렬", description = "사용자가 필터링을 하고 가게명 & 카테고리 & 지하철역 & 키워드에 관련된 가게를 검색할 때 가게 리스트를 조회하고 원하는 기준에 따라 정렬하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(mediaType ="application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public Page<StoreResponseDto> getStoreFilterSearch(
            @RequestParam(name = "menu", required = false) String keyword,
            @RequestParam(name = "location", required = false) String address,
            @RequestParam(name = "positive ratio", required = false) Double positiveRatio,
            @RequestParam(name = "reviews count", required = false) Long reviewsCount,
            @RequestParam(name = "rating", required = false) Double rating,
            @RequestParam(name = "keywords", required = false) String reviewword,
            @RequestParam(name = "single dining", required = false) Boolean isSoloDining,
            @RequestParam(name = "parking", required = false) Boolean isParking,
            @RequestParam(name = "waiting", required = false) Boolean isWaiting,
            @RequestParam(name = "pet friendly", required = false) Boolean isPetFriendly,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "nearby_station", required = false) String nearByStation,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "rating") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") String sortOrder
    ) {
        if(keyword == null && address == null && positiveRatio == null && reviewsCount == null && rating == null && reviewword == null &&
                isParking == null && isWaiting == null && isPetFriendly == null && isSoloDining == null && name == null && nearByStation == null)
            throw new IllegalArgumentException("최소한 하나의 조건은 입력해야 합니다.");
        return storeService.filterStores(name, nearByStation, keyword, address, reviewsCount, positiveRatio, rating,
                reviewword, isSoloDining, isParking, isWaiting, isPetFriendly, page, sortBy, sortOrder);
    }

    // 카테고리 선택 + 검색 (가게명 & 지하철역 & 키워드) + 정렬
    @GetMapping("/categorysearch")
    @Operation(summary = "카테고리 선택과 검색(가게명 & 지하철역 & 키워드) 로 가게 리스트를 페이지에 따라 조회 및 정렬", description = "사용자가 카테고리를 선택하고 가게명 & 지하철역 & 키워드에 관련된 가게를 검색할 때 가게 리스트를 조회하고 원하는 기준에 따라 정렬하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(mediaType ="application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public Page<StoreResponseDto> getStoreCategorySearch(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "nearby_station", required = false) String nearByStation,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "rating") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") String sortOrder
    ) {
        if(name == null && category == null && nearByStation == null)
            throw new IllegalArgumentException("최소한 하나의 검색 조건은 입력해야 합니다.");
        return storeService.categoryStores(name, category, nearByStation, page, sortBy, sortOrder);
    }

    // 가게 상세 정보 조회
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

    // 가게 모든 정보 조회
    @GetMapping("/all")
    @Operation(summary = "모든 가게 조회", description = "사용자가 대시보드를 클릭했을 때 모든 가게가 조회되기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = StoreResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public Page<StoreResponseDto> getStoreAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        return storeService.findAll(page);
    }
}
