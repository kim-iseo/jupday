	<%@page import="kr.or.iei.crew.model.vo.CrewBoardComment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.or.iei.crew.model.vo.CrewMember"%>
<%@page import="kr.or.iei.crew.model.vo.CrewBoard"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JUP:DAY | 크루 피드</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&family=Noto+Serif+KR:wght@400;500;600&display=swap" rel="stylesheet">    
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous">
<link rel="stylesheet" href="/assets/commons/default.css">
<link rel="stylesheet" href="/assets/commons/header.css">
<link rel="stylesheet" href="/assets/commons/footer.css">
<link rel="stylesheet" href="/assets/commons/common.css">
<link rel="stylesheet" href="/assets/css/crewOneFeed.css">
<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
<script src="/assets/js/ui.js"></script>
</head>
<body>

	<%
		CrewBoard cb = (CrewBoard)request.getAttribute("crewBoard");
		CrewMember cm = (CrewMember)request.getAttribute("crewMember");
		String crewName = (String)request.getAttribute("crewName");
		int currentPage = (int)request.getAttribute("currentPage");	
		int currentFeedPage = (int)request.getAttribute("currentFeedPage");
		
		ArrayList<CrewBoardComment> coList = cb.getCoList();
	%>

	<div id="wrap">
		<!-- header -->
		<%@ include file="/views/commons/header/header.jsp"%>
			
			
		<div id="content">
			<div class=box-title>
				<p class="tit-small">J U P : D A Y</p>
				<p class="tit-big"><%=crewName %></p>
			</div>
			
			<div class="write-top">
				<div class="box-subject">
					<p><%=cb.getFeedSubject() %></p>
				</div>
				<div class="box-writer">
					<div class="user-img">
						<%if(cb.getWriterImg()!=null) { %>
						<img alt="프로필사진" src="/upload/<%=cb.getWriterImg()%>.png">
						<%}else { %>
						<img alt="프로필사진" src="/assets/images/profile.png">
						<%} %>
					</div>
					<div class="user-name">
						<p><%=cb.getWriter() %></p>
						<span><%=cb.getFeedRegdate() %></span>
					</div>
				</div>
			</div>
			
			<div class="write-content">
				<div class="box-content">
					<p><%=cb.getFeedContent() %></p>
				</div>
				<div class="box-icon">
					<i class="far fa-heart"></i><span>좋아요 <%=cb.getFeedLikeCount() %></span>
					<i class="far fa-comment"></i><span>댓글 <%=cb.getFeedCommentCount() %></span>
				</div>
			</div>
			
			<div class="box-comment">
			
				<%if(coList.isEmpty()) {%>
					<p>현재 댓글이 없습니다. 댓글을 작성해 보세요</p>
				<%}else { %>	
					<%for(CrewBoardComment cbc:coList) {%>
						<div class="box-one-comment">
							<div class="user-img">
								<%if(cbc.getUserImg()!=null) { %>
									<img alt="프로필사진" src="/upload/<%=cbc.getUserImg()%>.png">
								<%}else { %>
									<img alt="프로필사진" src="/assets/images/profile.png">
								<%} %>
							</div>
							<div class="right-comment">
								<div class="user-comment">
									<p><%=cbc.getNick() %><span><%=cbc.getCommentRegdate() %></span></p>
								</div>
								<div class="txt-comment">
									<p><%=cbc.getCommentContent() %></p>
								</div>
								<div class="txt-comment">
									
								</div>
							</div>
							<%if(m!=null && m.getUserId().equals(cbc.getUserId())) {%>
								<div class="btn-comment">
									<button>수정</button>  
									<button>삭제</button>
								</div>
							<%} %>>
						</div>
					<%} %>
				<%} %>
				
				<div class="box-write-comment">
					<div class="user-nick">
						<p><%=m.getNick() %></p>
					</div>
					<form action="/crew/crewCommendWrite.do" method="post">
						<textarea name="comment" placeholder="댓글을 입력하세요"></textarea>
						<input type="hidden" name="crewNo" value="<%=cb.getCrewNo() %>">
						<input type="hidden" name="currentPage" value="<%=currentPage %>">
						<input type="hidden" name="feedNo" value="<%=cb.getFeedNo() %>">
						<input type="hidden" name="currentFeedPage" value="<%=currentFeedPage %>">
						<input type="submit" value="등록">
					</form>
				</div>
			</div>
			
			<div class="list-btn">
				<%if(m!=null && m.getNick().equals(cb.getWriter())) {%>
					<button class="btn-m btn-update"><a href="/crew/crewFeedValue.do?crewNo=<%=cb.getCrewNo() %>&currentPage=<%=currentPage %>&feedNo=<%=cb.getFeedNo()%>&currentFeedPage=<%=currentFeedPage%>">수정</a></button>
					<button class="btn-m btn-delete"><a href="/crew/crewDeleteFeed.do?crewNo=<%=cb.getCrewNo() %>&currentPage=<%=currentPage %>&feedNo=<%=cb.getFeedNo()%>&currentFeedPage=<%=currentFeedPage%>">삭제</a></button>
				<%} %>
				<button class="btn-m btn-golist"><a href="/crew/crewOnePage.do?crewNo=<%=cb.getCrewNo() %>&currentPage=<%=currentPage %>&currentFeedPage=<%=currentFeedPage%>">목록</a></button>
				
			</div>
			
		</div>
		
		
		
		<!-- footer -->
		<%@ include file="/views/commons/footer/footer.jsp"%>
	</div>
		
</body>
</html>