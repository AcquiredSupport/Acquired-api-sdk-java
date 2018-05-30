package com.Acquired.form;

import java.io.IOException;
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
 * Servlet implementation class Acs_notify
 */
@WebServlet("/Acs_notify")
public class Acs_notify extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Acs_notify() {
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
		String pares = request.getParameter("PaRes");
		String md = request.getParameter("md");
		System.out.println("Received the ACS servers response");		
		
		try {
			if(!pares.isEmpty() && pares != null) {
				
				AQPay auth = new AQPay();
				auth.setParam("pares", pares);
				//"md" need decrypt
				byte[] asBytes = Base64.getDecoder().decode(md);
				String mdstr = new String(asBytes, "utf-8");
				JsonObject mdjson = new JsonParser().parse(mdstr).getAsJsonObject();
				
				//set basic data
				auth.setParam("request_url", AQPayConfig.REQUESTURL);
				auth.setParam("company_id", AQPayConfig.COMPANYID);
				auth.setParam("company_pass", AQPayConfig.COMPANYPASS);
				auth.setParam("company_mid_id", AQPayConfig.COMPANYMIDID);
				auth.setParam("hash_code", AQPayConfig.HASHCODE);
												
				auth.setParam("original_transaction_id", mdjson.get("original_transaction_id").getAsString());
				auth.setParam("merchant_order_id", mdjson.get("merchant_order_id").getAsString());
				auth.setParam("amount", mdjson.get("amount").getAsString());
				auth.setParam("currency_code_iso3", mdjson.get("currency_code_iso3").getAsString());
				auth.setParam("transaction_type", mdjson.get("transaction_type").getAsString());
				JsonObject result = auth.postSettleACS();
				
				System.out.println("timestamp: " + result.get("timestamp"));
				System.out.println("response_code: " + result.get("response_code"));
				System.out.println("response_message: " + result.get("response_message"));
				System.out.println("transaction_id: " + result.get("transaction_id"));
				
				if(auth.isSignatureValid(result, AQPayConfig.HASHCODE)) {
					
					System.out.println("SUCCESS: Request sucess [postSettleACS]");
					
				}else {
					System.out.println("ERROR: Invalid response [postSettleACS]");					
				}
								
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
