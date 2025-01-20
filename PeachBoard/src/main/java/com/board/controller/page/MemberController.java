package com.board.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.enums.ErrorMessages;
import com.board.util.PageContentUtil;

import lombok.RequiredArgsConstructor;

/**
 * MemberPageController.java 
 * 
 * 로그인페이지 controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	/**
	 * 로그인 페이지
	 * @param isFail
	 * @param model
	 * @return 로그인 페이지
	 * 
	 * F.loginpage 는 이상하다
	 * -> /login, /signup 등이 적절.. 최소 login-page 정도가 적절하다.
	 */
	@GetMapping("/login")
	public String loginPage(
			@RequestParam(value = "isFail"
			, required = false
			, defaultValue = "false") String isFail
			, Model model) {

		// 로그인 실패 redirect 시 errorMsg 추가
		if("true".equals(isFail)) {
			model.addAttribute("failMsg",ErrorMessages.LOGIN_FAIL.getMessage());
		}

		return PageContentUtil.getViewPage(model,"login");
	}
} 
