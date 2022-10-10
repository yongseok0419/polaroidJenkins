package com.polaroid.app.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.polaroid.app.command.PostDetailDto;
import com.polaroid.app.command.PostDto;
import com.polaroid.app.command.PostLikeDto;



@Mapper
public interface PostMapper {

   public int registerPost(PostDto postDto); //등록
   public boolean updatePost(PostDto postDto);//수정
   public boolean updatePost(PostDto postDto, List <MultipartFile> uploadFiles);//수정
   public boolean updatePostTitleContent(PostDto postDto);//게시글 제목, 내용만 수정
   public PostDto updatePostDetail(int post_id); //게시글 수정 페이지
   public boolean deletePost(int post_id);//삭제
   
   public List<PostDto> selectPostList(); //전체 게시글 조회
   public PostDetailDto selectPostDetail(int post_id);// 상세조회
   
   public List<PostDto> selectLikePostList(int member_id); //좋아요 게시글 조회
   
   public List<PostDto> selectMyPostList(int member_id); //내 게시글 조회
   public int selectPostCount(int member_id); //내 게시글 수
   
   public int postAllLikeCount(int memberId);//내가 좋아요 받은 수
   public int postsLikeCount(int memberId);      //내가 좋아요 한 개수
   
   public int visitCount(int memberId);         //내 전체 게시글 조회수
   public int postViewCount(int postId);         //해당 게시글 조회수
   public int updateVisitCount(int post_id);      //게시글 조회수 증가
   
   public List<PostDto> searchPostList(String keyword);   //게시글 검색
   
   public int postFindLike(PostLikeDto postLike);   //게시글 좋아요 눌렀는지 여부 확인
   public int postLike(PostLikeDto postLike);   //게시글 좋아요
   public int postDeleteLike(PostLikeDto deletePostLike);   //게시글 좋아요 취소
   public int postLikeCount(int post_id); //좋아요 수   
   
}

