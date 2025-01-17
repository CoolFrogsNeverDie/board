package com.board.util;

import org.springframework.ui.Model;

import com.board.constants.BoardConstants;

/**
 * PageContentUtil.java
 * 
 * 페이지 컨텐츠 메서드 모음
 */
public class PageContentUtil {

	/**
	 * 화면단 html 위치 조회
	 * @param model
	 * @param content
	 * @return
	 */
	public static String getViewPage(Model model, String content) {
		model.addAttribute("pageTitle", content); // 페이지명
		return BoardConstants.ContentBaseRoot.CONTENT_ROOT + content; // html 경로
	}
}
