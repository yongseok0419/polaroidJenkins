package com.polaroid.app.profile;

import org.apache.ibatis.annotations.Mapper;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;
import com.polaroid.app.command.ProfileDto;

@Mapper
public interface ProfileMapper {

	//프로필 등록 시 닉네임만 수정
	public void updateMemberNick(MemberDto membetDto);
	
	//프로필 수정 시 닉네임, 비밀번호 수정
	public boolean updateMember(MemberDto memberDto);
	
	//프로필 등록
	public void insertProfileFile(ProfileDto profileDto);
	
	//프로필 등록(이미지를 선택하지 않은 경우)
	public void insertProfileExcludeImage(ProfileDto profileDto);
	
	//프로필 상세화면 조회
	public MemberProfileDto selectProfileDetail(int memberId);
	
	//프로필 존재유무 조회(회원은 하나의 프로필을 가져야하기때문에 2개 이상의 프로필을 가질 수 없다.)
	public int isProfile(int memberId);
	
	//프로필 수정
	public void updateProfile(ProfileDto profileDto);
	
	//프로필 수정(이미지를 선택하지 않은 경우)
	public void updateProfileExcludeImage(ProfileDto profileDto);
	
	//메인 화면 회원 정보 조회
	public MemberProfileDto selectMemberList(int memberId);

}
