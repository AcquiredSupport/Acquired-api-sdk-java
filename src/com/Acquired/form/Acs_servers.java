package com.Acquired.form;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Acquired.helper.AQPayCommont;

/**
 * Servlet implementation class Acs_servers
 */
@WebServlet("/Acs_servers")
public class Acs_servers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Acs_servers() {
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
		String pareq = request.getParameter("pareq");
		String md = request.getParameter("md");
		String url = request.getParameter("termurl");
		String pares = "pares";
		
		try {
			if(!pareq.isEmpty() && pareq != null) {					
				
				String post_data = "PaRes=" + pares + "&md=" + md;
				AQPayCommont util = new AQPayCommont();
				String result = util.http_request(url, post_data, 30);
				System.out.println("Acs_servers result: " + result);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
