package com.board.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 엔티티 클래스
 * 
 * 게시글 첨부파일 관리 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "file")
public class FileEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "member_number")
	private Integer id; // id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private PostEntity post; // 게시글

	@Column(name = "original_file_name")
	private String originalFileName; // 파일이름

	@Column(name = "file_name")
	private String fileName; // 변환 파일이름

	@Column(name = "content_type")
	private String contentType; // 컨텐츠 타입

	@Column(name = "upload_date")
	private LocalDateTime uploadDate; // 게시일자

	// 셍성자
	@Builder
	public FileEntity(String fileName, String originalFileName ,String contentType, LocalDateTime uploadDate, PostEntity post) {
		this.fileName = fileName;
		this.originalFileName = originalFileName;
		this.contentType = contentType;
		this.uploadDate = uploadDate;
		this.post = post;
	}
}
