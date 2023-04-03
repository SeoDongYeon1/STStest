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
import com.KoreaIT.test_dd.util.DBUtil;
import com.KoreaIT.test_dd.util.SecSql;

public class App {
	static List<Member> members = new ArrayList<>();
	static Member loginedMember = null;

	public void start() {
		System.out.println("==프로그램 시작==");
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.printf("명령어 > ");
			String cmd = sc.nextLine().trim();

			Connection conn = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("드라이버 로딩 실패");
			}

			try {
				String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

				conn = DriverManager.getConnection(url, "root", "1234");

				int actionResult = doAction(conn, sc, cmd);

				if (actionResult == -1) {
					System.out.println("==프로그램 종료==");
					break;
				}

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
			}
		}
	}

	private int doAction(Connection conn, Scanner sc, String cmd) {
		if (cmd.length() == 0) {
			System.out.println("명령어를 입력해주세요.");
			return 0;
		}
		
		if (cmd.equals("exit")) {
			return -1;
		}
		
		if(cmd.equals("article write")) {
			System.out.println("==게시물 작성==");
			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();
			
			SecSql sql = new SecSql();
			
			sql.append("INSERT INTO article");
			sql.append(" SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", `body` = ?", body);
		
			int id = DBUtil.insert(conn, sql);
			System.out.printf("%d번 게시글이 생성되었습니다.\n", id);
		}
		
		else if(cmd.equals("article list")) {
			System.out.println("==게시물 목록==");
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			List<Article> articles = new ArrayList<>();
			
			try {
				String sql = "SELECT * FROM article";
				sql += " ORDER BY id DESC;";

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);

				rs = pstmt.executeQuery(sql);

				while (rs.next()) { // while (rs.next())는 다음 데이터가 없을때 까지 반복
					int id = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");

					Article article = new Article(id, regDate, updateDate, title, body);
					articles.add(article);
				}
				
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
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(articles.size()==0) {
				System.out.println("게시글이 존재하지 않습니다.");
				return 0;
			}
			
			System.out.println("번호  |  제목  | 조회수 ");
			for(Article article : articles) {
				System.out.printf("%3d  | %s   | %2d \n", article.id, article.title, article.hit);
			}
		}
		

		else if(cmd.startsWith("article modify")) {
			System.out.println("==게시물 수정==");
			String[] cmdBits = cmd.split(" ");
			
			if(cmdBits.length < 3) {
				System.out.println("articel modify (숫자)로 입력해주세요.");
				return 0;
			}
			
			int id = Integer.parseInt(cmdBits[2]);
			
			System.out.printf("새 제목 : ");
			String title = sc.nextLine();
			System.out.printf("새 내용 : ");
			String body = sc.nextLine();
			
			PreparedStatement pstmt = null;
			
			try {
				String sql = "UPDATE article ";
				sql += "SET updateDate = NOW(), title = '" + title + "', `body` = '"+ body + "'";
				sql += " WHERE id = "+id + ";";
				// sql 쿼리문 작성

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				System.out.println("에러 : " + e);
			} finally {
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
		return 0;
	}
}
