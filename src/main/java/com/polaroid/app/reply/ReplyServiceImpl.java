package com.polaroid.app.reply;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polaroid.app.command.MemberReplyDto;
import com.polaroid.app.command.ReplyDto;
import com.polaroid.app.command.ReplyLikeDto;

@Transactional(readOnly = true)
@Service
public class ReplyServiceImpl implements ReplyService {

   @Autowired
   ReplyMapper replyMapper;

   //댓글 등록
   @Transactional
   @Override
   public void registerReply(ReplyDto replyDto) {
      replyMapper.insertReply(replyDto);
   }

   //댓글 삭제
   @Transactional
   @Override
   public int removeReply(int replyId) {
      replyMapper.deleteReply(replyId);
      return 1;
   }

   //댓글 수정
   @Transactional
   @Override
   public int modifyReply(ReplyDto replyDto) {
      replyMapper.updateReply(replyDto);
      return 1;
   }

   //댓글 조회
   @Override
   public List<MemberReplyDto> retrieveReplyList(Map<String, Integer> map) {
      return replyMapper.selectReplyList(map);
   }
   
   //댓글 개수
   @Override
   public int retrieveReplyCount(int postId) {
      return replyMapper.selectReplyCount(postId);
   }

   //댓글 좋아요
   @Transactional
   @Override
   public int replyLike(ReplyLikeDto replyLike) {
      return replyMapper.replyLike(replyLike);
   }
   
   //댓글 좋아요 취소
   @Transactional
   @Override
   public int replyRemoveLike(ReplyLikeDto deleteReplyLike) {
      return replyMapper.replyDeleteLike(deleteReplyLike);      
   }
   
}