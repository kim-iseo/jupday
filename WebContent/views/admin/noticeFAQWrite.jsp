<%@page import="kr.or.iei.member.model.vo.Member"%>
<%@page import="kr.or.iei.admin.notice.model.vo.AdminFAQ"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>관리자페이지 - 자주 묻는 질문 관리 : 글쓰기 </title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&family=Noto+Serif+KR:wght@400;500;600&display=swap" rel="stylesheet">    
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous">
<link rel="stylesheet" href="/assets/commons/default.css">
<link rel="stylesheet" href="/assets/commons/adminNavigation.css">
<link rel="stylesheet" href="/assets/commons/adminCommon.css">
<link rel="stylesheet" href="/assets/css/adminWrite.css">
<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
<script src="/assets/js/ui.js"></script>
</head>

<body>
    <div id="wrap">
        
        <%AdminFAQ adfaq = (AdminFAQ)request.getAttribute("adfaq"); %>
        <%Member m = (Member)session.getAttribute("member");%>
        <!-- navigation -->
		<%@ include file="/views/commons/header/navigationAdmin.jsp"%>
        
        <div id="header">
            <div class="admin-path">
                <p>줍데이공지</p>
                <p>자주 묻는 질문 관리</p>
            </div>

            <div class="box-user">
                <a href="/"><%=m.getNick() %>님</a>
                <a href="/member/logout.do">로그아웃</a>
            </div>
        </div>

        <div id="content">
            <div class="container">
				<form action="/admin/FAQWrite.do" method="post">
					<div class="box-write">
						<div class="box-subject">
							<input type="text" placeholder="제목을 입력하세요" name="title">
						</div>
						<div class="box-content">
							<textarea placeholder="내용을 입력하세요" name="content"></textarea>
						</div>
						<div class="box-upload">
							<label for="upload">
								<i class="far fa-image"></i>
								<span>사진첨부</span>
							</label>
							<input type="file" id="upload">
						</div>
					</div>
					<div class="box-button">
						<input type="submit" value="완료" class="btn-rec">
						<button class="btn-rec"><a href="/admin/adminFAQManageList.do">목록</a></button>
					</div>
				</form>
            </div>
        </div>
        
        <footer id="footer">
            <p>2021 ⓒ JUP DAY</p>
        </footer>
    </div>
</body>
</html>
