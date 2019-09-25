import java.util.*;
import java.util.regex.*;

public class Demo {
	public static void main (String[] args) {
		String str="123456我是JAVA{, ~!@asd";
 		String rex="^";
 
 		Pattern pattern=Pattern.compile(rex);
 		Matcher matcher=pattern.matcher(str);
 
 		String [] result=pattern.split(str); 
 
 		for(String string:result) {
 			System.out.println("分割的字符串:"+"["+string+"]");
 		}
	}
}

/*

*/