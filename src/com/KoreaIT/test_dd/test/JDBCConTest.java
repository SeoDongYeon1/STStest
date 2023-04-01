package com.KoreaIT.test_dd.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConTest {
	public static void main(String[] args) {
		Connection conn = null;
		
		// 1. 드라이버 연결
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
			// url은 DB에 연결할 때 찾는 주소를 적는 곳
			
			conn = DriverManager.getConnection(url, "root", "1234");
			// 연결 시도 -> url과 아이디, 비밀번호를 적는다.
			System.out.println("연결 성공!");
			
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
		}

	}
}