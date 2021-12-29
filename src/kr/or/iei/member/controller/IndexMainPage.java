package kr.or.iei.member.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.notice.model.service.NoticeCampaignService;
import kr.or.iei.notice.model.service.NoticeCampaignServiceImpl;
import kr.or.iei.notice.model.service.NoticeService;
import kr.or.iei.notice.model.service.NoticeServiceImpl;
import kr.or.iei.notice.model.vo.Notice;
import kr.or.iei.notice.model.vo.NoticeCampaign;

/**
 * Servlet implementation class IndexMainPage
 */
@WebServlet("/indexMainPage.do")
public class IndexMainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexMainPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		NoticeService nService = new NoticeServiceImpl();
		ArrayList<Notice> noticeList = nService.showNotice();
		
		NoticeCampaignService ncService = new NoticeCampaignServiceImpl();
		ArrayList<NoticeCampaign> noticeCampaignList = ncService.showNoticeCampaign();
		
		RequestDispatcher view = request.getRequestDispatcher("/index.jsp");
		
		request.setAttribute("noticeList", noticeList);
		request.setAttribute("noticeCampaignList", noticeCampaignList);
		
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
