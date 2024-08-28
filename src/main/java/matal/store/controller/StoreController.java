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

    // 가게 리스트 조회 이름 ANd 카테고리 AND 지하철역 + 정렬 기능
    @GetMapping("/search")
    @Operation(summary = "가게명 & 카테고리 & 지하철역 & 키워드 로 가게 리스트를 페이지에 따라 조회 및 정렬", description = "사용자가 가게명 & 카테고리 & 지하철역 & 키워드에 관련된 가게를 검색할 때 가게 리스트를 조회하고 원하는 기준에 따라 정렬하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(mediaType ="application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public List<StoreResponseDto> getStoreList(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "nearby_station", required = false) String nearby_station,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "rating") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") String sortOrder
    ) {
        if(name == null && category == null && nearby_station == null && keywords == null)
            throw new IllegalArgumentException("최소한 하나의 검색 조건은 입력해야 합니다.");
        return storeService.findStores(name, category, nearby_station, keywords, page, sortBy, sortOrder);
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
    public List<StoreResponseDto> getStoreAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        return storeService.findAll(page);
    }
}
