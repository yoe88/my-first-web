<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<main class="container" id="gallery-form">
    <input type="hidden" id="sequence" value="0">
    <form action="${contextPath}/galleries/${action}" method="POST" enctype="multipart/form-data" onsubmit="return validateGallery(this);">
        <table class="table">
            <tr class="row">
                <th class="col-md-2">제목</th>
                <td class="col-md-10"><input type="text" name="title" required minlength="1" maxlength="26" size="26" class="form-control"
                                             autocomplete="off" value="${model.title}">
                </td>
            </tr>

            <tr class="row">
                <th class="col-md-2">이미지</th>
                <td class="col-md-10">
                    <button type="button" class="btn btn-light border" onclick="uploadImg();">이미지 추가하기</button>
                </td>
            </tr>

            <tr>
                <td>
                    <div id="thumb-list">
                        <c:forEach items="${model.file}" var="f" varStatus="st">
                            <div class="img-box mr-2">
                                <i class="far fa-window-close text-danger pointer" data-no="${f.no}" onclick="removeThumbnail(this)"></i>
                                <img class="border rounded-circle" src="${contextPath}/file/thumb/gallery/${model.gno}/${f.fileName}/size?w=150&h=150" alt="thumbnail" width="60" height="60">
                            </div>
                        </c:forEach>
                    </div>
                </td>
            </tr>

            <tr>
                <td colspan="2" class="py-3 d-flex justify-content-around">
                    <input type="submit" value="${action == "new" ? "업로드" : "수정하기"}" class="btn btn-purple">
                    <input type="button" value="취소" class="btn btn-purple" onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</main>
<script src="<c:url value="/resources/js/commons.js" />"></script>
<script src="<c:url value="/resources/js/gallery.js" />"></script>
