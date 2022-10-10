package com.polaroid.app.command;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostListDto {
	private Integer post_id; //pk
	
	@NotBlank(message = "게시글 제목은 필수 입니다")
	private String post_title;
	@NotBlank(message = "게시글 내용은 필수 입니다")
	private String post_content;
	private LocalDateTime post_regdate; //자동으로 입력되는 날짜
	private Integer member_id;
	private Integer hashtag_id;
	
	private Integer upload_id; //pk
	private String upload_filename;
	private String upload_filepath;
	private String upload_fileuuid;
	
	private Integer postlike_id;
}