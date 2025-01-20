package com.board.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.MemberLoginRes;
import com.board.dto.PostDetailRes;
import com.board.dto.PostWriteReq;
import com.board.dto.ReplyRes;
import com.board.entity.FileEntity;
import com.board.entity.PostEntity;
import com.board.enums.ErrorMessages;
import com.board.repository.FileRepository;
import com.board.repository.PostRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * PostService.java
 * 
 * 게시글 저장, 삭제 서비스
 */
@Service
@RequiredArgsConstructor
public class PostService {

	private final HttpSession httpSession; // http 세션
	private final PostRepository postRepository; // 게시글 repository
	private final FileRepository fileRepository; // 파일 repository
	private final ReplyService replyService; // 댓글 service
	private final FileService fileService; // 파일 service
	/**
	 * 게시글 저장
	 * @param postWriteReq
	 * @return PostWriteRes
	 */
	@Transactional
	public void savePost(PostWriteReq postWriteReq) throws Exception{

		//1. 로그인여부 확인
		MemberLoginRes member = (MemberLoginRes) httpSession.getAttribute("currentUser");
		if(member == null) {
			throw new Exception(ErrorMessages.LOGIN_REQUIRED.getMessage());
		}
	
		//2. 게시글 입력값 검증
		validatePost(postWriteReq);

		//3. post 객체 생성
		PostEntity newPost = PostEntity.builder()
				.createDate(LocalDateTime.now()) // 작성일자
				.writer(member.getName()) // 작성자
				.title(postWriteReq.getTitle()) // 제목
				.content(postWriteReq.getContent()) // 내용
				.hits(0) // 조회수
				.build();

		//4. 첨부파일 관리
		MultipartFile file = postWriteReq.getFile(); // 첨부파일
		FileEntity newFile = null; // 파일 객체
		String fileName = null; // 변환된 파일명

		if (file != null && !file.isEmpty()) {

			// 4-1. 첨부파일 파일명 변환
			fileName = fileService.createFileName(file.getOriginalFilename()); // 파일명 변환

			// 4-2. 첨부파일 객체 생성
			newFile = FileEntity.builder()
					.fileName(fileName) // 변환 파일명
					.originalFileName(file.getOriginalFilename()) // 파일명
					.uploadDate(LocalDateTime.now()) // 업로드 일자
					.contentType(file.getContentType()) // 컨텐츠 타입
					.post(newPost) // 게시물
					.build();

			// 4-3. 게시글 객체에 첨부파일 정보 세팅
			newPost.setFileName(fileName); // 변환 파일명
			newPost.setOriginalFileName(file.getOriginalFilename()); // 파일명
			newPost.setFile(newFile); // 게시글에 첨부파일 객체 추가
		}

		//5. 게시글 저장
		postRepository.save(newPost);

		if(newFile != null) {
			try {
				//6. 실제 첨부파일 업로드 폴더 저장
				fileService.saveFile(file, fileName); // 파일 업로드 폴더 저장
			} catch (IOException e) {
				throw new Exception(ErrorMessages.FILE_UPLOAD_FAILED.getMessage(), e); // 파일 저장 실패 시 exception
			}
			//7. 첨부파일 저장
			fileRepository.save(newFile);
		}
	}

	/**
	 * 조회수 추가
	 * @param postId
	 */
	public void addHit(Long postId) {

		//Id로 게시글 조회
		Optional<PostEntity> optionalPost =  postRepository.findById(postId);

		//게시물 조회 시 조회수 +1하여 저장
		if(optionalPost.isPresent())
		{
			PostEntity postEntity = optionalPost.get();
			postEntity.setHits(postEntity.getHits() + 1);
			postRepository.save(postEntity);
		}
	}

	/**
	 * pageable 객체로 게시글 조회
	 * 
	 * @param pageable
	 * @return Page<PostDetailRes>
	 */
	public Page<PostDetailRes> findPost(Pageable pageable) {

		// 한 페이지당 3개식 글을 보여주고 정렬 기준은 ID기준으로 내림차순
		Page<PostEntity> postsPages = postRepository.findAllByOrderByCreateDateDesc(pageable);

		// PostEntity -> PostDetailRes 변환
		Page<PostDetailRes> postsResponseDtos = postsPages.map(
			postPage -> PostDetailRes.postFromEntity(postPage));

		return postsResponseDtos;
	}

	/**
	 * 게시물 id로 게시물 상세내용 조회
	 * @param postId
	 * @return PostDetailRes
	 * @throws Exception
	 */
	public PostDetailRes getPostDetailByPostId(Long postId) throws Exception {

		Optional<PostEntity> optionalPost =  postRepository.findById(postId);

		if(optionalPost.isPresent())
		{
			PostDetailRes postDetailRes = PostDetailRes.postFromEntity(optionalPost.get()); // res  객체 변환
			List<ReplyRes> replyList = replyService.findReplyList(optionalPost.get()); // 댓글 리스트 조회
			postDetailRes.setReplyList(replyList); // 댓글 리스트 추가
			return postDetailRes; // PostDetailRes 리턴
		}
		else
		{
			throw new Exception(ErrorMessages.POST_NOT_FOUND.getMessage()); //조회 실패 시 exception 리턴
		}
	}

	/**
	 * 게시판 입력값 검증
	 * @param postWriteReq
	 * @throws Exception
	 */
	private void validatePost(PostWriteReq postWriteReq) throws Exception{

		if(postWriteReq.getTitle() == null || "".equals(postWriteReq.getTitle())) {
			throw new Exception(ErrorMessages.TITLE_IS_NULL.getMessage());
		}

		if(postWriteReq.getContent() == null || "".equals(postWriteReq.getContent())) {
			throw new Exception(ErrorMessages.CONTENT_IS_NULL.getMessage());
		}
	}

}
