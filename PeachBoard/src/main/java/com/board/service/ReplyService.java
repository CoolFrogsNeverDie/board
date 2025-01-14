package com.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.board.config.constants.BoardContants;
import com.board.dto.MemberLoginRes;
import com.board.dto.ReplyReq;
import com.board.dto.ReplyRes;
import com.board.entity.PostEntity;
import com.board.entity.ReplyEntity;
import com.board.enums.ErrorMessages;
import com.board.repository.PostRepository;
import com.board.repository.ReplyRepository;

import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * ReplyService.java
 * 
 * 댓글 등록 삭제 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

	private final HttpSession httpSession; // http 세션
	private final PostRepository postRepository; // 게시글 repository
	private final ReplyRepository replyRepository; // 게시글 repository
	private final Logger LOG = LoggerFactory.getLogger(getClass()); //로그

	/**
	 * 댓글 등록 메서드
	 * @param replyReq
	 * @return ReplyRes
	 */
	public ReplyRes addReply(ReplyReq replyReq) {

		ReplyRes replyRes = null;

		try
		{
			LOG.info("#######################reply add Start#######################");

			// 1. 게시글 조회
			Optional<PostEntity> postEntity = postRepository.findPostById(Integer.valueOf(replyReq.getPostId()));

			// 2. 로그인여부 확인
			MemberLoginRes member = (MemberLoginRes) httpSession.getAttribute("currentUser");
			if(member == null)
			{
				throw new Exception(ErrorMessages.LOGIN_REQUIRED.getMessage());
			}

			LOG.info("#######################validate#######################");

			// 3. 게시글 존재여부 검증	
			if(!postEntity.isPresent())
			{
				throw new Exception(ErrorMessages.POST_NOT_FOUND.getMessage()); // 게시글 존재하지 않음 메세지
			}

			LOG.info("####################### ReplyEntity Builder #######################");

			// 4. 저장용 댓글 객체 생성
			ReplyEntity newReply = ReplyEntity.builder()
					.creaDate(LocalDateTime.now()) // 작성일자
					.content(replyReq.getContent()) // 내용
					.postEntity(postEntity.get()) // 게시글
					.writer(member.getUserName())
					.build();

			// 5. parent 있을 시 여기서 추가
			if(replyReq.getParentId() != null && !replyReq.getParentId().isEmpty())
			{	
				Optional <ReplyEntity> parent = replyRepository.findById(Integer.parseInt(replyReq.getParentId()));
				if(parent.isPresent()) {
					newReply.setParent(parent.get());
				}
			}

			// 6. 저장
			replyRepository.save(newReply);

			// 7. 댓글 리턴 객체 세팅
			replyRes = ReplyRes.replyFromEntity(newReply);
			replyRes.setResultMsg(BoardContants.State.SUCCESS);

		}
		catch (Exception e)
		{
			replyRes = new ReplyRes();
			replyRes.setResultMsg(ErrorMessages.FILE_SEARCH_FAILED.getMessage());
			LOG.error("addReply error : {}", e);
		}
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
		try 
		{
			replies = replyRepository.findByPostEntityOrderByCreateDateDesc(postEntity);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// 2. 변환된 결과를 저장할 새로운 리스트 생성
		List<ReplyRes> replyResList = new ArrayList<>();

		// 3. for 루프를 사용하여 변환 + 하위 댓글 리스트 추가
		for (ReplyEntity reply : replies) 
		{
			ReplyRes replyRes = ReplyRes.replyFromEntity(reply); // entity -> res 변환

			// 하위 댓글 세팅
			List<ReplyEntity> childList = replyRepository.findChildRepliesByParentOrderByCreateDateDesc(reply);
			List<ReplyRes> childRepliesRes = childList.stream()
					.map(child -> ReplyRes.replyFromEntity(child)) // ReplyEntity를 ReplyRes로 변환
					.collect(Collectors.toList());

			replyRes.setChilsReplyList(childRepliesRes); // 자식 댓글 검색
			replyResList.add(replyRes); // 자식 댓글 세팅
		}
		return replyResList;
	}

}
