package com.polaroid.app.command;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDto {
	
	private int memberId;										//번호
	private String memberEmail;								//이메일
	private String memberNick;								//닉네임
	private String memberName;								//이름
	private String memberPhone;							//전화번호
	private String memberPwd;								//비밀번호
	private int memberStatusCode;							//상태코드
	private String memberStopPeriod;						//정지기간
	private LocalDateTime memberFirstStopDate;		//최조정지일
	
	private String memberPwdCheck;						//프로필 비밀번호 변경에 필요한 필드명
	
}
