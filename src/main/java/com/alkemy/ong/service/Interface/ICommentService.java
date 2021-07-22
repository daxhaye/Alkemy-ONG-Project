package com.alkemy.ong.service.Interface;


import java.util.List;


import com.alkemy.ong.dto.request.CommentCreationDto;
import com.alkemy.ong.dto.response.CommentResponseDto;
import com.alkemy.ong.model.Comment;

public interface ICommentService {
    List<CommentResponseDto> commentsOrderedByDate();

    CommentResponseDto createComment(String email, CommentCreationDto dto);

    String deleteComment(Long id, String email);

    Comment getCommentById(Long id);
}

