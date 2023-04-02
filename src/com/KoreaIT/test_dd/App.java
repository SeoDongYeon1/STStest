package com.KoreaIT.test_dd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.test_dd.dto.Article;
import com.KoreaIT.test_dd.dto.Member;
import com.KoreaIT.test_dd.util.Util;

public class App {
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
				
				System.out.printf("%d번 게시글이 생성되었습니다.\n", article.id);
				lastArticleId++;
			}
			
			else if(cmd.equals("article list")) {
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				// Sql 구문을 실행시키는 기능을 갖는 객체를 pstmt라는 이름으로 생성
				
				List<Article> articles = new ArrayList<>();
				// 1. 드라이버 연결
				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "1234");
					// url은 DB에 연결할 때 찾는 주소를 적는 곳
					System.out.println("연결 성공!");

					String sql = "SELECT * FROM article";
					sql += " ORDER BY id DESC;";
					// sql 쿼리문 작성

					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);
					// conn.prepareStatement(sql)은 sql문을 전송한다는 뜻

					pstmt = conn.prepareStatement(sql);

					rs = pstmt.executeQuery(sql);
					// executeQuery()는 조회문(select, show 등)을 실행할 목적으로 사용한다.

					while (rs.next()) { // while (rs.next())는 다음 데이터가 없을때 까지 반복
						int id = rs.getInt("id");
						String regDate = rs.getString("regDate");
						String updateDate = rs.getString("updateDate");
						String title = rs.getString("title");
						String body = rs.getString("body");
						int memberId = rs.getInt("memberId");

						Article article = new Article(id, regDate, updateDate, title, body, memberId);
						articles.add(article);
					}
					
				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러 : " + e);
				} finally {
					try {
						if (rs != null && !rs.isClosed()) {
							rs.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
				if(articles.size()==0) {
					System.out.println("게시글이 존재하지 않습니다.");
					continue;
				}
				
				System.out.println("번호  |  제목  | 조회수 | 작성자");
				for(Article article : articles) {
					System.out.printf("%3d  | %s   | %2d  | %d\n", article.id, article.title, article.hit, article.memberId);
				}
			}
			

			else if(cmd.startsWith("article modify")) {
				String[] cmdBits = cmd.split(" ");
				
				if(cmdBits.length < 3) {
					System.out.println("articel modify (숫자)로 입력해주세요.");
					continue;
				}
				
				int id = Integer.parseInt(cmdBits[2]);
				
				System.out.printf("새 제목 : ");
				String title = sc.nextLine();
				System.out.printf("새 내용 : ");
				String body = sc.nextLine();
				
				Connection conn = null;
				PreparedStatement pstmt = null;
				// Sql 구문을 실행시키는 기능을 갖는 객체를 pstmt라는 이름으로 생성
				
				// 1. 드라이버 연결
				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "1234");
					// url은 DB에 연결할 때 찾는 주소를 적는 곳

					String sql = "UPDATE article ";
					sql += "SET updateDate = NOW(), title = '" + title + "', `body` = '"+ body + "'";
					sql += " WHERE id = "+id + ";";
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
				
				System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
			}
			else {
				System.out.println("존재하지 않는 명령어입니다.");
			}
		}
		System.out.println("==프로그램 종료==");
		sc.close();
	}

}
