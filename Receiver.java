import java.io.*;
import java.net.*;

//서버로 메시지를 전송하는 클래스
public class Receiver extends Thread 
{
	Socket socket;
	BufferedReader in =null;
	StringUtil su = new StringUtil();
	public Receiver(Socket socket)
	{
		this.socket = socket;
		try {
			in= new BufferedReader(new InputStreamReader(
									this.socket.getInputStream() ));
		
		}catch(IOException e) {
			System.out.println("예외:"+e);
			}
	}
	//run()메소드 재정의
	public void run() {
		String request;
		while (in!=null) {
			try {
				request = URLDecoder.decode(in.readLine(),"UTF-8");
				if(su.requestSplit(request, 0).equals("signup")) {
					System.out.println(su.requestSplit(request, 1));
					break;
				}
			}
			catch(java.net.SocketException ne) 
			{
				break;
			}catch (Exception e) {
				System.out.println("예외:"+e);
				}
				}
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("예외3:"+e);
			}	
		}
}




