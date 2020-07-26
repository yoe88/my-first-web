<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<sec:authentication property="principal.username" var="username" />
<sec:authentication property="principal.profileImage" var="profileImage" />
<main class="container my-3">
    <h4>${page_title}</h4>

    <section class="my-3">
        <form action="${contextPath}/member/edit/profile" method="post">
            <input type="hidden" name="_method" value="put"/>
            <c:if test="${type == 1}">
                <table class="w-100 border-bottom">
                    <tbody>
                    <tr class="border-top border-bottom">
                        <th class="bg-light-gray border-right text-center w-25">프로필 사진</th>
                        <td>
                            <div class="m-3">
                                <div>
                                    <img class="rounded-circle" id="profile-image" src="${contextPath}/file/thumb/profile/${username}/${profileImage}/size?w=160&h=160" width="100" height="100" alt="프로필사진">
                                </div>
                                <div class="my-3">
                                    <input type="hidden" id="isDelete" value="false">
                                    <input type="file" class="hide" id="input-file" accept="image/*" onchange="changeImageFile(this);">
                                    <button type="button" class="btn btn-outline-success mr-3" onclick="fileTrigger();">사진변경</button>
                                    <button type="button" class="btn mx-3 ${profileImage == "none" ? "disabled btn-outline-secondary" : "btn-outline-danger"}" id="delete-button" onclick="deleteProfileImage(this);">삭제</button>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="bg-light-gray border-right text-center w-25">닉네임</th>
                        <td>
                            <div class="m-3">
                                <input type="text" id="name" name="name" value="${m.name}" onkeydown="changeName();"><br>
                                <span class="error-name small"></span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${type == 3}">
                <div class="justify-content-center">
                    <div class="row justify-content-md-center">
                        <input type="password" class="form-control col-md-7 mb-3" autocomplete="new-password" placeholder="현재 비밀번호">
                        <input type="password" class="form-control col-md-7" name="password" autocomplete="new-password" placeholder="새 비밀번호">
                        <input type="password" class="form-control col-md-7" autocomplete="new-password" placeholder="새 비밀번호 확인">
                    </div>
                </div>
            </c:if>
            <c:if test="${type == 4}">
                <div class="justify-content-center  register">
                    <div class="row justify-content-md-center">
                        <div class="daum-address col-md-7">
                            <div class="input-group my-1">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-home"></i></span>
                                </div>

                                <input type="text" id="zonecode" name="zoneCode" class="form-control d-inline" placeholder="우편번호" autocomplete="off" readonly value="${m.zoneCode}">

                                <div class="input-group-append">
                                    <button type="button" onclick="execDaumPostcode()" class="input-group-text">우편번호 찾기</button><br>
                                </div>
                            </div>

                            <input type="text" id="address" name="address" placeholder="주소" class="form-control float-left" autocomplete="off" readonly style="width: 50%;" value="${m.address}">
                            <input type="text" id="extraAddress" name="extraAddress" placeholder="참고항목" class="form-control float-left" autocomplete="off" readonly style="width: 50%;" value="${m.extraAddress}">

                            <div class="input-group my-1">
                                <input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소" class="form-control d-inline" value="${m.detailAddress}" <c:if test="${!empty m.zoneCode}">onkeydown="changeAddress()"</c:if>>
                                <c:if test="${!empty m.zoneCode}">
                                    <div class="input-group-append">
                                        <button type="button" onclick="deleteAddress()" class="input-group-text">주소 삭제</button><br>
                                    </div>
                                </c:if>
                            </div>

                            <div id="wrap" style="display:none;border:1px solid;width:500px;height:300px;margin:5px 0;position:relative">
                                <img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1" onclick="foldDaumPostcode()" alt="접기 버튼">
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="text-center mt-3">
                <c:if test="${type == 1}">
                    <button type="button" id="apply" class="btn btn-success mr-3 disabled" onclick="modifyProfile(this);">적용하기</button>
                </c:if>
                <c:if test="${type != 1}">
                    <button type="button" id="apply" class="btn btn-success mr-3 <c:if test="${type == 4}">disabled</c:if>" onclick="modifyMember(this.form);">적용하기</button>
                </c:if>
                <a href="${contextPath}/member/me" class="btn btn-danger mx-3">취소</a>
            </div>
        </form>
    </section>
</main>
<script src="<c:url value="/resources/js/commons.js" />"></script>
<script src="<c:url value="/resources/js/member/DaumAddressAPI.js" />"></script>
<script src="<c:url value="/resources/js/member/modify.js" />"></script>
<!-- 다음 주소 API -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

