<%@page import="kr.or.iei.review.model.vo.Review"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JUP:DAY | 리뷰 수정</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&family=Noto+Serif+KR:wght@400;500;600&display=swap" rel="stylesheet">    
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous">
<link rel="stylesheet" href="/assets/commons/default.css">
<link rel="stylesheet" href="/assets/commons/header.css">
<link rel="stylesheet" href="/assets/commons/footer.css">
<link rel="stylesheet" href="/assets/commons/common.css">
<link rel="stylesheet" href="/assets/css/crewWriteFeed.css">
<link rel="stylesheet" href="/assets/css/reviewWriteForm.css">
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=aa153422867c293a7226f0c302b5e6b3"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
<script src="/assets/js/ui.js"></script>
<style>
	.dot {overflow:hidden;float:left;width:12px;height:12px;background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/mini_circle.png');}    
	.dotOverlay {position:relative;bottom:10px;border-radius:6px;border: 1px solid #ccc;border-bottom:2px solid #ddd;float:left;font-size:12px;padding:5px;background:#fff;}
	.dotOverlay:nth-of-type(n) {border:0; box-shadow:0px 1px 2px #888;}    
	.number {font-weight:bold;color:#ee6152;}
	.dotOverlay:after {content:'';position:absolute;margin-left:-6px;left:50%;bottom:-8px;width:11px;height:8px;background:url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white_small.png')}
	.distanceInfo {position:relative;top:5px;left:5px;list-style:none;margin:0;}
	.distanceInfo .label {display:inline-block;width:50px;}
	.distanceInfo:after {content:none;}
</style>
</head>

<body>
	
	<div id="wrap">
	
		<!-- header -->
		<%@ include file="/views/commons/header/header.jsp"%>
		
		<%
			request.setCharacterEncoding("UTF-8");
			
			String postContent = request.getParameter("postContent");
			String postTitle = request.getParameter("postTitle");
			int postNum = Integer.parseInt(request.getParameter("postNum"));
			int currentPage = Integer.parseInt(request.getParameter("currentPage"));	
		%>
			
		<div id="content">
			<div class=box-title>
				<p class="tit-small">J U P : D A Y</p>
				<p class="tit-big">리뷰 수정</p>
			</div>
			
			<div id="review_writing">
				<form action="/review/reviewPostUpdate.do" method="post">
					<input type="hidden" name="postNum" value="<%=postNum %>"/>
					<input type="hidden" name="currentPage" value="<%=currentPage%>"/>
					<div class="box-write">
						<div class="box-subject">
							<p><%=postTitle%></p>
						</div>
						<div class="box-content">
							<textarea name="postContent" text-align='left'>
								<%=postContent %>
							</textarea>
						</div>
						<div class="box-upload">
							<label for="upload">
								<i class="far fa-image"></i>
								<span>사진첨부</span>
							</label>
							<input type="file" id="upload">
						</div>
						<div class="box-map">
							<div class="map-top">
								<i class="fas fa-map-marked-alt"></i>
								<span>위치추가</span>
								<i class="fas fa-chevron-down"></i>
							</div>
							<div class="map-content">
								<img alt="" src="/assets/images/mapAPI.PNG" style="width:100%; height:100%;">
							</div>
						</div>
					</div>
					<div class="box-button">
						<input type="submit" value="수정" class="btn-m">
						<input type="button" value="목록" class="btn-m btn-golist" onclick="location.replace('/review/reviewAllSelect.do');"/>
					</div>
					<%-- 제목까지 수정하도록 구현하려고 했으나, 안타깝게도 제목쪽 DB가 not null 이다 이거 포기하자 --%>
				</form>
			</div>
			
		</div> 
		
		<!-- footer -->
		<%@ include file="/views/commons/footer/footer.jsp"%>	
	</div>
	
</body>
</html>