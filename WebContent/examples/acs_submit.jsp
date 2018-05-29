<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>3D Secure Redirect Page</title>
</head>
<body onload="OnLoadEvent()">
	<form id="acs_form" action="<%=request.getAttribute("url") %>" method="post">
		<input type="hidden" name="PaReq" value="<%=request.getAttribute("pareq") %>" />
		<input type="hidden" name="TermUrl" value="<%=request.getAttribute("termurl") %>" />
		<input type="hidden" name="MD" value="<%=request.getAttribute("md") %>" />
	</form>
<script type="text/javascript">
	function OnLoadEvent() {
		document.getElementById('acs_form').submit();
		}
</script>
</body>
</html>

