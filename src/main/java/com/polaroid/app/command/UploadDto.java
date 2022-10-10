package com.polaroid.app.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadDto {
	private Integer upload_id; //pk
	private String upload_filename;
	private String upload_filepath;
	private String upload_fileuuid;
	private String upload_type;
	private Integer post_id; //fk;
}
