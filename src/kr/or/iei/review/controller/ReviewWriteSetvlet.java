package kr.or.iei.review.controller;

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

import kr.or.iei.file.model.service.ReviewFileService;
import kr.or.iei.file.model.service.ReviewFileServiceImpl;
import kr.or.iei.file.model.vo.ReviewFileData;
import kr.or.iei.member.model.vo.Member;
import kr.or.iei.review.model.service.ReviewService;
import kr.or.iei.review.model.service.ReviewServiceImpl;
import kr.or.iei.review.model.vo.Review;

/**
 * Servlet implementation class ReviewWriteSetvlet
 */
@WebServlet("/review/reviewWrite.do")
public class ReviewWriteSetvlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewWriteSetvlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//로직 1 파일 업로드
		String uploadFilePath = request.getServletContext().getRealPath("/upload");
		//최대 파일 사이즈 크기 설정값
		int uploadFileSizeLimit = 1024 * 1024 * 50; //50MB
		//파일이름에 대한 인코딩 처리 값
		String enType = "UTF-8";
		
		//MultipartRequest 생성
		MultipartRequest multi = new MultipartRequest(request, 
													  uploadFilePath, 
													  uploadFileSizeLimit, 
													  enType, 
													  new DefaultFileRenamePolicy());
		
		//1. 원본파일 이름 가져오기
		String originalFileName = multi.getFilesystemName("reviewimg");
		
		//2. 업로드한 유저
		String fileUser = ((Member)request.getSession().getAttribute("member")).getUserId();
				
		//3. 업로드 시간(Calender Class)
		long currentTime = Calendar.getInstance().getTimeInMillis();
		//포멧을 지정하여 업로드 시간 구현하기
		SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Timestamp uploadTime = Timestamp.valueOf(fomatter.format(currentTime));
				
		//4. 파일 이름 변경 후 파일 경로 저장
		File file = new File(uploadFilePath+"\\"+originalFileName);
		file.renameTo(new File(uploadFilePath+"\\"+fileUser+"_"+currentTime+"_jupday.png"));
		//파일의 이름을 유저명_업로드시간(밀리세컨드)_kh 라는 이름으로 변경하겠다는 의미
		String changedFileName = fileUser+"_"+currentTime+"_jupday";
				
		//5. 해당 파일의 경로 저장
		String filePath = uploadFilePath+"\\"+changedFileName;
				
		//6. 파일의 사이즈
		File reFile = new File(filePath);
		long fileSize = reFile.length();
		
		
		ReviewFileData rfd = new ReviewFileData();
		rfd.setR_F_Original(originalFileName);
		rfd.setR_F_Change(changedFileName);
		rfd.setR_F_Filepath(filePath);
		rfd.setR_F_Filesize(fileSize);
		rfd.setUserId(fileUser);
		rfd.setR_F_File_Updatetime(uploadTime);
		
		ReviewFileService rfs = new ReviewFileServiceImpl();
		int result = rfs.uploadFile(rfd);
		
		//로직2 글 작성
		request.setCharacterEncoding("UTF-8");
		
		String postTitle = multi.getParameter("postTitle");
		String postContent = multi.getParameter("postContent");
		
		if(request.getSession().getAttribute("member")==null)
		{
			System.out.println("여기로 가지 않을까?");
			response.sendRedirect("/views/commons/error.jsp");
			return;
		}
		
		String userId = ((Member)request.getSession().getAttribute("member")).getUserId();
		
		Review review = new Review();
		review.setUserId(userId);
		review.setPostTitle(postTitle);
		review.setPostContent(postContent);
		
		
		ReviewService rService = new ReviewServiceImpl();
		int updateresult = rService.insertPostWrite(review);
		
		RequestDispatcher view = request.getRequestDispatcher("/views/review/reviewCreateResult.jsp");
		
		//성공 실패
		if(result>0 && updateresult>0) {//작성이 완료되면
			int postNum = rService.searchReviewNo(review); //그 글의 BoardNo를 가져옴
			request.setAttribute("result", true);			
			request.setAttribute("postNum", postNum);
			request.setAttribute("currentPage", 1);
		}else {
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
