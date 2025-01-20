package com.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * PostWriteRes.java
 * 
 * 게시글 작성 응답 dto
 */
@Getter
@Setter
@ToString
public class PostWriteRes {
	private Long id; // 게시글 아이디
	private String resultMsg; // 결과 메세지
}
