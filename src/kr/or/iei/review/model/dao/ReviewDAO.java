package kr.or.iei.review.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.member.model.vo.Member;
import kr.or.iei.review.model.vo.Review;
import kr.or.iei.review.model.vo.Review;
import kr.or.iei.review.model.vo.ReviewComment;

public class ReviewDAO {

	public ArrayList<Review> selectAllpostList(Connection conn, int currentPage, int recordCountPerPage) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;
		
		String query = "select *" + 
				"		from (select row_number() over(order by postnum desc)as num, review.* ,nick" + 
				"		from review " + 
				"		left join member on (review.userid=member.userid)" + 
				"		where review.del_YN='N') " + 
				"		where num between ? and ?";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Review review = new Review();
				
				review.setPostNum(rset.getInt("postNum"));
				review.setUserId(rset.getString("userId"));
				review.setRegDate(rset.getDate("regDate"));
				review.setPostTitle(rset.getString("postTitle"));
				review.setPostContent(rset.getString("postContent"));
				review.setHits(rset.getInt("hits"));
				review.setGood(rset.getInt("good"));
				review.setBest_YN(rset.getString("best_YN").charAt(0));
				review.setDel_YN(rset.getString("del_YN").charAt(0));
				review.setNick(rset.getString("nick"));
				
				list.add(review);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
		
	}

	public String getPageNavi(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage) {
		
		int recordTotalCount = totalCount(conn);
		
		int pageTotalCount = 0;
		
		if((recordTotalCount % recordCountPerPage) > 0) {
			pageTotalCount = (recordTotalCount / recordCountPerPage) +1;
		}else {
			pageTotalCount = (recordTotalCount / recordCountPerPage);
		};
		
		int startNavi = (((currentPage-1) / naviCountPerPage) * naviCountPerPage) + 1;
		int endNavi = startNavi + (naviCountPerPage-1);
		
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		
		StringBuilder sb = new StringBuilder();
		
		if(startNavi!=1) {sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");}
		
		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+i+"'>"+i+"</a></li>");			
			}
			
		}
		
		if(endNavi!=pageTotalCount) {sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li> ");}
		
		return sb.toString();
	}
	
	public int totalCount(Connection conn) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query = "select count(*) as count from review where del_YN='N'";
		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				count = rset.getInt("count");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return count;
		
	}

	public Review selectOneContent(Connection conn, int postNum) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		Review review = null;
		
		String query = "select review.*,nick from review " + 
				"left join member on (review.userid = member.userid) " + 
				"where postnum = ? and review.del_YN='N'" ;
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, postNum);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				
				review = new Review();
				
				review.setPostNum(rset.getInt("postNum"));
				review.setUserId(rset.getString("userId"));
				review.setRegDate(rset.getDate("regDate"));
				review.setPostTitle(rset.getString("postTitle"));
				review.setPostContent(rset.getString("postContent"));
				review.setHits(rset.getInt("hits"));
				review.setGood(rset.getInt("good"));
				review.setBest_YN(rset.getString("best_YN").charAt(0));
				review.setDel_YN(rset.getString("del_YN").charAt(0));
				review.setNick(rset.getString("nick"));
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return review;
	}


	public ArrayList<Review> selectAllpostList2(String userId, Connection conn, int currentPage, int recordCountPerPage) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;
		
		String query = "select *" + 
				"		from (select row_number() over(order by postnum desc)as num, review.* ,nick" + 
				"		from review " + 
				"		left join member on (review.userid=member.userid)" + 
				"		where review.del_YN='N' " + 
				"       and review.userId=?)" +
				"		where num between ? and ?";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Review review = new Review();
				
				review.setPostNum(rset.getInt("postNum"));
				review.setUserId(rset.getString("userId"));
				review.setRegDate(rset.getDate("regDate"));
				review.setPostTitle(rset.getString("postTitle"));
				review.setPostContent(rset.getString("postContent"));
				review.setHits(rset.getInt("hits"));
				review.setGood(rset.getInt("good"));
				review.setBest_YN(rset.getString("best_YN").charAt(0));
				review.setDel_YN(rset.getString("del_YN").charAt(0));
				review.setNick(rset.getString("nick"));
				
				list.add(review);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
			
			
			
			
	public ArrayList<ReviewComment> selectPostAllComment(Connection conn, int postNum) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		ArrayList<ReviewComment> commentslist = new ArrayList<ReviewComment>();
		
		String query = "select REVIEW_COMMENT.*,Member.nick from REVIEW_COMMENT " + 
				"left join member on (REVIEW_COMMENT.userid = member.userid) " + 
				"where R_C_DEL_YN='N' and postnum=? " + 
				"order by R_C_NO DESC";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, postNum);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				ReviewComment co = new ReviewComment();
				
				co.setR_c_no(rset.getInt("r_c_no"));
				co.setPostNum(rset.getInt("postNum"));
				co.setUserId(rset.getString("userId"));
				co.setR_c_comment(rset.getString("r_c_comment"));
				co.setR_c_regDate(rset.getDate("r_c_regDate"));
				co.setR_c_del_YN(rset.getString("r_c_del_YN").charAt(0));
				co.setNick(rset.getString("nick"));
				
				commentslist.add(co);
			}	

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return commentslist;
	}



	public String getSearchPageNavi(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage,
			String userId) {
		int recordTotalCount = totalSearchCount(conn, userId); //전체 게시글 갯수
		
		int pageTotalCount = 0; //전체 페이지 갯수
		
		if(recordTotalCount % recordCountPerPage > 0 ) { //나머지가 있으면 +1
			pageTotalCount = (recordTotalCount / recordCountPerPage) + 1;
		} else { //나머지가 없으면 그냥
			pageTotalCount = recordTotalCount / recordCountPerPage;
		}
		
		/* startNavi 공식
		startNavi = (((현재페이지 - 1)) / (navi 당 보여질 갯수) * navi 당 보여질 갯수) + 1
		코드로 표현
		StartNavi = (((currentPage-1) / naviCountPerPage) * naviCountPerPage) + 1
		
		endNavi 공식
		endNavi = 시작navi값 + (Navi당 보여질 갯수 -1)
		코드로 표현
		endNavi = startNavi + (naviCountPerPage -1)
		*/
		
		int startNavi = (((currentPage-1) / naviCountPerPage) * naviCountPerPage) + 1;
		int endNavi = startNavi + (naviCountPerPage -1);
		
		//단 위의 공식은 문제가 없지만, 하나의 상황에서는 예외가 있다.
		//만약 현재 페이지가 21페이지일 경우 < 21 22
		
		//만약 공식으로 구한 endNavi가 총  page수보다 크다면 총 page 수로 세팅하라는 코드
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		
		// PageNavi 모양 만들기
		
				StringBuilder sb = new StringBuilder();
				
				if(startNavi!=1)
				{
					sb.append("<li><a href='/member/myPageMyReview.do?currentPage="+(startNavi-1)+"&userId="+userId+"'><i class='fas fa-chevron-left'></i></a></li>");
				}
				
				for(int i=startNavi; i<=endNavi;i++)
				{
					if(i==currentPage)
					{
						sb.append("<li><a href='/member/myPageMyReview.do?currentPage="+i+"&userId="+userId+"' class='page_active'>"+i+"</a></li>");
					}else
					{
						sb.append("<li><a href='/member/myPageMyReview.do?currentPage="+i+"&userId="+userId+"'>"+i+"</a></li>");
					}
					
				}
				
				if(endNavi!=pageTotalCount)
				{
					sb.append("<li><a class='next' href='/member/myPageMyReview.do?currentPage="+(endNavi+1)+"&userId="+userId+"'><i class='fas fa-chevron-right'></i></a></li>");
				}
		
		return sb.toString();
	}

	private int totalSearchCount(Connection conn, String userId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query = 
		
		
		"SELECT COUNT(*) as count FROM REVIEW " + 
		"    LEFT JOIN MEMBER ON (REVIEW.USERID = MEMBER.USERID) " + 
		"WHERE REVIEW.DEL_YN='N' AND REVIEW.USERID like ?";
		
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				count = rset.getInt("count");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return count;
	}

	public int insertBoardComment(Connection conn, ReviewComment co) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "INSERT INTO REVIEW_COMMENT VALUES(REV_COMMENT_SEQ.nextval,?,?,?,sysdate,'N')";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, co.getPostNum());
			pstmt.setString(2, co.getUserId());
			pstmt.setString(3, co.getR_c_comment());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public int updateReviewComment(Connection conn, ReviewComment co) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		
		String query = "update REVIEW_COMMENT set R_C_COMMENT=? where R_C_NO=? and userid=?"; 
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, co.getR_c_comment());
			pstmt.setInt(2, co.getR_c_no());
			pstmt.setString(3, co.getUserId());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public int deleteReviewComment(Connection conn, int r_c_no, String userId) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "update REVIEW_COMMENT set R_C_DEL_YN='Y' where R_C_NO=? and userid=?"; 
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, r_c_no);
			pstmt.setString(2, userId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public int insertPostWrite(Connection conn, Review review) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "INSERT INTO review VALUES(REVIEW_Seq.NEXTVAL,?,SYSDATE,?,?,'0','0','N','N')";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, review.getUserId());
			pstmt.setString(2, review.getPostTitle());
			pstmt.setString(3, review.getPostContent());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public int searchBoardNo(Connection conn, Review review) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int postNum = 0;
		
		String query = "SELECT postnum FROM " + 
				"(SELECT ROW_NUMBER() OVER(order BY postnum DESC) AS NUM,review.*  FROM review " + 
				"WHERE userid=? AND posttitle=? AND postCONTENT=?) " + 
				"WHERE NUM = 1";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, review.getUserId());
			pstmt.setString(2, review.getPostTitle());
			pstmt.setString(3, review.getPostContent());
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				postNum = rset.getInt("postNum");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return postNum;
	}

	public int updatePost(Connection conn, Review review) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "update review set POSTCONTENT=? where POSTNUM=? and USERID=?";
		
		System.out.println("데이터 확인 : " + review.getPostContent());
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, review.getPostContent());
			pstmt.setInt(2, review.getPostNum());
			pstmt.setString(3, review.getUserId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public ArrayList<Review> selectSearchPostList(Connection conn, int currentPage, int recordCountPerPage,
			String keyword, String type) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		int start = currentPage * recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage * recordCountPerPage;
		
		String query ="";
		
		switch(type)
		{
		case "subject" : 
			query = " SELECT * " + 
					"		 FROM (SELECT ROW_NUMBER() OVER(order BY POSTNUM DESC) AS NUM, REVIEW.* ,NICK" + 
					"        FROM REVIEW " + 
					"        LEFT JOIN MEMBER ON (REVIEW.USERID=MEMBER.USERID)" + 
					"        WHERE REVIEW.DEL_YN='N' AND POSTTITLE like ?" + 
					"        ) " + 
					"		 WHERE NUM BETWEEN ? AND ?";
				break;
				
		case "writer" :
			query = " SELECT * " + 
					"		 FROM (SELECT ROW_NUMBER() OVER(order BY POSTNUM DESC) AS NUM, REVIEW.* ,NICK" + 
					"        FROM REVIEW " + 
					"        LEFT JOIN MEMBER ON (REVIEW.USERID=MEMBER.USERID)" + 
					"        WHERE REVIEW.DEL_YN='N' AND NICK like ?" + 
					"        ) " + 
					"		 WHERE NUM BETWEEN ? AND ?";
				break;
			
			
		case "all" :
			query = " SELECT * " + 
					"		 FROM (SELECT ROW_NUMBER() OVER(order BY POSTNUM DESC) AS NUM, REVIEW.* ,NICK" + 
					"        FROM REVIEW " + 
					"        LEFT JOIN MEMBER ON (REVIEW.USERID=MEMBER.USERID)" + 
					"        WHERE REVIEW.DEL_YN='N' AND (POSTTITLE like ? OR NICK like ?)" + 
					"        ) " + 
					"		 WHERE NUM BETWEEN ? AND ?";
				break;
		}
		
		try {
			pstmt = conn.prepareStatement(query);
			
			if(type.equals("all")) {
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setString(2, "%"+keyword+"%");
				pstmt.setInt(3, start);
				pstmt.setInt(4, end);
			}else {
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
			}
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Review review = new Review();
				
				review.setPostNum(rset.getInt("postNum"));
				review.setUserId(rset.getString("userId"));
				review.setRegDate(rset.getDate("regDate"));
				review.setPostTitle(rset.getString("postTitle"));
				review.setPostContent(rset.getString("postContent"));
				review.setHits(rset.getInt("hits"));
				review.setGood(rset.getInt("good"));
				review.setBest_YN(rset.getString("best_YN").charAt(0));
				review.setDel_YN(rset.getString("del_YN").charAt(0));
				review.setNick(rset.getString("nick"));
				
				list.add(review);
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
	
	public String getSearchPageNavi(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage,
			String keyword, String type) {
		
		int recordTotalCount = totalCount(conn);
		
		int pageTotalCount = 0;
		
		if((recordTotalCount % recordCountPerPage) > 0) {
			pageTotalCount = (recordTotalCount / recordCountPerPage) +1;
		}else {
			pageTotalCount = (recordTotalCount / recordCountPerPage);
		};
		
		int startNavi = (((currentPage-1) / naviCountPerPage) * naviCountPerPage) + 1;
		int endNavi = startNavi + (naviCountPerPage-1);
		
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		
		StringBuilder sb = new StringBuilder();
		
		if(startNavi!=1) {sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");}
		
		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+i+"'>"+i+"</a></li>");			
			}
			
		}
		
		if(endNavi!=pageTotalCount) {sb.append("<li><a href='/review/reviewAllSelect.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li> ");}
		
		return sb.toString();
		
	}
	
	private int totalSearchCount(Connection conn, String keyword, String type) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query ="";
		
		switch(type)
		{
		case "subject" : 
				query = "SELECT COUNT(*) as count FROM REVIEW " + 
						"    LEFT JOIN MEMBER ON (REVIEW.USERID = MEMBER.USERID) " + 
						"WHERE REVIEW.DEL_YN='N' AND POSTTITLE like ?";
				break;
			
		case "writer" :
				query = "SELECT COUNT(*) as count FROM REVIEW " + 
						"    LEFT JOIN MEMBER ON (REVIEW.USERID = MEMBER.USERID) " + 
						"WHERE REVIEW.DEL_YN='N' AND NICK like ?";
				break;
		
		case "all" :
				query = "SELECT COUNT(*) as count FROM REVIEW " + 
						"    LEFT JOIN MEMBER ON (REVIEW.USERID = MEMBER.USERID) " + 
						"WHERE REVIEW.DEL_YN='N' AND (POSTTITLE like ? OR NICK LIKE ?)";
				break;
		}
		
		try {
			pstmt = conn.prepareStatement(query);
			
			if(type.equals("all")) {
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setString(2, "%"+keyword+"%");
			}else {
				pstmt.setString(1, "%"+keyword+"%");
			}
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				count = rset.getInt("count");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return count;
	}

	

	public int deletePost(Connection conn, int postNum, String userId) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = "update review set DEL_YN='Y' where postnum=? and userid=?";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, postNum);
			pstmt.setString(2, userId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	public ArrayList<Review> selectAllBestReview(Connection conn) {
	
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		ArrayList<Review> list2 = new ArrayList<Review>();
		
		String query = "select review.*,nick from review " + 
				"left join member on (review.userid = member.userid) " + 
				"where BEST_YN='Y' " + 
				"order by postnum desc";
		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Review review = new Review();
				
				review.setPostNum(rset.getInt("postNum"));
				review.setUserId(rset.getString("userId"));
				review.setRegDate(rset.getDate("regDate"));
				review.setPostTitle(rset.getString("postTitle"));
				review.setPostContent(rset.getString("postContent"));
				review.setHits(rset.getInt("hits"));
				review.setGood(rset.getInt("good"));
				review.setBest_YN(rset.getString("best_YN").charAt(0));
				review.setDel_YN(rset.getString("del_YN").charAt(0));
				review.setNick(rset.getString("nick"));
				
				list2.add(review);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return list2;
	}
	
	public ArrayList<Review> selectMonthStamp(Connection conn, String userId, String startDate, String endDate) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		String query = "SELECT * FROM REVIEW WHERE USERID = ? AND REGDATE BETWEEN ? ||'01' AND ? ||'01'";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Review review = new Review();
				
				review.setRegDate(rset.getDate("REGDATE"));
				list.add(review);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
}




