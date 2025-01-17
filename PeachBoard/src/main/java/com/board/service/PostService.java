package com.board.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.board.constants.BoardConstants;
import com.board.dto.MemberLoginRes;
import com.board.dto.PostDetailRes;
import com.board.dto.PostWriteReq;
import com.board.dto.PostWriteRes;
import com.board.dto.ReplyRes;
import com.board.entity.FileEntity;
import com.board.entity.PostEntity;
import com.board.enums.ErrorMessages;
import com.board.repository.FileRepository;
import com.board.repository.PostRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transaction;
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

	/**
	 * 게시글 저장
	 * @param postWriteReq
	 * @return PostWriteRes
	 */
	@Transactional
	public PostWriteRes savePost(PostWriteReq postWriteReq) {

		PostWriteRes postWriteRes = new PostWriteRes(); // 게시글 작성 결과값

		try {

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

			//4. 첨부파일 업로드
			MultipartFile file = postWriteReq.getFile();
			String fileName = null;
			if (file != null && !file.isEmpty()) {
				try {
					fileName = saveFile(file); // 파일 저장 후 파이명 반환
				} catch (IOException e) {
					throw new Exception(ErrorMessages.FILE_UPLOAD_FAILED.getMessage(), e); // 파일 저장 오류 메세지
				}
			}

			//5. 파일 저장
			FileEntity newFile = FileEntity.builder()
									.fileName(fileName) // 변환 파일명
									.originalFileName(file.getOriginalFilename()) // 파일명
									.uploadDate(LocalDateTime.now()) // 업로드 일자
									.contentType(file.getContentType()) // 컨텐츠 타입
									.post(newPost) // 게시물
									.build();

			//6. 게시글 객체에 파일 정보 추가
			newPost.setFileName(fileName); // 변환 파일명
			newPost.setOriginalFileName(file.getOriginalFilename()); // 파일명
			newPost.setFile(newFile); // 파일 객체

			//7. DB 저장
			postRepository.save(newPost);
			fileRepository.save(newFile);
	
			//8. 리턴값 세팅
			postWriteRes.setId(newPost.getId()); // 게시글 아이디
			postWriteRes.setResultMsg(BoardConstants.State.SUCCESS); // 성공 메세지
	
		} catch(Exception e) {
			postWriteRes.setResultMsg(e.getMessage()); // 실패 메세지
		}

		return postWriteRes;
	}

	/**
	 * 조회수 추가
	 * @param postId
	 */
	public void addHit(String postId) {

		//Id로 게시글 조회
		Optional<PostEntity> optionalPost =  postRepository.findPostById(Integer.valueOf(postId));

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
	public PostDetailRes getPostDetailByPostId(String postId) throws Exception {

		Optional<PostEntity> optionalPost =  postRepository.findPostById(Integer.valueOf(postId));

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

		if(postWriteReq.getTitle() == null || postWriteReq.getTitle().equals("")) {
			throw new Exception(ErrorMessages.TITLE_IS_NULL.getMessage());
		}

		if(postWriteReq.getContent() == null || postWriteReq.getContent().equals("")) {
			throw new Exception(ErrorMessages.CONTENT_IS_NULL.getMessage());
		}
	}

	/**
	 * 파일 저장
	 * @param file
	 * @return fileName
	 * @throws IOException
	 */
	private String saveFile(MultipartFile file) throws IOException {

		String uploadDir = "C:/dev/upload/"; // 업로드 경로
		String fileName = createFileName(file.getOriginalFilename()); // 새로운 파일명 생성
		String filePath = uploadDir + fileName; // 파일 저장 경로

		File dest = new File(filePath); // 지정 파일 객체

		//디렉토리 없을 시 생성
		if(!dest.exists())
		{
			dest.mkdirs();
		}

		file.transferTo(dest); // 파일 지정 경로 저장

		return fileName;
	}

	/**
	 * 파일명 생성
	 * @param fileName
	 * @return
	 */
	private String createFileName(String fileName) {
		return UUID.randomUUID().toString() + "_" + fileName;
	}
}
