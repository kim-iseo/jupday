package kr.or.iei.admin.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.admin.notice.model.service.AdminFAQService;
import kr.or.iei.admin.notice.model.service.AdminFAQServiceImpl;
import kr.or.iei.admin.notice.model.vo.AdminFAQ;

/**
 * Servlet implementation class AdminFAQWriteServlet
 */
@WebServlet("/admin/FAQWrite.do")
public class AdminFAQWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminFAQWriteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		AdminFAQ adf = new AdminFAQ();
		adf.setFaq_Title(title);
		adf.setFaq_Content(content);
		
		AdminFAQService adfService = new AdminFAQServiceImpl();
		int result = adfService.insertFAQWrite(adf);
		int faqNo = adfService.searchFAQPostNo(adf);
		
		if(result>0)
		{
			response.sendRedirect("/admin/adminFAQSelectContent.do?faqNo="+faqNo+"&currentPage=1");
		}else
		{
			response.sendRedirect("/views/commons/error.jsp");
		}		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
