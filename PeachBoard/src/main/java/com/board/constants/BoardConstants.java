package com.board.constants;

import com.board.enums.ErrorMessages;

/**
 * BoardContants.java 
 * 
 * 상수갑 관리 클래스
 * 
 * 피드백 : Constants 를 사용할 시 class 단위로 배포할 때 장애가 날 수 있음(전체 배포 시 상관 x)
 */
public class BoardConstants {
	
	// 생성자 사용방지
	private BoardConstants() {
		throw new UnsupportedOperationException(ErrorMessages.NO_CONSTRUCTOR.getMessage());
	}

	// 요청결과
	public static class State {
		public static final String SUCCESS = "SUCCESS";
		public static final String FAIL = "FAIL";
	}

	// content 파일 기본 경로
	public static class ContentBaseRoot {
		public static final String CONTENT_ROOT = "content/"; 
	}
}
