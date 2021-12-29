<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
		boolean result = (boolean)request.getAttribute("result");
	%>
	

	<script>
	<%if(result) {%>
		alert('정상적으로 처리가 완료되었습니다. :)');
	<%}else {%>
		alert('정상적으로 처리되지않았습니다.');
	<%} %>
	location.replace("/admin/reviewManageList.do");
	</script>

</body>
</html>