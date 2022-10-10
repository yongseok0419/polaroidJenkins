package com.polaroid.app.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
	

	private Integer post_id; //PK
	private String member_nick;
	private LocalDateTime post_regdate; // 날짜
	
	@NotBlank(message = "제목은 필수 입니다")
	private String post_title; //제목
	@NotBlank(message = "내용은 필수 입니다")
	private String post_content; //내용
	
	private Integer member_id; //회원 아이디
	private Integer hashtag_id; //해시태그 아이디
	private Integer upload_id;//업로드 아이디
	private List<UploadDto> uploads = new ArrayList < > ();
	
	private int postview;	//게시글 방문자 수

}
