$(document).ready(function() {
	reply.init();
});

//댓글 이벤트용 객체
let reply = {
	init: function() {
		with(reply){
			// 댓글 등록 이벤트
			$('#addReply_btn').click(function(e) {
				var content = $('#reply_content').val();
				var postId = $('#post_id').text();

				//입력값 검증
				if(!validate(content, postId))
				{
					return false;
				}

				var data = {
					content: content,
					postId: postId
				};
				addReply(data);
			});

			//대댓글 인풋창 보기 버튼 이벤트
			$(document).on('click', '[id^="sub_reply_"]', function(e) {
				var replyId = e.target.id.split('_')[2];  // sub_reply_{id} 형태에서 id 추출
				$('#comment_' + replyId).append(reply.createRecomment(replyId));
			});

			//대댓글 등록 버튼 이벤트
			$(document).on('click', '[id^="sub_addReply_btn_"]', function(e) {
				var id = e.target.id;
				var parentId = id.slice(id.lastIndexOf('_') + 1) // sub_reply_{id} 형태에서 Parentid 추출
				var postId = $('#post_id').text();
				var content = $('#sub_reply_content_' + parentId).val();

				var data = {
					content: content,
					postId: postId,
					parentId : parentId
				};
				addReply(data);
			});	
		}
	},
	validate: function(content, postId){
		if (!content || content.trim() === "") {
			alert('댓글 내용을 입력해주세요.');
			return false;
		}
		if (!postId) {
			alert('게시글 ID가 존재하지 않습니다.');
			return false;
		}
		return true; // 모든 검사를 통과하면 true 반환
	},
	addReply: function(data){
		var url = '/reply/new'; // 요청을 보낼 URL

		$.ajax({
			url: url,
			type: 'POST',
			contentType: 'application/json', // JSON 형식으로 보내기
			data: JSON.stringify(data), // 데이터를 JSON 형식으로 변환하여 보냄
			success: function(result) {

				if(result == null){
					return false;
				}

				if(result.resultMsg === "SUCCESS"){
					//1. parent 여부 확인
					//2.  댓글 그리기 분기
					if(result.parentId != null){
						$('#comment_' + result.parentId).append(reply.createsubComment(result));
						$('#child-reply-' + result.parentId).remove();
					}else{
						$('#comments-list').append(reply.createComment(result));
						$('#reply_content').val('');
					}
				}else{
					alert(result.resultMsg);
				}
			},
			error: function(xhr, status, error) {
				console.error('오류:', error);
			}
		});
	},
	createComment: function(result){
		var commentHtml = '';
		commentHtml += '<div class="comment" id="comment_' + result.id + '">';
		commentHtml += '	<p class="comment-author"> 작성자: '+ result.writer + '</p>';
		commentHtml += '	<p class="comment-content">' + result.content + '</p>';
		commentHtml += '    <button class="btn-dark" id="sub_reply_' + result.id + '">+ Re</button>';
		commentHtml += '</div>';

		return commentHtml;
	},
	createRecomment: function(parentId){
		// 대댓글을 담을 영역 추가
		var commentHtml = '';

		commentHtml += '<div class="comment-form child-replies" id="child-reply-' + parentId + '">';
		commentHtml += '  <textarea id="sub_reply_content_' + parentId + '" placeholder="댓글을 작성해주세요..." rows="4"></textarea>';
		commentHtml += '  <button class="btn btn-dark" id="sub_addReply_btn_' + parentId + '">댓글 작성</button>';
		commentHtml += '</div>';

		return commentHtml;
	},
	createsubComment: function(result){
		var commentHtml = '';
		commentHtml += '<div class="child-reply">';
		commentHtml += '	<p class="comment-author">작성자: ';
		commentHtml += '		<span>' + result.writer + '</span>';
		commentHtml += '	</p>';
		commentHtml += '	<p class="comment-content">' + result.content + '</p>';
		commentHtml += '</div>';

		return commentHtml;
	}
}