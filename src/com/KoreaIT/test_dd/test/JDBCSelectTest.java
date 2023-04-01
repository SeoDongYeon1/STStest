package com.KoreaIT.test_dd.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.KoreaIT.test_dd.dto.Article;

public class JDBCSelectTest {
	public static void main(String[] args) {
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

			String sql = "SELECT * FROM article;";
			// sql 쿼리문 작성

			System.out.println(sql);

			pstmt = conn.prepareStatement(sql);
			// conn.prepareStatement(sql)은 sql문을 전송한다는 뜻

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery(sql);
			// executeQuery()는 조회문(select, show 등)을 실행할 목적으로 사용한다.

			while (rs.next()) {
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
		System.out.println(articles);
	}
}
