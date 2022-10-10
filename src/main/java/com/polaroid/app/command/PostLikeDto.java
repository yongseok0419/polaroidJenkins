package com.polaroid.app.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLikeDto {
   private Integer postLike_id;
   private Integer post_id;
   private Integer member_id;
}