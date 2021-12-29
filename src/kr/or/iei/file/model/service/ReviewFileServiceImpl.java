package kr.or.iei.file.model.service;

import java.sql.Connection;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.file.model.dao.ReviewFileDAO;
import kr.or.iei.file.model.vo.ReviewFileData;

public class ReviewFileServiceImpl implements ReviewFileService {

	private ReviewFileDAO rfDAO = new ReviewFileDAO();
	
	@Override
	public int uploadFile(ReviewFileData rfd) {
		Connection conn = JDBCTemplate.getConnection();
		int result = rfDAO.insertFileUpload(conn,rfd);
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		JDBCTemplate.close(conn);
		return result;
	}

}
