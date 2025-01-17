package com.board.controller.page;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.service.FileService;

import lombok.RequiredArgsConstructor;

/**
 * FileController.java 
 * 
 * 게시글 파일 관리 controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

	private final FileService fileService;

	/**
	 * 파일 다운로드
	 * @param fileName
	 * @return 파일
	 */
	@GetMapping("/download/{fileName}")
	public ResponseEntity<byte[]> downloadFile(
			@PathVariable("fileName") String fileName) {

		ResponseEntity<byte[]> result =  fileService.downloadFile(fileName);

		return result;
	}

}
