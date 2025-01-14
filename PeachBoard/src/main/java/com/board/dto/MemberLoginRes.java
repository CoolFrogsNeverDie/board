package com.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * MemberLoginRes.java
 * 
 * session 로그인 정보 dto 
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class MemberLoginRes {

	private String userName; // 사용자 아이디

	private String name; // 사용자 이름

}
