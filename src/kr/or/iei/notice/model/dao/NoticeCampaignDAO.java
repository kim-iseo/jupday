package kr.or.iei.notice.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.notice.model.vo.NoticeCampaign;

public class NoticeCampaignDAO {

	public ArrayList<NoticeCampaign> showNoticeCampaign(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<NoticeCampaign> list = new ArrayList<NoticeCampaign>();
		
		String query =  "SELECT * FROM ( " +
                		"SELECT ROW_NUMBER() OVER(ORDER BY NC_NO DESC)AS NUM, NOTICE_CAMPAIGN.* " +
                		"FROM NOTICE_CAMPAIGN " +
                		"WHERE NC_DEL_YN='N') " +
                		"WHERE NUM BETWEEN 1 AND 3";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				NoticeCampaign nc = new NoticeCampaign();
				
				nc.setCampaignNo(rset.getInt("nc_no"));
				nc.setCampaignTitle(rset.getString("nc_title"));
				nc.setCampaignContent(rset.getString("nc_content"));
				nc.setCampaignRegDate(rset.getDate("nc_regDate"));
				
				
				list.add(nc);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}

}
