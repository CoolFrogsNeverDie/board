package com.board.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * PostWriteReq.java
 * 
 * 게시글 작성 요청 dto
 */
@Getter
@Setter
@ToString
public class PostWriteReq {
	
	@NotBlank(message = "제목은 필수 입력 사항입니다.")
	@Size(min=5,max=30, message = "내용은 최소 {min}자 이상, 최대 {max}자 이하여야 합니다.")
	private String title; // 제목

	@NotBlank(message = "제목은 필수 입력 사항입니다.")
	@Size(min=5,max=2000, message = "내용은 최소 {min}자 이상, 최대 {max}자 이하여야 합니다.")
	private String content; // 내용

	private MultipartFile file; // 첨부파일
}
