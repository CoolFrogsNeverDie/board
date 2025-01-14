package com.board.controller.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.board.dto.PostDetailRes;
import com.board.service.PostService;
import com.board.util.PageContentUtil;
import com.board.util.Pagination;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * BoardPageController.java 
 * 
 * 게시판 페이지 관리 controller
 * 게시판 조회 페이징 결과 반환 
 */
@Controller
@RequiredArgsConstructor
public class BoardController {

	private final PostService postService; // 게시글 서비스

	/**
	 * 
	 * @param pageable 페이징 정보
	 * @param session http 세션 객체
	 * @param model
	 * @return 게시판
	 */
	@RequestMapping(value = "/board", method =  RequestMethod.GET)
	public String boardPage(
			@PageableDefault(size=5, page=0) Pageable pageable
			, HttpSession session
			, Model model)
	{

		Page<PostDetailRes> posts = postService.findPost(pageable); // pageable 객체로 게시글 리스트 조회
		Pagination pagination = new Pagination(posts.map(PostDetailRes -> (Object)PostDetailRes )); // 페이징 정보

		model.addAttribute("posts", posts);
		model.addAttribute("paging", pagination);

		return PageContentUtil.getViewPage(model,"board");
	}
}
