<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_MASTER')" var="isMaster" />
<sec:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin" />
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>

<main class="container mb-5" id="admin-member">

    <table class="table font-pretty-night">
        <tr>
            <td rowspan="4">
                <div class="img">
                    <img src="${contextPath}/file/thumb/profile/${member.ID}/${member.PROFILEIMAGE == null ? "none" : member.PROFILEIMAGE}/size?w=300&h=300" alt="프로필사진">
                </div>
            </td>
            <td>
                아이디: ${member.ID}
            </td>
        </tr>
        <tr>
            <td>닉네임: ${member.NAME}</td>
        </tr>
        <tr>
            <td>이메일: ${member.EMAIL}</td>
        </tr>
        <tr>
            <td>가입일: <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss" value="${member.REGDATE}" /></td>
        </tr>
        <tr>
            <td colspan="2"><p>주소: <c:out value="${member.ZONECODE}" default="없음" /> ${member.ADDRESS}${member.EXTRAADDRESS} ${member.DETAILADDRESS}</p></td>
        </tr>
        <tr>
            <td colspan="2">
                회원등급:  <%--회원등급 변경은 마스터만 가능--%>
                <c:if test="${not isMaster or member.ROLENAME == '마스터' }">  <%--마스터 계정이 아니면서 조회한 회원등급이 마스터인경우--%>
                    ${member.ROLENAME}
                </c:if>

                <c:if test="${isMaster and member.ROLENAME != '마스터'}">      <%--마스터 계정이면서 조회한 회원등급이 마스터가 아닌경우--%>
                    <label>&nbsp;<input type="radio" name="role" value="admin" ${member.ROLENAME == "관리자" ? "checked" : ""}>&nbsp;관리자</label>
                    <label>&nbsp;<input type="radio" name="role" value="user"  ${member.ROLENAME == "사용자" ? "checked" : ""}>&nbsp;사용자</label>
                    <button class="btn btn-outline-info float-right" onclick="changeRole('${member.ID}');">변경하기</button>
                </c:if>
            </td>
        </tr>
        <c:if test="${member.ROLENAME != '마스터'}">                                         <%--마스터 계정은 건들지 못하게 한다. 항상 활성화--%>
            <c:if test="${isMaster or not(isAdmin && member.ROLENAME == '관리자')}">        <%--관리자 이지만 다른 관리자는 건들지 못하게 한다.--%>
            <tr>
                <td colspan="2">
                    회원상태:
                    <label>&nbsp;<input type="radio" name="enable" value="1"  ${member.ENABLE == 1 ? "checked" : ""}>&nbsp;활동중</label>
                    <label>&nbsp;<input type="radio" name="enable" value="0"  ${member.ENABLE == 0 ? "checked" : ""}>&nbsp;활동중지</label>
                    <button class="btn btn-outline-info float-right" onclick="changeEnable('${member.ID}');">변경하기</button>
                </td>
            </tr>
            </c:if>
        </c:if>
        <tr>
            <td colspan="2">
                <div class="text-center">
                    <a href="${contextPath}/admin/members${qs}" class="btn btn-purple">돌아가기</a>
                </div>
            </td>
        </tr>

    </table>
</main>
<script src="<c:url value="/resources/js/commons.js"/>"></script>
<script src="<c:url value="/resources/js/admin.js"/>"></script>
