<%@page import="kr.or.iei.crew.model.vo.Crew"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>나의 크루</title>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&family=Noto+Serif+KR:wght@400;500;600&display=swap" rel="stylesheet">    
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet" href="/assets/commons/default.css">
<link rel="stylesheet" href="/assets/commons/header.css">
<link rel="stylesheet" href="/assets/commons/footer.css">
<link rel="stylesheet" href="/assets/commons/common.css">
<link rel="stylesheet" href="/assets/css/myPageMyCrew.css">
<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
<script src="/assets/js/ui.js"></script>
</head>
<body>
	<%
	//서블릿에서 보내는 값 가져오기
	ArrayList<Crew> manageList = (ArrayList<Crew>)request.getAttribute("manageList");
	ArrayList<Crew> joinList = (ArrayList<Crew>)request.getAttribute("joinList");
	%>
	
	<div id="wrap">
		<%@ include file="/views/commons/header/header.jsp"%>
		<% if(m!=null) {%>
		<div id="content">
			<div class="user-info">
				<div class=box-title>
					<p class="tit-small">J U P : D A Y</p>
					<p class="tit-big">크루 정보</p>
				</div>
				<div>
					<div class="info">
						<div class="crew">
							<div class="tit-crew">운영 크루</div>
							<%if(manageList.isEmpty()) {%>
							<div class="crew-area-empty"> <p class="crew-empty">운영중이신 크루가 존재하지 않습니다.<p> </div>
							<% } else { %>
								<%for(Crew c:manageList) {%>
							<div class="crew-area">
								<%if(c.getCrewImg()!=null) { %>
								<div class="crew-image"><img alt="크루이미지" src="/upload/<%=c.getCrewImg()%>.png"></div>
								<%}else { %>
								<div class="crew-image"><img alt="크루이미지" src="/assets/images/crew.png"></div>
								<%} %>
								<div class="crew-info">
									<p class="crew-name"><%=c.getCrewName() %></p>
									<p class="crew-reg"><%=c.getCrewCreateDate() %></p>
								</div>
								<div class="crew-button"><button class="btn-s"><a href="/views/crew/crewSetting.jsp?crewNo=<%=c.getCrewNo()%>">설정</a></button></div>
								</div>
							<%} %>	
							<% } %>
						</div>
						
							<div class="crew">
							<div class="tit-crew">가입 크루</div>
							<%if(joinList.isEmpty()) {%>
							<div class="crew-area-empty"> <p class="crew-empty">가입중이신 크루가 존재하지 않습니다.<p> </div>
							<% } else { %>
							<%for(Crew c:joinList) {%>
							<div class="crew-area">
								<%if(c.getCrewImg()!=null) { %>
								<div class="crew-image"><img alt="크루이미지" src="<%=c.getCrewImg()%>"></div>
								<%}else { %>
								<div class="crew-image"><img alt="크루이미지" src="/assets/images/crew.png"></div>
								<%} %>
								<div class="crew-info">
									<p class="crew-name"><%=c.getCrewName() %></p>
									<p class="crew-reg"><%=c.getCrewCreateDate() %></p>
								</div>
								<div class="crew-button"><button class="btn-s"><a href="/crew/crewOnePage.do?crewNo=<%=c.getCrewNo()%>&currentPage=1">이동</a></button></div>
								</div>
							<%} %>	
							<% } %>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%@ include file="/views/commons/footer/footer.jsp"%>
	</div>
	
		<% } else { %>
			<script>
				location.replace("/views/member/memberLogin.jsp");
			</script>
		<%}  %>

</body>
</html>