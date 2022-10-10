package com.polaroid.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;
import com.polaroid.app.command.SendMailHelper;
import com.polaroid.app.member.MemberService;
import com.polaroid.app.profile.ProfileService;

@Controller
public class MemberController {

		@Autowired
		@Qualifier("memberService")
		MemberService memberService;
		
		@Autowired
		ProfileService profileService;
		
		@Autowired
		SendMailHelper sendMailHelper;
		
		@Value("${spring.mail.username}")
		private String fromAddress;
		
		//이메일 인증
		@ResponseBody
		@PostMapping("/authForm")
		public String authForm(@RequestBody MemberDto memberDto) throws Exception {
			//인증번호 난수 생성
			Random random = new Random();
			
			// 111111~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용
			int authCode = random.nextInt(888888) + 111111;
			
			//이메일 인증
			System.out.println("fromaddress : " + fromAddress);
			
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			map.put("authCode", authCode); 			
			String body = "인증번호 : " + authCode;
			
			String[] toAddress = {memberDto.getMemberEmail()};
			System.out.println("toAddress : " + toAddress);
			String subject = "polaroid에서 요청하신 인증번호를 발송해드립니다";
			sendMailHelper.sendMail(fromAddress, toAddress, subject, body);
			
			//DB에 인증번호 저장 (이메일, 인증번호)
			Map<String, Object> map1 = new HashMap<>();
			map1.put("authEmail", memberDto.getMemberEmail());
			map1.put("authCode", authCode);		
			
			int cnt = memberService.registerAuthentication(map1);
			System.out.println("cnt : " + cnt);
			
			return String.valueOf(cnt);
		}
		
		// 비밀번호 변경 링크 메일전송
        @ResponseBody
        @PostMapping("/forgotPwd")
        public String forgotPwd(@RequestBody Map<String, String> map) throws Exception {

           String memberEmail =  map.get("memberEmail");
           
           //닉네임
           int cnt = memberService.retrieveMemberEmail(memberEmail);

           if (cnt == 1) {
        	          	   
              
              String[] toAddress = { memberEmail };
              String subject = "더욱 간편하게 polaroid에 다시 로그인해보세요";
              
              Map<String, Object> map2 = new HashMap<>();
              String url = "http://localhost:8282/modifyPwdForm?memberEmail=" + memberEmail;
              map2.put("url", url);
              //map2.put("nickName", ?);
              String result = sendMailHelper.getMailBody("html/email-inlineimage", map2);
              
              sendMailHelper.sendMail(fromAddress, toAddress, subject, result);

              return "1";

           } else {
              return "0";
           }
        }
        
    	// 비밀번호 변경 화면
        @GetMapping("/modifyPwdForm")
        public String modifyPwdForm(@RequestParam(value = "memberEmail") String memberEmail) throws Exception {

           return "/modifyPwd";
        }
        
        // 비밀번호 변경
        @ResponseBody
        @PostMapping("/changePwd")
        public String changePwd(@RequestBody HashMap<String, Object> map) throws Exception {
           
           String memberPwd  = (String)map.get("memberPwd");      
           String memberEmail = ((ArrayList<String>)map.get("memberEmail")).get(0);   
           
           System.out.println("memberEmail : " + memberEmail);
           System.out.println("memberPwd : " + memberPwd);
           
           MemberDto memberDto = new MemberDto();
           memberDto.setMemberEmail(memberEmail);
           memberDto.setMemberPwd(memberPwd);
           
           int result = memberService.modifyPwd(memberDto);

           if (result == 1) {
              
              return "1";
           } else {
              return "0";
           }
        }

		//인증번호 일치 여부
		@ResponseBody
		@PostMapping("/checkAuthForm")
		public String checkAuthForm(@RequestBody Map<String, Integer> map) throws Exception {
			
			//DB에 있는 인증번호 일치 여부
			int cnt = memberService.checkAuthentication(map.get("memberAuthentication"));
			
			return String.valueOf(cnt);
		}
		
		//이메일 중복 체크
		@ResponseBody
		@PostMapping("/checkEmail")
		public String checkEmail(@RequestBody Map<String, String> map) throws Exception {
			
			int cnt = memberService.checkEmail(map.get("memberEmail"));
			
			return String.valueOf(cnt);
		}
		
		//닉네임 중복 체크
		@ResponseBody
		@PostMapping("/checkNick")
		public String checkNick(@RequestBody Map<String, String> map) throws Exception {
					
			int cnt = memberService.checkNick(map.get("memberNick"));
					
			return String.valueOf(cnt);
		}
				
		//전화번호 중복 체크
		@ResponseBody
		@PostMapping("/checkPhone")
		public String checkPhone(@RequestBody Map<String, String> map) throws Exception {
					
			int cnt = memberService.checkPhone(map.get("memberPhone"));
					
			return String.valueOf(cnt);
		}		
		
		//회원가입
		@PostMapping("/joinForm")
		public String joinForm(MemberDto memberDto) throws Exception {
			
			//500 Error 내기(시연)
			//memberService = null;
			
			memberService.registerMember(memberDto);
			
			return "redirect:/loginForm";
		}
		

		//로그인 요청 처리
		@ResponseBody
		@PostMapping("/login")
		public String login(@RequestBody MemberDto memberDto, HttpSession session,  Model model) throws Exception {
			
			MemberDto login = memberService.findMember(memberDto);
			
			if(login == null) {
				return "0";
			} else if(login.getMemberStatusCode() == 1 || login.getMemberStatusCode() == 2 ||
					      login.getMemberStatusCode() == 3 || login.getMemberStatusCode() == 4) {	//정지된 회원이거나 탈퇴한 회원인 경우
				return "2";
		    } else if(memberDto.getMemberEmail().startsWith("admin")){ //관리자인 경우
		    	MemberProfileDto mpd = profileService.retrieveMemberList(login.getMemberId());
				int cnt = profileService.isProfile(login.getMemberId());		
				session.setAttribute("isProfile", cnt);
				session.setAttribute("mpd", mpd);	
		    	session.setAttribute("member", login);
		    	//session.setMaxInactiveInterval(30*60);
				return "3";
		    } else { //일반 회원인 경우
		    	MemberProfileDto mpd = profileService.retrieveMemberList(login.getMemberId());
				int cnt = profileService.isProfile(login.getMemberId());		
				session.setAttribute("isProfile", cnt);
				session.setAttribute("mpd", mpd);	
		    	session.setAttribute("member", login);		    	
		    	//session.setMaxInactiveInterval(30*60);			
				return "1";
			}			
		}
		
		//로그아웃
		@GetMapping("/logoutForm")
		public String logoutForm(HttpSession session) throws Exception {
			
			session.invalidate();
			
			return "redirect:/loginForm";
		}
		
		//회원탈퇴 비동기 메세지
		@ResponseBody
		@PostMapping("/withdrawalForm")
		public String withdrawalForm(@RequestBody MemberDto member, HttpSession session) throws Exception {
			
			System.out.println("memberPwd : " + member.getMemberPwd());
			MemberDto login = (MemberDto)session.getAttribute("member");
			
			
			if(member.getMemberPwd().equals(login.getMemberPwd())) {
				return "1";
			} else {
				return "0";
			}		
			
		}
		
		//회원 탈퇴
		@GetMapping("/withdrawal")
		public String withdrawal(HttpSession session) throws Exception {
			//비밀번호 체크해서 맞으면 회원탈퇴
			MemberDto login = (MemberDto)session.getAttribute("member");
			int cnt = memberService.modifyMember(login.getMemberId());
			return "/login";
		}
		
		//관리자페이지에 모든 회원 조회
		@GetMapping("adminIndex")
		public String adminIndex(Model model) {
		
		List<MemberProfileDto> mpdList = memberService.adminRetrieveMemberAll();
		model.addAttribute("mpdList", mpdList);
					
		return "adminIndex";
		}
		
		//관리자가 한 명의 회원에게 임의로 정지를 시킬 수 있다.(7일, 30일, 영구)
		@GetMapping("updateMemberStopperiod")
		public String modifyMemberStopperiod(MemberProfileDto memberProfileDto) {
			
			//관리자가 한 명의 회원에게 임의로 정지를 시킬 수 있다.(7일, 30일, 영구)
			int cnt = memberService.modifyMemberStop(memberProfileDto);
			//정지기간이 7일, 30일, 영구에 따라 상태코드 변경
			int cnt1 = memberService.modifyStatusCode(memberProfileDto);
			
			return "redirect:/adminIndex";
		}
		
		//정지된 회원의 리스트 조회
		@GetMapping("adminStop")
		public String adminStop(Model model) {
			
			List<MemberProfileDto> smpdList = memberService.stopRetrieveMemberList();
			model.addAttribute("smpdList", smpdList); //정지된 MemberProfileDto 리스트
			
			return "adminStop";
		}
		
		//관리자가 정지된 회원을 다시 미정지 상태로 수정
		@GetMapping("memberStopRecovery")
		public String memberStopRecovery(MemberProfileDto memberProfileDto) {
			
			//관리자가 정지된 회원을 다시 미정지 상태로 수정
			int cnt = memberService.modifyMemberStopRecovery(memberProfileDto);
			//정지기간이 7일, 30일, 영구에 따라 상태코드 변경
			int cnt1 = memberService.modifyStatusCode(memberProfileDto);
			//관리자가 정지된 회원을 다시 미정지 상태로 수정할 때 최초정지일 null로 수정
			int cnt2 = memberService.modifyFirstStopDate(memberProfileDto);
			
			return "redirect:/adminStop";
		}
		
}
