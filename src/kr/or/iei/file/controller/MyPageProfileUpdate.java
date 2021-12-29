package kr.or.iei.file.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.or.iei.file.model.service.MemberFileService;
import kr.or.iei.file.model.service.MemberFileServiceImpl;
import kr.or.iei.file.model.vo.MemberFileData;
import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.member.model.service.MemberServiceImpl;
import kr.or.iei.member.model.vo.Member;


/**
 * Servlet implementation class MyPageProfileUpdate
 */
@WebServlet("/file/profileUpdate.do")
public class MyPageProfileUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyPageProfileUpdate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 실제 업로드 되어야 하는 경로
		String uploadFilePath = request.getServletContext().getRealPath("/upload");

		// 최대 파일 사이즈 크기 설정값
		int uploadFileSizeLimit = 1024 * 1024 * 1024; //1GB // 1 = 1bite
		
		// 인코딩 처리
		String encType = "UTF-8";
		
		// 정책 관련 객체 MutipartRequest 객체를 생성 (동일한 이름의 파일이 들어올 경우)
		MultipartRequest multi = new MultipartRequest(request, uploadFilePath, uploadFileSizeLimit, encType, new DefaultFileRenamePolicy());

		
		//위에서 5가지 정보를 바탕으로 MultipartRequest 객체를 생성하게 되는 순간, 이미 파일은 해당 경로에 업로드 되는 상태가 된다.
		
		//파일 사이즈 검증
		File tmp = new File(uploadFilePath+"\\"+multi.getFilesystemName("file"));

		if(tmp.length()>(1024*1024*50)) {
			tmp.delete();
			response.sendRedirect("/views/file/fileSizeLimit.jsp");
			return;
		}
		
		
		//DB에 데이터를 저장하기 위하여 처리하는 영역
		
		// 원본 파일 이름
		String originalFileName = multi.getFilesystemName("file");
		
		// 업로드한 유저명
		String fileUser = ((Member)request.getSession().getAttribute("member")).getUserId();
		
		// 업로드한 시간 (Calendar Class)
		long currentTime = Calendar.getInstance().getTimeInMillis();
		// 포멧을 지정하여 업로드 시간 구현
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Timestamp uploadTime = Timestamp.valueOf(formatter.format(currentTime));
		
		// 변경된 파일 이름 변수
		String changedFileName = null;
		// 파일테이블에 업로드 결과값 변수
		int result = 0;
		if(originalFileName != null) {
			// 파일 이름 변경 후 변경된 파일 이름 저장
		File file = new File(uploadFilePath+"\\"+originalFileName);
		file.renameTo(new File(uploadFilePath+"\\"+fileUser+"_"+currentTime+"_jupday.png"));
		changedFileName = fileUser+"_"+currentTime+"_jupday";

		String filePath = uploadFilePath+"\\"+changedFileName;
		// 해당 파일의 경로 저장 (변경된 파일 이름)
		
		// 파일의 사이즈 (파일의 사이즈가 클 수 있으므로, long type 사용)
		File reFile = new File(filePath);
		long fileSize = reFile.length();
		
		// 위의 5가지 정보를 이용해서 객체 만들기
		MemberFileData mfd = new MemberFileData();
		mfd.setM_P_Original(originalFileName);
		mfd.setM_P_Change(changedFileName);
		mfd.setM_P_File_Upth(filePath);
		mfd.setM_P_FilePae(fileSize);
		mfd.setUserId(fileUser);
		mfd.setM_P_File_UpdateTime(uploadTime);
		
		System.out.println("originalFileName : " + originalFileName);
		
		//비즈니스로직처리
		System.out.println("1번 비즈니스로직 전");
		MemberFileService mfService = new MemberFileServiceImpl();
		result = mfService.insertFileUpdate(mfd);
		System.out.println("1번 비즈니스로직 후");
		}
		
		request.setCharacterEncoding("UTF-8");
		
		String userId = ((Member)(request.getSession()).getAttribute("member")).getUserId();
		
		// String p_image = multi.getParameter("p_image");
		String p_image = changedFileName;
		String nick = multi.getParameter("nick");
		String email = multi.getParameter("email");
		
		// Member m2 = new Member(userId, p_image, nick, email);
		Member m2 = new Member();
		m2.setUserId(userId);
		m2.setNick(nick);
		m2.setP_Image(p_image);
		m2.setEmail(email);
				System.out.println("3번 객체만들기 후");
				System.out.println("m2 : " + m2.toString());
				System.out.println("3번 비즈니스로직 전");
		MemberService mService2 = new MemberServiceImpl();              
		int result3 = mService2.updateOneMember(m2);					
		System.out.println("3번 비즈니스로직 후");
		
		
		RequestDispatcher view = request.getRequestDispatcher("/views/member/memberUpdateResult.jsp");
			result = originalFileName == null ? 1 : result;
			System.out.println("result : " + result);
			if(result > 0 && result3 > 0) {
				System.out.println("성공");
				request.setAttribute("result", true);
			} else {
				System.out.println("실패");
				request.setAttribute("result", false);
			}
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
