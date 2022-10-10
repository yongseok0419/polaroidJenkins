package com.polaroid.app.upload;

import org.apache.ibatis.annotations.Mapper;

import com.polaroid.app.command.UploadDto;

@Mapper
public interface UploadMapper {
	public boolean registerUploadFile(UploadDto uploadFiles);
	
	public void deleteUploadFile(int post_id);
	
}
