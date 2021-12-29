<%@page import="kr.or.iei.member.model.vo.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

        <%
           Member m = (Member)session.getAttribute("member");
        %>
        
        <header id="header">
            <div class="logo">
                <a href="/"><img src="/assets/images/jupday_logo.png" alt="줍데이로고"></a>
            </div>
            <nav>
                <ul class="gnb-list">
                    <li><a href="/crew/crewAllList.do">줍데이크루</a>
                        <ul class="lnb-list">
                            <li><a href="/crew/crewAllList.do">크루 검색</a></li>
                            <li><a href="/views/crew/crewCreate.jsp">크루 생성</a></li>
                            <li><a href="/crew/crewJoinList.do">크루 목록</a></li>
                        </ul>
                    </li>
                    <li><a href="/review/reviewAllSelect.do">줍데이리뷰</a>
                        <ul class="lnb-list">
                            <li><a href="/review/reviewAllSelect.do">리뷰 목록</a></li>
                            <li><a href="/views/review/reviewWriteForm.jsp">리뷰 작성</a></li>
                        </ul>
                    </li>
                    <li><a href="/views/notice/noticeNewsAllSelect.do">줍데이공지</a>
                        <ul class="lnb-list">
                            <li><a href="/views/notice/noticeNewsAllSelect.do">줍데이 소식</a></li>
                            <li><a href="/notice/noticeFAQSelect.do">자주묻는 질문</a></li>
                        </ul>
                    </li>
                    <li><a href="/views/member/myPage.jsp">마이줍데이</a>
                        <ul class="lnb-list">
                            <li><a href="/views/member/myPage.jsp">회원 정보</a></li>
                            <li><a href="/member/myPageMyCrew.do">크루 목록</a></li>
                            <li><a href="/member/myPageMyReview.do">리뷰 목록</a></li>
                        </ul>
                    </li>
                </ul>
            </nav>
            <div class="link-user">
            
                <%if(m!=null){%>
                    <%if(m.getAuthority_Id().equals("root")) { //관리자로 로그인할 경우 닉네임 누를시 관리자페이지로 이동%>
                    <div class="box-user">
                         <a href="/admin/MemberManageList.do"><%=m.getNick()%>님 </a>
                         <a href="/member/logout.do">로그아웃</a><br>
                    </div>
                    <%} %>
                                
                <div class="box-user">
                     <a href="/views/member/myPage.jsp"><%=m.getNick()%>님 </a>
                     <a href="/member/logout.do">로그아웃</a><br>
                </div>        
                                
                <%}else{%>
                
                <div class="box-user">
                    <a href="/views/member/memberLogin.jsp">로그인</a>
                    <a href="/views/member/memberJoinus.jsp">회원가입</a>
                </div>
                
                <%}%>
                

                
            </div>
        </header>