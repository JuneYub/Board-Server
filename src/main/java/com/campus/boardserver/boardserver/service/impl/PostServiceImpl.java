package com.campus.boardserver.boardserver.service.impl;

import com.campus.boardserver.boardserver.dto.CommentDTO;
import com.campus.boardserver.boardserver.dto.PostDTO;
import com.campus.boardserver.boardserver.dto.TagDTO;
import com.campus.boardserver.boardserver.dto.UserDTO;
import com.campus.boardserver.boardserver.mapper.CommentMapper;
import com.campus.boardserver.boardserver.mapper.PostMapper;
import com.campus.boardserver.boardserver.mapper.TagMapper;
import com.campus.boardserver.boardserver.mapper.UserProfileMapper;
import com.campus.boardserver.boardserver.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TagMapper tagMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date());

        if (memberInfo != null) {
            postMapper.register(postDTO);
            Integer postId = postDTO.getId();
            for(int i = 0 ; i < postDTO.getTagDTOList().size(); i++) {
                TagDTO tagDTO = postDTO.getTagDTOList().get(i);
                tagMapper.register(tagDTO);
                Integer tagId = tagDTO.getId();
                tagMapper.createPostTag(tagId, postId);
            }
        } else {
            log.error("register ERROP! {}", postDTO);
            throw new RuntimeException("register ERROP! 게시글 수정 메서드를 확인해주세요" + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyPosts(int accountId) {
        List<PostDTO> postDtoList = postMapper.selectMyPosts(accountId);
        return postDtoList;
    }

    @Override
    public void updatePosts(PostDTO postDTO) {
        if(postDTO != null && postDTO.getId() != 0) {
            postMapper.updatePosts(postDTO);
        } else {
            log.error("updatePosts ERROP! {}", postDTO);
            throw new RuntimeException("updatePosts ERROP! 게시글 수정 메서드를 확인해주세요" + postDTO);
        }
    }

    @Override
    public void deletePosts(int userId, int postId) {
        if(userId != 0 && postId != 0) {
            postMapper.deletePosts(postId);
        } else {
            log.error("deletePosts ERROP! {}", postId);
            throw new RuntimeException("deletePosts ERROP! 게시글 수정 메서드를 확인해주세요" + postId);
        }
    }

    @Override
    public void registerComment(CommentDTO commentDTO) {
        if (commentDTO.getPostId() != 0) {
            commentMapper.register(commentDTO);
        } else {
            log.error("registerComment {}", commentDTO );
            throw new RuntimeException("registerComment {}" + commentDTO);
        }
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        if (commentDTO != null) {
            commentMapper.updateComments(commentDTO);
        } else {
            log.error("update error!");
            throw new RuntimeException("update error!");
        }
    }

    @Override
    public void deletePostComment(int userId, int commentId) {
        if(userId != 0 && commentId != 0) {
            commentMapper.deleteComment(commentId);
        } else {
            log.error("delete error! {}" , commentId);
            throw new RuntimeException("delete error! {}" + commentId);
        }
    }

    @Override
    public void registerTag(TagDTO tagDTO) {
        if(tagDTO != null) {
            tagMapper.register(tagDTO);
        } else {
            log.error("registerTag error!{}", tagDTO);
            throw new RuntimeException("registerTag error!" + tagDTO);


        }
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        if (tagDTO != null) {
            tagMapper.updateTags(tagDTO);
        } else {
            log.error("updateTag error!");
            throw new RuntimeException("updateTag error!");
        }
    }

    @Override
    public void deletePostTag(int userId, int tagId) {
        if(userId != 0 && tagId != 0) {
            tagMapper.deletePostTag(tagId);
        } else {
            log.error("deletePostTag error!");
            throw new RuntimeException("deletePostTag error!");
        }
    }
}
