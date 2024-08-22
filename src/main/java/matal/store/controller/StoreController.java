package matal.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreRequestDto;
import matal.store.dto.StoreResponseDto;
import matal.store.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "store", description = "store API")
public class StoreController {

    private final StoreService storeService;

    //이름으로 가게 리스트 조회
    @GetMapping("/search/name")
    @Operation(summary = "가게명으로 가게 리스트 조회", description = "사용자가 가게명을 검색할 때 관련 가게 리스트를 조회하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                content = {@Content(mediaType ="application/json",
                        array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public List<StoreResponseDto> getStoreListByNmae(@RequestParam(name = "name", required = false) String name) {
        if(name == null)
            throw new IllegalArgumentException("Error");
        return storeService.findStoresByName(name);
    }

    //카테고리로 가게 리스트 조회
    @GetMapping("/search/category")
    @Operation(summary = "카테고리명으로 가게 리스트 조회", description = "사용자가 카테고리를 검색할 때 관련 가게 리스트를 조회하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(mediaType ="application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public List<StoreResponseDto> getStoreListByCategory(@RequestParam(name = "category", required = false) String category) {
        if(category == null)
            throw new IllegalArgumentException("Error");
        return storeService.findStoresByCategory(category);
    }

    //지하철역으로 가게 리스트 조회
    @GetMapping("/search/nearby_station")
    @Operation(summary = "근처 역 이름으로 가게 리스트 조회", description = "사용자가 인근 역 이름으로 가게 리스트를 조회하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(mediaType ="application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public List<StoreResponseDto> getStoreListByStation(@RequestParam(name = "nearby_station", required = false) String nearby_station) {
        if(nearby_station == null)
            throw new IllegalArgumentException("Error");
        return storeService.findStoresByStation(nearby_station);
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
