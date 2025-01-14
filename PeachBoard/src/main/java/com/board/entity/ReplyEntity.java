package com.board.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 엔티티 클래스
 * 
 * 댓글 데이터 관리 엔티티
 */
@Entity
@Getter
@Setter
@Table(name="reply")
@NoArgsConstructor
public class ReplyEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "reply_number")
	private Integer id; // id
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "post_id")
	private PostEntity postEntity; // 게시글

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private ReplyEntity parent; // 부모 댓글

	@JoinColumn(name = "writer")
	private String writer; // 작성자

	@Column(name = "content")
	private String content; // 내용

	@Column(name = "create_date")
	private LocalDateTime createDate; // 작성일

	@Column(name = "modifiedDate_date")
	private LocalDateTime modifiedDate; // 작성일

	@Column(name = "delete_yn")
	private char deleteYn; // 삭제 여부

	@OneToMany(mappedBy = "parent")
	private List<ReplyEntity> childReplies; // 자식 댓글 리스트 추가

	@Builder
	public ReplyEntity(PostEntity postEntity,ReplyEntity parentId, String writer, LocalDateTime creaDate, LocalDateTime modifiedDate, String content, ReplyEntity parent){
		this.postEntity = postEntity;
		this.writer = writer;
		this.createDate = creaDate;
		this.modifiedDate = modifiedDate;
		this.content = content;
		this.parent = parent;
	}
}
