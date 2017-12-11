package com.Acquired.helper;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.*;

public class AQPayCommont {

	
	public String trimString(String value) throws Exception {	
		String ret = null;		
		if(value != null) {
			ret = value;
			
			if(ret.length() == 0) {
				ret = null;
			}
		}
		return ret;
	}
	
	public String now() throws Exception {
		Date d = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");
		return ft.format(d);
	}
	
	public String sha256hash(String secret) throws Exception {
		
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");

		byte[] shaByteArr = mDigest.digest(secret.getBytes(Charset.forName("UTF-8")));
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < shaByteArr.length; i++) {
	    		sb.append(String.format("%02x", shaByteArr[i]));
	    }

		return sb.toString();
		
	}
	
	public String requestHash(Map<String, String> param, String secret) throws Exception {
		
		String _id;
		String str = "";
		
		if(param.get("company_id") != null) {
			_id = param.get("company_id");
		}else {
			_id = param.get("mid_id");
		}
		
		String transaction_type = param.get("transaction_type");
		String[] transaction_type_1 = {"AUTH_ONLY","AUTH_CAPTURE","CREDIT"};
		String[] transaction_type_2 = {"CAPTURE","VOID","REFUND","SUBSCRIPTION_MANAGE"};
		
		if(Arrays.asList(transaction_type_1).contains(transaction_type)) {
			str = param.get("timestamp") + param.get("transaction_type") + _id + param.get("merchant_order_id");
		}else if(Arrays.asList(transaction_type_2).contains(transaction_type)){
			str = param.get("timestamp") + param.get("transaction_type") + _id + param.get("original_transaction_id");
		}
		
		String secstr = str + secret;
		
		return sha256hash(secstr);
		
	}
	
	public String responseHash(JsonObject param, String secret) throws Exception {
		
		String _id;
		String str = "";
		
		if(param.get("company_id") != null) {
			_id = param.get("company_id").getAsString();
		}else {
			_id = param.get("mid").getAsString();
		}
				
		str = param.get("timestamp").getAsString() + param.get("transaction_type").getAsString() + _id + param.get("transaction_id").getAsString() + param.get("response_code").getAsString();
		
		String secstr = str + secret;
		
		return sha256hash(secstr);
		
	}
	
	
	public String client_ip(HttpServletRequest request) {
		
		String remoteAddr = request.getRemoteAddr();
		String forwarded = request.getHeader("X-Forwarded-For");
		String realIp = request.getHeader("X-Real-IP");
		String ip = null;
		
		if(realIp == null) {
			if(forwarded == null) {
				ip = remoteAddr;
			}else {
				ip = remoteAddr + "/" + forwarded;
			}
		}else {
			if(realIp.equals(forwarded)) {
				ip = realIp;
			}else {
				ip = realIp + "/" + forwarded.replaceAll(", " + realIp, "");
			}
		}
		
		return ip;
	}
	
	public String http_request(String url, String data, int connectTimeout) throws Exception {
		return http_request(url, data, connectTimeout, "");
	}
	
	public String http_request(String url, String data, int connectTimeout, String type) throws Exception {
        PrintWriter out = null;
        String result = "";
                
        try {
            URL realUrl = new URL(url); 
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
                                  
            switch(type) {
            		case "json":conn.setRequestProperty("Content-type", "application/json");break;
            		default:break;
            }
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(connectTimeout*1000);
                        
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            out = new PrintWriter(conn.getOutputStream());           
            out.print(data);
            out.flush();
            
            int resultCode = conn.getResponseCode();
            if(HttpURLConnection.HTTP_OK == resultCode) {
            	
            		StringBuffer sb = new StringBuffer();
            		String readLine = new String();
            		BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            		while((readLine = responseReader.readLine()) != null) {
            			sb.append(readLine).append("\n");
            		}
            		responseReader.close();            		
            		
            		result = sb.toString();
            		
            }else {
            		result = "resultCode: " + resultCode; 
            }                        
            
        } catch (Exception e) {
            System.out.println("post error！"+e);
            e.printStackTrace();
        }
        
		return result;        
    }
	
}