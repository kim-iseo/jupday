package kr.or.iei.admin.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.member.model.vo.Member;

public class AdminMemberDAO {
	
	public int updateMemberEndYN(String userId, char endYN, Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "UPDATE MEMBER SET end_YN=? WHERE userId=? AND authority_id!='root'";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, String.valueOf(endYN));
			pstmt.setString(2, userId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	public ArrayList<Member> selectAllMemberPageList(Connection conn, int currentPage, int recordCountPerPage) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Member> list = new ArrayList<Member>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;
		
		String query = "select * from "
				+ " (select row_number() "
				+ " over (order by AUTHORITY_ID ASC) AS NUM, "
				+ " MEMBER.* FROM MEMBER ) "
				+ " WHERE NUM BETWEEN ? AND ?";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) 
			{
				//Member m은 MemberDAO와 겹쳐서 mem으로 대체
				Member mem = new Member();
				mem.setAuthority_Id(rset.getString("authority_Id")); //1
				mem.setUserId(rset.getString("userId")); //2
				mem.setUserPwd(rset.getString("userPwd")); //3
				mem.setNick(rset.getString("nick")); //4
				mem.setEmail(rset.getString("email")); //5
				mem.setP_Image(rset.getString("p_Image")); //6
				mem.setEnrollDate(rset.getDate("enrollDate")); //7
				mem.setEnd_YN(rset.getString("end_yn").charAt(0)); //8
				list.add(mem);
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
		
		//전체 회원수
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
			sb.append("<li><a href='/admin/MemberManageList.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
		}

		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/admin/MemberManageList.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/admin/MemberManageList.do?currentPage="+i+"'>"+i+"</a></li>");
			}
		}

		if(endNavi!=pageTotalCount) {
			sb.append("<li><a href='/admin/MemberManageList.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
		}
		
		return sb.toString();
				
	}
	
	public int totalCount(Connection conn)
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query = "SELECT COUNT(*) as count FROM MEMBER";
		
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

	

}

