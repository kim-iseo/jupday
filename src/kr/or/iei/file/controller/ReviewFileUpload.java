package kr.or.iei.file.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.or.iei.file.model.service.ReviewFileService;
import kr.or.iei.file.model.service.ReviewFileServiceImpl;
import kr.or.iei.file.model.vo.MemberFileData;
import kr.or.iei.file.model.vo.ReviewFileData;
import kr.or.iei.member.model.vo.Member;
import kr.or.iei.review.model.service.ReviewServiceImpl;

/**
 * Servlet implementation class ReviewFileUpload
 */
@WebServlet("/file/reviewFileUpload.do")
public class ReviewFileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewFileUpload() {
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
		File tmp = new File(uploadFilePath+"\\"+multi.getFilesystemName("files"));

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
		
		//포멧에 맞는 업로드 시간 구현
		Timestamp uploadTime = Timestamp.valueOf(formatter.format(currentTime));
				
		// 변경된 파일 이름 변수
		String changedFileName = null;
		
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
		ReviewFileData rfd = new ReviewFileData();
		rfd.setR_F_Original(originalFileName);
		rfd.setR_F_Change(changedFileName);
		rfd.setR_F_Filepath(filePath);
		rfd.setR_F_Filesize(fileSize);
		rfd.setUserId(fileUser);
		rfd.setR_F_File_Updatetime(uploadTime);
				
		ReviewFileService rfs = new ReviewFileServiceImpl();
		int result = rfs.uploadFile(rfd);
		
		RequestDispatcher view = request.getRequestDispatcher("/views/review/reviewWriteForm.jsp");
		if(result>0) {
			request.setAttribute("result", true);
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
