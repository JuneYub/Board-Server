package com.campus.boardserver.boardserver.controller;

import com.campus.boardserver.boardserver.dto.PostDTO;
import com.campus.boardserver.boardserver.dto.request.PostSearchRequest;
import com.campus.boardserver.boardserver.service.impl.PostSearchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@Log4j2
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchServiceImpl postSearchService;

    @PostMapping
    public PostSearchResponse search(@RequestBody PostSearchRequest postSearchRequest) {
        List<PostDTO> postDTOList = postSearchService.getProducts(postSearchRequest);
        return new PostSearchResponse(postDTOList);
    }

    @GetMapping
    public PostSearchResponse searchByTagName(String tagName) {
        List<PostDTO> postDTOList = postSearchService.getPostByTag(tagName);
        return new PostSearchResponse(postDTOList);
    }


    // ------ response 객체 -------

    @Getter
    @AllArgsConstructor
    private static class PostSearchResponse {
        private List<PostDTO> postDTOList;
    }
}
