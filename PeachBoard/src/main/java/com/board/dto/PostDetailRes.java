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
@Builder
@Getter
@Setter
public class PostDetailRes {

	private Long id; // 게시글 아이디

	private String writer; // 작성자

	private LocalDateTime createDate; // 작성일자

	private String title; // 제목

	private String content; // 내용

	private int hits; // 조회수

	private String fileName; // 변환 파일명

	private String originalFileName; // 오리지널 파일명

	private List<ReplyRes> replyList; // 댓글 리스트
}
