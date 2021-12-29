package kr.or.iei.admin.review.model.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import kr.or.iei.admin.crew.model.dao.AdminCrewDAO;
import kr.or.iei.admin.review.model.dao.AdminReviewDAO;
import kr.or.iei.admin.review.model.vo.AdminReview;
import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.crew.model.vo.Crew;

public class AdminReviewServiceImpl implements AdminReviewService {
	private AdminReviewDAO adrDAO = new AdminReviewDAO();
	
	@Override
	public HashMap<String, Object> selectAllReviewPageList(int currentPage) {
		
			Connection conn = JDBCTemplate.getConnection();
			
			//한 개의 page에서 몇 개의 목록으로 보여줄 것인지
			int recordCountPerPage = 10;
			
			ArrayList<AdminReview> list = adrDAO.selectAllReviewPageList(conn, currentPage, recordCountPerPage);
			
			//한 개의 pageNavi에서 보여질 Navi 개수
			int naviCountPerPage = 5;
			
			String pageNavi = adrDAO.getPageNavi(conn,naviCountPerPage,recordCountPerPage,currentPage);
			
			HashMap<String,Object> hm = new HashMap<String,Object>();
			
			hm.put("list", list);
			hm.put("pageNavi", pageNavi);
			
			JDBCTemplate.close(conn);
			return hm;
		}

	@Override
	public int updateReviewDelYN(int postnum, char delYN) {
		Connection conn = JDBCTemplate.getConnection();
		int result = adrDAO.updateReviewDelYN(postnum,delYN,conn);
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		JDBCTemplate.close(conn);
		return result;
	}

	@Override
	public int updateReviewBestYN(int postnum, char bestYN) {
		Connection conn = JDBCTemplate.getConnection();
		int result = adrDAO.updateReviewBestYN(postnum,bestYN,conn);
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		JDBCTemplate.close(conn);
		return result;
	}





	}


