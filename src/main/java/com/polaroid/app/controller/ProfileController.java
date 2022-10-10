package com.polaroid.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;
import com.polaroid.app.command.ProfileDto;
import com.polaroid.app.profile.ProfileService;

@Controller
public class ProfileController {

	@Autowired
	@Qualifier("profileService")
	ProfileService profileService;
	
	
	@PostMapping("/registProfile")
	public String registProfile(ProfileDto profileDto, MemberDto memberDto,
									 @RequestParam("upload") MultipartFile upload,
									 Model model, HttpSession session){
		
		//이미지 파일 검증
//			if(upload.getContentType().contains("image") == false) {		//이미지가 아닌경우
//				//다시 등록화면으로
//				model.addAttribute("profileDto", profileDto);
//				model.addAttribute("valid_files", "이미지형식만 등록가능합니다.");
//				return "/registProfile";
//			}
		
		System.out.println("파일의 갯수 : " + upload.getSize());
		
		//프로필 등록
		MemberDto member = (MemberDto)session.getAttribute("member");
		
		profileDto.setMemberId(member.getMemberId());
		memberDto.setMemberId(member.getMemberId());
		
		//프로필 존재 유무
		int cnt = profileService.isProfile(member.getMemberId());
		System.out.println("cnt : " + cnt);
		if(cnt == 0) {	//회원의 프로필 정보가 없는 경우
			//프로필 수정 //회원의 정보가 있는 경우
			if(upload.getSize() == 0) {
				//회원정보가 있는 경우 중 프로필 화면에서 이미지를 선택하지 않은 경우
				profileService.registProfileExcludeImage(profileDto, memberDto);
				
			} else {
				//회원정보가 있는 경우 증 프로필 화면에서 이미지를 선택한 경우
				//프로필 등록
				profileService.registerProfile(profileDto, memberDto, upload);
			}
			
		}
		
		int memberId = ((MemberDto)session.getAttribute("member")).getMemberId();
		MemberProfileDto mpd = profileService.retrieveMemberList(memberId);
		int count = profileService.isProfile(memberId);		
		session.setAttribute("isProfile", count);
		session.setAttribute("mpd", mpd);
		
		return "redirect:/index";
	}
	
	@PostMapping("/updateProfile")
	public String updateProfile(ProfileDto profileDto, MemberDto memberDto,
									 @RequestParam("upload") MultipartFile upload,
									 Model model, HttpSession session){
		System.out.println("--------------------------------------------------------");
		
//		//이미지 파일 검증
//			if(upload.getContentType().contains("image") == false) {		//이미지가 아닌경우
//				//다시 등록화면으로
//				model.addAttribute("profileDto", profileDto);
//				model.addAttribute("valid_files", "이미지형식만 등록가능합니다.");
//				return "/registProfile";
//			}
		
		System.out.println("파일의 갯수 : " + upload.getSize());
		
		//프로필 등록(수정)
		MemberDto member = (MemberDto)session.getAttribute("member");
		
		profileDto.setMemberId(member.getMemberId());
		memberDto.setMemberId(member.getMemberId());
		
		//프로필 존재 유무
		int cnt = profileService.isProfile(member.getMemberId());
		System.out.println("cnt : " + cnt);
		if(cnt == 1) {
			//프로필 수정 //회원의 정보가 있는 경우
			if(upload.getSize() == 0) {
				//회원정보가 있는 경우 중 프로필 화면에서 이미지를 선택하지 않은 경우
				profileService.modifyProfileExcludeImage(profileDto, memberDto);
				
			} else {
				//회원정보가 있는 경우 증 프로필 화면에서 이미지를 선택한 경우
				profileService.modifyProfile(profileDto, memberDto, upload);
			}
		}
		
		int memberId = ((MemberDto)session.getAttribute("member")).getMemberId();
		MemberProfileDto mpd = profileService.retrieveMemberList(memberId);
		int count = profileService.isProfile(memberId);		
		session.setAttribute("isProfile", count);
		session.setAttribute("mpd", mpd);					
		
		return "redirect:/index";
	}
	
	//프로필 수정 화면
	@GetMapping("/updateProfileForm")
	public String updateProfileForm(HttpSession session, Model model) {
		
		
		
		MemberDto member = (MemberDto)session.getAttribute("member");
	
		
		MemberProfileDto mpd = profileService.retrieveProfileDetail(member.getMemberId());
		
		System.out.println("mpd : " + mpd);
		model.addAttribute("mpd", mpd);
		
		return "/updateProfile";	//redirect가 없으면 forward 방식(model에 바인딩된 정보를 끌고간다.)
	}
	
}
