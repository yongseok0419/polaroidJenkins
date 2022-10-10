package com.polaroid.app.profile;

import org.springframework.web.multipart.MultipartFile;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;
import com.polaroid.app.command.ProfileDto;

public interface ProfileService {
	
	//프로필 등록(이미지를 선택한 경우)
	public boolean registerProfile(ProfileDto profileDto, MemberDto memberDto, MultipartFile upload);
	
	//프로필 등록(이미지를 선택하지 않은 경우)
	public void registProfileExcludeImage(ProfileDto profileDto, MemberDto memberDto);
	
	//프로필 조회
	public MemberProfileDto retrieveProfileDetail(int memberId);
	
	//프로필 존재유무
	public int isProfile(int memberId);
	
	//프로필 수정(이미지를 선택한 경우)
	public void modifyProfile(ProfileDto profileDto, MemberDto memberDto, MultipartFile upload);
	
	//프로필 수정(이미지를 선택하지 않은 경우)
	public void modifyProfileExcludeImage(ProfileDto profileDto, MemberDto memberDto);
	
	//메인 화면 회원 정보 조회
	public MemberProfileDto retrieveMemberList(int memberId);
	
}
