package com.board.enums;

/**
 * ErrorMessages.enum
 * 
 * 에러메세지 관리 enum
 */
public enum ErrorMessages {

	LOGIN_FAIL("사용자가 유효하지 않습니다."), // 로그인 실패 오류
	LOGIN_REQUIRED("로그인 후 사용 가능합니다."), // 로그인 x 권한 오류
	POST_NOT_FOUND("게시글을 찾을 수 없습니다."), // 게시글 검색 불가 오류
	PAGE_NOT_FOUND("페이지를 찾을 수 없습니다."), // 페이지 오류
	TITLE_IS_NULL("제목은 비어있을 수 없습니다."), // 제목값 검증 오류
	CONTENT_IS_NULL("내용은 비어있을 수 없습니다."), // 내용값 검증 오류
	NO_CONSTRUCTOR("생성자 사용 불가"), // 생성자 사용 금지 오류
	REPLY_ADD_FAILED("댓글 등록에 실패했습니다."), // 생성자 사용 금지 오류
	FILE_SEARCH_FAILED("파일 조회에 실패했습니다."), // 생성자 사용 금지 오류
	FILE_DOWNLOAD_FAILED("파일 다운로드에 실패했습니다."), // 생성자 사용 금지 오류
	FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다."); // 파일 업로드 오류

	private final String message;

	// 생성자
	ErrorMessages(String message) {
		this.message = message;
	}

	// 메시지를 가져오는 메서드
	public String getMessage() {
		return message;
	}

}
