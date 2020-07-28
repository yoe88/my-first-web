<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal.username" var="username" />
    <sec:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin" />
</sec:authorize>

<main id="boardDetail">
    <input type="hidden" id="user-name" value="${username}">
    <input type="hidden" id="admin" value="${isAdmin}">
    <input type="hidden" id="ano" value="${b.articleNo}">

    <section class="container">
        <table class="table ">
            <tr class="row">
                <td class="col-md-12 clearfix">
                    <div class="d-inline-block pt-2 mr-2">
                        <img src="${contextPath}/file/thumb/profile/${b.id}/${b.profileImage == null ? "none" : b.profileImage}/size?w=30&h=30" width="30" height="30"
                        class="border rounded-circle" alt="프로필 이미지">
                    </div>
                    <input type="hidden" value="${b.articleNo}" id="articleNo">
                    <div class="d-inline-block align-middle" style="word-break: break-all">
                        <span class="board-info">작성자 </span>${b.name}(${b.id})
                        <span class="board-info ml-2">추천 </span>${b.recommend}
                        <span class="board-info ml-2">조회 </span>${b.hit}
                        <fmt:parseDate value="${b.regDate}" pattern="yyyy-MM-dd'T'HH:mm" var="dateTime" type="both"/>
                        <span class="ml-2"><fmt:formatDate pattern="yy.MM.dd HH:mm" value="${dateTime}" /></span>
                    </div>
                    <a href="${contextPath}/boards${qs}" class="btn btn-outline-danger float-right mt-1">목록</a>
                </td>
            </tr>

            <tr class="row">
                <th class="col-md-2">제목</th>
                <td class="col-md-10"><input type="text" class="form-control" readonly value="${b.title}"/>
                </td>
            </tr>


            <c:if test="${!empty b.fileName}">
                <tr class="row">
                    <th class="col-md-2">첨부파일</th>
                    <td class="col-md-10 clearfix">
                        <div class="form-control text-center">
                            <a href="${contextPath}/file/boards/${b.articleNo}/${b.fileName}?fName=${b.encodeOriginalFileName}" class="text-info">
                               <h5 class="d-inline" style="word-break: break-all">${fn:substring(b.originalFileName,0 , 36)}</h5>
                            </a>
                        </div>
                    </td>
                </tr>
            </c:if>


            <tr class="row">
                <th class="col-md-2">내용</th>
                <td class="col-md-10">
                    <textarea rows="14" name="content" class="form-control" readonly style="white-space: pre">${b.content}</textarea>
                </td>
            </tr>

            <c:if test="${isAdmin || username == b.id}">
            <!-- 관리자 이상 또는 작성자만 보이게하기 -->
            <tr>
                <td colspan="2" class="py-3 d-flex justify-content-around">
                <sec:authorize access="hasRole('ROLE_ADMIN')"> <%--관리자 이상만 비공개 처리 가능--%>
                    <input type="button" value="비공개하기" class="btn btn-purple" onclick="disableBoard('${b.articleNo}');">
                </sec:authorize>
                <c:if test="${username == b.id}">  <%--본인이 쓴 글만 수정 가능--%>
                    <a href="${b.articleNo}/edit" class="btn btn-purple">수정하기</a>
                </c:if>
                    <input type="button" value="삭제하기" class="btn btn-purple" onclick="deleteBoard('${b.articleNo}');">
                </td>
            </tr>
            </c:if>
        </table>
    </section>
    <div class="container my-3 clearfix action">
        <button class="btn btn-outline-dark" onclick="toggleViewComment();">댓글<span class="text-danger ml-1" id="total-comment-count">0</span></button>
        <button class="btn btn-outline-dark" onclick="upRecommend();">추천하기<span class="text-danger ml-1" id="recommend-count">${b.recommend}</span></button>
        <a href="${b.articleNo}/reply" class="btn btn-outline-danger float-right">답글</a>
    </div>


    <section class="container my-3 p-2 bg-light-gray" id="comment-view">

        <div class="bg-white mb-2 comment-write-box">
            <textarea class="form-control no-resize" rows="1" placeholder="댓글입력" maxlength="200"
                     onfocus="focusTextArea(this);"  onkeyup="checkMaxLength(this);"></textarea>
            <div class="p-2 clearfix hide">
                <span class="currentLength">0</span><span class="maxLength text-muted">/200</span>
                <button type="button" class="float-right btn btn-outline-success btn-sm" data-cno="0" onclick="addComment(this);">댓글등록</button>
                <button type="button" class="float-right btn btn-outline-danger btn-sm mr-3" onclick="cancelComment(this);">취소</button>
            </div>
        </div>

        <!-- 댓글리스트 -->
        <ul id="comment-list" class="list-group">
            <%--<li class="list-group-item p-2">
                <div class="comment-info">
                    <span class="comment-writer text-muted">작성자입니다12</span>
                    <span class="mx-2" style="color: #e2d7df;">|</span>
                    <span class="comment-regdate text-muted">20.10.20 19:20:58</span>
                    <!-- 작성자 or 관리자 -->
                    <span class="float-right comment-option">
                        <div class="dropdown">
                        <!--Trigger-->
                        <a  type="button" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false"><i class="fas fa-ellipsis-v text-muted"></i></a>
                        <!--Menu-->
                        <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#">삭제</a>
                            <a class="dropdown-item" href="#">????</a>
                        </div>
                        </div>
                    </span>
                </div>
                <hr class="my-1">
                <p class="comment-content my-1">댓글댓글댓글댓글댓글댓글댓글댓글댓글댓글
                </p>
                <a class="small text-dark" href="javascript:;" onclick="replyComment(this);">
                    <span class="reply-txt mr-1">답글</span><span class="reply-cnt font-weight-bold">0</span>
                    <input type="radio" name="reply">
                </a>
                <!--답글들어갈목록-->
            </li>

            <!-- 이거 복사 -->
            <li class="list-group-item p-2">
                <div class="comment-info">
                    <img src="${contextPath}/file/thumb/${b.id}/${b.profileImage == null ? "none" : b.profileImage}/size?w=30&h=30" width="30" height="30"
                         alt="프로필이미지" class="rounded-circle" width="30" height="30">
                    <span class="comment-writer text-muted">작성자입니다12</span>
                    <span class="mx-2" style="color: #e2d7df;">|</span>
                    <span class="comment-regdate text-muted">20.10.20 19:20:58</span>

                    <!-- 작성자 or 관리자 -->
                    <span class="float-right comment-option">
                        <div class="dropdown">
                        <!--Trigger-->
                        <a  type="button" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false"><i class="fas fa-ellipsis-v"></i></a>
                        <!--Menu-->
                        <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#">삭제</a>
                            <a class="dropdown-item" href="#">????</a>
                        </div>
                        </div>
                    </span>
                </div>
                <hr class="my-1">
                <p class="comment-content my-1">댓글댓글댓글댓글댓글댓글댓글댓글</p>
                <a class="small text-dark" href="javascript:;" data-ano="${b.articleNo}" data-cno="123" onclick="replyComment(this);">
                    <span class="reply-txt mr-1">답글</span><span class="reply-cnt font-weight-bold">0</span>
                    <input type="radio" name="reply">
                </a>
            </li>--%>
        </ul>
    </section>
</main>

<script>
    function auto_grow(element) {
        element.style.height = (element.scrollHeight)+"px";
    }
</script>
<script src="<c:url value="/resources/js/commons.js" />"></script>
<script src="<c:url value="/resources/js/board.js" />"></script>
<script src="<c:url value="/resources/js/boardDetail.js" />"></script>