package com.board.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;

import lombok.Getter;


/**
 * Pagination.java 
 * 
 * session 로그인 정보 dto 
 */
@Getter
public class Pagination {

	private int currentPage; //현재 페이지
	private int totalPages; //총 페이지수
	private List<Integer> pageNumbers; // 페이지 번호 리스트
	private int prePage; // 이전 페이지

	//생성자
	public Pagination(Page<Object> obj)
	{
		if(obj != null)
		{
			this.currentPage = obj.getNumber() + 1;
			this.totalPages = obj.getTotalPages();
			this.pageNumbers = getPageNumbers(obj.getTotalPages());
			this.prePage = this.currentPage - 2;
		}
	}

	/**
	 * 페이지 번호 리스트
	 * @param totalPages
	 * @return 페이지 번호 리스트
	 */
	private List<Integer> getPageNumbers(int totalPages) {
		return IntStream.rangeClosed(1, totalPages)
			.boxed()
			.collect(Collectors.toList());
	}
}