package com.board.config.constants;

import com.board.enums.ErrorMessages;

/**
 * BoardContants.java 
 * 
 * 상수갑 관리 클래스
 */
public class BoardContants {
	
	// 생성자 사용방지
	private BoardContants()
	{
		throw new UnsupportedOperationException(ErrorMessages.NO_CONSTRUCTOR.getMessage());
	}

	// 요청결과
	public static class State
	{
		public static final String SUCCESS = "SUCCESS";
		public static final String FAIL = "FAIL";
	}

	// content 파일 기본 경로
	public static class ContentBaseRoot
	{
		public static final String CONTENTROOT = "content/"; 
	}
}
