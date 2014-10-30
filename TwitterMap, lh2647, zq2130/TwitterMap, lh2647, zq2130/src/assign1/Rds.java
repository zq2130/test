package assign1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Rds {

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
 			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:cs6998.cgr7pyr447yn.us-east-1.rds.amazonaws.com:3306","huanglin","11111111");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from emp");
			while(rs.next()) {			
				System.out.print("学号：");
				System.out.println(rs.getString("ename"));
			} 
		}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(rs != null) {
						rs.close();
						rs = null;
					}
					if(stmt != null) {
						stmt.close();
						stmt = null;
					}
					if(conn != null) {
						conn.close();
						conn = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
}
