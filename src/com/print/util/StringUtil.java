package com.print.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Pattern;

import android.util.Base64;

/**
 * @author STH
 *
 */
public class StringUtil {

	/**
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static int getInt(String str, int defaultValue) {
		if (str == null)
			return defaultValue;
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static double getDouble(String str, double defaultValue) {
		if (str == null)
			return defaultValue;
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static double Currency2Double(String str){
		if (str == null)
			return 0.0;
		try {
			StringBuffer sb = new StringBuffer();
			String [] temp = StringUtil.split(str, ",");
			for(int i=0;i<temp.length;i++){
				sb.append(temp[i]);
			}
			return Double.parseDouble(sb.toString());
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	public static String[] split(String original, String separator) {   
	    Vector nodes = new Vector();   
	    
	    // Parse nodes into vector   
	    int index = original.indexOf(separator);   
	    while(index>=0) {   
	        nodes.addElement( original.substring(0, index) );   
	        original = original.substring(index+separator.length());   
	        index = original.indexOf(separator);   
	    }   
	    // Get the last node   
	    nodes.addElement( original );   
	    
	    // Create splitted string array   
	    String[] result = new String[ nodes.size() ];   
	    if( nodes.size()>0 ) {   
	        for(int loop=0; loop<nodes.size(); loop++)   
	        result[loop] = (String)nodes.elementAt(loop);   
	    }   
	    return result;   
	}  
	
	public static String GetDisplayTitle(String alias, String creditCode)
    {
        StringBuffer sb = new StringBuffer();
        if (alias != null && alias.trim() != "")
        {
            sb.append("[");
            sb.append(alias.trim());
            sb.append("] ");
        }
        if (creditCode != null && creditCode.trim() != "")
        {
            if (creditCode.length() > 10)
            {
                sb.append(creditCode.substring(0, 6));
                sb.append("...");
                sb.append(creditCode.substring(creditCode.length() - 4, 4));
            }
            else
                sb.append(creditCode);
        }
        return sb.toString();
    }
	
	public static String formatAccountNo(String accountNo){
		try{
			StringBuffer s = new StringBuffer();
			for (int i=0; i<accountNo.length()-10; i++){
				s.append("*");
			}
			
			StringBuffer sb = new StringBuffer(accountNo);
			sb.replace(6, accountNo.length()-4, s.toString());
			return sb.toString();
		} catch(Exception e){
			return accountNo;
		}
	}

	public static boolean getBool(String str, boolean b) {
		return null != str && str.equalsIgnoreCase("true") ? true : false;
	}
	
	public static InputStream getInputStream(String str){
		InputStream inputStream = new ByteArrayInputStream(str.getBytes());
		return inputStream;
	}
	
	 /**
	  * 返回字节数组
	  * 
	  * @param in输入的流
	  * @return
	  * @throws Exception
	  */
	public static byte[] InputStram2byteArray(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (in != null) {
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				out.close();
				in.close();
				return out.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String inputStreamToString(InputStream stream){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 8192);   
	        StringBuilder sb = new StringBuilder();   
	        String line = null; 
	        while ((line = reader.readLine()) != null) {   
	        	if (line.trim().length() > 0){
	        		sb.append(line);  
	        	}
	         }
	        return sb.toString();
	        
		}catch(IOException e){
			e.printStackTrace();
			
		} 
		
		return "";
	}
	
	public static String Image2Base64(String imageName){
		  //对文件的操作
		  try{
			  FileInputStream in = new FileInputStream(imageName);
			  byte buffer[] = StringUtil.InputStram2byteArray(in);//把图片文件流转成byte数组
			  byte[] encode = Base64.encode(buffer,Base64.DEFAULT);//使用base64编码
			  return new String(encode);
			  
		  }catch(Exception e){
			  e.printStackTrace();
			  return "";
		  }
	}
	
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	    return pattern.matcher(str).matches(); 
	}
	
	/**
	 * 12->12.00   or 11111.1 -> 1,111.10
	 * @param str
	 * @return
	 */
	public static String formatAmount(float f){
		try{
			DecimalFormat df = new DecimalFormat("###,###.00");
			return df.format(f);
		} catch(Exception e){
			return "";
		}
	}
	
	/**
	 *  将金额转化银联要求的12位长字符串格式  12.08 -> 0000000001208
	 *  点付宝也可以直接使用
	 */
	public static String amount2String(String amount){
		String temp = amount.replace(".", "").replace(",", "");
		return String.format("%012d", Long.parseLong(temp));
	}
	
	/**
	 *  将12位长的银联格式的字符串转为金额格式 ￥ 1,200.00
	 *  
	 * @param str
	 * @return
	 */
	public static String String2SymbolAmount(String str){
		try{
			String tempStr = NumberFormat.getNumberInstance().format(Long.parseLong(str, 10)).replace(",", "");
			double temp = Long.parseLong(tempStr) / 100.00;
			return NumberFormat.getCurrencyInstance(Locale.CHINA).format(temp);
		} catch(Exception e){
			e.printStackTrace();
			return str;
		}
	}
	
	
	public static double String2AmountFloat(String str){
		try{
			String tempStr = NumberFormat.getNumberInstance().format(Long.parseLong(str, 10)).replace(",", "");
			return Long.parseLong(tempStr) / 100.00;
		} catch(Exception e){
			e.printStackTrace();
			return 0.00;
		}
	}
	
	public static String MD5Crypto(String str){
    	try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
                
        return "";
    }
    
    private static String toHexString(byte[] b) {  //String to  byte
    	char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };
    	
        StringBuilder sb = new StringBuilder(b.length * 2);  
        for (int i = 0; i < b.length; i++) {  
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
            sb.append(HEX_DIGITS[b[i] & 0x0f]);  
        }  
        return sb.toString();  
    }
	
	
}
