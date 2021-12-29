package kr.or.iei.admin.notice.model.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import kr.or.iei.admin.notice.model.dao.AdminNoticeDAO;
import kr.or.iei.admin.notice.model.vo.AdminFAQ;
import kr.or.iei.admin.notice.model.vo.AdminNotice;
import kr.or.iei.common.JDBCTemplate;

public class AdminFAQServiceImpl implements AdminFAQService {
	private AdminNoticeDAO adnDAO = new AdminNoticeDAO();
	
	@Override
	public HashMap<String, Object> selectAllFAQPageList(int currentPage) {
	Connection conn = JDBCTemplate.getConnection();
		
		//한 개의 page에서 몇 개의 목록으로 보여줄 것인지
		int recordCountPerPage = 10;
		
		ArrayList<AdminFAQ> list = adnDAO.selectAllFAQPageList(conn, currentPage, recordCountPerPage);
		
		//한 개의 pageNavi에서 보여질 Navi 개수
		int naviCountPerPage = 5;
		
		String pageFAQNavi = adnDAO.getpageFAQNavi(conn,naviCountPerPage,recordCountPerPage,currentPage);
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("list", list);
		hm.put("pageFAQNavi", pageFAQNavi);
				
		JDBCTemplate.close(conn);
		return hm;
	}

	@Override
	public int updateFAQDelYN(int faqNo, char delYN) {
		Connection conn = JDBCTemplate.getConnection();
		int result = adnDAO.updateFAQDelYN(faqNo,delYN,conn);
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		JDBCTemplate.close(conn);
		return result;
	}

	@Override
	public AdminFAQ selectOneFAQContent(int faqNo) {
		Connection conn = JDBCTemplate.getConnection();
		AdminFAQ adfaq = adnDAO.selectOneFAQContent(conn, faqNo);
		JDBCTemplate.close(conn);
		return adfaq;
		
	}

	@Override
	public int updateFAQPost(AdminFAQ adfaq) {
		Connection conn = JDBCTemplate.getConnection();
		int result = adnDAO.updateFAQPost(conn,adfaq);
		if(result>0)JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		JDBCTemplate.close(conn);
		
		return result;
	}

	@Override
	public int insertFAQWrite(AdminFAQ adf) {
		Connection conn = JDBCTemplate.getConnection();
		int result = adnDAO.insertFAQWrite(conn, adf);
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		JDBCTemplate.close(conn);
		return result;
	}

	@Override
	public int searchFAQPostNo(AdminFAQ adfwrite) {
		Connection conn = JDBCTemplate.getConnection();
		int faqNo = adnDAO.searchFAQPostNo(conn, adfwrite);
		JDBCTemplate.close(conn);
		return faqNo;
	}


	/*
	 * 	@Override
	public int insertNoticePostWrite(AdminNotice adnwrite) {
		Connection conn = JDBCTemplate.getConnection();
		int result = adnDAO.insertNoticePostWrite(conn, adnwrite);
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		JDBCTemplate.close(conn);
		return result;
	}

	@Override
	public int searchNoticePostNo(AdminNotice adnwrite) {
		Connection conn = JDBCTemplate.getConnection();
		int nNo = adnDAO.searchNoticePostNo(conn, adnwrite);
		JDBCTemplate.close(conn);
		return nNo;
	}*/
	

}
