package com.Acquired.form;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Acquired.helper.AQPay;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Capture
 */
@WebServlet("/Capture")
public class Capture extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Capture() {
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
		String original_transaction_id = request.getParameter("original_transaction_id");
		String amount = request.getParameter("amount");
							
		try {
			AQPay aqpay = new AQPay();
			//set basic data
			aqpay.setParam("request_url", AQPayConfig.REQUESTURL);
			aqpay.setParam("company_id", AQPayConfig.COMPANYID);
			aqpay.setParam("company_pass", AQPayConfig.COMPANYPASS);
			aqpay.setParam("company_mid_id", AQPayConfig.COMPANYMIDID);
			aqpay.setParam("hash_code", AQPayConfig.HASHCODE);
			
			aqpay.setParam("original_transaction_id", original_transaction_id);
			aqpay.setParam("amount", amount);			
			JsonObject result = aqpay.capture();
			
			System.out.println("timestamp: " + result.get("timestamp"));
			System.out.println("response_code: " + result.get("response_code"));
			System.out.println("response_message: " + result.get("response_message"));
			System.out.println("transaction_id: " + result.get("transaction_id"));
			
			//Perform actions based on the result
			if(aqpay.isSignatureValid(result, AQPayConfig.HASHCODE)) {
				
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
