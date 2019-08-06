import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClient 
{
	Scanner s = new Scanner(System.in);
	String ServerIP =null;
	
	
	public MultiClient(String[] args) {
		this.ServerIP = "localhost";
		if (args.length > 0)
			ServerIP = args[0]; 
	}
	public static void main(String[] args) throws UnknownHostException,IOException  
	{
		MultiClient mc=new MultiClient(args);
		try {
			mc.menu1();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Socket Connect() throws IOException {
		
		Socket socket = new Socket(ServerIP, 9999); // 소켓 객체 생성
		System.out.println("서버와 연결이 되었습니다........");
		return socket;
	}
	public void menu1() throws IOException //메뉴 화면 
	{
	System.out.println("[메뉴 선택]");
	System.out.println("1.로그인");
	System.out.println("2.회원가입");
	System.out.println("3.종료");
	System.out.print("선택 : ");
	String choice = s.nextLine();
	switch (choice) 
	{
	case "1":
		break;
	case "2":
		signup();
		break;
	case "3":
		return;
	default:
		System.out.println("잘못입력하셨습니다.");
		menu1();
		break;
	}
	}
	public void signup() throws IOException 
	{
		Socket socket = Connect();
		String id = null;
		String password = null;
		String passCheck = null;
		String email=null;
		String nickname=null;
		while (id == null) {
			System.out.println("사용할 아이디를 입력해주세요 : ");
			id = s.nextLine();
			if (id.trim().equals("")) {
				System.out.println("필수입력값입니다");
				id = null;
			}
		}
		while (password == null) {
			System.out.println("사용할 패스워드를 입력해주세요 : ");
			password = s.nextLine();
			System.out.println("패스워드 확인");
			passCheck = s.nextLine();

			if (password.equals(passCheck)) {
				if (password.trim().equals("")) {
					System.out.println("필수입력값입니다.");
					password = null;
				}
			} else {
				System.out.println("패스워드가 일치하지 않습니다.");
				password = null;
			}
		}
		/*while (email == null) {
			System.out.println("사용하는 이메일을 입력해주세요 : ");
			id = s.nextLine();
			if (email.trim().equals("")) {
				System.out.println("필수입력값입니다");
				email= null;
			}
		}
		while (nickname == null) {
			System.out.println("사용하실 닉네임을 입력해주세요 : ");
			id = s.nextLine();
			if (nickname.trim().equals("")) {
				System.out.println("필수입력값입니다");
				nickname = null;
			}
		}*/
		String request = String.format("signup/%s/%s", id, password);
		Thread sender = new Sender(socket, request);
		Thread receiver = new Receiver(socket);
		ExecutorService exr = Executors.newFixedThreadPool(2);
		exr.submit(sender);
		try {
			exr.submit(receiver).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		exr.shutdown();
		menu1();
	}
}

