package com.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * ReplyReq.java
 * 
 * 댓글 요청 객체
 */
@Getter
@Setter
public class ReplyReq {

	@NotBlank(message = "게시글이 존재하지 않습니다.")
	private String postId; // 게시글 아이디

	private String parentId; // 부모 댓글 아이디

	@NotBlank(message = "댓글 내용이 없을 수 없습니다.")
	@Size(min=1,max=100, message = "내용은 최소 {min}자 이상, 최대 {max}자 이하여야 합니다.")
	private String content; // 내용
}
