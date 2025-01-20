package com.board.controller.page;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * FileController.java 
 * 
 * 게시글 파일 관리 controller
 */
@Slf4j
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

		ResponseEntity<byte[]> result = null;

		try {
			result =  fileService.downloadFile(fileName);
		}catch (Exception e) {
			log.error("downloadFile is error : {}",e.getMessage(),e );
			
		}

		return result;
	}

}
