package com.KoreaIT.test_dd.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCInsertTest {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// Sql 구문을 실행시키는 기능을 갖는 객체를 pstmt라는 이름으로 생성
		
		// 1. 드라이버 연결
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "1234");
			// url은 DB에 연결할 때 찾는 주소를 적는 곳
			System.out.println("연결 성공!");

			String sql = "INSERT INTO article";
			sql += " SET regDate = NOW(),";
			sql += "updateDate = NOW(),";
			sql += "title = CONCAT('제목 ',RAND()),";
			sql += "`body` = CONCAT('내용 ',RAND()),";
			sql += "memberId = 1;";
			// sql 쿼리문 작성

			System.out.println(sql);

			pstmt = conn.prepareStatement(sql);
			// conn.prepareStatement(sql)은 sql문을 전송한다는 뜻
			
			int affectedRow = pstmt.executeUpdate();
			// executeUpdate()는 조회문(select, show 등)을 제외한 create, drop, insert, delete, update 등등 문을 처리할 때 사용한다
			// 안쓰면 DB에 적용이 안됨.
			
			System.out.println(":::::::"+affectedRow);

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

	}
}