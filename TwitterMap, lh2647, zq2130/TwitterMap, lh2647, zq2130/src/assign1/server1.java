package assign1;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class server
 */
public class server1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public server1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		response.setContentType("application/json"); 
		String keyword=request.getParameter("filter");
		List<record> result=new ArrayList<record>();
		simpledb1 db=new simpledb1();
		try {
			result=db.query(keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(result);
		out.println(ans);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		response.setContentType("application/json"); 
		String keyword=request.getParameter("filter");
		if (keyword.contains("filter"))
			keyword=keyword.substring(7);
		List<record> result=new ArrayList<record>();
		simpledb1 db=new simpledb1();
		try {
			result=db.query(keyword);
			System.out.println(result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(result.size());
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(result);
		out.println(ans);
		out.flush();
	}

}
