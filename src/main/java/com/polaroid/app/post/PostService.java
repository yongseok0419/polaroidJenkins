package com.polaroid.app.post;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.polaroid.app.command.PostDetailDto;
import com.polaroid.app.command.PostDto;
import com.polaroid.app.command.PostLikeDto;

public interface PostService {
   

   public List<PostDto> retrievePostList(); //전체 게시글 조회

   public List<PostDto> retrieveMyPostList(int member_id); //내 게시글 조회 
   public int selectPostCount(int member_id); //내 게시글 수

   public List<PostDto> retrieveLikePostList(int member_id);  //좋아요 게시글 조회
   
   public PostDetailDto retrivePostDetail(int post_id);// 상세조회
   
   public boolean updatePost(PostDto postDto, List<MultipartFile> uploadFiles);//수정
   
   public boolean updatePost(PostDto postDto);// 게시글 내용, 제목만 수정
   
   public boolean removePost(int post_id);// 게시글 전체 삭제 
   
   public PostDto modifyPostDetail(int post_id); //수정 전 데이터 불러오기
   
   public boolean registerPost(PostDto postDto, List<MultipartFile> uploadFiles);//등록
   
   public int retrievePostLikeCount(int memberId); //내가 좋아요 받은 수
   public List<PostDto> searchPostList(String keyword);   //게시글 검색
   
   
   public int postLike(PostLikeDto postLike);            //게시글 좋아요 누르기
   public int postFindLike(PostLikeDto postLike);   //좋아요 눌렀는지 확인 여부
   public int postLikeCount(int post_id);               //게시글 좋아요 개수
   public int postRemoveLike(PostLikeDto deletePostLike);   //좋아요 취소
   
 public int retrievePostsLikeCount(int memberId);      //내가 게시글 좋아요 총 개수
   
   public int postViewCount(int postId);            //해당 게시글 조회수
   public int viewVisitCount(int memberId);            //내 모든 게시글 조회수
   public int plusVisitCount(int post_id);            //게시글 조회수 증가
   
}