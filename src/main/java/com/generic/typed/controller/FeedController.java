package com.generic.typed.controller;

import com.generic.typed.dto.request.BookmarkRequest;
import com.generic.typed.dto.response.FeedListResponse;
import com.generic.typed.security.jwt.util.IfLogin;
import com.generic.typed.security.jwt.util.LoginMemberDto;
import com.generic.typed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {
    private final FeedService feedService;

    /**
     * 전체 피드 목록 조회
     */
    @GetMapping
    public ResponseEntity<FeedListResponse> getFeedList(
            @IfLogin LoginMemberDto loginMemberDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        FeedListResponse response = feedService.getFeedList(
                loginMemberDto.getEmail(), page, size);
        return ResponseEntity.ok(response);
    }


    /**
     * 북마크한 피드 목록 조회
     */
    @GetMapping("/bookmarks")
    public ResponseEntity<FeedListResponse> getBookmarkedFeed(
            @IfLogin LoginMemberDto loginMemberDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        FeedListResponse response = feedService.getBookmarkedFeed(
                loginMemberDto.getEmail(), page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * 북마크 추가
     */
    @PostMapping("/bookmarks")
    public ResponseEntity<Void> addBookmark(
            @IfLogin LoginMemberDto loginMemberDto,
            @RequestBody BookmarkRequest request) {

        feedService.addBookmark(
                loginMemberDto.getEmail(), request.getContentType(), request.getContentId());
        return ResponseEntity.ok().build();
    }

    /**
     * 북마크 삭제
     */
    @DeleteMapping("/bookmarks/{contentType}/{contentId}")
    public ResponseEntity<Void> removeBookmark(
            @IfLogin LoginMemberDto loginMemberDto,
            @PathVariable String contentType,
            @PathVariable Long contentId) {

        feedService.removeBookmark(
                loginMemberDto.getEmail(), contentType, contentId);
        return ResponseEntity.ok().build();
    }
}