<%@page import="kr.or.iei.review.model.vo.ReviewComment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.or.iei.review.model.vo.Review"%>
<%@page import="kr.or.iei.member.model.vo.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JUP:DAY | 줍데이리뷰</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&family=Noto+Serif+KR:wght@400;500;600&display=swap" rel="stylesheet">    
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous">
<link rel="stylesheet" href="/assets/commons/default.css">
<link rel="stylesheet" href="/assets/commons/header.css">
<link rel="stylesheet" href="/assets/commons/footer.css">
<link rel="stylesheet" href="/assets/commons/common.css">
<link rel="stylesheet" href="/assets/css/crewOneFeed.css">
<link rel="stylesheet" href="/assets/css/reviewWriteForm.css">
<link rel="stylesheet" href="/assets/css/reviewSelectContent.css">
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
<!-- 위의 지도 부분은 카카오 지도 API에 필요한 CSS, 지도를 불러오지 못했지만 스타일은 남겨둠 -->
</head>

<body>
	
	<div id="wrap">
	
		<!-- header -->
		<%@ include file="/views/commons/header/header.jsp"%>
			
		<div id="content">
			<div class=box-title>
				<p class="tit-small">J U P : D A Y</p>
				<p class="tit-big">줍데이리뷰</p>
			</div>
		
			<%
				Review review = (Review)request.getAttribute("review");
				int currentPage = (int)request.getAttribute("currentPage");	
				//ReviewFileData rfd = (ReviewFileData).request.getAttribute("");
			%>

			<div id="review_writing">
				<div class="write-top">
					<div class="box-subject">
						<p><%=review.getPostTitle() %></p>
					</div>
					<div class="box-writer">
						<div class="user-img">
							<img alt="" src="/upload/gramnotebook_1640736448800_jupday.png">
						</div>
						<div class="user-name">
							<p><%=review.getNick() %></p>
							<span><%=review.getRegDate() %></span>
						</div>
					</div>
				</div>
				
				<div class="write-content">
					<div class="box-content">
						<div class="box-content-img">
							<img alt="" src="/assets/images/review_default.jpg">
							
						</div>
						<p>
						<%=review.getPostContent() %>
						<input type="hidden" name="reviewNo" value="<%=review.getPostNum() %>"/>
						<input type="hidden" name="currentPage" value="<%=currentPage%>"/>
						</p>
					</div>
					
					<div class="box-map">
						<div class="map-top">
							<i class="fas fa-map-marked-alt"></i>
							<span>지도 보기</span>
							<i class="fas fa-chevron-down"></i>
						</div>
						<div class="map-content">
							<img alt="" src="/assets/images/mapAPI.PNG" style="width:100%; height:100%;">
						</div>
					</div>
					
					<div class="box-icon">
						<i class="far fa-heart"></i><span><%=review.getGood()%></span>
						<i class="far fa-comment"></i><span>댓글 3</span>
					</div>
				</div>
				
				<div class="box-comment">
				
				<%
					ArrayList<ReviewComment> commentslist = review.getComments();
				%>
				<%if(commentslist.isEmpty()){ %>
					<p>현재 댓글이 없습니다. 댓글을 작성해보세요</p>
				<%}else{ %>
				
					<%for(ReviewComment co : commentslist){ %>
				
					<div class="box-one-comment">
						<div class="user-img">
							<img alt="" src="/assets/images/profile.png">
						</div>
						<div class="right-comment">
							<div class="user-comment">
								<p><%=co.getNick() %><span><%=co.getR_c_regDate() %></span></p>
							</div>
							<div class="txt-comment">								
								<p><%=co.getR_c_comment() %></p>
							</div>
							<div class="btn-comment">
								<form action ="/views/review/reviewCommentUpdate.jsp" method="post" id="commentUpdateForm" display="none">			
									<input type="hidden" name="comment" value="<%=co.getR_c_comment() %>"/>
									<input type="hidden" name="commentNo" value="<%=co.getR_c_no() %>"/>
									<input type="hidden" name="postNum" value="<%=co.getPostNum() %>"/>
									<input type="hidden" name="currentPage" value="<%=currentPage%>"/>
								</form>
							
								<%if(m!=null && m.getUserId().equals(co.getUserId())) {%>
									
									
									<button class="coModifyBtn" onclick='coModify("/views/review/reviewCommentUpdate.jsp?comment=<%=co.getR_c_comment()%>&postNum=<%=co.getPostNum()%>&commentNo=<%=co.getR_c_no() %>&currentPage=<%=currentPage%>");'>수정</button>  
									<button class="coDeleteBtn" commentNo="<%=co.getR_c_no()%>">삭제</button>
									
									<script>
										function coModify(str){
											console.log(str);
											
											window.open(str,"_blank","width=300px, height=100px");
										}
									</script>
									
									<script>
										$('.coDeleteBtn').click(function(){
											
											if(window.confirm('정말로 해당 댓글을 삭제하시겠습니까?')){
												//댓글을 삭제하기 위해 필요한 정보는? 댓글번호, 작성자, 게시글번호, 현재페이지도 있어야 한다(다시 해당 post로 돌아가려면)
												var commentNo = $(this).attr("commentNo");
												var postNum = <%=review.getPostNum()%>;
												var currentPage = <%=currentPage%>;
												
												//servlet으로 데이터를 전송하기 위한 form 생성
												var formTag = document.createElement("form");
												
												formTag.setAttribute('action','/review/deletePostComment.do');
												formTag.setAttribute('method','post');
												formTag.setAttribute('display','none');
												
												//<input type="hidden" name="commentNo" value=commentNo>
												var inputTag = document.createElement("input");
												inputTag.setAttribute('type','hidden');
												inputTag.setAttribute('name','commentNo');
												inputTag.setAttribute('value',commentNo);
												
												formTag.appendChild(inputTag);
												
												//<input type="hidden" name="boardNo" value=boardNo>
												var inputTag = document.createElement("input");
												inputTag.setAttribute('type','hidden');
												inputTag.setAttribute('name','postNum');
												inputTag.setAttribute('value',postNum);
												
												formTag.appendChild(inputTag);
												
												//<input type="hidden" name="currentPage" value=currentPage>
												var inputTag = document.createElement("input");
												inputTag.setAttribute('type','hidden');
												inputTag.setAttribute('name','currentPage');
												inputTag.setAttribute('value',currentPage);
												
												formTag.appendChild(inputTag);
												
												document.body.appendChild(formTag); 
	
												formTag.submit();
	
											}else{
												alert('댓글 삭제를 취소하였습니다');
											}
											
										});
									</script>
									
								<%}else{ %>
									
								<%} %>
							</div>		
						</div>
					</div>

					<%} %>
					
				<%} %>
				
					<%if(m!=null){ %>
					<form action="/review/reviewCommentWrite.do" method="post">
					<div class="box-write-comment">
						<div class="user-nick">
							<p><%=m.getNick() %></p>
							
						</div>
						<textarea name="r_c_comment" placeholder="댓글을 입력하세요"></textarea>
						<input type="hidden" name="postNum" value="<%=review.getPostNum() %>"/>
						<input type="hidden" name="currentPage" value="<%=currentPage %>"/>
						<button>등록</button>											
					</div>
					</form>	
						
					<%}else{ %>
					<div class="box-write-comment">	
						<div class="user-nick">
							<p>none</p>
						</div>
						<textarea disabled placeholder="로그인 후 사용해주세요"></textarea>
						<button disabled>등록</button>
						<%--추루 여기다 location.replace를 줘서 로그인 페이지로 돌아가게 만들 예정 --%>
					</div>	
					<%} %>
					
					
				</div>
	
				<div class="list-btn">

				<%if(m!=null && m.getUserId().equals(review.getUserId())){ %>
				<form action="/views/review/reviewUpdateForm.jsp" method="post" display="none">	
					<input type="hidden" name="postNum" value="<%=review.getPostNum() %>"/>
					<input type="hidden" name="currentPage" value="<%=currentPage%>"/>
					<input type="hidden" name="postContent" value="<%=review.getPostContent()%>"/>
					<input type="hidden" name="postTitle" value="<%=review.getPostTitle()%>"/>
				
					<button class="btn-m btn-update" id="modifyBtn">수정</button>
					<button class="btn-m btn-delete" id="deleteBtn">삭제</button>
					<button class="btn-m btn-golist"><a href="/review/reviewAllSelect.do?currentPage=<%=currentPage%>">목록</a></button><br>
				</form>		
					
				
					
					<script>
						$('#deleteBtn').click(function(){
							if(window.confirm('게시글을 삭제하시겠습니까?')){
								
								var formTag = document.createElement("form");
								formTag.setAttribute("action","/review/reviewPostDelete.do");
								formTag.setAttribute("method", "post");
								
								var inputTag = document.createElement("input");
								inputTag.setAttribute("type","hidden");
								inputTag.setAttribute("name","postNum");
								inputTag.setAttribute("value","<%=review.getPostNum()%>");
								
								formTag.appendChild(inputTag);
								
								document.body.appendChild(formTag);
								
								formTag.submit();
								
							}else{
								alert('삭제 취소');
							}
						});
					</script>
					
				<%}else{ %>			
					<%-- 현재, 글 작성자가 아닌 경우 수정 버튼을 클릭하면 그냥 disable를 주어 수정하지 않도록 했음 조금 더 발전시킬 여지는 있지만 로직은 동작함 --%>
					<button class="btn-m btn-update" disabled>수정</button>
					<button class="btn-m btn-golist"><a href="/review/reviewAllSelect.do?currentPage=<%=currentPage%>">목록</a></button>
				<%} %>
				</div>
			</div>
			
		</div> 
	
		<!-- footer -->
		<%@ include file="/views/commons/footer/footer.jsp"%>	
	</div>
	
	
</body>
</html>