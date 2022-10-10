package com.polaroid.app.member;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;

@Transactional(readOnly = true)
@Service("memberService")
public class MemberServiceImpl implements MemberService {

		@Autowired
		MemberMapper memberMapper;
		
		//회원정보 조회
		@Override
		public MemberDto retrieveMemberByMemberId(String memberId) {
			return memberMapper.selectMemberByMemberId(memberId);
		}
		
		//이메일 인증
		@Transactional
		@Override
		public int registerAuthentication(Map<String, Object> map) {
			return memberMapper.insertAuthentication(map);
		}
		
		//이메일 인증번호 일치 여부
		@Override
		public int checkAuthentication(Integer memberAuthentication) {
			return memberMapper.selectAuthentication(memberAuthentication);
		}
		
		//이메일 중복 체크
		@Override
		public int checkEmail(String memberEmail) {
			return memberMapper.selectEmail(memberEmail);
		}

		//닉네임 중복 체크
		@Override
		public int checkNick(String memberNick) {
			return memberMapper.selectNick(memberNick);
		}
		
		//전화번호 중복 체크
		@Override
		public int checkPhone(String memberPhone) {
			return memberMapper.selectPhone(memberPhone);
		}
		
		//회원가입
		@Transactional
		@Override
		public int registerMember(MemberDto MemberDto) {
			return memberMapper.insertMember(MemberDto);
		}

		//로그인
		@Override
		public MemberDto findMember(MemberDto memberDto) {
			return memberMapper.selectMember(memberDto);
		}
		
		//회원 탈퇴
		@Transactional
		@Override
		public int modifyMember(int memberId) {
			return memberMapper.updateMember(memberId);
		}
		
		//비밀번호 변경 시 DB에 회원이 있는지 유무
	    @Override
	    public int retrieveMemberEmail(String toAddress) {
	        return memberMapper.selectMemberEmail(toAddress);
	    }
	      
	    //비밀번호 재설정
	    @Transactional
	    @Override
	    public int modifyPwd(MemberDto memberDto) {
	         
	        return memberMapper.updatePwd(memberDto);
	    }
	    
	    //관리자페이지에 모든 회원 조회
		@Override
		public List<MemberProfileDto> adminRetrieveMemberAll() {
			return memberMapper.adminSelectMemberAll();
		}

		//관리자가 한 명의 회원에게 임의로 정지를 시킬 수 있다.(7일, 30일, 영구)
		@Transactional
		@Override
		public int modifyMemberStop(MemberProfileDto memberProfileDto) {
			return memberMapper.updateMemberStop(memberProfileDto);
		}
		
		//정지기간이 7일, 30일, 영구에 따라 상태코드 변경
		@Transactional
		@Override
		public int modifyStatusCode(MemberProfileDto memberProfileDto) {
			return memberMapper.updateStatusCode(memberProfileDto);
		}

		//정지된 회원의 리스트 조회
		@Override
		public List<MemberProfileDto> stopRetrieveMemberList() {
			return memberMapper.stopSelectMemberList();
		}

		//관리자가 정지된 회원을 다시 미정지 상태로 수정
		@Transactional
		@Override
		public int modifyMemberStopRecovery(MemberProfileDto memberProfileDto) {
			return memberMapper.updateMemberStopRecovery(memberProfileDto);
		}
		
		//관리자가 정지된 회원을 다시 미정지 상태로 수정할 때 최초정지일 null로 수정
		@Transactional
		@Override
		public int modifyFirstStopDate(MemberProfileDto memberProfileDto) {
			return memberMapper.updateFirstStopDate(memberProfileDto);
		}

}
