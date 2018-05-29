package com.Acquired.form;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Acquired.helper.AQPay;
import com.google.gson.JsonObject;


/**
 * Servlet implementation class Auth_
 */
@WebServlet("/Auth_")
public class Auth_ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Auth_() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());	
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 *	"timestamp" has been set on AQPay.java
		 *	"request_hash" has been set on AQPay.java
		 *	"company_id" has been set on AQPayConfig.java
		 *	"company_pass" has been set on AQPayConfig.java 
		 *	
		 *	How to use
		 *	step 1: Check customer post data.  (need you to do)
		 *	step 2: Set parameters by use setParam().
		 *	step 3: Post parameters like AQPay.capture().
		 *	step 4: Check response hash by use AQPay.isSignatureValid(result).
		 *	step 5: Do your business according to the result. (need you to do)
		 *
		 *	Use 3-D secure
		 *	1. setParam['action'] = "ENQUIRE".
		 *	2. post to Acquired with AUTH_ONLY or AUTH_CAPTURE request.
		 *	3. set pareq,ACS_url,termurl,md after Acquired response.
		 *	4. post to ACS by use AQPay.postToACS().
		 *	5. The termurl will receive the ACS post.Please see acs_notify.php.
		 *	6. post SETTLEMENT to Acquired on acs_notify.php.
		 *
		 */
				
		/*====== step 1: Check customer post data ======*/
		request.setCharacterEncoding("UTF-8");		
		String amount = request.getParameter("amount");
		String transaction_type = request.getParameter("transaction_type");
		switch (transaction_type) {
	        case "2":transaction_type = "AUTH_CAPTURE";break;
	        default: transaction_type = "AUTH_ONLY";break;
	    }
		Date d = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHms");		
		String merchant_order_id = ft.format(d) + (int)(Math.random()*1000);
		String subscription_type = request.getParameter("subscription");
		switch (subscription_type) {
	        case "1":subscription_type = "INIT";break;
	        default: subscription_type = "";break;
	    }
		String tds_action = request.getParameter("tds");
		switch (tds_action) {
	        case "1":tds_action = "ENQUIRE";break;
	        default: tds_action = "";break;
	    }
		String currency = request.getParameter("currency");
		String title = request.getParameter("title");
		String fname = request.getParameter("fname");
		String mname = request.getParameter("mname");
		String lname = request.getParameter("lname");
		String gender = request.getParameter("gender");
		String dob = request.getParameter("dob");		
		String customer_ipaddress = "127.0.0.1";
		String company = request.getParameter("company");
		String name = request.getParameter("name");
		String number = request.getParameter("number");
		String type = request.getParameter("type");
		String cvv = request.getParameter("cvv");
		String cardexp = request.getParameter("cardexp");
		String address = request.getParameter("address");
		String address2 = request.getParameter("address2");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zipcode = request.getParameter("zipcode");
		String iso2 = request.getParameter("iso2");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
							
		try {
			/*====== step 2: Set parameters ======*/
			AQPay aqpay = new AQPay();			
			aqpay.setParam("vt", "");
			aqpay.setParam("useragent", "");
			//set transaction data
			aqpay.setParam("merchant_order_id", merchant_order_id);
			aqpay.setParam("transaction_type", transaction_type);
			aqpay.setParam("subscription_type", subscription_type);
			aqpay.setParam("amount", amount);
			aqpay.setParam("currency_code_iso3", currency);
			aqpay.setParam("merchant_customer_id", "C101");
			aqpay.setParam("merchant_custom_1", "C1");
			aqpay.setParam("merchant_custom_2", "C2");
			aqpay.setParam("merchant_custom_3", "C3");
			//set customer data
			aqpay.setParam("customer_title", title);
			aqpay.setParam("customer_fname", fname);
			aqpay.setParam("customer_mname", mname);
			aqpay.setParam("customer_lname", lname);
			aqpay.setParam("customer_gender", gender);
			aqpay.setParam("customer_dob", dob);
			aqpay.setParam("customer_ipaddress", customer_ipaddress);
			aqpay.setParam("customer_company", company);
			//set billing data
			aqpay.setParam("cardholder_name", name);
			aqpay.setParam("cardnumber", number);
			aqpay.setParam("card_type", type);
			aqpay.setParam("cardcvv", cvv);
			aqpay.setParam("cardexp", cardexp);
			aqpay.setParam("billing_street", address);
			aqpay.setParam("billing_street2", address2);
			aqpay.setParam("billing_city", city);
			aqpay.setParam("billing_state", state);
			aqpay.setParam("billing_zipcode", zipcode);
			aqpay.setParam("billing_country_code_iso2", iso2);
			aqpay.setParam("billing_phone", phone);
			aqpay.setParam("billing_email", email);
			//set billing data
			aqpay.setParam("action", tds_action);
			
			/*====== step 3: Post parameters ======*/
			JsonObject result = aqpay.auth_();
			
			System.out.println("timestamp: " + result.get("timestamp"));
			System.out.println("response_code: " + result.get("response_code"));
			System.out.println("response_message: " + result.get("response_message"));
			System.out.println("transaction_id: " + result.get("transaction_id"));
			
			/*====== step 4: Check response hash ======*/
			if(aqpay.isSignatureValid(result)) {
				
				/*====== step 5: Perform actions based on the result ======*/
				System.out.println("SUCCESS: Request sucess");
				response.getWriter().append("SUCCESS: Request sucess");				
				
				/*====== deal 3-D secure ======*/
				if(tds_action.equals("ENQUIRE") && result.get("tds") != null) {										
					
					JsonObject tdsobj = result.get("tds").getAsJsonObject();
					
					String[] response_code_array = {"501","502"};
					if(Arrays.asList(response_code_array).contains(result.get("response_code").getAsString())) {
						aqpay.setParam("pareq", tdsobj.get("pareq").getAsString());
						aqpay.setParam("ACS_url", tdsobj.get("url").getAsString());
						
						String termurl = "http://youdomain.com/acs_notify";
						aqpay.setParam("termurl", termurl);
						
						/*
	                     *  Set MD field 
	                     *  This field will required for the subsequent SETTLEMENT request.   
	                     *  if you store these param in your sqlserver then you can just set a id
	                        And you can read these param from you sqlserver when post SETTLEMENT request.
	                     */
						JsonObject md = new JsonObject();
						md.addProperty("company_id", result.get("company_id").getAsString());
						md.addProperty("original_transaction_id", result.get("transaction_id").getAsString());
						md.addProperty("merchant_order_id", result.get("merchant_order_id").getAsString());
						md.addProperty("amount", result.get("amount").getAsString());
						md.addProperty("currency_code_iso3", result.get("currency_code_iso3").getAsString());
						md.addProperty("transaction_type", result.get("transaction_type").getAsString());
						
						//"md" must be encrypted and Base64 encoded.
						String mdstr = Base64.getEncoder().encodeToString(md.toString().getBytes("utf-8"));
						aqpay.setParam("md", mdstr);
						
						request.setAttribute("url", tdsobj.get("url").getAsString());
						request.setAttribute("pareq", tdsobj.get("pareq").getAsString());
						request.setAttribute("termurl", termurl);
						request.setAttribute("md", mdstr);
						request.getRequestDispatcher("/examples/acs_submit.jsp").forward(request, response);												
						
					}					
					
				}
				
			}else {
				System.out.println("ERROR: Invalid response");
				response.getWriter().append("ERROR: Invalid response");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
