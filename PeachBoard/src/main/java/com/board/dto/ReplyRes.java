package com.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.board.entity.ReplyEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ReplyRes.java
 * 
 * 댓글 응답 객체
 */
@Getter
@Setter
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 추가
public class ReplyRes {

	private Long parentId; // 부모 댓글 아이디

	private Long id;

	private String writer; // 작성자

	private String content; // 내용

	private LocalDateTime createTime; // 작성일

	private String resultMsg; // 결과 메세지

	private List<ReplyRes> chilsReplyList; // 자식 댓글 리스트

	/**
	 * replyEntity 이용해 ReplyRes 생성
	 * @param replyEntity
	 * @return
	 */
	public static ReplyRes replyFromEntity(ReplyEntity replyEntity) {
		return ReplyRes.builder()
				.parentId(replyEntity.getParent() != null ? replyEntity.getParent().getId() : null)
				.id(replyEntity.getId())
				.writer(replyEntity.getWriter())
				.createTime(replyEntity.getCreateDate())
				.content(replyEntity.getContent())
				.build();
	}
}
