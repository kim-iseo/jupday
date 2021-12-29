package kr.or.iei.admin.notice.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.admin.notice.model.vo.AdminCampaign;
import kr.or.iei.admin.notice.model.vo.AdminFAQ;
import kr.or.iei.admin.notice.model.vo.AdminNotice;
import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.notice.model.vo.Notice;

public class AdminNoticeDAO {

	public ArrayList<AdminNotice> selectAllNoitcePageList(Connection conn, int currentPage, int recordCountPerPage) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<AdminNotice> list = new ArrayList<AdminNotice>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;		
		
		String query = " SELECT N_NO, N_TITLE, N_CONTENT, N_REGDATE, N_DEL_YN FROM NOTICE " + 
						" WHERE N_NO BETWEEN ? AND ? ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) 
			{
				AdminNotice an = new AdminNotice();
				an.setN_No(rset.getInt("n_No"));
				an.setN_Title(rset.getString("n_Title"));
				an.setN_Content(rset.getString("n_Content"));
				an.setN_regDate(rset.getDate("n_regDate"));
				an.setN_Del_YN(rset.getString("n_Del_YN").charAt(0));
				list.add(an);
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

	public String getPageNavi(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage) {
		
		//전체 공지수
		int recordTotalCount = totalCount(conn);
		
		//전체 페이지수
		int pageTotalCount = 0;
		
		if((recordTotalCount % recordCountPerPage)>0)
		{
			pageTotalCount = (recordTotalCount / recordCountPerPage) + 1;
		}else
		{
			pageTotalCount = (recordTotalCount / recordCountPerPage);
		}
		
		int startNavi = (((currentPage-1) / naviCountPerPage) * naviCountPerPage) +1;
		int endNavi = startNavi + (naviCountPerPage-1);
		
		//endNavi가 총 page수보다 클 경우 총 page수로 셋팅
		if(endNavi > pageTotalCount)
		{
			endNavi = pageTotalCount;
		}
				
		//pageNavi 모양 만들기
		StringBuilder sb = new StringBuilder();
		
		if(startNavi!=1) {
			sb.append("<li><a href='/admin/noticeManageList.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
		}

		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/admin/noticeManageList.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/admin/noticeManageList.do?currentPage="+i+"'>"+i+"</a></li>");
			}
		}

		if(endNavi!=pageTotalCount) {
			sb.append("<li><a href='/admin/noticeManageList.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
		}
		
		return sb.toString();
				
	}
	
	public int totalCount(Connection conn)
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query = "SELECT COUNT(*) as count FROM NOTICE";
		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				count = rset.getInt("count");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return count;
	}

	public ArrayList<AdminCampaign> selectAllCampaignPageList(Connection conn, int currentPage, int recordCountPerPage) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<AdminCampaign> list = new ArrayList<AdminCampaign>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;		
		
		String query = " SELECT NC_NO, NC_TITLE, NC_CONTENT, NC_REGDATE, NC_DEL_YN " + 
					"	FROM NOTICE_CAMPAIGN " + 
					"	WHERE NC_NO BETWEEN ? AND ? order by nc_no ASC ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) 
			{
				AdminCampaign ac = new AdminCampaign();
				
				ac.setNc_No(rset.getInt("nc_No"));
				ac.setNc_Title(rset.getString("nc_Title"));
				ac.setNc_Content(rset.getString("nc_Content"));
				ac.setNc_regDate(rset.getDate("nc_regDate"));
				ac.setNc_Del_YN(rset.getString("nc_Del_YN").charAt(0));
				
				list.add(ac);
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
	public String getpageCamNavi(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage) {
		
		//전체 공지수
		int recordTotalCount = totalCamCount(conn);
		
		//전체 페이지수
		int pageTotalCount = 0;
		
		if((recordTotalCount % recordCountPerPage)>0)
		{
			pageTotalCount = (recordTotalCount / recordCountPerPage) + 1;
		}else
		{
			pageTotalCount = (recordTotalCount / recordCountPerPage);
		}
		
		int startNavi = (((currentPage-1) / naviCountPerPage) * naviCountPerPage) +1;
		int endNavi = startNavi + (naviCountPerPage-1);
		
		//endNavi가 총 page수보다 클 경우 총 page수로 셋팅
		if(endNavi > pageTotalCount)
		{
			endNavi = pageTotalCount;
		}
				
		//pageNavi 모양 만들기
		StringBuilder sb = new StringBuilder();
		
		if(startNavi!=1) {
			sb.append("<li><a href='/admin/adminCampaignManageList.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
		}

		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/admin/adminCampaignManageList.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/admin/adminCampaignManageList.do?currentPage="+i+"'>"+i+"</a></li>");
			}
		}

		if(endNavi!=pageTotalCount) {
			sb.append("<li><a href='/admin/adminCampaignManageList.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
		}
		
		return sb.toString();
				
	}
	
	public int totalCamCount(Connection conn)
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query = "SELECT COUNT(*) as count FROM NOTICE_CAMPAIGN";
		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				count = rset.getInt("count");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return count;
	}

	public ArrayList<AdminFAQ> selectAllFAQPageList(Connection conn, int currentPage, int recordCountPerPage) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<AdminFAQ> list = new ArrayList<AdminFAQ>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;		
		
		String query = " SELECT FAQ_NO, FAQ_TITLE, FAQ_CONTENT, FAQ_DEL_YN FROM FAQBOX WHERE FAQ_NO BETWEEN ? AND ? ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) 
			{
				AdminFAQ afaq = new AdminFAQ();
				
				afaq.setFaq_No(rset.getInt("faq_No"));
				afaq.setFaq_Title(rset.getString("faq_Title"));
				afaq.setFaq_Content(rset.getString("faq_Content"));
				afaq.setFaq_Del_YN(rset.getString("faq_Del_YN").charAt(0));
				
				list.add(afaq);
								
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

	public String getpageFAQNavi(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage) {
				//전체 공지수
				int recordTotalCount = totalFAQCount(conn);
				
				//전체 페이지수
				int pageTotalCount = 0;
				
				if((recordTotalCount % recordCountPerPage)>0)
				{
					pageTotalCount = (recordTotalCount / recordCountPerPage) + 1;
				}else
				{
					pageTotalCount = (recordTotalCount / recordCountPerPage);
				}
				
				int startNavi = (((currentPage-1) / naviCountPerPage) * naviCountPerPage) +1;
				int endNavi = startNavi + (naviCountPerPage-1);
				
				//endNavi가 총 page수보다 클 경우 총 page수로 셋팅
				if(endNavi > pageTotalCount)
				{
					endNavi = pageTotalCount;
				}
						
				//pageNavi 모양 만들기
				StringBuilder sb = new StringBuilder();
				
				if(startNavi!=1) {
					sb.append("<li><a href='/admin/adminFAQManageList.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
				}

				for(int i=startNavi; i<=endNavi; i++) {
					
					if(i==currentPage) {
						sb.append("<li><a href='/admin/adminFAQManageList.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
					}else {
						sb.append("<li><a href='/admin/adminFAQManageList.do?currentPage="+i+"'>"+i+"</a></li>");
					}
				}

				if(endNavi!=pageTotalCount) {
					sb.append("<li><a href='/admin/adminFAQManageList.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
				}
				
				return sb.toString();
	}
	
	public int totalFAQCount(Connection conn)
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query = "SELECT COUNT(*) as count FROM FAQBOX";
		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				count = rset.getInt("count");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return count;
	}

	public int updateNoticeDelYN(int nNo, char delYN, Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE NOTICE SET N_DEL_YN=? WHERE N_NO=?";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, String.valueOf(delYN));
			pstmt.setInt(2, nNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int updateCampaignDelYN(int ncNo, char delYN, Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE NOTICE_CAMPAIGN SET NC_DEL_YN=? WHERE NC_NO=?";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, String.valueOf(delYN));
			pstmt.setInt(2, ncNo);
			
			result = pstmt.executeUpdate();
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int updateFAQDelYN(int faqNo, char delYN, Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE FAQBOX SET FAQ_DEL_YN=? WHERE FAQ_NO=?";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, String.valueOf(delYN));
			pstmt.setInt(2, faqNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return result;
	}
	
	public AdminFAQ selectOneFAQContent(Connection conn, int faqNo) {
		
	      PreparedStatement pstmt = null;
	      ResultSet rset = null;
	      
	      AdminFAQ adfaq = null;
	      
	      String query =" SELECT FAQ_NO, FAQ_TITLE, FAQ_CONTENT, FAQ_DEL_YN " + 
	               " FROM FAQBOX WHERE FAQ_NO=? AND FAQ_DEL_YN='N'";
	      
	      try {
	         pstmt = conn.prepareStatement(query);
	         pstmt.setInt(1, faqNo);
	         
	         rset = pstmt.executeQuery();
	         
	         if(rset.next())
	         {
	            
	            adfaq = new AdminFAQ();

	            adfaq.setFaq_No(rset.getInt("faq_No"));
	            adfaq.setFaq_Title(rset.getString("faq_Title"));
	            adfaq.setFaq_Content(rset.getString("faq_Content"));
	            adfaq.setFaq_Del_YN(rset.getString("faq_Del_YN").charAt(0));
	                        
	         }
	         
	      } catch (SQLException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }finally {
	         JDBCTemplate.close(rset);
	         JDBCTemplate.close(pstmt);
	      }
	     
	      return adfaq;
	      
	      
	   }

	public AdminCampaign selectOneCampaignContent(Connection conn, int ncNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		AdminCampaign adcam = null;
		
		String query = " SELECT NC_NO, NC_TITLE, NC_CONTENT, NC_REGDATE, NC_DEL_YN " + 
						" FROM NOTICE_CAMPAIGN WHERE NC_NO=? AND NC_DEL_YN='N' ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, ncNo);
			
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				adcam = new AdminCampaign();
				
				adcam.setNc_No(rset.getInt("nc_No"));
				adcam.setNc_Title(rset.getString("nc_Title"));
				adcam.setNc_Content(rset.getString("nc_Content"));
				adcam.setNc_regDate(rset.getDate("nc_regDate"));
				adcam.setNc_Del_YN(rset.getString("nc_Del_YN").charAt(0));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
			
		return adcam;
	}

	public AdminNotice selectOneNoticeContent(Connection conn, int nNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
				
		AdminNotice adnotice = null;
		
		String query = "SELECT N_NO, N_TITLE, N_CONTENT, N_REGDATE, N_DEL_YN " + 
				" FROM NOTICE WHERE N_NO=? AND N_DEL_YN='N'";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, nNo);
			
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				adnotice = new AdminNotice();
				
				adnotice.setN_No(rset.getInt("n_No"));
				adnotice.setN_Title(rset.getString("n_Title"));
				adnotice.setN_Content(rset.getString("n_Content"));
				adnotice.setN_regDate(rset.getDate("n_regDate"));
				adnotice.setN_Del_YN(rset.getString("n_Del_YN").charAt(0));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return adnotice;
	}

	public int updateNoticePost(Connection conn, AdminNotice adnoup) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE NOTICE SET N_TITLE = ?, N_CONTENT= ? WHERE N_NO=?";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,adnoup.getN_Title());
			pstmt.setString(2, adnoup.getN_Content());
			pstmt.setInt(3, adnoup.getN_No());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		} return result;
		
		
	}

	public int updateCampaignPost(Connection conn, AdminCampaign adc) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE NOTICE_CAMPAIGN SET NC_TITLE = ? , NC_CONTENT = ? WHERE NC_NO= ? ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, adc.getNc_Title());
			pstmt.setString(2, adc.getNc_Content());
			pstmt.setInt(3, adc.getNc_No());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);			
		}return result;
		
	}

	public int insertCampaignPostWrite(Connection conn, AdminCampaign adcwrite) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "INSERT INTO NOTICE_CAMPAIGN VALUES (NC_NO_SEQ.NEXTVAL,'admin',?,?,SYSDATE,0,'N')";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, adcwrite.getNc_Title());
			pstmt.setString(2, adcwrite.getNc_Content());
			
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		} return result;
		
		
		
	}

	public int searchCampaignPostNo(Connection conn, AdminCampaign adcwrite) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int ncNo = 0;
		
		String query = "SELECT NC_NO FROM " + 
				"	(SELECT ROW_NUMBER() OVER(ORDER BY NC_NO ASC) AS NUM,NOTICE_CAMPAIGN.* FROM NOTICE_CAMPAIGN " + 
				"	WHERE NC_TITLE=? AND NC_CONTENT=?) WHERE NUM = 1 ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, adcwrite.getNc_Title());
			pstmt.setString(2, adcwrite.getNc_Content());
			
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				ncNo = rset.getInt("nc_No");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return ncNo;
		
		
	}

	public int updateFAQPost(Connection conn, AdminFAQ adfaq) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE FAQBOX SET FAQ_TITLE= ? , FAQ_CONTENT= ?  WHERE FAQ_NO=? ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, adfaq.getFaq_Title());
			pstmt.setString(2, adfaq.getFaq_Content());
			pstmt.setInt(3, adfaq.getFaq_No());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);			
		}return result;
	}

	public int insertNoticePostWrite(Connection conn, AdminNotice adnwrite) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = " INSERT INTO NOTICE VALUES(N_NO_SEQ.NEXTVAL,'admin',?,?,sysdate,0,'N') ";
		
		try {
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, adnwrite.getN_Title());
			pstmt.setString(2, adnwrite.getN_Content());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int searchNoticePostNo(Connection conn, AdminNotice adnwrite) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int nNo = 0;
		
		String query = " SELECT N_NO FROM " + 
				"		(SELECT ROW_NUMBER() OVER(ORDER BY N_NO ASC) AS NUM,NOTICE.* FROM NOTICE " + 
				"		WHERE N_TITLE=? AND N_CONTENT=?) WHERE NUM = 1 ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, adnwrite.getN_Title());
			pstmt.setString(2, adnwrite.getN_Content());
			
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				nNo = rset.getInt("n_No");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		} 
		return nNo;

	}

	public int insertFAQWrite(Connection conn, AdminFAQ adf) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "INSERT INTO FAQBOX VALUES(FAQ_NO_SEQ.NEXTVAL,?,?,'N')";
		
		try {
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, adf.getFaq_Title());
			pstmt.setString(2, adf.getFaq_Content());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int searchFAQPostNo(Connection conn, AdminFAQ adf) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int faqNo = 0;
		
		String query = " SELECT FAQ_NO FROM (SELECT ROW_NUMBER() OVER(ORDER BY FAQ_NO ASC) AS NUM " + 
				" FAQBOX.* FROM FAQBOX WHERE FAQ_TITLE=? AND FAQ_CONTENT=?) WHERE NUM=1 ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, adf.getFaq_Title());
			pstmt.setString(2, adf.getFaq_Content());
			
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				faqNo = rset.getInt("faqNo");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return faqNo;
		
		
		
		

	} 


	}

	
