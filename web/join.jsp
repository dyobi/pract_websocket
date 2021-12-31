<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String nickname = request.getParameter("nickname");

    session.setAttribute("name", nickname);

    response.sendRedirect("chatting.jsp");
%>