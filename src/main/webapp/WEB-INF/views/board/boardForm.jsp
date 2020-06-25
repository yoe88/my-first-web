<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .table td, .table th{
        border-top: 0;
    }
    .table tr{
        border-top: 1px solid #dee2e6
    }
    .table th{
        padding: 0.7rem 0.75rem 0.1rem;
    }
    .table td{
        padding: 0.1rem 0.75rem 0.7rem;
    }
    @media (min-width: 768px){
        .table th, .table td{
            padding: .75em;
        }
    }
    .thumb-img{
        display: none;
    }
</style>

<main>
    <section class="container">
        <div>
            <h1 class="font-Mapo">* form *</h1>
        </div>

        <form action="${context}/boards" method="POST" enctype="multipart/form-data" onsubmit="return validateBoard(this);">
            <table class="table">
                <tr class="row">
                    <th class="col-md-2">제목</th>
                    <!-- required maxlength="26"  12345678901234567890123456 -->
                    <td class="col-md-10"><input type="text" name="title" required minlength="1" maxlength="26" size="26" class="form-control"
                                                 autocomplete="off">
                    </td>
                </tr>
                <tr class="row">
                    <th class="col-md-2">내용</th>
                    <!-- required -->
                    <td class="col-md-10">
                        <textarea rows="14" name="content" class="form-control" required minlength="4" onkeyup="checkMaxByte(this);"></textarea>
                        <div class="float-right">
                            <span class="currentByte">0</span><span class="maxByte">/4000 byte</span>
                        </div>
                    </td>
                </tr>
                <tr class="row">
                    <th class="col-md-2">첨부파일<img class="thumb-img rounded-circle" src="" alt="썸넬" width="50" height="50"></th>
                    <td class="col-md-10">
                        <div class="custom-file">
                            <input type="file" class="custom-file-input" id="customFile" name="file" onchange="changeFile(this)">
                            <label class="custom-file-label" for="customFile">Choose file</label>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" class="py-3 d-flex justify-content-around">
                        <input type="submit" value="쓰기" class="btn btn-purple">
                        <input type="button" value="취소" class="btn btn-purple" onclick="cancle();">
                    </td>
                </tr>
            </table>

        </form>

    </section>
</main>
<script src="<c:url value="/resources/js/commons.js" />"></script>
<script src="<c:url value="/resources/js/boards.js" />"></script>