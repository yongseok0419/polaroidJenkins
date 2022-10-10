package com.polaroid.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polaroid.app.command.MemberDto;
import com.polaroid.app.command.PostDto;
import com.polaroid.app.command.PostLikeDto;
import com.polaroid.app.post.PostService;

@Controller
public class PostController {

   @Autowired
   @Qualifier("postService")
   PostService postService;

   // 전체 게시글 조회
   @GetMapping("postListAll")
   public String postListAll() {
      return "/listAll";
   }

   // 좋아요 게시글 조회
   @GetMapping("postLikeList")
   public String postLikeList() {
      return "/listLike";
   }

   @GetMapping("postUpload")
   public String postUpload() {
      return "/upload";
   }

   // 게시글 등록
   @PostMapping("/registerPost")
   public String registerPost(@Valid PostDto postDto, Errors errors, Model model,
         @RequestParam("file") List<MultipartFile> uploadFiles, HttpSession session) {

      if (errors.hasErrors()) {
         List<FieldError> list = errors.getFieldErrors();
         for (FieldError err : list) {
            if (err.isBindingFailure()) {
               model.addAttribute("valid" + err.getField(), "빈칸이 있습니다.");
            } else {
               model.addAttribute("valid_" + err.getField(), err.getDefaultMessage());
            }
         }
         model.addAttribute("postDto", postDto);
         return "/upload";
      }

      // 공백데이터 제거
      uploadFiles = uploadFiles.stream().filter((f) -> !f.isEmpty()).collect(Collectors.toList());
      // 이미지 파일검증
      for (MultipartFile f : uploadFiles) {
         if (f.getContentType().contains("image") == false) { // 이미지가 아닌경우
            // 다시 등록화면으로
            model.addAttribute("vo", postDto);
            model.addAttribute("valid_files", "이미지형식만 등록가능합니다");
            return "/upload";
         }
      }

      // 게시글 등록
      MemberDto member = (MemberDto) session.getAttribute("member");
      int member_id = member.getMemberId();
      postDto.setMember_id(member_id);

      boolean result = postService.registerPost(postDto, uploadFiles); // 상품데이터, 이미지데이터

      return "redirect:/index";

   }

   // 게시글 수정
   @PostMapping("/updatePost")
   public String updatePost(@Valid PostDto postDto, Errors errors, Model model,
         @RequestParam("file") List<MultipartFile> uploadFiles, HttpSession session) {

      if (errors.hasErrors()) {
         List<FieldError> list = errors.getFieldErrors();
         for (FieldError err : list) {
            if (err.isBindingFailure()) {
               model.addAttribute("valid" + err.getField(), "빈칸이 있습니다.");
            } else {
               model.addAttribute("valid_" + err.getField(), err.getDefaultMessage());
            }
         }
         model.addAttribute("postDto", postDto);

         return "/update";
      }

      // 공백데이터 제거
      uploadFiles = uploadFiles.stream().filter((f) -> !f.isEmpty()).collect(Collectors.toList());

      if (uploadFiles.size() != 0) {
         // 기존 업로드 이미지 제거
         // 새로운 업로드 이미지
         MemberDto member = (MemberDto) session.getAttribute("member");
         int member_id = member.getMemberId();
         postDto.setMember_id(member_id);
         postService.updatePost(postDto, uploadFiles);

      } else {
         postService.updatePost(postDto);
      }

      return "redirect:/index";
   }

   // 게시글 삭제
   @GetMapping("/deletePost")
   public String deletePost(@RequestParam(value = "post_id") int post_id, RedirectAttributes RA) {
      boolean result = postService.removePost(post_id);

      if (result) {
         RA.addFlashAttribute("msg", "삭제 성공하였습니다");
      } else {
         RA.addFlashAttribute("msg", "삭제 실패하였습니다");
      }

      return "redirect:/index";
   }

   // 게시글 검색
   @PostMapping("/search")
   public @ResponseBody Map<String, Object> searchPostList(@RequestBody HashMap<String, String> keyword) {

      List<PostDto> searchPostList = postService.searchPostList(keyword.get("keyword"));

      Map<String, Object> map = new HashMap<>();

      map.put("searchPostList", searchPostList);

      return map;

   }

   // 게시글 좋아요
   @GetMapping("/postLike/{post_id}")
   public @ResponseBody Map<String, Integer> postLike(@PathVariable int post_id, HttpSession session) {

      MemberDto member = (MemberDto) session.getAttribute("member");
      int member_id = member.getMemberId();

      PostLikeDto postLikeDto = new PostLikeDto();
      postLikeDto.setMember_id(member_id);
      postLikeDto.setPost_id(post_id);

      Map<String, Integer> map = new HashMap<String, Integer>();

      // int postLike = postService.postLike(post_id);
      int postFindLike = postService.postFindLike(postLikeDto); // 좋아요 눌렀으면 1, 안눌렀으면 0
      System.out.println("postFindLike : " + postFindLike);

      if (postFindLike == 0) { // 좋아요 안 누른 상태
         int postLike = postService.postLike(postLikeDto);// 좋아요 누르기
         int postLikeCount = postService.postLikeCount(post_id); // 좋아요 개수

         map.put("postLike", postLike);
         map.put("postLikeCount", postLikeCount);
         map.put("postFindLike", postFindLike);
      }

      return map;

   }

   // 게시글 좋아요 취소
   @GetMapping("/deletePostLike/{post_id}")
   public @ResponseBody Map<String, Integer> deleteLike(@PathVariable int post_id, HttpSession session) {
      Map<String, Integer> map = new HashMap<String, Integer>();

      MemberDto member = (MemberDto) session.getAttribute("member");
      int member_id = member.getMemberId();

      PostLikeDto postLikeDto = new PostLikeDto();
      postLikeDto.setMember_id(member_id);
      postLikeDto.setPost_id(post_id);

      int postFindLike = postService.postFindLike(postLikeDto);

      if (postFindLike == 1) {
         int deletePostLike = postService.postRemoveLike(postLikeDto); // 좋아요 삭제
         int postLikeCount = postService.postLikeCount(post_id); // 좋아요 개수

         map.put("deletePostLike", deletePostLike);
         map.put("postLikeCount", postLikeCount);
      }

      return map;
   }

}