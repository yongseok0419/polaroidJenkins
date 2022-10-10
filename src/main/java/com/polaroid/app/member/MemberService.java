package com.polaroid.app.member;

import java.util.List;
import java.util.Map;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;

public interface MemberService {
	
	//회원정보 조회
	public MemberDto retrieveMemberByMemberId(String memberId);
	
	//이메일 인증
	public int registerAuthentication(Map<String, Object> map);
	
	//이메일 인증번호 일치 여부
	public int checkAuthentication(Integer memberAuthentication);
	
	//이메일 중복 체크
	public int checkEmail(String memberEmail);
	
	//닉네임 중복 체크
	public int checkNick(String memberNick);
	
	//전화번호 중복 체크
	public int checkPhone(String memberPhone);
	
	//회원가입
	public int registerMember(MemberDto memberDto);
	
	//로그인
	public MemberDto findMember(MemberDto memberDto);
	
	//회원 탈퇴
	public int modifyMember(int memberId);
	
	//회원 비밀번호 변경
	public int modifyPwd(MemberDto memberDto);
	   
	//비밀번호 변경 시 DB에 회원이 있는지 유무
	public int retrieveMemberEmail(String toAddress);
	
	//관리자페이지에 모든 회원 조회
	public List<MemberProfileDto> adminRetrieveMemberAll();
	
	//관리자가 한 명의 회원에게 임의로 정지를 시킬 수 있다.(7일, 30일, 영구)
	public int modifyMemberStop(MemberProfileDto memberProfileDto);
	
	//정지기간이 7일, 30일, 영구에 따라 상태코드 변경
	public int modifyStatusCode(MemberProfileDto memberProfileDto);
	
	//정지된 회원의 리스트 조회
	public List<MemberProfileDto> stopRetrieveMemberList();
	
	//관리자가 정지된 회원을 다시 미정지 상태로 수정
	public int modifyMemberStopRecovery(MemberProfileDto memberProfileDto);
	
	//관리자가 정지된 회원을 다시 미정지 상태로 수정할 때 최초정지일 null로 수정
	public int modifyFirstStopDate(MemberProfileDto memberProfileDto);
	
}
