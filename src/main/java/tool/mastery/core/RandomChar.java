package tool.mastery.core;

import java.util.Random;

/**
 * 生成随机字符串 数字+字母
 * @author Administrator
 *
 */
public class RandomChar {

    public static String getRandomString(int length) { 
    	// 若是为0 则默认为8
    	if(length == 0) {
    		length = 8;//length表示生成字符串的长度
    	}
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
        Random random = new Random();     
        StringBuffer sb = new StringBuffer();     
        for (int i = 0; i < length; i++) {     
            int number = random.nextInt(base.length());     
            sb.append(base.charAt(number));     
        }     
        return sb.toString();     
     }   
    
    public static void main(String[] args) {
		System.out.println(getRandomString(3));
	}
}
