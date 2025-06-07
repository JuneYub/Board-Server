package com.campus.boardserver.boardserver.service;

import com.campus.boardserver.boardserver.dto.PostDTO;
import com.campus.boardserver.boardserver.dto.request.PostSearchRequest;

import java.util.List;

public interface PostSearchService {
    List<PostDTO> getProducts(PostSearchRequest postSearchRequest);
}
