package matal.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import matal.store.dto.StoreInfoResponseDto;
import matal.store.dto.StoreResponseDto;
import matal.store.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "store", description = "store API")
public class StoreController {

    private final StoreService storeService;

    //이름으로 가게 리스트 조회
    @GetMapping("/search/name")
    @Operation(summary = "Search store List Bu name", description = "검색 결과로 나온 가게 리스트들을 확인할 수 있다.")
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
    @Operation(summary = "Search store List Bu name", description = "검색 결과로 나온 가게 리스트들을 확인할 수 있다.")
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
    @Operation(summary = "Search store List Bu name", description = "검색 결과로 나온 가게 리스트들을 확인할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(mediaType ="application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StoreResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public List<StoreResponseDto> getStoreListByStation( @RequestParam(name = "nearby_station", required = false) String nearby_station) {
        if(nearby_station == null)
            throw new IllegalArgumentException("Error");
        return storeService.findStoresByStation(nearby_station);
    }

    //가게 상세 정보 조회
    @GetMapping("{storeid}")
    @Operation(summary = "Get store detail infotmation", description = "가게 리스트 중 하나를 클릭하면 가게 번호를 조회하여 해당 가게의 상세 정보, 리뷰 등을 조회할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = StoreInfoResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public StoreInfoResponseDto getstoreInfo(@PathVariable("storeid") Long storeid) {
        if(storeid == null)
            throw new IllegalArgumentException("Error");
        return storeService.getStoreInfo(storeid);
    }
}