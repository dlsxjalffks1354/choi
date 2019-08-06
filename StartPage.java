import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class StartPage 
{
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
			}catch(ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
				}
			}
			Connection con;
			PreparedStatement pstmt1;//회원가입 페이지
			PreparedStatement pstmt2;//아아디찾기 페이지
			PreparedStatement pstmt3;//비밀번호 찾기 페이지
			PreparedStatement pstmt4;//회원탈퇴 페이지
			PreparedStatement pstmt5;//로그인 페이지
			PreparedStatement pstmt6;//로그인 후 비밀번호 변경
			PreparedStatement pstmt7;//본인 프로필 확인
			
			Scanner sc = new Scanner(System.in);
			
			public static void showMenu() //메뉴 화면 
			{
			System.out.println("[메뉴 선택]");
			System.out.println("1.회원 가입");
			System.out.println("2.로그인");
			System.out.println("3.아이디 찾기");
			System.out.println("4.비밀번호 찾기");
			System.out.println("5.종료");
			System.out.print("선택 : ");
			}
			
			public static void showMenu2()//로그인 메뉴화면
			{
				System.out.println("[메뉴 선택]");
				System.out.println("1.서버입장");
				System.out.println("2.패스워드 변경");
				System.out.println("3.회원 탈퇴");
				System.out.println("4.본인의 프로필 확인");
				System.out.println("5.로그아웃");
				System.out.print("선택 : ");
			}
			
			public void dorun(String[] args) 
			{
				boolean a=true;
				connectDatabase();
				int choice;
				
				while(true) {
					showMenu();
					choice=sc.nextInt();
					sc.nextLine();
					switch(choice) {
					case 1:
						Signup();		
						break;
					case 2:
						Login(args);
						break;
					case 3:
						SeekId();
						break;
					case 4:
						SeekPass();
						break;
					case 5:
						System.out.println("프로그램을 종료합니다.");
						return;
					default:
						System.out.println("잘 못 입력하셨습니다.");
						break;
					}
				}
			}
			
			
			public void Signup() 
			{
				System.out.print("회원 가입 페이지 입니다.\n");
				System.out.print("만드실 아이디를 입력해주세요 : ");
				String id = sc.nextLine();
				System.out.print("만드실 비밀번호를 입력해주세요 : ");
				String password = sc.nextLine();
				System.out.print("사용중인 Email을 입력해주세요 : ");
				String email = sc.nextLine();
				System.out.print("사용하실 닉네임을 입력해주세요 : ");
				String nickname = sc.nextLine();
				int blacklist=0;
				String sql = "insert into join values(?,?,?,?,?)";
				
				
				try {
					pstmt1= con.prepareStatement(sql);
					pstmt1.setString(1, id);
					pstmt1.setString(2, password);
					pstmt1.setString(3, email);
					pstmt1.setString(4, nickname);
					pstmt1.setLong(5, blacklist);
					int updateCount = pstmt1.executeUpdate();
					System.out.println("회원가입이 완료되었습니다.");
				}catch (Exception e) 
				{
					System.out.println("입력 에러입니다.");
				}
			}
				
			public void SeekId() 
			{
				System.out.print("ID찾기 페이지입니다. ");
				System.out.print("이메일을 입력해주세요 : ");
				String email = sc.nextLine();
				String sql = "select id from join where email = ?";
				try {
					pstmt2=con.prepareStatement(sql);
					pstmt2.setString(1, email);
					ResultSet rs = pstmt2.executeQuery();
					if(rs.next()) 
					{
						System.out.println("찾으시는 ID는"+" : " + rs.getString(1)+ " 입니다");
					}else {
						System.out.println("해당 값이 없습니다");
					}
					rs.close();
				}catch(Exception e) {
					System.out.println("알 수 없는 에러가 발생했습니다.");
				}
			}
			
			
			public void SeekPass() 
			{
				System.out.print("아이디를 입력해주세요. : ");
				String id = sc.nextLine();
				System.out.print("이메일을 입력해주세요 : ");
				String email = sc.nextLine();
				String sql = "select password from join where id = ? and email= ? ";
				try {
					pstmt3=con.prepareStatement(sql);
					pstmt3.setString(1, id);
					pstmt3.setString(2, email);
					ResultSet rs = pstmt3.executeQuery();
					if(rs.next()) 
					{
						System.out.println(id+"의 패스워드는 "+" : " + rs.getString(1)+ " 입니다");
					}else {
						System.out.println("해당 값이 없습니다");
					}
					rs.close();
				}catch(Exception e) {
					System.out.println("알 수 없는 에러가 발생했습니다.");
				}
			}
			
			public void	Withdrawal()
			{
				System.out.print("아이디를 입력해주세요. : ");
				String id = sc.nextLine();
				System.out.print("패스워드을 입력해주세요 : ");
				String password = sc.nextLine();
				System.out.print("이메일을 입력해주세요 : ");
				String email = sc.nextLine();
				
				String sql = "delete from join where id = ? and password= ? and email= ?";
				try {
					pstmt4=con.prepareStatement(sql);
					pstmt4.setString(1, id);
					pstmt4.setString(2, password);
					pstmt4.setString(3, email);
					int updateCount = pstmt4.executeUpdate();
					
					System.out.println("회원 탈퇴되셨습니다. 다음에 또 이용해주세요 ");
					
				}catch(Exception e) {
					System.out.println("데이터베이스 삭제 에러입니다.");
				}
			}
			public void	goServer(String[] args) throws UnknownHostException,IOException   
			{ 
				MultiClient mc= new MultiClient(args);
				mc.main(args);
			}	
			public void repass()
			{
				System.out.print("현재 사용중인 비밀번호를 입력해주세요 ");
				String password = sc.nextLine();
				System.out.print("앞으로 사용하고자 할 비밀번호를 입력해주세요 ");
				String password2 = sc.nextLine();
				String sql = "update join set password = replace(password,?,?) where password like ?";
				try {
					pstmt7=con.prepareStatement(sql);
					pstmt7.setString(1, password);
					pstmt7.setString(2, password2);
					pstmt7.setString(3, password);
					int updateCount = pstmt7.executeUpdate();
					
					System.out.println("비밀번호가 변경되었습니다.");
					}catch(Exception e) {
						System.out.println("알 수 없는 에러가 발생했습니다.");
					}
			}
			
			public void Profile()
			{
				
			}
			public void Login(String[] args) 
			{
				boolean a=true;
				int choice;
				System.out.print("아이디를 입력해주세요. : ");
				String id = sc.nextLine();
				System.out.print("패스워드을 입력해주세요 : ");
				String password = sc.nextLine();
				String sql = "select * from join where id =? and password= ?";
				try {
					pstmt5=con.prepareStatement(sql);
					pstmt5.setString(1, id);
					pstmt5.setString(2, password);
					ResultSet rs = pstmt5.executeQuery();
					if(rs.next()) 
					{
						System.out.println(id+"님 환영합니다.");
						showMenu2();
						choice=sc.nextInt();
						sc.nextLine();
						while (a=true) 
						switch(choice)
						{
						case 1:
							goServer(args);
							a=false;
							break;
						case 2:
							repass();
							break;
						case 3:
							Withdrawal();
							break;
						case 4:
							Profile();
							break;
						case 5:
							System.out.println("메인 메뉴로 이동합니다.");
							return;
						default:
							System.out.println("잘못 입력하셨습니다.");
							break;
						}
					}else {
						System.out.println("해당 ID는 존재하지 않습니다.");
					}
					rs.close();
				}catch(Exception e) {
					System.out.println("알 수 없는 에러가 발생했습니다.");
				}
			}
			

			public void connectDatabase() 
			{
					try {
							con = DriverManager.getConnection(
										"jdbc:oracle:thin:@localhost:1521:xe",
										"scott",
										"tiger");
						}
					catch(Exception e) 
					{
						e.printStackTrace();
					}
			}	

}
