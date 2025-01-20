package com.board.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.board.entity.FileEntity;
import com.board.enums.ErrorMessages;
import com.board.repository.FileRepository;

import lombok.RequiredArgsConstructor;

/**
 * FileService.java
 * 
 * 파일 관리 service 파일 분리
 */
@Service
@RequiredArgsConstructor
public class FileService {

	@Value("${board.file.auload.path}")
	private String FILE_DOWNLOAD_PATH;
	private final FileRepository fileRepository; // 파일 repository

	public ResponseEntity<byte[]> downloadFile(String fileName) throws Exception{

		FileEntity fileEntity = fileRepository.findFileByfileName(fileName).orElse(null); // 파일이름으로 파일 조회 
		File file = new File(FILE_DOWNLOAD_PATH + fileName); // 파일 객체 생성
		HttpHeaders header = new HttpHeaders(); // http 헤더 설정용
		ResponseEntity<byte[]> result = null;

		if(fileEntity == null) {
			throw new Exception(ErrorMessages.FILE_SEARCH_FAILED.getMessage());
		}
		// 컨텐츠 타입 설정
		header.add("Content-Type", fileEntity.getContentType());
		// 컨텐츠 표시 방법 헤더 설정
		header.add(HttpHeaders.CONTENT_DISPOSITION,
				"attachment;filename=\"" + URLEncoder.encode(fileEntity.getOriginalFileName(), "UTF-8") + "\"");
		// 파일 바이트 배열로 생성
		result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

		return result;
	}
	
	/**
	 * 파일 저장
	 * @param file
	 * @return fileName
	 * @throws IOException
	 */
	public void saveFile(MultipartFile file, String fileName) throws IOException {

		String filePath = FILE_DOWNLOAD_PATH + fileName; // 파일 저장 경로

		File dest = new File(filePath); // 지정 파일 객체

		//디렉토리 없을 시 생성
		if(!dest.exists())
		{
			dest.mkdirs();
		}

		file.transferTo(dest); // 파일 지정 경로 저장
	}

	/**
	 * 파일명 생성
	 * @param fileName
	 * @return
	 */
	public String createFileName(String fileName) {
		return UUID.randomUUID().toString() + "_" + fileName;
	}
}
