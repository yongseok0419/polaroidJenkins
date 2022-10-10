package com.polaroid.app.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyLikeDto {
   
   private Integer replylikeId;     	//댓글 좋아요 번호
   private Integer replyId;         		//댓글 번호
   private Integer memberId;         //멤버 번호
   
}