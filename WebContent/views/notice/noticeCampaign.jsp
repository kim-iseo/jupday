<%@page import="kr.or.iei.notice.model.vo.NoticeCampaign"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JUP:DAY | 줍데이 소식</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&family=Noto+Serif+KR:wght@400;500;600&display=swap" rel="stylesheet">    
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous">
<link rel="stylesheet" href="/assets/commons/default.css">
<link rel="stylesheet" href="/assets/commons/header.css">
<link rel="stylesheet" href="/assets/commons/footer.css">
<link rel="stylesheet" href="/assets/commons/common.css">
<link rel="stylesheet" href="/assets/css/noticeCampaign.css">
<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
<script src="/assets/js/ui.js"></script>
</head>

<body>

<%


HashMap<String,Object> pageDataMap = (HashMap<String,Object>)request.getAttribute("pageDataMap");

ArrayList<NoticeCampaign> list = (ArrayList<NoticeCampaign>)pageDataMap.get("list");//꺼내준다.
String pageNavi = (String)pageDataMap.get("pageNavi");//꺼내준다
int currentPage =(int)request.getAttribute("currentPage");
%>

   <div id="wrap">
   
      <!-- header -->
      <%@ include file="/views/commons/header/header.jsp"%>

      <div id="content">
         <div class="box-title">
            <p class="tit-small">J U P : D A Y</p>
            <p class="tit-big">줍데이소식</p>
         </div>
         
         <div class="notice-top">
         	 <div class="box-button">
				<button class="btn-notice">
					<a href="/views/notice/noticeNewsAllSelect.do">공지사항</a>
				</button>
				<button class="btn-campaign">
					<a href="/notice/noticeCampaignAllSelect.do">캠페인 안내</a>
				</button>
			 </div>
		 </div>
		
        <div class="area-campaign">
        	<div class="box-search">
				<form  action="/notice/noticeCampaignSearch.do" method="get">
					<div class="select-search">
						<select name="type">
							<option value="subject">글제목</option>
							<option value="content">글내용</option>
							<option value="all">제목+내용</option>
						</select>
						<i class="fas fa-chevron-down icon-arrow"></i>
					</div>
					<div class="input-search">
						<i class="fas fa-search icon-search"></i><!--/// 검색 기능 출발점2 ///-->
						<input type="text" name="keyword" placeholder="검색어를 검색하세요">
					</div>
					<input type="submit" class="btn-rec" value="검색">
				</form>
			 </div>
			 
			 <div class="box-campaign-list">
			     <%for(NoticeCampaign noticeCampaign : list){%>			        
			 	<div class="box-campaign">
			 		<a href="/notice/CampaignContent.do?campaignNo=<%=noticeCampaign.getCampaignNo()%>&currentPage=<%=currentPage%>"><!-- 누르면 이동 -->
			 			<div class="img-campaign">
				 			<img alt="" src="/assets/images/campaign01.jpg">
				 		</div>
				 		<div class="txt-campaign">
				 			<p><%=noticeCampaign.getCampaignTitle() %></p>
				 			<p><%=noticeCampaign.getCampaignRegDate() %></p>
				 		</div>
			 		</a>
			 	</div>
			 	 <% }%>
			 </div>
			 
			 <div id="page_wrap">
		        <ul class="page_ul">
		            <li><%=pageNavi %></li>
		        </ul>
		    </div>
        </div> 
         
         
      </div>
      
      <!-- footer -->
      <%@ include file="/views/commons/footer/footer.jsp"%>
   </div>

</body>
</html>