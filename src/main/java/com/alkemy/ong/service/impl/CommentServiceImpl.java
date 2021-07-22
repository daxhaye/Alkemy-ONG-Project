package com.alkemy.ong.service.impl;

import com.alkemy.ong.Enum.ERole;
import com.alkemy.ong.dto.response.CommentResponseDto;
import com.alkemy.ong.model.Comment;
import com.alkemy.ong.model.Role;
import com.alkemy.ong.repository.CommentRepository;
import com.alkemy.ong.service.Interface.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;


import com.alkemy.ong.service.Interface.INewsService;
import org.springframework.data.projection.ProjectionFactory;

import com.alkemy.ong.dto.request.CommentCreationDto;
import com.alkemy.ong.model.News;
import com.alkemy.ong.model.User;
import com.alkemy.ong.repository.UsersRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class CommentServiceImpl implements ICommentService{

	private final UsersRepository repoUser;
	private final INewsService newsService;
	private final ProjectionFactory projectionFactory;
	private final CommentRepository repoComment;

	@Autowired
	public CommentServiceImpl(UsersRepository repoUser, INewsService newsService, ProjectionFactory projectionFactory, CommentRepository repoComment) {
		this.repoUser = repoUser;
		this.newsService = newsService;
		this.projectionFactory = projectionFactory;
		this.repoComment = repoComment;
	}

	@Autowired
	private MessageSource messageSource;

	public List<CommentResponseDto> commentsOrderedByDate() {return (List<CommentResponseDto>) repoComment.findAllByOrderCreatedDesc();}

	@Override
	public CommentResponseDto createComment(String email, CommentCreationDto dto) {
		
		User user = repoUser.findByEmail(email).get();
		News post = newsService.getNewById(dto.getNews());
		
		Comment comment = new Comment(
				user,
				dto.getBody(),
				post
		);

		return projectionFactory.createProjection(CommentResponseDto.class, repoComment.save(comment));
	}

	@Override
	public String deleteComment(Long id, String email) {
		User user = repoUser.findByEmail(email).get();
		Comment comment = getCommentById(id);
		if(isAdmin(user.getRoles()) || isCreator(user.getId(), comment.getId())){
			repoComment.deleteById(id);
		}else
			throw new EntityNotFoundException(messageSource.getMessage("comment.error.invalid.user",null,Locale.getDefault()));

		return messageSource.getMessage("comment.delete.successful",null, Locale.getDefault());

	}

	public Comment getCommentById(Long id){
		return repoComment.findById(id).orElseThrow(
				() -> new EntityNotFoundException(
						messageSource.getMessage("comment.error.notFound",null,Locale.getDefault())
				)
		);
	}

	public Boolean isAdmin(Set<Role> role){
		for(Role r : role){
			if(r.getRoleName() == ERole.ROLE_ADMIN){
				return true;
			}
		}
		return false;
	}

	public Boolean isCreator(long id, Long commentId){
		Comment comment = repoComment.getById(commentId);
		if(comment.getUser().getId() == id)
			return true;
		return false;
	}
}
