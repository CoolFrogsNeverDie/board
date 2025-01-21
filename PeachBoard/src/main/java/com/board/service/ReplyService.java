package com.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.board.constants.BoardConstants;
import com.board.dto.ReplyReq;
import com.board.dto.ReplyRes;
import com.board.entity.PostEntity;
import com.board.entity.ReplyEntity;
import com.board.enums.ErrorMessages;
import com.board.repository.PostRepository;
import com.board.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ReplyService.java
 * 
 * 댓글 등록 삭제 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

	private final PostRepository postRepository; // 게시글 repository
	private final ReplyRepository replyRepository; // 게시글 repository

	/**
	 * 댓글 등록 메서드
	 * @param replyReq
	 * @return ReplyRes
	 */
	public ReplyRes saveReply(ReplyReq replyReq) throws Exception{

		ReplyRes replyRes = null;

			log.info("#######################reply add Start#######################");

			// 1. 게시글 조회
			Optional<PostEntity> postEntity = postRepository.findById(replyReq.getPostId());

			// 2. 로그인여부 확인
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if("anonymousUser".equals(principal)) {
				throw new Exception(ErrorMessages.LOGIN_REQUIRED.getMessage());
			}
			UserDetails userDetails = (UserDetails) principal; // 로그인 상태면 userDetails 객체 선언

			log.info("#######################validate#######################");

			// 3. 게시글 존재여부 검증	
			if(!postEntity.isPresent()) {
				throw new Exception(ErrorMessages.POST_NOT_FOUND.getMessage()); // 게시글 존재하지 않음 메세지
			}

			log.info("####################### ReplyEntity Builder #######################");

			// 4. 저장용 댓글 객체 생성
			ReplyEntity newReply = ReplyEntity.builder()
					.createDate(LocalDateTime.now()) // 작성일자
					.content(replyReq.getContent()) // 내용
					.postEntity(postEntity.get()) // 게시글
					.writer(userDetails.getUsername()) // 작성자
					.build();

			// 5. parent 있을 시 여기서 추가
			if(replyReq.getParentId() != null && replyReq.getParentId() != 0L) {
				Optional <ReplyEntity> parent = replyRepository.findById(replyReq.getParentId());
				if(parent.isPresent()) {
					newReply.setParent(parent.get());
				}
			}

			// 6. 저장
			replyRepository.save(newReply);

			// 7. 댓글 리턴 객체 세팅
			replyRes = ReplyRes.builder()
					.parentId(newReply.getParent() != null ? newReply.getParent().getId() : null) // 부모 댓글 아이디
					.id(newReply.getId())
					.writer(newReply.getWriter()) // 작성자
					.content(newReply.getContent()) // 내용
					.build();

			replyRes.setResultMsg(BoardConstants.State.SUCCESS);

		return replyRes;
	}

	/**
	 * replyList 찾기
	 * @param postEntity
	 * @return
	 */
	public List<ReplyRes> findReplyList(PostEntity postEntity) {
		// 1. 댓글 리스트 조회
		List<ReplyEntity> replies = new ArrayList<>();  // replies 변수 선언 위치 변경

		try {
			replies = replyRepository.findByPostEntityOrderByCreateDateDesc(postEntity);
		} catch (Exception e) {
			return null; // 리스트 조회 중 에러 발생 시 null 리턴
		}

		// 2. 변환된 결과를 저장할 새로운 리스트 생성
		List<ReplyRes> replyResList = new ArrayList<>();

		// 3. 변환된 댓글 세팅
		for (ReplyEntity reply : replies) {
			// 3-1. 댓글 객체 dto 변환
			ReplyRes replyRes = ReplyRes.builder()
									.id(reply.getId()) // 댓글 아이디
									.writer(reply.getWriter()) // 작성자
									.content(reply.getContent()) // 내용
									.build(); // entity -> res 변환

			// 3-2. 하위 댓글 조회
			List<ReplyEntity> childList = replyRepository.findByParentOrderByCreateDateAsc(reply);

			// 3-3. 하위 댓글 존재할 시에만 하위 댓글 세팅
			if(!childList.isEmpty()) {
				List<ReplyRes> childRepliesRes = childList.stream()
						.map(child -> ReplyRes.builder()
								.parentId(child.getParent().getId()) // 부모 댓글
								.id(child.getId()) // 댓글 아이디
								.writer(child.getWriter()) // 작성자
								.content(child.getContent()) // 내용
								.build()) // ReplyEntity를 ReplyRes로 변환
						.collect(Collectors.toList());

				replyRes.setChilsReplyList(childRepliesRes); // 하위 댓글 세팅
			}
			replyResList.add(replyRes); // 댓글 리스트 세팅
		}
		return replyResList;
	}

}
