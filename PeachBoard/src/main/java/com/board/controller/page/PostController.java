package com.board.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.board.config.constants.BoardContants;
import com.board.dto.PostDetailRes;
import com.board.dto.PostWriteReq;
import com.board.dto.PostWriteRes;
import com.board.service.PostService;
import com.board.util.PageContentUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * PostPageController.java 
 * 
 * 게시글 controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/post", method = {RequestMethod.GET, RequestMethod.POST})
public class PostController {
	
	private final PostService postService; // 게시글 서비스

	/**
	 * 게시글 작성 페이지
	 * @param session
	 * @param model
	 * @return 게시글 작성 페이지
	 */
	@RequestMapping(value = "/form", method =  RequestMethod.GET)
	public String postForm(
			HttpSession session
			,Model model)
	{
		return PageContentUtil.getViewPage(model,"postForm"); // 게시글 작성 폼
	}

	/**
	 * 새로운 게시글 등록
	 * @param postWriteReq
	 * @param bindingRst
	 * @param redirectAttr
	 * @param model
	 * @return 페이지 분기
	 */
	@RequestMapping(value = "/new", method =  RequestMethod.POST)
	public String newPost(
			@Valid @ModelAttribute PostWriteReq postWriteReq // 사용자 입력 객체
			,BindingResult bindingRst
			,RedirectAttributes redirectAttr
			,Model model)
	{

		// PostWriteReq dto 입력값 검증 -> 실패 시 errorMsg + 등록 폼 리턴
		if(bindingRst.hasErrors()) 
		{
			String errorMsg = bindingRst.getAllErrors().get(0).getDefaultMessage(); // dto 검증 실패 errorMsg
			redirectAttr.addFlashAttribute("errorMsg", errorMsg);
			return "redirect:/post/form";
		}

		PostWriteRes postWriteRes = postService.savePost(postWriteReq); // 게시글 저장

		if(postWriteRes.getResultMsg().equals(BoardContants.State.SUCCESS)) // 저장 성공
		{
			return "redirect:/board";
		}
		else 
		{
			redirectAttr.addFlashAttribute("errorMsg", postWriteRes.getResultMsg()); // 오류 메세지
			return "redirect:/post/form";
		}
	}

	/**
	 * 게시글 상세 보기
	 * @param postId
	 * @param model
	 * @return 게시글 상세 페이지
	 */
	@RequestMapping(value = "/{postId}" , method = RequestMethod.GET)
	public String postDetailPage(
			@PathVariable("postId") String postId // 게시글 아이디
			, Model model) 
	{
		try 
		{
			PostDetailRes post = postService.getPostDetailByPostId(postId); //게시글 아이디로 게시글 정보 가져오기
			if(post != null) 
			{
				model.addAttribute("post", post); //게시글 내용
			}
			postService.addHit(postId); //조회수 추가
		}
		catch (Exception e)
		{
			model.addAttribute("errorMsg", e.getMessage()); //게시글 없을 시 errorMsg 리턴
		}

		return PageContentUtil.getViewPage(model,"postDetail");
	}

}
