package com.KoreaIT.test_dd.dto;

import java.time.LocalDateTime;

public class Member extends Dto{
	public String loginId;
	public String loginPw;
	public String name;
	
	public Member(int id, LocalDateTime regDate, LocalDateTime updateDate, String loginId, String loginPw, String name) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.loginId = loginId;
		this.loginPw = loginPw;
		this.name = name;
	}
}
