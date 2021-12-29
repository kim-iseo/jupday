package kr.or.iei.notice.model.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.crew.model.vo.Crew;
import kr.or.iei.notice.model.dao.NoticeDAO;
import kr.or.iei.notice.model.vo.AnswerFAQ;
import kr.or.iei.notice.model.vo.BoxFAQ;
import kr.or.iei.notice.model.vo.Notice;
import kr.or.iei.notice.model.vo.NoticeCampaign;
import kr.or.iei.notice.model.vo.QuestionFAQ;

public class NoticeServiceImpl implements NoticeService {

	private NoticeDAO nDAO = new NoticeDAO();

	@Override
	public HashMap<String, Object> selectAllPostList(int currentPage) {
		
       Connection conn = JDBCTemplate.getConnection();
		
		//하나의 Page에서 몇개의 목록으로 보여줄 것인지에 대한 값이 필요
		int recordCountPerPage= 10; 
		
		ArrayList<Notice> list =nDAO.selectAllPostPageList(conn,currentPage,recordCountPerPage);
		
		//2.하나의 pageNavi Bar에 보여질 Navi 개수를 설정
		int naviCountPerPage=5;
		

		String pageNavi=nDAO.getPageNavi(conn,naviCountPerPage,recordCountPerPage,currentPage);
		
	
        HashMap<String, Object> hm = new HashMap<String, Object>();//둘이 타입이 달라서 object 타입도 추가
		
		hm.put("list", list);//put으로 넣어준다.("키",벨류)
		hm.put("pageNavi", pageNavi);
		
		JDBCTemplate.close(conn);//return 전에만 해주면 된다.
		
		return hm;
		
	}

	@Override
	public Notice selectOnePost(int noticeNo) {
		Connection conn = JDBCTemplate.getConnection();
		
		Notice notice = nDAO.selectAllOnePost(conn, noticeNo);
		JDBCTemplate.close(conn);
		
		return notice;
	}
	
	

	@Override
	public HashMap<String, Object> selectAllCampaignPostList(int currentPage) {

		    Connection conn = JDBCTemplate.getConnection();
			
			//하나의 Page에서 몇개의 목록으로 보여줄 것인지에 대한 값이 필요
			int recordCountPerPage= 8; 
			
			ArrayList<NoticeCampaign> list =nDAO.selectAllCampaignPostList(conn,currentPage,recordCountPerPage);
			
			//2.하나의 pageNavi Bar에 보여질 Navi 개수를 설정
			int naviCountPerPage=5;
			

			String pageNavi=nDAO.getPageNavi2(conn,naviCountPerPage,recordCountPerPage,currentPage);
			
		
	        HashMap<String, Object> hm = new HashMap<String, Object>();//둘이 타입이 달라서 object 타입도 추가
			
			hm.put("list", list);//put으로 넣어준다.("키",벨류)
			hm.put("pageNavi", pageNavi);
			
			JDBCTemplate.close(conn);//return 전에만 해주면 된다.
			
			return hm;
		
	}

	@Override
	public NoticeCampaign selectOneCampaignPost(int campaignNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		NoticeCampaign noticeCampaign = nDAO.selectOneCampaignPost(conn, campaignNo);
		JDBCTemplate.close(conn);
		
		return noticeCampaign;
	
	}

	@Override
	public ArrayList<Notice> showNotice() {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Notice> list = nDAO.showNotice(conn);
		JDBCTemplate.close(conn);
		return list;
	}

	public HashMap<String, Object> selectSearchNewsContent(int currentPage, String keyword, String type) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		//하나의 Page에서 몇개의 목록으로 보여줄 것인지에 대한 값이 필요
		int recordCountPerPage= 10; 
		
		//
		ArrayList<Notice> list =nDAO.selectSearchNewsContentList(conn,currentPage,recordCountPerPage,keyword,type);
		
		//2.하나의 pageNavi Bar에 보여질 Navi 개수를 설정
		int naviCountPerPage=5;
		/*
		System.out.println("검색 키워드: "+ keyword);
		System.out.println("검색 타입: "+ type);
		
		for(Notice notice : list) {
			
			System.out.println("제목" + notice.getNoticeTitle() +"/"+ "작성자: " + notice.getNoticeContent());
		} 확인하는 코드: 이상무
		*/
		

		String pageNavi=nDAO.getSearchPageNavi(conn,naviCountPerPage,recordCountPerPage,currentPage,keyword,type);
		
		//DB 연결 해제 
		JDBCTemplate.close(conn);
		
		// 리턴을 하기 위하여 HashMap 객체를 만들어서 리턴
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		map.put("list", list);
		map.put("pageNavi", pageNavi);
		
		return map;
		
		
		
	}

	@Override
	public HashMap<String, Object> selectSearchCampaignContent(int currentPage, String keyword, String type) {

		Connection conn = JDBCTemplate.getConnection();
		
		//하나의 Page에서 몇개의 목록으로 보여줄 것인지에 대한 값이 필요
		int recordCountPerPage= 8; 
		
		//
		ArrayList<NoticeCampaign> list =nDAO.selectSearchCampaignContentList(conn,currentPage,recordCountPerPage,keyword,type);
		
		//2.하나의 pageNavi Bar에 보여질 Navi 개수를 설정
		int naviCountPerPage=5;


		String pageNavi=nDAO.getSearchPageNavi2(conn,naviCountPerPage,recordCountPerPage,currentPage,keyword,type);
		
		//DB 연결 해제 
		JDBCTemplate.close(conn);
		
		// 리턴을 하기 위하여 HashMap 객체를 만들어서 리턴
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		map.put("list", list);
		map.put("pageNavi", pageNavi);
		
		return map;
	}

	@Override
	public HashMap<String, Object> selectAllFAQList(int currentPage) {
		 Connection conn = JDBCTemplate.getConnection();
			
			//하나의 Page에서 몇개의 목록으로 보여줄 것인지에 대한 값이 필요
			int recordCountPerPage= 10; 
			

			ArrayList<BoxFAQ> list =nDAO.selectAllFAQPageList(conn,currentPage,recordCountPerPage);

			
			//2.하나의 pageNavi Bar에 보여질 Navi 개수를 설정
			int naviCountPerPage=5;
			

			String pageNavi=nDAO.getPageNavi3(conn,naviCountPerPage,recordCountPerPage,currentPage);
			
		
	        HashMap<String, Object> hm = new HashMap<String, Object>();//둘이 타입이 달라서 object 타입도 추가
			
			hm.put("list", list);//put으로 넣어준다.("키",벨류)
			hm.put("pageNavi", pageNavi);
			
			JDBCTemplate.close(conn);//return 전에만 해주면 된다.
			
			return hm;
			
		}
	}

