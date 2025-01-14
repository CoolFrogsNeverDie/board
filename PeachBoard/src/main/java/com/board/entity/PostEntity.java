package com.board.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 엔티티 클래스
 * 
 * 게시글 데이터 관리 엔티티
 */
@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class PostEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "post_number")
	private Integer id; // id

	@JoinColumn(name = "writer")
	private String writer; // 작성자
	
	@Column(name = "create_date")
	private LocalDateTime createDate; // 작성일

	@Column(name = "title")
	private String title; // 제목

	@Column(name = "content" , length = 2000)
	private String content; // 내용

	@Column(name = "file_name")
	private String fileName; // 변환된 파일명

	@Column(name = "original_file_name")
	private String originalFileName; // 파일명

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_id")
	private FileEntity file; // 파일

	@OneToMany(mappedBy = "postEntity", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@OrderBy("id asc") // 댓글 정렬    
	private List<ReplyEntity> replys;

	@Column(name = "hits")
	private int hits; // 조회수

	@Column(name = "delete_yn")
	private char deleteYn; // 삭제여부

	// 생성자
	@Builder
	public PostEntity(String writer, LocalDateTime createDate, String title, String content, int hits, String fileName, String originalFileName) {
		this.writer = writer;
		this.createDate = createDate;
		this.title = title;
		this.content = content;
		this.hits = hits;
		this.fileName = fileName;
		this.originalFileName = originalFileName;
	}
}
