package kr.or.iei.notice.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.crew.model.vo.Crew;
import kr.or.iei.notice.model.vo.AnswerFAQ;
import kr.or.iei.notice.model.vo.BoxFAQ;
import kr.or.iei.notice.model.vo.Notice;
import kr.or.iei.notice.model.vo.NoticeCampaign;
import kr.or.iei.notice.model.vo.QuestionFAQ;

public class NoticeDAO {

	public ArrayList<Notice> selectAllPostPageList(Connection conn, int currentPage, int recordCountPerPage) {

		PreparedStatement pstmt = null;
		ResultSet rset=null;
		
		ArrayList<Notice> list = new ArrayList<Notice>();
		
		/*
		   --TOP-N 분석--
		   --전체적인 순위를 만들고, 그 순위에서 가장 최상위에서 몇번째 가지
		   
		   --1page=>NUM이 1~5까지의 글을 가져오면 됨
		   --2Page=>NUM이 6~10까지의 글을 가져오면 됨
		   --3Page=>NUM이 11~15까지의 글을 가져오면 됨
		   
		   --공식
		   --START = 현재 페이지 * 목록개수 -(목록개수-1)
		   --END=현재페이지 * 목록개수
		   
		   --ex)만약 1페이지라면 (목록개수 5개)
		   --START=1*5-(5-1)=>1
		   --END = 1*5=>5
		   
		   --ex)만약 2페이지라면 (목록개수 5개)
		   --START=2*5-(5-1)=>6
		   --END = 2*5=>10
		   
		  --ex)만약 3페이지라면 (목록개수 5개)
		   --START=3*5-(5-1)=>11
		   --END = 3*5=>15  
		*/
		
		int start = currentPage*recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage*recordCountPerPage;
		
		
		
		String query ="select *" + 
				"		from (select row_number() over(order by n_no desc)as num, NOTICE.* " + 
				"		from NOTICE " + 
				"		where NOTICE.N_DEL_YN='N') " + 
				"		where num between ? and ?";
		
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();//실행
			
			while(rset.next()) {
				
				Notice notice = new Notice();
				
				notice.setNoticeNo(rset.getInt("N_NO"));
				notice.setNoticeWriter(rset.getString("N_WRITER"));
				notice.setNoticeTitle(rset.getString("N_TITLE"));
				notice.setNoticeContent(rset.getString("N_CONTENT"));
				notice.setNoticeRegDate(rset.getDate("N_REGDATE"));
				notice.setNoticeHits(rset.getInt("N_HITS"));
				notice.setDelYN(rset.getString("N_DEL_YN").charAt(0));

				
				list.add(notice);
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
		// 총 페이지는 몇개의 페이지로 만들어져야 하는지가 나와야한다.
		// 총 포스트가 몇개인지 확인해야합니다.
		// SELECT COUNT(*)FROM NOTICE ;	
		// 14개
		
		
		int recordTotalCount = totalCount(conn);//카운터(전체 게시글 수) 값이 들어온다 (14개)
		
       int pageTotalCount = 0; //전체 페이지 개수
       //만약 post수가 5개 라면? -> 1page
       //만약 post수가 6개 라면? -> 2page
       //만약 post수가 14개라면? -> 2page
		
		if((recordTotalCount % recordCountPerPage)>0) {//나머지가 있으면  14%10
			pageTotalCount = (recordTotalCount / recordCountPerPage)+1;//나머지가 있으니 21+1 22페이지
		}else {//나머지가 있으면
			pageTotalCount = (recordTotalCount / recordCountPerPage);//나머지가 없으면
		}
		
		/* 현재 page가 1이라면 - startNavi 값은 : 1,-endNavi 값은 : 5
		 * 현재 page가 3이라면 - startNavi 값은 : 1,-endNavi 값은 : 5
		 * 현재 page가 7이라면 - startNavi 값은 : 6,-endNavi 값은 : 10
		 * 
		 * startNavi 공식
		 * startNavi=((현재 페이지-1)/ ((Navi당 보여질 개수))* Navi 당 보여질 갯수) +1
		 * 
		 * 코드로 표현
		 * startNavi = ((currentPage-1) / naviCountPerPage) * naviCountPerPage)+1
		 * ex) (((1-1)/5)*5)+1->1
		 * ex) (((3-1)/5)*5)+1->1
		 * ex) (((7-1)/5)*5)+1->6
		 * 
		 * endNavi 공식
		 * endNavi = startNavi +(naviCounterPerPage-1)
		 * 
		 * ex) 1+ (5-1) ->5
		 * ex) 6+ (5-1) ->10
		 * 
		 * 
		 * */
		
		int startNavi = (((currentPage-1)/naviCountPerPage)*naviCountPerPage)+1;
		int endNavi = startNavi+(naviCountPerPage-1);
		
		
		//만약 공식으로 구한 endNavi가 총페이지 수 보다 크다면 총 페이지 수로 세팅하여라
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		
		
		
		/*pageNavi 모양 만들기
		 * 1단계 모양
		 * 만약 1page 라면: 1 2 3 4 5 모양을 먼저 만들도록 하겠습니다.
		 * 만약 7page 라면: 6 7 8 9 10 모양을 먼저 만들도록 하겠습니다.
		 * 
		 * 2단계 모양
		 *  만약 1page 라면 
		 *   <a href="">1</a> <a href="">2</a> <a href="">3</a> <a href="">4</a> <a href="">5</a>
		 * 
		 * 3단계 모양
		 * 			현재 페이지를 강조하는 느낌을 부여
		 * 만약 현재 3Page 라면 
		 *     <a href="">1</a> <a href="">2</a> <B style='font-size:1.2em'>3</B></a> <a href="">4</a>
		 * 
		 * 4단계 모양
		 *  <이전   1 2 3 4 5  다음 >
		 * 
		 * 5단계 모양 - 불필요한 요소 없애기 
		 *         즉, 첫번째 페이지에서는 "< 이전" 이라는 모양은 불필요 함
		 *             마지막 페이지에서는 "다음 >" 이라는 모양은 불필요함 
		 * 
		 * 6단계 - 링크 부여하기 
		 *        링크 주소를  <a href='/board/boardAllSelect.do?currentPage="+i+"'> 넣어준다
		 * 
		 * */
		
	
		
		
		//pageNavi 모양 만들기
		StringBuilder sb = new StringBuilder();

		if(startNavi!=1) {
			sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
		}

		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+i+"'>"+i+"</a></li>");
			}
		}

		if(endNavi!=pageTotalCount) {
			sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
		}
		
		return sb.toString();//toString String으로 꺼내겠다는 이야기에 타입이 String이다.
		
		
	}
	
	public int totalCount(Connection conn)
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		//as count 컬럼 이름을 붙인것
		String query = "select count(*) as count from NOTICE where N_DEL_YN='N'";
		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {//count라는 이름의 칼럼을 꺼낸것
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

	public Notice selectAllOnePost(Connection conn, int noticeNo) {
		//제일 먼저 필요한것은 PreparedStatement
		 PreparedStatement pstmt = null;
		 ResultSet rset = null;
		 
		 Notice notice = null;
		 
		 String query = "SELECT NOTICE.*FROM NOTICE WHERE N_NO=? and NOTICE.N_DEL_YN='N'";
		
		 try {
			pstmt = conn.prepareStatement(query);		
			pstmt.setInt(1, noticeNo);
			
			rset=pstmt.executeQuery();
			
			if(rset.next()) {
				
		        notice = new Notice();
				
				notice.setNoticeNo(rset.getInt("N_NO"));
				notice.setNoticeWriter(rset.getString("N_WRITER"));
				notice.setNoticeTitle(rset.getString("N_TITLE"));
				notice.setNoticeContent(rset.getString("N_CONTENT"));
				notice.setNoticeRegDate(rset.getDate("N_REGDATE"));
				notice.setNoticeHits(rset.getInt("N_HITS"));
				notice.setDelYN(rset.getString("N_DEL_YN").charAt(0));


				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		 
		 return notice;
		
		
	}
	
	
	
	

	public ArrayList<NoticeCampaign> selectAllCampaignPostList(Connection conn, int currentPage, int recordCountPerPage) {
		
		
		PreparedStatement pstmt = null;
		ResultSet rset=null;
		
		ArrayList<NoticeCampaign> list = new ArrayList<NoticeCampaign>();
		
		
		int start = currentPage*recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage*recordCountPerPage;
		
		
		
		String query = "select *" + 
				"		from (select row_number() over(order by NC_NO desc)as num, NOTICE_CAMPAIGN.* " + 
				"		from NOTICE_CAMPAIGN " + 
				"		where NOTICE_CAMPAIGN.NC_DEL_YN='N' " + 
				"       ) " +
				"		where num between ? and ?";
		
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rset = pstmt.executeQuery();//실행
			
			while(rset.next()) {
				
				NoticeCampaign noticeCampaign = new NoticeCampaign();
				
				noticeCampaign.setCampaignNo(rset.getInt("NC_NO"));
				noticeCampaign.setCampaignWriter(rset.getString("NC_WRITER"));
				noticeCampaign.setCampaignTitle(rset.getString("NC_TITLE"));
				noticeCampaign.setCampaignContent(rset.getString("NC_CONTENT"));
				noticeCampaign.setCampaignRegDate(rset.getDate("NC_REGDATE"));
				noticeCampaign.setCampaignHits(rset.getInt("NC_HITS"));
				noticeCampaign.setDelYN(rset.getString("NC_DEL_YN").charAt(0));
				
				list.add(noticeCampaign);
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
	

	public String getPageNavi2(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage) {
		// 총 페이지는 몇개의 페이지로 만들어져야 하는지가 나와야한다.
		// 총 포스트가 몇개인지 확인해야합니다.
		// SELECT COUNT(*)FROM NOTICE ;	
		// 14개
		
		
		int recordTotalCount = totalCount2(conn);//카운터(전체 게시글 수) 값이 들어온다 (14개)
		
       int pageTotalCount = 0; //전체 페이지 개수
       //만약 post수가 5개 라면? -> 1page
       //만약 post수가 6개 라면? -> 2page
       //만약 post수가 14개라면? -> 2page
		
		if((recordTotalCount % recordCountPerPage)>0) {//나머지가 있으면  14%10
			pageTotalCount = (recordTotalCount / recordCountPerPage)+1;//나머지가 있으니 21+1 22페이지
		}else {//나머지가 있으면
			pageTotalCount = (recordTotalCount / recordCountPerPage);//나머지가 없으면
		}
		
		/* 현재 page가 1이라면 - startNavi 값은 : 1,-endNavi 값은 : 5
		 * 현재 page가 3이라면 - startNavi 값은 : 1,-endNavi 값은 : 5
		 * 현재 page가 7이라면 - startNavi 값은 : 6,-endNavi 값은 : 10
		 * 
		 * startNavi 공식
		 * startNavi=((현재 페이지-1)/ ((Navi당 보여질 개수))* Navi 당 보여질 갯수) +1
		 * 
		 * 코드로 표현
		 * startNavi = ((currentPage-1) / naviCountPerPage) * naviCountPerPage)+1
		 * ex) (((1-1)/5)*5)+1->1
		 * ex) (((3-1)/5)*5)+1->1
		 * ex) (((7-1)/5)*5)+1->6
		 * 
		 * endNavi 공식
		 * endNavi = startNavi +(naviCounterPerPage-1)
		 * 
		 * ex) 1+ (5-1) ->5
		 * ex) 6+ (5-1) ->10
		 * 
		 * 
		 * */
		
		int startNavi = (((currentPage-1)/naviCountPerPage)*naviCountPerPage)+1;
		int endNavi = startNavi+(naviCountPerPage-1);
		
		
		//만약 공식으로 구한 endNavi가 총페이지 수 보다 크다면 총 페이지 수로 세팅하여라
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		
		
		
		/*pageNavi 모양 만들기
		 * 1단계 모양
		 * 만약 1page 라면: 1 2 3 4 5 모양을 먼저 만들도록 하겠습니다.
		 * 만약 7page 라면: 6 7 8 9 10 모양을 먼저 만들도록 하겠습니다.
		 * 
		 * 2단계 모양
		 *  만약 1page 라면 
		 *   <a href="">1</a> <a href="">2</a> <a href="">3</a> <a href="">4</a> <a href="">5</a>
		 * 
		 * 3단계 모양
		 * 			현재 페이지를 강조하는 느낌을 부여
		 * 만약 현재 3Page 라면 
		 *     <a href="">1</a> <a href="">2</a> <B style='font-size:1.2em'>3</B></a> <a href="">4</a>
		 * 
		 * 4단계 모양
		 *  <이전   1 2 3 4 5  다음 >
		 * 
		 * 5단계 모양 - 불필요한 요소 없애기 
		 *         즉, 첫번째 페이지에서는 "< 이전" 이라는 모양은 불필요 함
		 *             마지막 페이지에서는 "다음 >" 이라는 모양은 불필요함 
		 * 
		 * 6단계 - 링크 부여하기 
		 *        링크 주소를  <a href='/board/boardAllSelect.do?currentPage="+i+"'> 넣어준다
		 * 
		 * */
		
	
		
		
		//pageNavi 모양 만들기
		StringBuilder sb = new StringBuilder();

		if(startNavi!=1) {
			sb.append("<li><a href='/notice/noticeCampaignAllSelect.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
		}

		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/notice/noticeCampaignAllSelect.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/notice/noticeCampaignAllSelect.do?currentPage="+i+"'>"+i+"</a></li>");
			}
		}

		if(endNavi!=pageTotalCount) {
			sb.append("<li><a href='/notice/noticeCampaignAllSelect.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
		}
		
		return sb.toString();//toString String으로 꺼내겠다는 이야기에 타입이 String이다.
		
		
	}
	
	public int totalCount2(Connection conn)
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		//as count 컬럼 이름을 붙인것
		String query = "select count(*) as count from NOTICE_CAMPAIGN where NC_DEL_YN='N'";
		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {//count라는 이름의 칼럼을 꺼낸것
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

	public NoticeCampaign selectOneCampaignPost(Connection conn, int campaignNo) {
		
		//제일 먼저 필요한것은 PreparedStatement
		 PreparedStatement pstmt = null;
		 ResultSet rset = null;
		 
		 NoticeCampaign noticeCampaign = null;
		 
		 String query = "SELECT NOTICE_CAMPAIGN.*FROM NOTICE_CAMPAIGN WHERE NC_NO=? and NOTICE_CAMPAIGN.NC_DEL_YN='N'";
		
		 try {
			pstmt = conn.prepareStatement(query);		
			pstmt.setInt(1, campaignNo);
			
			rset=pstmt.executeQuery();
			
			if(rset.next()) {

				noticeCampaign = new NoticeCampaign();
				
				noticeCampaign.setCampaignNo(rset.getInt("NC_NO"));
				noticeCampaign.setCampaignWriter(rset.getString("NC_WRITER"));
				noticeCampaign.setCampaignTitle(rset.getString("NC_TITLE"));
				noticeCampaign.setCampaignContent(rset.getString("NC_CONTENT"));
				noticeCampaign.setCampaignRegDate(rset.getDate("NC_REGDATE"));
				noticeCampaign.setCampaignHits(rset.getInt("NC_HITS"));
				noticeCampaign.setDelYN(rset.getString("NC_DEL_YN").charAt(0));

				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		 
		 return noticeCampaign;
	}

public ArrayList<Notice> showNotice(Connection conn) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Notice> list = new ArrayList<Notice>();
		
		String query =  "SELECT * FROM ( " +
                		"SELECT ROW_NUMBER() OVER(ORDER BY N_NO DESC)AS NUM, NOTICE.* " +
                		"FROM NOTICE " +
                		"WHERE N_DEL_YN='N') " +
                		"WHERE NUM BETWEEN 1 AND 3";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				Notice n = new Notice();
		
				n.setNoticeNo(rset.getInt("n_no"));
				n.setNoticeTitle(rset.getString("n_title"));
				n.setNoticeContent(rset.getString("n_content"));
				n.setNoticeRegDate(rset.getDate("n_regDate"));
				
				list.add(n);
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
public ArrayList<Notice> selectSearchNewsContentList(Connection conn, int currentPage, int recordCountPerPage,
		String keyword, String type) {
	
	PreparedStatement pstmt = null;
	ResultSet rset=null;
	ArrayList<Notice> list = new ArrayList<Notice>();
	
	
	int start = currentPage*recordCountPerPage - (recordCountPerPage-1);
	int end = currentPage*recordCountPerPage;
	
	String query="";
	
	switch(type)
	{//제목-3
	case "subject" :
		query=  "select *" + 
				"		from (select row_number() over(order by N_NO desc)as num, NOTICE.* " + 
				"		from NOTICE " + 
				"		where N_TITLE like ? and NOTICE.N_DEL_YN='N') " + 
				"		where num between ? and ?"; 

		break;
		//내용-3
	case "content" :
		query=  "select *" + 
				"		from (select row_number() over(order by N_NO desc)as num, NOTICE.* " + 
				"		from NOTICE " + 
				"		where N_CONTENT like ? and NOTICE.N_DEL_YN='N') " + 
				"		where num between ? and ?"; 
		break;
		
	case "all" :
		 query = "select *" + 
				 "		from (select row_number() over(order by N_NO desc)as num, NOTICE.* " + 
				 "		from NOTICE " + 
				 "		where N_CONTENT like ? or N_Title LIKE ? and NOTICE.N_DEL_YN='N') " + 
				 "		where num between ? and ?"; 
		 
		 break;
	}
	
	
	try {
		pstmt = conn.prepareStatement(query);
		
		if(type.equals("all"))
		{
			pstmt.setString(1, "%"+keyword+"%");
			pstmt.setString(2, "%"+keyword+"%");
			pstmt.setInt(3, start);
			pstmt.setInt(4, end);
			
		}else 
		{
			pstmt.setString(1, "%"+keyword+"%");
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
		}

		
		rset = pstmt.executeQuery();//실행
		
		while(rset.next()) {
			
			Notice notice = new Notice();
			
			notice.setNoticeNo(rset.getInt("N_NO"));
			notice.setNoticeWriter(rset.getString("N_WRITER"));
			notice.setNoticeTitle(rset.getString("N_TITLE"));
			notice.setNoticeContent(rset.getString("N_CONTENT"));
			notice.setNoticeRegDate(rset.getDate("N_REGDATE"));
			notice.setNoticeHits(rset.getInt("N_HITS"));
			notice.setDelYN(rset.getString("N_DEL_YN").charAt(0));

			
			list.add(notice);
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

	   int recordTotalCount = totalSearchCount(conn,keyword,type);
		
       int pageTotalCount = 0; //전체 페이지 개수

		
		if((recordTotalCount % recordCountPerPage)>0) {
			pageTotalCount = (recordTotalCount / recordCountPerPage)+1;
		}else {
			pageTotalCount = (recordTotalCount / recordCountPerPage);
		}
		
		
		int startNavi = (((currentPage-1)/naviCountPerPage)*naviCountPerPage)+1;
		int endNavi = startNavi+(naviCountPerPage-1);
		
		
		//만약 공식으로 구한 endNavi가 총페이지 수 보다 크다면 총 페이지 수로 세팅하여라
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		
		
			
		//pageNavi 모양 만들기
		StringBuilder sb = new StringBuilder();

		if(startNavi!=1) {
			sb.append("<li><a href='/notice/noticeNewsSearch.do?currentPage="+(startNavi-1)+"&keyword="+keyword+"&type="+type+"'><i class='fas fa-chevron-left'></i></a></li>");
		}

		for(int i=startNavi; i<=endNavi; i++) {
			
			if(i==currentPage) {
				sb.append("<li><a href='/notice/noticeNewsSearch.do?currentPage="+i+"&keyword="+keyword+"&type="+type+"' class='page_active'>"+i+"</a></li>");
			}else {
				sb.append("<li><a href='/notice/noticeNewsSearch.do?currentPage="+i+"&keyword="+keyword+"&type="+type+"'>"+i+"</a></li>");
			}
		}

		if(endNavi!=pageTotalCount) {
			sb.append("<li><a href='/notice/noticeNewsSearch.do?currentPage="+(endNavi+1)+"&keyword="+keyword+"&type="+type+"'><i class='fas fa-chevron-right'></i></a></li>");
		}

		//System.out.println(sb.toString());
		
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
			   query ="SELECT COUNT(*) as count FROM NOTICE WHERE N_TITLE LIKE ? and NOTICE.N_DEL_YN='N'";			   		
			   break;
			   
		case "content" :
			   query="SELECT COUNT(*) as count FROM NOTICE WHERE N_CONTENT LIKE ? and NOTICE.N_DEL_YN='N'";
			   break;
			   
		case "all" : 
			   query="SELECT COUNT(*) as count FROM NOTICE WHERE N_TITLE LIKE ? and NOTICE.N_DEL_YN='N'" +
			         "  or N_CONTENT LIKE ? and NOTICE.N_DEL_YN='N'";
			   break;		
		}
		
		
		try {
			pstmt = conn.prepareStatement(query);
			
			if(type.equals("all"))
			{
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setString(2, "%"+keyword+"%");
			}else
			{
				pstmt.setString(1, "%"+keyword+"%");
			}
			

			rset = pstmt.executeQuery();
			
			if(rset.next()) {//count라는 이름의 칼럼을 꺼낸것
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

	public ArrayList<NoticeCampaign> selectSearchCampaignContentList(Connection conn, int currentPage, int recordCountPerPage,
		String keyword, String type) {
		PreparedStatement pstmt = null;
		ResultSet rset=null;
		ArrayList<NoticeCampaign> list = new ArrayList<NoticeCampaign>();
		
		
		int start = currentPage*recordCountPerPage - (recordCountPerPage-1);
		int end = currentPage*recordCountPerPage;
		
		String query="";
		
		switch(type)
		{//제목-3
		case "subject" :
			query=  "select *" + 
					"		from (select row_number() over(order by NC_NO desc)as num, NOTICE_CAMPAIGN.* " + 
					"		from NOTICE_CAMPAIGN " + 
					"		where NC_TITLE like ? and NOTICE_CAMPAIGN.NC_DEL_YN='N') " + 
					"		where num between ? and ?"; 

			break;
			//내용-3
		case "content" :
			query=  "select *" + 
					"		from (select row_number() over(order by NC_NO desc)as num, NOTICE_CAMPAIGN.* " + 
					"		from NOTICE_CAMPAIGN " + 
					"		where NC_CONTENT like ? and NOTICE_CAMPAIGN.NC_DEL_YN='N') " + 
					"		where num between ? and ?"; 
			break;
			
		case "all" :
			 query = "select *" + 
					 "		from (select row_number() over(order by NC_NO desc)as num, NOTICE_CAMPAIGN.* " + 
					 "		from NOTICE_CAMPAIGN " + 
					 "		where NC_CONTENT like ? or NC_TITLE LIKE ? and NOTICE_CAMPAIGN.NC_DEL_YN='N') " + 
					 "		where num between ? and ?"; 
			 
			 break;
		}
		
		
		try {
			pstmt = conn.prepareStatement(query);
			
			if(type.equals("all"))
			{
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setString(2, "%"+keyword+"%");
				pstmt.setInt(3, start);
				pstmt.setInt(4, end);
				
			}else 
			{
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
			}

			
			rset = pstmt.executeQuery();//실행
			
			while(rset.next()) {
				
				NoticeCampaign noticeCampaign = new NoticeCampaign();
				
				noticeCampaign.setCampaignNo(rset.getInt("NC_NO"));
				noticeCampaign.setCampaignWriter(rset.getString("NC_WRITER"));
				noticeCampaign.setCampaignTitle(rset.getString("NC_TITLE"));
				noticeCampaign.setCampaignContent(rset.getString("NC_CONTENT"));
				noticeCampaign.setCampaignRegDate(rset.getDate("NC_REGDATE"));
				noticeCampaign.setCampaignHits(rset.getInt("NC_HITS"));
				noticeCampaign.setDelYN(rset.getString("NC_DEL_YN").charAt(0));

				
				list.add(noticeCampaign);
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

	public String getSearchPageNavi2(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage,
			String keyword, String type) {
		 int recordTotalCount = totalSearchCount2(conn,keyword,type);
			
	       int pageTotalCount = 0; //전체 페이지 개수

			
			if((recordTotalCount % recordCountPerPage)>0) {
				pageTotalCount = (recordTotalCount / recordCountPerPage)+1;
			}else {
				pageTotalCount = (recordTotalCount / recordCountPerPage);
			}
			
			
			int startNavi = (((currentPage-1)/naviCountPerPage)*naviCountPerPage)+1;
			int endNavi = startNavi+(naviCountPerPage-1);
			
			
			//만약 공식으로 구한 endNavi가 총페이지 수 보다 크다면 총 페이지 수로 세팅하여라
			if(endNavi > pageTotalCount) {
				endNavi = pageTotalCount;
			}
			
			
				
			//pageNavi 모양 만들기
			StringBuilder sb = new StringBuilder();

			if(startNavi!=1) {
				sb.append("<li><a href='/notice/noticeCampaignSearch.do?currentPage="+(startNavi-1)+"&keyword="+keyword+"&type="+type+"'><i class='fas fa-chevron-left'></i></a></li>");
			}

			for(int i=startNavi; i<=endNavi; i++) {
				
				if(i==currentPage) {
					sb.append("<li><a href='/notice/noticeCampaignSearch.do?currentPage="+i+"&keyword="+keyword+"&type="+type+"' class='page_active'>"+i+"</a></li>");
				}else {
					sb.append("<li><a href='/notice/noticeCampaignSearch.do?currentPage="+i+"&keyword="+keyword+"&type="+type+"'>"+i+"</a></li>");
				}
			}

			if(endNavi!=pageTotalCount) {
				sb.append("<li><a href='/notice/noticeCampaignSearch.do?currentPage="+(endNavi+1)+"&keyword="+keyword+"&type="+type+"'><i class='fas fa-chevron-right'></i></a></li>");
			}

			
			return sb.toString();
	}


	
private int totalSearchCount2(Connection conn, String keyword, String type) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int count = 0;
		
		String query ="";
		
		switch(type)
		{
		case "subject" :
			   query ="SELECT COUNT(*) as count FROM NOTICE_CAMPAIGN WHERE NC_TITLE LIKE ? and NOTICE_CAMPAIGN.NC_DEL_YN='N'";			   		
			   break;
			   
		case "content" :
			   query="SELECT COUNT(*) as count FROM NOTICE_CAMPAIGN WHERE NC_CONTENT LIKE ? and NOTICE_CAMPAIGN.NC_DEL_YN='N'";
			   break;
			   
		case "all" : 
			   query="SELECT COUNT(*) as count FROM NOTICE_CAMPAIGN WHERE NC_TITLE LIKE ?" +
			         "  or NC_CONTENT LIKE ? and NOTICE_CAMPAIGN.NC_DEL_YN='N'";
			   break;		
		}
		
		
		try {
			pstmt = conn.prepareStatement(query);
			
			if(type.equals("all"))
			{
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setString(2, "%"+keyword+"%");
			}else
			{
				pstmt.setString(1, "%"+keyword+"%");
			}
			

			rset = pstmt.executeQuery();
			
			if(rset.next()) {//count라는 이름의 칼럼을 꺼낸것
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

public ArrayList<BoxFAQ> selectAllFAQPageList(Connection conn, int currentPage, int recordCountPerPage) {

	PreparedStatement pstmt = null;
	ResultSet rset=null;
	
	ArrayList<BoxFAQ> list = new ArrayList<BoxFAQ>();
	
	int start = currentPage*recordCountPerPage - (recordCountPerPage-1);
	int end = currentPage*recordCountPerPage;
	
	
	
	String query ="select *" + 
			"		from (select row_number() over(order by FAQ_NO)as num, FAQBOX.* " + 
			"		from FAQBOX " + 
			"		where FAQBOX.FAQ_DEL_YN='N') " + 
			"		where num between ? and ?";
	
	
	try {
		pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, start);
		pstmt.setInt(2, end);
		
		rset = pstmt.executeQuery();//실행
		
		while(rset.next()) {
			
			BoxFAQ boxFAQ = new BoxFAQ();
			
			boxFAQ.setBoxFAQNo(rset.getInt("FAQ_NO"));
			boxFAQ.setBoxAnswerFAQTitle(rset.getString("FAQ_TITLE"));
			boxFAQ.setBoxFAQContent(rset.getString("FAQ_CONTENT"));
			boxFAQ.setDelYN(rset.getString("FAQ_DEL_YN").charAt(0));

			
			list.add(boxFAQ);
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

public String getPageNavi3(Connection conn, int naviCountPerPage, int recordCountPerPage, int currentPage) {

	int recordTotalCount = totalCount3(conn);
	
   int pageTotalCount = 0; //전체 페이지 개수
   //만약 post수가 5개 라면? -> 1page
   //만약 post수가 6개 라면? -> 2page
   //만약 post수가 14개라면? -> 2page
	
	if((recordTotalCount % recordCountPerPage)>0) {//나머지가 있으면  14%10
		pageTotalCount = (recordTotalCount / recordCountPerPage)+1;//나머지가 있으니 21+1 22페이지
	}else {//나머지가 있으면
		pageTotalCount = (recordTotalCount / recordCountPerPage);//나머지가 없으면
	}
	

	
	int startNavi = (((currentPage-1)/naviCountPerPage)*naviCountPerPage)+1;
	int endNavi = startNavi+(naviCountPerPage-1);
	
	
	//만약 공식으로 구한 endNavi가 총페이지 수 보다 크다면 총 페이지 수로 세팅하여라
	if(endNavi > pageTotalCount) {
		endNavi = pageTotalCount;
	}
	
	
	
	
	
	//pageNavi 모양 만들기
	StringBuilder sb = new StringBuilder();

	if(startNavi!=1) {
		sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+(startNavi-1)+"'><i class='fas fa-chevron-left'></i></a></li>");
	}

	for(int i=startNavi; i<=endNavi; i++) {
		
		if(i==currentPage) {
			sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+i+"' class='page_active'>"+i+"</a></li>");
		}else {
			sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+i+"'>"+i+"</a></li>");
		}
	}

	if(endNavi!=pageTotalCount) {
		sb.append("<li><a href='/views/notice/noticeNewsAllSelect.do?currentPage="+(endNavi+1)+"'><i class='fas fa-chevron-right'></i></a></li>");
	}
	
	return sb.toString();
	
}
public int totalCount3(Connection conn)
{
	PreparedStatement pstmt = null;
	ResultSet rset = null;
	
	int count = 0;
	//as count 컬럼 이름을 붙인것
	String query = "select count(*) as count from FAQBOX where FAQ_DEL_YN='N'";
	
	try {
		pstmt = conn.prepareStatement(query);
		rset = pstmt.executeQuery();
		
		if(rset.next()) {//count라는 이름의 칼럼을 꺼낸것
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



