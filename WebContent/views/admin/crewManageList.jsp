<%@page import="kr.or.iei.admin.crew.model.dao.AdminCrewDAO"%>
<%@page import="java.util.HashMap"%>
<%@page import="kr.or.iei.crew.model.vo.Crew"%>
<%@page import="kr.or.iei.member.model.vo.Member"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>관리자페이지 - 크루 삭제 관리</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&family=Noto+Serif+KR:wght@400;500;600&display=swap" rel="stylesheet">    
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous">
<link rel="stylesheet" href="/assets/commons/default.css">
<link rel="stylesheet" href="/assets/commons/adminNavigation.css">
<link rel="stylesheet" href="/assets/commons/adminCommon.css">
<link rel="stylesheet" href="/assets/css/adminBoard.css">
<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
<script src="/assets/js/ui.js"></script>
</head>

<body>
    <div id="wrap">
    
        <!-- navigation -->
		<%@ include file="/views/commons/header/navigationAdmin.jsp"%>
        
        <div id="header">
            <div class="admin-path">
                <p>줍데이크루</p>
                <p>크루 삭제 관리</p>
            </div>
            
            <% Member m = (Member)session.getAttribute("member"); %>
            
		<%//페이징 처리 된 데이터 가져오기
		HashMap<String,Object> pageDataMap = (HashMap<String,Object>)request.getAttribute("pageDataMap");
		ArrayList<Crew> list = (ArrayList<Crew>)pageDataMap.get("list");
		String pageNavi = (String)pageDataMap.get("pageNavi"); %>
		
            <div class="box-user">
                <a href="/"><%=m.getNick() %>님</a>
                <a href="/member/logout.do">로그아웃</a>
            </div>
        </div>
        
        <div id="content">
            <div class="container">
                <div class="container_top">
                    <div class="count-all">
                        <p>전체 <span>7,777</span> 건</p>
                    </div>
                    
                   	<div class="box-search">
						<form action="">
							<div class="select-search">
								<select>
									<option>검색필터</option>
									<option value="userId">아이디</option>
	                                <option value="nick">크루장닉네임</option>
	                                <option value="crewName">크루명</option>
								</select>
								<i class="fas fa-chevron-down icon-arrow"></i>
							</div>
							<div class="input-search">
								<i class="fas fa-search icon-search"></i>
								<input type="text" name="search" placeholder="크루장(크루명)을 검색하세요" />
							</div>
							<input type="submit" class="btn-rec" value="검색">
						</form>
					</div>
                </div>

                <div class="table_wrap">
                    <table>
                        <thead>
                            <tr>
                                <th width="150">크루번호</th>
                                <th width="300">크루명</th>
                                <th width="300">크루설명</th>
                                <th width="200">회원수</th>
                                <th width="200">생성일자</th>
                                <th width="150">피드관리</th>
                                <th width="150">탈퇴 / 복원</th>
                            </tr>
                        </thead>
                        <%for(Crew cr : list) {%>
                        <tbody>
                            <tr>
                                <td><%=cr.getCrewNo() %></td>
                                <td><%=cr.getCrewName() %></td>
                                <td class="text-ellipsis"><%=cr.getCrewInfo() %></td>
                                <td><%=cr.getCrewCount() %></td>
                                <td><%=cr.getCrewCreateDate() %></td>
                                <td><button class="man_btn"><a href="/admin/crewFeedManageList.do">관리</a></button>
                                <td>
                                <%if(cr.getCrewDelYN()=='N') {%>
                                <a href="/admin/crewDelYNChange.do?c_No=<%=cr.getCrewNo()%>&c_Del_YN=<%=cr.getCrewDelYN()%>"><button class="del_btn">삭제</button></a>
                                <%}else {%>
                                <a href="/admin/crewDelYNChange.do?c_No=<%=cr.getCrewNo()%>&c_Del_YN=<%=cr.getCrewDelYN()%>"><button class="re_btn">복원</button></a>
                                <%} %>
                                </td>
                            </tr>
                        </tbody>
                        <%} %>
                    </table>

                    <div id="page_wrap">
                        <ul class="page_ul">
					        <%=pageNavi %>
                        </ul>
                    </div>                    
                    
                </div>
            </div>
        </div>
        
        <footer id="footer">
            <p>2021 ⓒ JUP DAY</p>
        </footer>
    </div>
    
    
    
    <%--<script>
	$('.del_btn').click(function(){
		
		var data = $(this).html();
		
		if(data=='Y')
		{
			window.confirm(<%=cr.getCrewName() %> + " 크루를 삭제처리하시겠습니까?");
			
		}
		
	});
	
	$('.re_btn').click(function(){
		
		var data = $(this).html();
		
		if(data=='Y')
		{
			window.confirm(<%=cr.getCrewName %> + " 크루를 복원하시겠습니끼?");	
		}
		
	});
    </script>  --%>
</body>
</html>
