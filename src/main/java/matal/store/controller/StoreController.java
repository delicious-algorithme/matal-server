package matal.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import matal.global.exception.ErrorResponse;
import matal.store.dto.request.StoreSearchFilterRequestDto;
import matal.store.dto.response.StoreListResponseDto;
import matal.store.dto.response.StoreResponseDto;
import matal.store.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "store", description = "store API")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/searchAndFilter")
    @Operation(summary = "통합 필터 및 검색 기능", description = "사용자가 검색과 필터를 통해 특정 유형의 가게를 불러올 때 사용할 수 있는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = StoreListResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "실패",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<Page<StoreListResponseDto>> searchAndFilterStores(@RequestBody StoreSearchFilterRequestDto requestDto) {
        requestDto.validateFields();
        Page<StoreListResponseDto> response = storeService.searchAndFilterStores(requestDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "고유 ID 값으로 가게 상세 조회", description = "사용자가 가게 리스트 중 하나를 선택할 때 가게의 상세 정보를 조회하기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = StoreListResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "실패",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<StoreResponseDto> getStoreDetail(@PathVariable Long id) {
        StoreResponseDto storeResponse = storeService.findById(id);
        return ResponseEntity.ok().body(storeResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "모든 가게 조회", description = "사용자가 대시보드를 클릭했을 때 모든 가게가 조회되기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = StoreListResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    public ResponseEntity<Page<StoreListResponseDto>> getStoreAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        Page<StoreListResponseDto> storeListResponse = storeService.findAll(page);
        return ResponseEntity.ok().body(storeListResponse);
    }

    @GetMapping("/top")
    @Operation(summary = "상위 10개의 가게 조회", description = "별점, 긍정비율 순으로 정렬했을 때 상위 10개의 가게 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = StoreListResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "실패"),
    })
    private ResponseEntity<Page<StoreListResponseDto>> getStoreTop() {
        Page<StoreListResponseDto> storeListResponse = storeService.findTop();
        return ResponseEntity.ok().body(storeListResponse);
    }
}
