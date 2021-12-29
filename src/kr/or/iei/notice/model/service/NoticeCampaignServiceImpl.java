package kr.or.iei.notice.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.notice.model.dao.NoticeCampaignDAO;
import kr.or.iei.notice.model.vo.NoticeCampaign;

public class NoticeCampaignServiceImpl implements NoticeCampaignService {
	
	private NoticeCampaignDAO ncDAO = new NoticeCampaignDAO();

	@Override
	public ArrayList<NoticeCampaign> showNoticeCampaign() {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<NoticeCampaign> list = ncDAO.showNoticeCampaign(conn);
		JDBCTemplate.close(conn);
		return list;
	}

}
