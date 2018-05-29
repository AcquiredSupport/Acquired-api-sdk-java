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
 * Servlet implementation class Rebill
 */
@WebServlet("/Rebill")
public class Rebill extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Rebill() {
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
		String transaction_type = request.getParameter("transaction_type");
		String amount = request.getParameter("amount");
		String currency_code_iso3 = request.getParameter("currency");
		String original_transaction_id = request.getParameter("original_transaction_id");
		switch (transaction_type) {
	        case "2":transaction_type = "AUTH_CAPTURE";break;
	        default: transaction_type = "AUTH_ONLY";break;
	    }
		Date d = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHms");		
		String merchant_order_id = ft.format(d) + (int)(Math.random()*1000);
							
		try {
			AQPay aqpay = new AQPay();
			aqpay.setParam("transaction_type", transaction_type);
			aqpay.setParam("merchant_order_id", merchant_order_id);
			aqpay.setParam("amount", amount);
			aqpay.setParam("currency_code_iso3", currency_code_iso3);
			aqpay.setParam("original_transaction_id", original_transaction_id);							
			JsonObject result = aqpay.rebill();
			
			System.out.println("timestamp: " + result.get("timestamp"));
			System.out.println("response_code: " + result.get("response_code"));
			System.out.println("response_message: " + result.get("response_message"));
			System.out.println("transaction_id: " + result.get("transaction_id"));
			
			// Perform actions based on the result
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
