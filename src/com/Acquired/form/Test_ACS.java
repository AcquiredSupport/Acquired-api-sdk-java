package com.Acquired.form;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Acquired.helper.AQPay;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class Test_ACS
 */
@WebServlet("/Test_ACS")
public class Test_ACS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test_ACS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());				
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		/*
	     * Use to test 3-D secure process.
	     * "pareq","ACS_url" should be from Acquired response.
	     * "md" need to be encrypted.
	     */
		
		String basePath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		String resultstr = "{\"mid_id\":\"1014\",\"mid_pass\":\"test\",\"transaction_id\":\"101750\","
				+ "\"merchant_order_id\":\"20171128091112\","
				+ "\"amount\":\"1\","
				+ "\"currency_code_iso3\":\"CBA\","
				+ "\"transaction_type\":\"AUTH_ONLY\","
				+ "\"response_code\":\"501\","
				+ "\"tds\":{\"pareq\":\"testpareq\",\"url\":\"" + basePath + "Acs_servers\"}}";
		JsonObject result = new JsonParser().parse(resultstr).getAsJsonObject();
		String tds_action = "ENQUIRE";
		
		try {
			if(tds_action.equals("ENQUIRE") && result.get("tds") != null) {
				
				JsonObject tdsobj = result.get("tds").getAsJsonObject();
				
				String[] response_code_array = {"501","502"};
				if(Arrays.asList(response_code_array).contains(result.get("response_code").getAsString())) {
					AQPay aqpay = new AQPay();
					aqpay.setParam("pareq", tdsobj.get("pareq").getAsString());//should be Acquired response
					aqpay.setParam("ACS_url", tdsobj.get("url").getAsString());//should be Acquired response
					String termurl = basePath + "Acs_notify";
					aqpay.setParam("termurl", termurl);
										
					JsonObject md = new JsonObject();
					md.addProperty("mid_id", "1014");
					md.addProperty("mid_pass", "test");
					md.addProperty("transaction_id", result.get("transaction_id").getAsString());
					md.addProperty("merchant_order_id", result.get("merchant_order_id").getAsString());
					md.addProperty("amount", result.get("amount").getAsString());
					md.addProperty("currency_code_iso3", result.get("currency_code_iso3").getAsString());
					md.addProperty("transaction_type", result.get("transaction_type").getAsString());
					String mdstr = Base64.getEncoder().encodeToString(md.toString().getBytes("utf-8"));//"md" need encrypt
					aqpay.setParam("md", mdstr);										
					
					String postResult = aqpay.postToACS();
					System.out.println("Post ACS Result: " + postResult);
					
				}					
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
