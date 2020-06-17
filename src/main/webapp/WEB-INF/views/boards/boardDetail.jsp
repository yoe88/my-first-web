<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
    <title>YH - Index</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--부트스트랩 설정-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    <!-- 아이콘  설정-->
    <script src="https://kit.fontawesome.com/9766199556.js" crossorigin="anonymous"></script>

    <!-- 내가만든 스타일 -->
    <link rel="shortcut icon" href="./favicon.ico">
    <link rel="icon" href="./favicon.ico">
    <!-- <link rel="stylesheet" href="../css/mystyle.css"> -->
    <link rel="stylesheet" href="<c:url value="/resources/css/mystyle.css" />">

    <style>
        .table td,
        .table th {
            border-top: 0;
        }

        .table tr {
            border-top: 1px solid #dee2e6
        }

        .table th {
            padding: 0.7rem 0.75rem 0.1rem;
        }

        .table td {
            padding: 0.1rem 0.75rem 0.7rem;
        }

        @media (min-width: 768px) {

            .table th,
            .table td {
                padding: .75em;
            }
        }
        .btn-outline-dark:hover{
            color: inherit;
            background-color: inherit;
            border-color: inherit;
        }
        .btn:focus {
            box-shadow: 0 0 0 0.2rem rgba(246, 199, 255, 0.5)!important;
        }
        #comment-view{
            background-color: #f3f0f0;
        } 
        .comment-option{
            cursor: pointer;
        }
        .comment-content{
            word-break: break-all;
        }
        .dropdown-menu{
            min-width: 0;
        }
        .dropdown-item:not(:first-child){
            border-top: 1px solid lightgray;
        }      
        div#comment-write-box{
            border: 1px solid #ced4da;
            border-radius: .25rem;
            border-collapse: collapse;
        }
        div#comment-write-box textarea{
           border: 1px solid white;
        }
        div#comment-write-box textarea:focus{
            border: 1px solid #775cdc;
        }
        a:hover{
            text-decoration: none;
        }
        li .form-control{
            border: 1px solid white;
        }
        input[type=radio]{
            display: none;
        }
    </style>

</head>

<body>
    <header>
        <nav class="navbar navbar-expand-md bg-purple navbar-dark mb-5">
            <div class="container py-2">
                <a class="navbar-brand font-weight-bolder" href="#" style="font-size: 150%;">YH</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="collapsibleNavbar">
                    <ul class="nav navbar-nav">
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-clipboard pr-1"></i>게시판</a>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-images pr-1"></i>포토갤러리</a>
                        </li>
                    </ul>
                    <ul class="navbar-nav align-items-lg-center ml-auto">
                        <a class="nav-item nav-link" href="/login"><i class="fas fa-sign-in-alt pr-1"></i>로그인</a>
                        <a class="nav-item nav-link" href="/register"><i class="fas fa-user-plus pr-1"></i>회원가입</a>
                        <a class="nav-item nav-link" href="/myInfor"><i class="fas fa-user pr-1"></i>내정보</a>
                    </ul>
                </div>
            </div>
        </nav>
    </header>
    <main>
        <section class="container">
            <div>
                <h1 class="h1-title">* detail *</h1>
            </div>

            <table class="table">
                <tr class="row">
                    <th class="col-md-2">제목</th>
                    <!-- required maxlength="26"  12345678901234567890123456 -->
                    <td class="col-md-10"><input type="text" name="title" class="form-control" readonly
                            autocomplete="off">
                    </td>
                </tr>
                <tr class="row">
                    <th class="col-md-2">첨부파일</th>
                    <td class="col-md-10">
                        <div class="form-control text-center">
                            <a href="javascript:;" class="text-info">
                                <h5 class="d-inline">123.zip</h5>
                            </a>
                        </div>
                    </td>
                </tr>
                <tr class="row">
                    <th class="col-md-2">내용</th>
                    <!-- required -->
                    <td class="col-md-10">
                        <textarea rows="14" name="content" class="form-control" readonly></textarea>
                    </td>
                </tr>

                <!-- 관리자 또는 작성자만 보이게하기 -->
                <tr>
                    <td colspan="2" class="py-3 d-flex justify-content-around">
                        <input type="button" value="수정하기" class="btn btn-purple" onclick="modifyBoard();">
                        <input type="button" value="삭제하기" class="btn btn-purple" onclick="deleteBoard();">
                    </td>
                </tr>                   
            </table>
        </section>
        <div class="container my-3">
            <button class="btn btn-outline-dark" onclick="toggleComment();">댓글<span class="text-danger ml-1" id="total-comment-count">5</span></button>
            <button class="btn btn-outline-dark" onclick="deleteBoard();">추천하기<span class="text-danger ml-1" id="hit-count">10</span></button>
            <button class="btn btn-outline-danger float-right" onclick="deleteBoard();">답글</button>
        </div>

        
        <section class="container my-3 p-2 bg-light-gray" id="comment-view">
            <form action="">
                <div class="bg-white mb-2" id="comment-write-box">
                    <textarea class="form-control no-resize" rows="4" placeholder="댓글입력" maxlength="200" onkeyup="checkMaxLength(this);"></textarea>
                    <div class="p-2 clearfix">
                        <span class="currentLength">0</span><span class="maxLength text-muted">/200</span>
                        <button type="button" class="float-right btn btn-outline-danger btn-sm">댓글등록</button>
                    </div>
                </div>
            </form>
            <!-- 댓글리스트 -->
            <ul id="comment-list" class="list-group">
                <li class="list-group-item p-2">
                    <div>
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

                <li class="list-group-item p-2">
                    <div>
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
                    <p class="comment-content my-1">댓글댓글댓글댓글댓글댓글댓글댓글
                    </p>
                    <a class="small text-dark" href="javascript:;" onclick="replyComment(this);">
                        <span class="reply-txt mr-1">답글</span><span class="reply-cnt font-weight-bold">0</span>
                        <input type="radio" name="reply">
                    </a>  
                </li>
            </ul>
        </section>
    </main> 

    <footer class="container-fluid text-center p-3 bg-black">
        Copyright &copy; 2020 YH Web
    </footer>

    <script>     
        function auto_grow(element) {
            element.style.height = (element.scrollHeight)+"px";
        }
    </script>
    <script src="/js/boards.js"></script>
    <!-- <script src="<c:url value="/resources/js/boards.js" />"></script> -->
</body>

</html>