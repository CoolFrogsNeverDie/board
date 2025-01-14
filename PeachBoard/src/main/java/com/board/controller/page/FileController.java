package com.board.controller.page;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.entity.FileEntity;
import com.board.enums.ErrorMessages;
import com.board.repository.FileRepository;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

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

	private final String FILE_DOWNLOAD_PATE = "C:/dev/upload/"; // 기본 업로드 경로
	private final FileRepository fileRepository; // 파일 repository
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	/**
	 * 파일 다운로드
	 * @param fileName
	 * @return 파일
	 */
	@GetMapping("/download/{fileName}")
	public ResponseEntity<byte[]> downloadFile(
			@PathVariable("fileName") String fileName)
	{
		FileEntity fileEntity = fileRepository.findFileByfileName(fileName).orElse(null); // 파일이름으로 파일 조회 
		File file = new File(FILE_DOWNLOAD_PATE + fileName); // 파일 객체 생성
		HttpHeaders header = new HttpHeaders(); // http 헤더 설정용
		ResponseEntity<byte[]> result = null;

		try
		{
			if(fileEntity == null)
			{
				throw new Exception(ErrorMessages.FILE_SEARCH_FAILED.getMessage());
			}
			// 컨텐츠 타입 설정
			header.add("Content-Type", fileEntity.getContentType());
			// 컨텐츠 표시 방법 헤더 설정
			header.add(HttpHeaders.CONTENT_DISPOSITION,
					"attachment;filename=\"" + URLEncoder.encode(fileEntity.getOriginalFileName(), "UTF-8") + "\"");
			// 파일 바이트 배열로 생성
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		}
		catch (Exception ex)
		{
			LOG.error("==================================================");
			LOG.error("downloadFile error : {}{}", ex.getMessage(), ex);
			LOG.error("==================================================");
		}
		return result;
	}
}
