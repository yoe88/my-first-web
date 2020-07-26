<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>에러</title>
    <script>
        console.log('error');
        alert('${msg}');
        location.href = '${contextPath}${redirect}';
    </script>
</head>
<body>

</body>
</html>
