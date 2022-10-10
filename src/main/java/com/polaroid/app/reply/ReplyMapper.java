package com.polaroid.app.reply;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.polaroid.app.command.MemberReplyDto;
import com.polaroid.app.command.ReplyDto;
import com.polaroid.app.command.ReplyLikeDto;

@Mapper
public interface ReplyMapper {
   
   public List<MemberReplyDto> selectReplyList(Map<String, Integer> map);
   public void insertReply(ReplyDto replyDto);
   public int deleteReply(int replyId);
   public void updateReply(ReplyDto replyDto);
   public int selectReplyCount(int postId);  //댓글 개수
   
   public int replyLike(ReplyLikeDto replyLike);            //댓글 좋아요
   public int replyDeleteLike(ReplyLikeDto deleteReplyLike);   //댓글 좋아요 취소

}