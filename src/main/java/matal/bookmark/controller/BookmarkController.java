package matal.bookmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import matal.bookmark.dto.response.BookmarkResponseDto;
import matal.bookmark.service.BookmarkService;
import matal.global.auth.LoginMember;
import matal.global.exception.ErrorResponse;
import matal.member.dto.request.AuthMember;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@Tag(name = "Bookmark", description = "Bookmark API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    @Operation(summary = "북마크 생성 기능", description = "사용자가 가게를 북마크로 저장할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "북마크 생성 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "401", description = "회원 세션 정보 없음",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "가게 조회 실패",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<Void> createBookmark(
            @LoginMember @Parameter(hidden = true) AuthMember authMember,
            @RequestBody Long storeId)
    {
        bookmarkService.saveBookmark(authMember, storeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "북마크 조회 기능", description = "사용자가 북마크한 가게들을 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크 조회 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "401", description = "회원 세션 정보 없음",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "회원 조회 실패",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<Page<BookmarkResponseDto>> getBookmarks(
            @LoginMember @Parameter(hidden = true) AuthMember authMember,
            @RequestParam(name = "page", defaultValue = "0") int page)
    {
        Page<BookmarkResponseDto> bookmarkResponse = bookmarkService.getBookmarks(authMember, page);
        return ResponseEntity.ok(bookmarkResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "북마크 삭제 기능", description = "사용자가 가게를 북마크로 저장할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "북마크 삭제 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "401", description = "회원 세션 정보 없음",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "북마크 정보 조회 실패",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<Void> deleteBookmark(
            @LoginMember @Parameter(hidden = true) AuthMember authMember,
            @PathVariable Long id)
    {
        bookmarkService.deleteBookmark(authMember, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
