package com.polaroid.app.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;

@Mapper
public interface MemberMapper {
	
	//회원정보 조회
	public MemberDto selectMemberByMemberId(String memberId); 
	
	//이메일 인증
	public int insertAuthentication(Map<String, Object> map);
	
	//이메일 인증번호 일치여부
	public int selectAuthentication(Integer memberAuthentication);
	
	//이메일 중복 체크
	public int selectEmail(String memberEmail);
	
	//닉네임 중복 체크
	public int selectNick(String memberNick);
	
	//전화번호 중복 체크
	public int selectPhone(String memberPhone);
	
	//회원가입
	public int insertMember(MemberDto memberDto);
	
	//로그인
	public MemberDto selectMember(MemberDto memberDto);
	
	//회원탈퇴
	public int updateMember(int memberId);
	
	//회원 비밀번호 변경
	public int updatePwd(MemberDto memberDto);
	   
	//비밀번호 변경 시 DB에 회원이 있는지 유무
	public int selectMemberEmail(String toAddress);
	
	//관리자페이지에 모든 회원 조회
	public List<MemberProfileDto> adminSelectMemberAll();
	
	//관리자가 한 명의 회원에게 임의로 정지를 시킬 수 있다.(7일, 30일, 영구)
	public int updateMemberStop(MemberProfileDto memberProfileDto);
	
	//정지기간이 7일, 30일, 영구에 따라 상태코드 변경
	public int updateStatusCode(MemberProfileDto memberProfileDto);
	
	//정지된 회원의 리스트 조회
	public List<MemberProfileDto> stopSelectMemberList();
	
	//관리자가 정지된 회원을 다시 미정지 상태로 수정
	public int updateMemberStopRecovery(MemberProfileDto memberProfileDto);
	
	//관리자가 정지된 회원을 다시 미정지 상태로 수정할 때 최초정지일 null로 수정
	public int updateFirstStopDate(MemberProfileDto memberProfileDto);
	
}
