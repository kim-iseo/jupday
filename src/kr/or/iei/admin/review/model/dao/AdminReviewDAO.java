package kr.or.iei.admin.review.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.admin.review.model.vo.AdminReview;
import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.member.model.vo.Member;

public class AdminReviewDAO {

	public ArrayList<AdminReview> selectAllReviewPageList(Connection conn, int currentPage, int recordCountPerPage) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<AdminReview> list = new ArrayList<AdminReview>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;		
		
		String query = " SELECT * FROM( " + 
				"	SELECT ROW_NUMBER() OVER (ORDER BY R.POSTNUM DESC) AS NUM, " + 
				"	R.POSTNUM, NICK, R.REGDATE, R.POSTTITLE, R.POSTCONTENT, R.GOOD, R.BEST_YN, R.DEL_YN " + 
				"	FROM REVIEW R LEFT JOIN MEMBER M ON (R.USERID=M.USERID)) " + 
				"	WHERE NUM BETWEEN ? AND ? ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) 
			{
				AdminReview re = new AdminReview();
				re.setPostnum(rset.getInt("postNum"));
				re.setNick(rset.getString("nick"));
				re.setRegDate(rset.getDate("regDate"));
				re.setPostTitle(rset.getString("postTitle"));
				re.setPostContent(rset.getString("postContent"));
				re.setGood(rset.getInt("good"));
				re.setBest_YN(rset.getString("best_YN").charAt(0));
				re.setDel_YN(rset.getString("del_YN").charAt(0));
				
				list.add(re);
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
		
		//전체 리뷰수
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
			sb.append("<li><a href='/admin/reviewManageList.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
		}

		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/admin/reviewManageList.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/admin/reviewManageList.do?currentPage="+i+"'>"+i+"</a></li>");
			}
		}

		if(endNavi!=pageTotalCount) {
			sb.append("<li><a href='/admin/reviewManageList.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
		}
		
		return sb.toString();
				
	}
	
	public int totalCount(Connection conn)
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query = "SELECT COUNT(*) as count FROM REVIEW";
		
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

	public int updateReviewDelYN(int postnum, char delYN, Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE REVIEW SET DEL_YN=? WHERE POSTNUM=?";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, String.valueOf(delYN));
			pstmt.setInt(2, postnum);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	public int updateReviewBestYN(int postnum, char bestYN, Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE REVIEW SET BEST_YN=? WHERE POSTNUM=?";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, String.valueOf(bestYN));
			pstmt.setInt(2, postnum);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	}


