<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chatting</title>
    <style>
        .container {
            position: absolute;
            top: calc(50% - 150px);
            left: calc(50% - 250px);

            width: 500px;
            height: 300px;
            background-color: #D3D3D3;

            border: black 1px dashed;
            border-radius: 8px;

            font-family: 'Barlow', sans-serif;
        }

        .nickname {
            margin: 0 30px;
            padding: 0 10px;

            width: 440px;
            height: 44px;

            font-size: 20px;
        }

        .input_btn {
            margin: 20px;
            padding: 0;

            width: 80px;
            height: 50px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 style="text-align: center; margin: 40px 0;">WELCOME</h1>
    <form style="display: flex; flex-direction: column; align-items: center;" action="join.jsp" method="post">
        <input class="nickname" name="nickname" type="text" placeholder="Nickname">
        <div style="display: flex; flex-direction: row;">
            <input class="input_btn" type="reset" value="RESET">
            <input class="input_btn" type="submit" value="JOIN">
        </div>
    </form>
</div>
</body>
<script>
</script>
</html>
