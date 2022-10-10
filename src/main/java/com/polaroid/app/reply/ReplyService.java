package com.polaroid.app.reply;

import java.util.List;
import java.util.Map;

import com.polaroid.app.command.MemberReplyDto;
import com.polaroid.app.command.ReplyDto;
import com.polaroid.app.command.ReplyLikeDto;

public interface ReplyService {

   //댓글 등록
   public void registerReply(ReplyDto replyDto);
   
   //댓글 삭제
   public int removeReply(int reply_id);
   
   //댓글 수정
   public int modifyReply(ReplyDto replyDto);
   
   //댓글 조회
   public List<MemberReplyDto> retrieveReplyList(Map<String, Integer> map);
   
   //댓글 개수
   public int retrieveReplyCount(int postId);
   
   //댓글 좋아요
   public int replyLike(ReplyLikeDto replyLike);   
   
   //댓글 좋아요 취소
   public int replyRemoveLike(ReplyLikeDto deleteReplyLike);    
   
}