package com.KoreaIT.test_dd.dto;

public class Article extends Dto{
	public String title;
	public String body;
	public int hit;
	public int memberId;
	
	public Article(int id, String regDate, String updateDate, String title, String body, int memberId) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.title = title;
		this.body = body;
		this.memberId = memberId;
	}

	public void IncreaseHit() {
		this.hit++;
	}
}
