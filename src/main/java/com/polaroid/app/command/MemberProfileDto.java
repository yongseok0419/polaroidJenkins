package com.polaroid.app.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberProfileDto {
	
	private int memberId;						//번호
	private String memberEmail;				//이메일
	private String memberNick;				//닉네임
	private String memberName;				//이름
	private String memberPhone;			//전화번호
	private String memberPwd;				//비밀번호
	private String memberStopPeriod;		//정지기간
	
	private String profileFileName;	//프로필 파일명
	private String profileFilePath;	//프로필 파일경로
	private String profileFileUuid;	//프로필 식별
	private String profileAccount;	//프로필 공개 여부
	private String profileWebsite;		//프로필 웹사이트
	private String profileAboutMe;	//프로필 자기소개
	
}
