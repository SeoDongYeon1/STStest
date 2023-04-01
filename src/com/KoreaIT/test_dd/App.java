package com.KoreaIT.test_dd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
//		int lastMemberId = 0;
		
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
//				if(islogined()==false) {
//					System.out.println("로그인 후 이용해주세요.");
//					continue;
//				}
				int id = lastArticleId + 1;
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				String regDate = Util.getNowDateStr();
				String updateDate = Util.getNowDateStr();
				int memberId = 1;
				
				Article article = new Article(id, regDate, updateDate, title, body, memberId);
				articles.add(article);
				
				Connection conn = null;
				PreparedStatement pstmt = null;
				// Sql 구문을 실행시키는 기능을 갖는 객체를 pstmt라는 이름으로 생성
				
				// 1. 드라이버 연결
				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "1234");
					// url은 DB에 연결할 때 찾는 주소를 적는 곳

					String sql = "INSERT INTO article";
					sql += " SET regDate = NOW(),";
					sql += "updateDate = NOW(),";
					sql += "title = '"+ title + "',";
					sql += "`body` = '" + body + "',";
					sql += "memberId = 1;";
					// sql 쿼리문 작성

					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);
					// conn.prepareStatement(sql)은 sql문을 전송한다는 뜻

					pstmt.executeUpdate();
					// executeUpdate()는 조회문(select, show 등)을 제외한 create, drop, insert, delete, update 등등 문을 처리할 때 사용한다
					// 안쓰면 DB에 적용이 안됨.

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러 : " + e);
				} finally {
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						if (pstmt != null && !pstmt.isClosed()) {
							pstmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
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
					System.out.printf("%d  |  %s    | %d  | %d\n", article.id, article.title, article.hit, article.memberId);
				}
			}
			
			else if(cmd.startsWith("article detail")) {
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
			else if(cmd.startsWith("article delete")) {
//				if(islogined()==false) {
//					System.out.println("로그인 후 이용해주세요.");
//					continue;
//				}
				
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
//				if(islogined()==false) {
//					System.out.println("로그인 후 이용해주세요.");
//					continue;
//				}
//				
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
//			else if(cmd.equals("member join")) {
//				if(islogined()) {
//					System.out.println("로그아웃 후 이용해주세요.");
//					continue;
//				}
//				int id = lastMemberId + 1;
//				String loginId = null;
//				String loginPw = null;
//				String loginPwConfirm = null;
//				String name = null;
//				
//				while(true) {
//					System.out.printf("로그인 아이디 : ");
//					loginId = sc.nextLine();
//					
//					if(loginId.length()==0) {
//						System.out.println("필수 정보입니다.");
//						continue;
//					}
//					
//					if(isJoinableLoginId(loginId)==false) {
//						System.out.println("이미 사용중인 아이디입니다.");
//						continue;
//					}
//					System.out.println("사용 가능한 아이디입니다.");
//					break;
//				}
//				
//				while(true) {
//					System.out.printf("로그인 비밀번호 : ");
//					loginPw = sc.nextLine();
//					
//					if(loginPw.length()==0) {
//						System.out.println("필수 정보입니다.");
//						continue;
//					}
//					while(true) {
//						System.out.printf("로그인 비밀번호 확인: ");
//						loginPwConfirm = sc.nextLine();
//						
//						if(loginPwConfirm.length()==0) {
//							System.out.println("필수 정보입니다.");
//							continue;
//						}
//						break;
//					}
//					if(loginPw.equals(loginPwConfirm)==false) {
//						System.out.println("비밀번호가 일치하지 않습니다.");
//						continue;
//					}
//					System.out.println("비밀번호가 일치합니다.");
//					break;
//				}
//				while(true) {
//					System.out.printf("이름 : ");
//					name = sc.nextLine();
//					
//					if(name.length()==0) {
//						System.out.println("필수 정보입니다.");
//						continue;
//					}
//					break;
//				}
//				String regDate = Util.getNowDateStr();
//				String updateDate = Util.getNowDateStr();
//				
//				Member member = new Member(id, regDate, updateDate, loginId, loginPw, name);
//				members.add(member);
//				System.out.println(name+"님 회원가입 되었습니다.");
//			}
//			else if(cmd.equals("member login")) {
//				if(islogined()) {
//					System.out.println("로그아웃 후 이용해주세요.");
//					continue;
//				}
//				System.out.printf("아이디 : ");
//				String loginId = sc.nextLine();
//				System.out.printf("비밀번호 : ");
//				String loginPw = sc.nextLine();
//				
//				Member member = getMemberByLoginId(loginId);
//				
//				if(member==null) {
//					System.out.println("아이디 또는 비밀번호를 확인해주세요.");
//					continue;
//				}
//				
//				if(member.loginPw.equals(loginPw)==false) {
//					System.out.println("아이디 또는 비밀번호를 확인해주세요.");
//					continue;
//				}
//				System.out.printf("로그인 성공!, %s님 반갑습니다.\n", member.name);
//				loginedMember = member;
//			}
//			else if(cmd.equals("member logout")) {
//				if(islogined()==false) {
//					System.out.println("로그인 후 이용해주세요.");
//					continue;
//				}
//				System.out.println(loginedMember.name + "님 로그아웃 되었습니다.");
//				loginedMember = null;
//			}
			else {
				System.out.println("존재하지 않는 명령어입니다.");
			}
		}
		System.out.println("==프로그램 종료==");
		sc.close();
	}

//	private boolean islogined() {
//		if(loginedMember!=null) {
//			return true;
//		}
//		return false;
//	}
//
//	private boolean isJoinableLoginId(String loginId) {
//		for(Member member : members) {
//			if(member.loginId.equals(loginId)) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private Member getMemberByLoginId(String loginId) {
//		for(Member member : members) {
//			if(member.loginId.equals(loginId)) {
//				return member;
//			}
//		}
//		return null;
//	}

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
