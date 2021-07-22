package com.alkemy.ong.controller;

import com.alkemy.ong.dto.response.CommentResponseDto;
import com.alkemy.ong.service.Interface.ICommentService;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.alkemy.ong.dto.request.CommentCreationDto;
import com.alkemy.ong.security.JwtFilter;
import com.alkemy.ong.security.JwtProvider;
import com.amazonaws.services.lexruntime.model.NotAcceptableException;


@RestController
@RequestMapping(path = "/comments")
public class CommentController {

	private final MessageSource message;
	private final JwtFilter jwtFilter;
	private final  JwtProvider jwtProvider;
	private final  ICommentService iComment;


	@Autowired
	public CommentController(MessageSource message, JwtFilter jwtFilter, JwtProvider jwtProvider, ICommentService iComment) {
		this.message = message;
		this.jwtFilter = jwtFilter;
		this.jwtProvider = jwtProvider;
		this.iComment = iComment;
	}

	@GetMapping
	public ResponseEntity<List<CommentResponseDto>> commentsOrderedByDate(){
		return ResponseEntity.status(HttpStatus.OK).body(iComment.commentsOrderedByDate());
	}

	@PostMapping
	public ResponseEntity<Object> addComment(@RequestBody @Valid CommentCreationDto dto, HttpServletRequest request){
		try {
			String token = jwtFilter.getToken(request);
			String email = jwtProvider.getEmailFromToken(token);
			return ResponseEntity.status(HttpStatus.CREATED).body(iComment.createComment(email,dto));
		}catch(NotAcceptableException e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.getMessage("comment.error.create", null, Locale.getDefault()));
		}
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable("id") Long id, HttpServletRequest request){
		try{
			String token = jwtFilter.getToken(request);
			String email = jwtProvider.getEmailFromToken(token);
			return ResponseEntity.status(HttpStatus.OK).body(iComment.deleteComment(id,email));
		}catch (Exception e){
			if(e.getMessage().equals(message.getMessage("comment.error.notFound",null,Locale.getDefault())))
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
			else
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}



}
