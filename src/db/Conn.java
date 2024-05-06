package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conn {

	private Connection con;
	
	public Connection getConnection() {
		return con;
	}

	public void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace(); // 또는 다른 예외 처리를 수행할 수 있음
			}
		}
	}

	public Conn() throws ClassNotFoundException, SQLException {
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 메모리로딩 선언
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
		} catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // 또는 다른 예외 처리를 수행할 수 있음
        }
	
	}
}
