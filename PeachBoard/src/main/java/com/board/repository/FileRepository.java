package com.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.board.entity.FileEntity;

/**
 * FileRepository
 * 
 * 파일 레파지토리
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long>{

	/**
	 * 파일명으로 파일 찾기
	 * @param fileName
	 * @return Optional<FileEntity>
	 */
	Optional<FileEntity> findFileByfileName(String fileName);
}
