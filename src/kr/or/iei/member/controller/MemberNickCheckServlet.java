package kr.or.iei.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.member.model.service.MemberServiceImpl;

/**
 * Servlet implementation class MemberNickCheckServlet
 */
@WebServlet("/member/nickCheck.do")
public class MemberNickCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberNickCheckServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//중복 체크를 확인할 nick 값을 받아와야 한다.
		String nick = request.getParameter("nick");
		
		MemberService mService = new MemberServiceImpl();
		boolean result= mService.selectNickCheck(nick);
		
		RequestDispatcher view = request.getRequestDispatcher("/views/member/nickCheck.jsp");
		
		request.setAttribute("nickCheck", result);
		request.setAttribute("nick", nick);
		
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
