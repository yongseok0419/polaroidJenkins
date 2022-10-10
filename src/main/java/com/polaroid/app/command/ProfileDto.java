package com.polaroid.app.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProfileDto {

	private int profileId;					//프로필 번호
	private String profileFileName;	//프로필 파일명
	private String profileFilePath;	//프로필 파일경로
	private String profileFileUuid;	//프로필 식별
	private String profileAccount;	//프로필 공개 여부
	private String profileWebsite;		//프로필 웹사이트
	private String profileAboutMe;	//프로필 자기소개
	private int memberId;				//fk
	
}
