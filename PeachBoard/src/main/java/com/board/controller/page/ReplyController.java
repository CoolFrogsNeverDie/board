package com.board.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.ReplyReq;
import com.board.dto.ReplyRes;
import com.board.service.ReplyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * ReplyController.java
 * 
 * 댓글 등록 삭제 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

	private final ReplyService replyService; // 댓글 서비스

	/**
	 * 댓글 등록
	 * @param replyReq
	 * @param bindingRst
	 * @return ReplyRes 요청 반환값
	 */
	@ResponseBody
	@RequestMapping(value ="/new", method =RequestMethod.POST)
	public ReplyRes addReply(
			@Valid @RequestBody  ReplyReq replyReq
			, BindingResult bindingRst) {
		// ReplyReq dto 입력값 검증 -> 실패 시 errorMsg 추가 후 리턴
		if(bindingRst.hasErrors()) {
			String errorMsg = bindingRst.getAllErrors().get(0).getDefaultMessage(); // dto 검증 실패 errorMsg
			new ReplyRes().setResultMsg(errorMsg);
		}

		ReplyRes replyRes = replyService.addReply(replyReq); //댓글 등록

		return replyRes;
	}

}
