import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class Database {

	Connection con = null;
	PreparedStatement pstmt = null;
	String sql = null;
	ResultSet rs = null;
	
	public void signup(String id, String password) {
		try {
			sql = "insert into users (id, password) values (?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			//pstmt.setString(3, email);
			//pstmt.setString(4, nickname);
			pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("중복된 ID가 있습니다.");
		} catch (SQLException sqle) {
			System.out.println("데이터베이스 입력오류입니다.");
		} 

	}
	
	public void connectDatabase() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
		} catch (SQLException sqle) {
			System.out.println("Connection Error");
		}
	}
}

