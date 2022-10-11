package com.polaroid.app.controller;

import java.util.HashMap;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.MemberProfileDto;
import com.polaroid.app.command.PostDetailDto;
import com.polaroid.app.command.PostDto;
import com.polaroid.app.command.PostLikeDto;
import com.polaroid.app.post.PostService;
import com.polaroid.app.profile.ProfileService;

@Controller
public class ScreenController {

	@Autowired
	PostService postService;

	@Autowired
	ProfileService profileService;

	// 팔로우/팔로워 리스트 화면

	@GetMapping("follow")
	public String follow() {
		return "follow";
	}

	// 비밀번호 찾기 화면
	@GetMapping("forgotPwd")
	public String forgotPwd() {
		return "forgotPwd";
	}

	//메인 화면
	@GetMapping("/index")
	public String index(Model model, HttpSession session, 
			@RequestParam(name = "memberId", defaultValue = "0") int memberId)  throws Exception {
		
		//내 게시글 조회
		if (memberId == 0) {
			MemberDto member = (MemberDto)session.getAttribute("member");
			memberId = member.getMemberId();
		}		

		MemberProfileDto mpd = profileService.retrieveMemberList(memberId);
		int cnt = profileService.isProfile(memberId);

		List <PostDto> list = postService.retrieveMyPostList(memberId);				
		int postCount = postService.selectPostCount(memberId);
		int postsLikeCount = postService.retrievePostLikeCount(memberId);

		int postVisitCount = postService.viewVisitCount(memberId);		//내 모든 게시물 조회수

		model.addAttribute("posts", list);
		model.addAttribute("postCount", postCount);
		model.addAttribute("postsLikeCount", postsLikeCount);
		model.addAttribute("postVisitCount", postVisitCount);
		
		return "/index";		
	}
	
	// 회원가입 화면
	@GetMapping("join")
	public String Join() {
		return "join";
	}

	// 게시글 전체 조회

	@GetMapping("listAll")
  public String listAll(Model model, HttpSession session) {
	    List <PostDto> list = postService.retrievePostList();
	    model.addAttribute("posts", list);
	    
	    MemberDto member = (MemberDto)session.getAttribute("member");
	      
	   //프로필 존재 유무
		int cnt = profileService.isProfile(member.getMemberId());
			
		model.addAttribute("isProfile", cnt);
	      return "/listAll";
	   }
	

	// 좋아요 게시글 조회
	@GetMapping("listLike")
	public String listLike(Model model, HttpSession session) {

		int member_id = ((MemberDto) session.getAttribute("member")).getMemberId();
		PostLikeDto postLike = new PostLikeDto();
		postLike.setMember_id(member_id);

		List<PostDto> list = postService.retrieveLikePostList(member_id);
		model.addAttribute("likeLists", list);
		return "listLike";
	}

	// 팔로우 게시글 화면
	@GetMapping("listFollow")
	public String listFollow() {
		return "listFollow";
	}

	// 로그인 화면
	@GetMapping("loginForm")
	public String loginForm() {
		return "login";
	}

	// 비밀번호 수정 화면
	@GetMapping("modifyPwd")
	public String modifyPwd() {
		return "modifyPwd";
	}

	// 프로필 등록 화면
	@GetMapping("registProfile")
	public String registProfile() {
		return "registProfile";
	}

	// 업로드 화면
	@GetMapping("upload")
	public String upload() {
		return "upload";
	}

	// 게시글 상세조회
	@GetMapping(value = "/posts/{post_id}")
	public @ResponseBody Map<String, Object> selectPostDetail(@PathVariable("post_id") int post_id,
			HttpSession session) {

		PostLikeDto postLike = new PostLikeDto();
		int memberId = ((MemberDto) session.getAttribute("member")).getMemberId();
		postLike.setPost_id(post_id);
		postLike.setMember_id(memberId);

		int ispostLike = postService.postFindLike(postLike);
		int postLikeCount = postService.postLikeCount(post_id);
		int postView = postService.postViewCount(post_id);

		PostDetailDto post = postService.retrivePostDetail(post_id);
		Map<String, Object> map = new HashMap<>();
		map.put("post", post);
		map.put("ispostLike", ispostLike);
		map.put("postLikeCount", postLikeCount);
		map.put("postView", postView);
		return map;
		
		
	}
	
	//게시글 조회수 증가
	@PutMapping(value = "/postView/{post_id}")
	public @ResponseBody void postsViewUp(@PathVariable("post_id") int post_id) {
		postService.plusVisitCount(post_id);				//해당 게시글 조회수 증가		
	}
	

	// 게시글 수정
	@GetMapping("updateForm")
	public String updateForm(Model model, @RequestParam(value = "post_id", defaultValue = "2") int post_id) {
		// post_id = 30;

		PostDto postDto = postService.modifyPostDetail(post_id); //

		model.addAttribute("postDto", postDto);

		return "update";
	}
	
}
