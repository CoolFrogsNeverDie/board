
$(document).ready(function() {
	$('#fileInput').change(function(e) {
		var file = e.target.files[0];

		// 파일이 선택되지 않은 경우
		if (!file) return;

		// 파일 크기 10MB 체크
		var maxFileSize = 10 * 1024 * 1024;

		if (file.size > maxFileSize) {

			$('#fileError').text('파일 크기는 10MB를 넘을 수 없습니다.');

			// 선택한 파일 초기화 (파일 삭제)
			$('#fileInput').val('');
		}
	});
});