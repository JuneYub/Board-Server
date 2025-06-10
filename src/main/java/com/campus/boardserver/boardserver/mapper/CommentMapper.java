package com.campus.boardserver.boardserver.mapper;

import com.campus.boardserver.boardserver.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    public int register(CommentDTO commentDTO);

    public void updateComments(CommentDTO commentDTO);

    public void deleteComment(int commentId);
}
