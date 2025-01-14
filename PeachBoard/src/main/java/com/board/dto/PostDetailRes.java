package com.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.board.entity.PostEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * PostDetailRes.java 
 * 
 * 게시글 상세 응답 dto
 */
@Getter
@Setter
@Builder
public class PostDetailRes {

	private Integer id; // 게시글 아이디

	private String writer; // 작성자

	private LocalDateTime createDate; // 작성일자

	private String title; // 제목

	private String content; // 내용

	private int hits; // 조회수

	private String fileName; // 변환 파일명

	private String originalFileName; // 오리지널 파일명

	private List<ReplyRes> replyList; // 댓글 리스트

	/**
	 * postEntity 이용해 PostDetailRes 객체 생성
	 * @param postEntity
	 * @return
	 */
	public static PostDetailRes postFromEntity(PostEntity postEntity)
	{
		return PostDetailRes.builder()
				.id(postEntity.getId())
				.writer(postEntity.getWriter())
				.createDate(postEntity.getCreateDate())
				.title(postEntity.getTitle())
				.content(postEntity.getContent())
				.hits(postEntity.getHits())
				.fileName(postEntity.getFileName())
				.originalFileName(postEntity.getOriginalFileName())
				.build();
	}

}
