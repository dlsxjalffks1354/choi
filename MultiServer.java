import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {

   ServerSocket serverSocket = null;
   Socket socket = null;
   Map<String, PrintWriter> clientMap;
   StringUtil su = new StringUtil();
	Database db = new Database();

   // 생성자
   public MultiServer() {
       // 클라이언트의 출력스트림을 저장할 해쉬맵 생성.
       clientMap = new HashMap<String, PrintWriter>();
       // 해쉬맵 동기화 설정.
       Collections.synchronizedMap(clientMap);
   }

   public void init() {

       try {
           serverSocket = new ServerSocket(9999); // 9999포트로 서버소켓 객체생성.
           System.out.println("서버가 시작되었습니다.");


           while (true) {
               socket = serverSocket.accept();
               System.out.println(socket.getInetAddress() + ":" + socket.getPort());
               Thread mst = new MultiServerT(socket); // 쓰렛드 생성.
               mst.start(); // 쓰레드 시동.
           }
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           try {
               serverSocket.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }

   // 접속자 리스트 보내기
   public void list(PrintWriter out) {
       //  출력스트림을 순차적으로 얻어와서 해당 메시지를 출력한다.
       Iterator<String> it =clientMap.keySet().iterator();
       String msg = "사용자 리스트 [";
       while (it.hasNext()) {
           msg += (String)it.next() + ",";
       }
       msg = msg.substring(0, msg.length() -1 ) + "]";
       try {
			out.println(URLEncoder.encode(msg, "UTF-8"));
		} catch (Exception e) {

		}

	}

   // 접속된 모든 클라이언트들에게 메시지를 전달.
   public void sendAllMsg(String user, String msg) {

       // 출력스트림을 순차적으로 얻어와서 해당 메시지를 출력한다.
       Iterator it = clientMap.keySet().iterator();

       while (it.hasNext()) {
           try {
               PrintWriter it_out = (PrintWriter) clientMap.get(it.next());
               if (user.equals(""))
					it_out.println(URLEncoder.encode(msg, "UTF-8"));
               //it_out.println(msg);
				else
					it_out.println("[" + URLEncoder.encode(user, "UTF-8") + "]" + URLEncoder.encode(msg, "UTF-8"));
               	//it_out.println("["+user+"]"+msg);
           } catch (Exception e) {
               System.out.println("예외:" + e);
           }
       }
   }

   public static void main(String[] args) {
       //서버객체 생성.
	  MultiServer ms= new MultiServer();
	  ms.db.connectDatabase();
	   ms.init();
	   
   }


   /////////////////////////////////////////////////////////////////
   // 내부클래스
   // 클라이언트로부터 읽어온 메시지를 다른 클라이언트(socket)에 보내는 역활을 하는 메서드

   class MultiServerT extends Thread {
       Socket socket;
       PrintWriter out = null;
       BufferedReader in = null;

       // 생성자
       public MultiServerT(Socket socket) {
           this.socket = socket;
           try {
        	   in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				out = new PrintWriter(socket.getOutputStream(), true);
           } catch (Exception e) {
               System.out.println("에외:" + e);
           }
       }

       //쓰레드를 사용하기 위해서 run()메서드 재정의
       @Override
       public void run() {

           //String name = ""; //클라이언트로부터 받은이름을 저장할 변수.
           try {
				String request;
				while(in != null) {
					request = URLDecoder.decode(in.readLine(),"UTF-8");
					if(su.requestSplit(request, 0).equals("signup")) {
						db.signup(su.requestSplit(request, 1), su.requestSplit(request, 2));//,
							//	su.requestSplit(request, 3), su.requestSplit(request, 4));
						out.println(URLEncoder.encode("signup/회원가입완료", "UTF-8"));
					}
				}		

//                System.out.println("Bye....");
           } catch (Exception e) {
               System.out.println("예외:" + e);
           } finally {
               // 예외가 발생할 때 퇴장. 해쉬맵에서 해당 데이터 제거.
               // 보통 종료하거나 나가면 java.net.SocketException: 예외발생
              // clientMap.remove(name);
              // sendAllMsg("",name + "님이 퇴장하셨습니다.");
               //System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");

               try {
                   in.close();
                   out.close();
                   socket.close();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
   }

}


