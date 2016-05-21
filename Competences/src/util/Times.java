package util;

public class Times {

	public static int parse(String time){
		String[] split = time.split("/");
		return Integer.parseInt(split[2]+split[1]+split[0]);
	}
}
