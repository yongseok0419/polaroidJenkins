package com.polaroid.app.post;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.polaroid.app.command.PostDetailDto;
import com.polaroid.app.command.PostDto;
import com.polaroid.app.command.PostLikeDto;
import com.polaroid.app.command.UploadDto;
import com.polaroid.app.upload.UploadMapper;

import net.coobird.thumbnailator.Thumbnailator;

/*@Transactional(readOnly = true)*/
@Service("postService")
public class PostServiceImpl implements PostService {

   @Autowired
   PostMapper postMapper;
   
   @Autowired
   UploadMapper uploadMapper;

   @Value("${project.upload.path}")
   private String upload_filepath;

   // 폴더생성함수
   @Transactional
   public String makeFolder() {
      String path = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
      File file = new File(upload_filepath + "/" + path);
      if (file.exists() == false) {
         file.mkdirs(); // 파일생성
      }
      return path; // 경로
   }

   // 게시글 등록
   @Transactional
   @Override
   public boolean registerPost(PostDto postDto, List<MultipartFile> uploadFiles) {

      // 1. 폼데이터 인서트
      postMapper.registerPost(postDto);

      System.out.println("post_id" + postDto.getPost_id());

      // 2. 파일업로드
      for (MultipartFile file : uploadFiles) {
         // 실제파일명 (브라우저별로 조금씩 다를수가있음)
         String origin = file.getOriginalFilename();
         // 저장할파일명(경로가 \\가 들어오는 경우 잘라서 처리)
         String filename = origin.substring(origin.lastIndexOf("/") + 1);
         // 파일사이즈
         long size = file.getSize();
         // 랜덤이름
         String uuid = UUID.randomUUID().toString();
         // 날짜경로
         String path = makeFolder();
         // 업로드경로
         String saveName = upload_filepath + "/" + path + "/" + uuid + "_" + filename;
         // 썸네일경로
         String thumbnailName = upload_filepath + "/" + path + "/thumb_" + uuid + "_" + filename;

         System.out.println(filename);
         System.out.println(size);
         System.out.println(saveName);

         try {
            File saveFile = new File(saveName);
            file.transferTo(saveFile); // 파일업로드
            // 썸네일 생성 업로드
            Thumbnailator.createThumbnail(saveFile, new File(thumbnailName), 500, 500);

         } catch (Exception e) {
            e.printStackTrace();
            System.out.println("업로드중 에러 발생");
         }

         // selectKey키방식

         // 3. 파일의경로를 DB인서트
         uploadMapper.registerUploadFile(UploadDto.builder().post_id(postDto.getPost_id()).upload_filename(filename)
               .upload_filepath(path).upload_fileuuid(uuid).build());

      }
      return true;
   }

   // 전체 게시글 보기
   @Override
   public List<PostDto> retrievePostList() {

      return postMapper.selectPostList();
   }
   
   // 좋아요 게시글 조회
   public List<PostDto> retrieveLikePostList(int member_id) {
      return postMapper.selectLikePostList(member_id);
   }

   // 내 게시글 리스트
   @Override
   public List<PostDto> retrieveMyPostList(int member_id) {

      return postMapper.selectMyPostList(member_id);
   }

   // 내 게시글 갯수
   @Override
   public int selectPostCount(int member_id) {

      return postMapper.selectPostCount(member_id);
   }

   // 게시글 상세보기
   @Override
   public PostDetailDto retrivePostDetail(int post_id) {

      return postMapper.selectPostDetail(post_id);
   }

   // 게시글 수정
   @Transactional
   @Override
   public boolean updatePost(PostDto postDto, List<MultipartFile> uploadFiles) {

      // 기존 업로드 이미지 제거
      // 새로운 업로드 이미지

      // 1. 폼데이터 인서트
      uploadMapper.deleteUploadFile(postDto.getPost_id());

      System.out.println(postDto.getPost_id());

      postMapper.updatePost(postDto);

      // 2. 파일업로드
      for (MultipartFile file : uploadFiles) {
         // 실제파일명 (브라우저별로 조금씩 다를수가있음)
         String origin = file.getOriginalFilename();
         // 저장할파일명(경로가 \\가 들어오는 경우 잘라서 처리)
         String filename = origin.substring(origin.lastIndexOf("/") + 1);
         // 파일사이즈
         long size = file.getSize();
         // 랜덤이름
         String uuid = UUID.randomUUID().toString();
         // 날짜경로
         String path = makeFolder();
         // 업로드경로
         String saveName = upload_filepath + "/" + path + "/" + uuid + "_" + filename;
         // 썸네일경로
         String thumbnailName = upload_filepath + "/" + path + "/thumb_" + uuid + "_" + filename;

         System.out.println(filename);
         System.out.println(size);
         System.out.println(saveName);

         try {
            File saveFile = new File(saveName);
            file.transferTo(saveFile); // 파일업로드
            // 썸네일 생성 업로드
            Thumbnailator.createThumbnail(saveFile, new File(thumbnailName), 500, 500);

         } catch (Exception e) {
            e.printStackTrace();
            System.out.println("업로드중 에러 발생");
         }

         // selectKey키방식

         // 3. 파일의경로를 삭제하고 DB인서트

         uploadMapper.registerUploadFile(UploadDto.builder().post_id(postDto.getPost_id()).upload_filename(filename)
               .upload_filepath(path).upload_fileuuid(uuid).build());

      }
      return true;
   }
   
   //게시글 내용, 제목만 수정
   @Transactional
   @Override
   public boolean updatePost(PostDto postDto) {
      
      return postMapper.updatePostTitleContent(postDto);
   }

   // 게시글 삭제
   @Transactional
   @Override
   public boolean removePost(int post_id) {

      return postMapper.deletePost(post_id);
   }

   // 수정할 때 등록된 데이터 불러오기
   @Transactional
   @Override
   public PostDto modifyPostDetail(int post_id) {

      return postMapper.updatePostDetail(post_id);
   }

   // 게시글 검색
   @Override
   public List<PostDto> searchPostList(String keyword) {
      return postMapper.searchPostList(keyword);
   }

   // 게시글 좋아요 누르기
   @Transactional
   @Override
   public int postLike(PostLikeDto postLike) {

      return postMapper.postLike(postLike);

   }

   // 좋아요 눌렀는지 안눌렀는지
   @Override
   public int postFindLike(PostLikeDto postLike) {

      return postMapper.postFindLike(postLike);
   }

   // 게시글 좋아요 갯수 불러오기
   @Override
   public int postLikeCount(int post_id) {

      return postMapper.postLikeCount(post_id);
   }

   // 전체 리스트에서 게시글 좋아요 취소
   @Transactional
   @Override
   public int postRemoveLike(PostLikeDto deletePostLike) {

      return postMapper.postDeleteLike(deletePostLike);
   }
   
   //내가 좋아요 받은 수
   @Override
   public int retrievePostLikeCount(int memberId){
	   return postMapper.postAllLikeCount(memberId);
	}
   
 //내가 좋아요 한 개수
   @Override
   public int retrievePostsLikeCount(int memberId) {
      return postMapper.postsLikeCount(memberId);
   }
   
   //해당 게시글 조회수
   @Override
   public int postViewCount(int postId) {
      return postMapper.postViewCount(postId);
   }
   //내 전체 게시글 조회수
	@Override
	public int viewVisitCount(int memberId) {
		return postMapper.visitCount(memberId);
	}
   
   //게시글 조회수 증가
   @Override
   public int plusVisitCount(int post_id) {
      return postMapper.updateVisitCount(post_id);
   }


}