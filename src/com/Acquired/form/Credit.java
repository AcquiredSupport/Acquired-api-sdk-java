package com.Acquired.form;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Acquired.helper.AQPay;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Credit
 */
@WebServlet("/Credit")
public class Credit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Credit() {
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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");		
		String amount = request.getParameter("amount");		
		Date d = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHms");		
		String merchant_order_id = ft.format(d) + (int)(Math.random()*1000);			
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
			AQPay aqpay = new AQPay();
			//set transaction data
			aqpay.setParam("merchant_order_id", merchant_order_id);
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
			
			JsonObject result = aqpay.credit();
			
			System.out.println("timestamp: " + result.get("timestamp"));
			System.out.println("response_code: " + result.get("response_code"));
			System.out.println("response_message: " + result.get("response_message"));
			System.out.println("transaction_id: " + result.get("transaction_id"));
			
			//Perform actions based on the result
			if(aqpay.isSignatureValid(result)) {
				
				System.out.println("SUCCESS: Request sucess");
				response.getWriter().append("SUCCESS: Request sucess");							
				
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
