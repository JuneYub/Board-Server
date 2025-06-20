package com.campus.boardserver.boardserver.controller;

import com.campus.boardserver.boardserver.aop.LoginCheck;
import com.campus.boardserver.boardserver.dto.CommentDTO;
import com.campus.boardserver.boardserver.dto.PostDTO;
import com.campus.boardserver.boardserver.dto.TagDTO;
import com.campus.boardserver.boardserver.dto.UserDTO;
import com.campus.boardserver.boardserver.dto.response.CommonResponse;
import com.campus.boardserver.boardserver.service.UserService;
import com.campus.boardserver.boardserver.service.impl.PostServiceImpl;
import com.campus.boardserver.boardserver.service.impl.UserServiceImpl;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Log4j2
public class PostController {

    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    public PostController(UserServiceImpl userService, PostServiceImpl postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(String accountId, @RequestBody PostDTO postDTO) {
        postService.register(accountId, postDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPost", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("my-posts")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<PostDTO>>> myPostInfo(String accountId) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        List<PostDTO> postDTOList = postService.getMyPosts(memberInfo.getId());
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "myPostInfo", postDTOList);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostResponse>> updatePosts(String accountId,
                                                                    @PathVariable(name = "postId") int postId,
                                                                    @RequestBody PostRequest postRequest) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        PostDTO postDTO = PostDTO.builder()
                .id(postId)
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(postRequest.getUserId())
                .fileId(postRequest.getFileId())
                .updateTime(new Date())
                .build();
        postService.updatePosts(postDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePosts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDeleteRequest>> deletePosts(String accountId,
                                                                         @PathVariable(name = "postId") int postId,
                                                                         @RequestBody PostDeleteRequest postDeleteRequest) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        postService.deletePosts(postId, memberInfo.getId());
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deletePosts", postDeleteRequest);
        return ResponseEntity.ok(commonResponse);
    }

    // -- comments --

    @PostMapping("comments")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> registerPostComment(String accountId,@RequestBody CommentDTO commentDTO) {
        postService.registerComment(commentDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "registerPostComment", commentDTO);

        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> updatePostComment(String accountId,
                                                                        @PathVariable(name = "commentId") int commentId,
                                                                        @RequestBody CommentDTO commentDTO) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null)
            postService.updateComment(commentDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "updatePostComment", commentDTO);

        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> deletePostComment(String accountId,
                                                                        @PathVariable(name = "commentId") int commentId,
                                                                        @RequestBody CommentDTO commentDTO) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null)
            postService.deletePostComment(memberInfo.getId(), commentId);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "deletePostComment", commentDTO);

        return ResponseEntity.ok(commonResponse);
    }

    // -- tags --
    @PostMapping("tags")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> registerPostTag(String accountId, @RequestBody TagDTO tagDTO) {
        postService.registerTag(tagDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "registerPostTag", tagDTO);

        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("tags/{tagId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> updatePostTag(String accountId,
                                                                        @PathVariable(name = "tagId") int tagId,
                                                                        @RequestBody TagDTO tagDTO) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null)
            postService.updateTag(tagDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "updatePostTag", tagDTO);

        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("tags/{tagId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> deletePostComment(String accountId,
                                                                        @PathVariable(name = "tagId") int tagId,
                                                                        @RequestBody TagDTO tagDTO) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null)
            postService.deletePostTag(memberInfo.getId(), tagId);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "deletePostComment", tagDTO);

        return ResponseEntity.ok(commonResponse);
    }



    // --- response 객체 ---

    @Getter
    @AllArgsConstructor
    private static class PostResponse {
        private List<PostDTO> postDTOs;
    }

    // --- request 객체 ---

    @Setter
    @Getter
    private static class PostRequest {
        private String name;
        private String contents;
        private int views;
        private int categoryId;
        private int userId;
        private int fileId;
        private Date updateTime;
    }

    @Setter
    @Getter
    private static class PostDeleteRequest {
        private int id;
        private int accountId;
    }
}
