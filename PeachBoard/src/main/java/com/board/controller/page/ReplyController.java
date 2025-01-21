package com.board.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.ReplyReq;
import com.board.dto.ReplyRes;
import com.board.enums.ErrorMessages;
import com.board.service.ReplyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ReplyController.java
 * 
 * 댓글 등록 삭제 컨트롤러
 */
@Slf4j
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
	@PostMapping("/new")
	public ReplyRes addReply(
			@Valid @RequestBody  ReplyReq replyReq
			, BindingResult bindingRst) {

		ReplyRes replyRes = null;

		// ReplyReq dto 입력값 검증 -> 실패 시 errorMsg 추가 후 리턴
		if(bindingRst.hasErrors()) {
			String errorMsg = bindingRst.getAllErrors().get(0).getDefaultMessage(); // dto 검증 실패 errorMsg
			replyRes = new ReplyRes();
			replyRes.setResultMsg(errorMsg);
			log.error("addReply - replyReq validation : {}", errorMsg);
			return replyRes;
		}

		try {
			replyRes = replyService.saveReply(replyReq); //댓글 등록
		}catch (Exception e) {
			replyRes = new ReplyRes();
			replyRes.setResultMsg(ErrorMessages.REPLY_ADD_FAILED.getMessage()); // 댓글 등록 실패 메세지 조회
			log.error("addReply error : {}", e.getMessage(), e);
		}

		return replyRes;
	}
}
