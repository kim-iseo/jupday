package kr.or.iei.file.model.service;

import java.sql.Connection;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.file.model.dao.MemberFileDAO;
import kr.or.iei.file.model.vo.MemberFileData;

public class MemberFileServiceImpl implements MemberFileService {

	@Override
	public int insertFileUpdate(MemberFileData mfd) {
		Connection conn = JDBCTemplate.getConnection();
		int result = MemberFileDAO.insertFileUpload(conn, mfd);
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		return result;
	}

}
