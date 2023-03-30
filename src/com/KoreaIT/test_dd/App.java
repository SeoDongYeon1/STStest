package com.KoreaIT.test_dd;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.test_dd.dto.Article;
import com.KoreaIT.test_dd.dto.Member;
import com.KoreaIT.test_dd.util.Util;

public class App {
	static List<Article> articles = new ArrayList<>();
	static List<Member> members = new ArrayList<>();
	static Member loginedMember = null;
	
	public void start() {
		int lastArticleId = 0;
		int lastMemberId = 0;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("==프로그램 시작==");
		while(true) {
			System.out.printf("명령어 > ");
			String cmd = sc.nextLine();
			
			if(cmd.length()==0) {
				System.out.println("명령어를 입력해주세요.");
				continue;
			}
			
			if(cmd.equals("exit")) {
				break;
			}
			
			if(cmd.equals("article write")) {
				int id = lastArticleId + 1;
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				String regDate = Util.getNowDateStr();
				String updateDate = Util.getNowDateStr();
				int memberId = loginedMember.id;
				
				Article article = new Article(id, regDate, updateDate, title, body, memberId);
				articles.add(article);
				
				System.out.printf("%d번 게시글이 생성되었습니다.\n", id);
				lastArticleId++;
			}
			else if(cmd.equals("article list")) {
				if(articles.size()==0) {
					System.out.println("게시글이 존재하지 않습니다.");
					continue;
				}
				
				System.out.println("번호  |  제목  | 조회수 | 작성자");
				for(int i = articles.size()-1; i>=0; i--) {
					Article article = articles.get(i);
					System.out.printf("%d  |  %s    | %d  | %s");
				}
			}
			else if(cmd.equals("article detail")) {
				String[] cmdBits = cmd.split(" ");
				
				if(cmdBits.length < 3) {
					System.out.println("articel detail (숫자)로 입력해주세요.");
					continue;
				}
					
				int id = Integer.parseInt(cmdBits[2]);
				
				Article foundArticle = getArticleById(id);
				
				if(foundArticle==null) {
					System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
					continue;
				}
				foundArticle.IncreaseHit();
				System.out.printf("번호 : %d \n", foundArticle.id);
				System.out.printf("제목 : %s \n", foundArticle.title);
				System.out.printf("내용 : %s \n", foundArticle.body);
				System.out.printf("작성날짜 : %s \n", foundArticle.regDate);
				System.out.printf("수정날짜 : %s \n", foundArticle.updateDate);
				System.out.printf("조회수 : %s \n", foundArticle.hit);
				System.out.printf("작성자 : %d \n", foundArticle.memberId);
			}
			else if(cmd.equals("article delete")) {
				String[] cmdBits = cmd.split(" ");
				
				if(cmdBits.length < 3) {
					System.out.println("articel delete (숫자)로 입력해주세요.");
					continue;
				}
				
				int id = Integer.parseInt(cmdBits[2]);
				
				Article foundArticle = getArticleById(id);
				
				if(foundArticle==null) {
					System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
					continue;
				}
				System.out.printf("%d번 게시글이 삭제되었습니다.\n", foundArticle.id);
				articles.remove(foundArticle);
			}
			else if(cmd.equals("article modify")) {
				String[] cmdBits = cmd.split(" ");
				
				if(cmdBits.length < 3) {
					System.out.println("articel modify (숫자)로 입력해주세요.");
					continue;
				}
				
				int id = Integer.parseInt(cmdBits[2]);
				
				Article foundArticle = getArticleById(id);
				
				if(foundArticle==null) {
					System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
					continue;
				}
				System.out.printf("새 제목 : ");
				String title = sc.nextLine();
				System.out.printf("새 내용 : ");
				String body = sc.nextLine();
				String updateDate = Util.getNowDateStr();
				
				foundArticle.title = title;
				foundArticle.body = body;
				foundArticle.updateDate = updateDate;
				
				System.out.printf("%d번 게시글이 수정되었습니다.\n", foundArticle.id);
			}
			else {
				System.out.println("존재하지 않는 명령어입니다.");
			}
		}
		System.out.println("==프로그램 종료==");
		sc.close();
	}

	private Article getArticleById(int id) {
		int i = 0;
		for(Article article : articles) {
			if(article.id==id) {
				return articles.get(i);
			}
			i++;
		}
		return null;
	}

}
