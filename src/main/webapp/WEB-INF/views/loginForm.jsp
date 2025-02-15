<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MusicMemo</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .carousel-item {
            height: 300px;
            background: #777;
            color: white;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 24px;
        }
        footer {
            text-align: center;
            padding: 10px;
            background: #f8f9fa;
            position: relative;
            bottom: 0;
            width: 100%;
        }
    </style>
</head>
<body>

<!-- Body -->
<main class="container mt-4">
    <h2>로그인</h2>
    <form action="/login" method="post" class="needs-validation" novalidate>
        <img class="mb-4" src="${pageContext.request.contextPath}/assets/brand/bootstrap-logo.svg" alt="" width="72" height="57">
        <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

        <!-- Username Field -->
        <div class="form-floating">
            <input type="text" name="username" class="form-control" id="floatingInput" placeholder="Username" required="true" />
            <div class="text-danger">
                <!-- 오류 메시지를 여기에 추가 -->
            </div>
            <label for="floatingInput">Username</label>
        </div>

        <!-- Password Field -->
        <div class="form-floating">
            <input type="password" name="password" class="form-control" id="floatingPassword" placeholder="Password" required="true" />
            <div class="text-danger">
                <!-- 오류 메시지를 여기에 추가 -->
            </div>
            <label for="floatingPassword">Password</label>
        </div>

        <!-- Email Field -->
        <div class="form-floating">
            <input type="email" name="email" class="form-control" id="floatingEmail" placeholder="Email" required="true" />
            <div class="text-danger">
                <!-- 오류 메시지를 여기에 추가 -->
            </div>
            <label for="floatingEmail">Email address</label>
        </div>

        <!-- Nickname Field -->
        <div class="form-floating">
            <input type="text" name="nickname" class="form-control" id="floatingNickname" placeholder="Nickname" required="true" />
            <div class="text-danger">
                <!-- 오류 메시지를 여기에 추가 -->
            </div>
            <label for="floatingNickname">Nickname</label>
        </div>
        <!-- Remember me Checkbox -->
        <div class="checkbox mb-3">
            <label>
                <input type="checkbox" name="rememberMe" value="remember-me"> Remember me
            </label>
        </div>

        <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
        <p class="mt-5 mb-3 text-muted">&copy; 2017–2021</p>
    </form>
</main>

<%@ include file="footer.jsp" %>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
