package com.KoreaIT.test_dd.dto;

public class Article extends Dto{
	public String title;
	public String body;
	public int hit;
	
	public Article(int id, String regDate, String updateDate, String title, String body) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.title = title;
		this.body = body;
	}

	public void IncreaseHit() {
		this.hit++;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", body=" + body + ", hit=" + hit + "]";
	}
}
