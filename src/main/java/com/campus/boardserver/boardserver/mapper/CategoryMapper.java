package com.campus.boardserver.boardserver.mapper;

import com.campus.boardserver.boardserver.dto.CategoryDTO;

public interface CategoryMapper {

    public int register(CategoryDTO categoryDTO);

    public void updateCategory(CategoryDTO categoryDTO);

    public void deleteCategory(int categoryId);
}
