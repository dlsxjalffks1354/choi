
public class StringUtil {
	public String requestSplit(String request, int index) {
		String[] result = request.split("/");
		return result[index];
	}

}
