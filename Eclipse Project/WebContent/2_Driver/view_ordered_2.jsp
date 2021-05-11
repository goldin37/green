<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<%
	if(session.getAttribute("id") == null){
%>
		<jsp:forward page ="login.html"></jsp:forward>
<%
	}

	String id = (String)session.getAttribute("id");
	String name =(String)session.getAttribute("name");
	String license = (String)session.getAttribute("license");
	String truck_type = (String)session.getAttribute("truck_type");
	String truck_number = (String)session.getAttribute("truck_number");
	String phone = (String)session.getAttribute("phone");
	
%>
<html lang="en">
<head>
    <meta charset="euc-kr">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>운송내역조회(2/2)</title>
    <link rel = "stylesheet" type = "text/css" href = "../style/mystyle.css">
</head>
<body>
    <header>
        <br><br><br><br><br><br><br><br><br><br><br>
        <h2>믿을 수 있는 화물 중개 플랫폼</h2>
        <h1>트럭커(TRUCKER)</h1>
    </header>
      <nav>
          <table>
            <tr>
                <td><a href = "#">접수현황</a></td>
                <td><a href = "#">마이페이지</a></td>
                <td><a href = "#">고객센터</a></td>
                <td><a href = "#">회사소개</a></td>
            </tr>
        </table>
      </nav>
    <section class="box">
        <h2 id="box">운송내역조회</h2> 
        <p style="font-weight: bolder; padding-left: 90px;">
           	환영합니다 ! <%= name %>(<%=id %>)님 
        </p>
        <table>
            <tr>
               <td>조회 기간:</td>
               <td>
                   <input type = "datetime-local" value = "<%= "period_start" %>"> ~
                   <input type = "datetime-local"  value = "<%= "period_end" %>">
               </td>
            </tr>
<!--         </table> -->
<!--     </section> -->
<!--     <section class="now"> -->
<!--         <h2 id="box">운송세부내역</h2>  -->
<!--         <table> -->
            <form name="form" action="view_ordered_1.jsp" method="post" enctype="multipart/form-data">
            <tr><td>번호 : <%= "ordered_num" %></td></tr>
            <tr><td>운송일자 : <%= "complete_date_time" %></td></tr>
            <tr><td>고객이름 : <%= "CUSTOMER_NAME" %></td></tr>
            <tr><td>고객연락처 : <%= "CUSTOMER_TELEPHONE" %></td></tr>
            <tr><td>화물종류 : <%= "CARGO_TYPE" %></td></tr>
            <tr><td>화물무게 : <%= "CARGO_WEIGHT" %></td></tr>
            <tr><td>이동거리 : <%= "DISTANCE" %></td></tr>
            <tr><td>출발지 : <%= "FROM_WHERE" %> <%= "FROM_SPEC" %></td></tr>
            <tr><td>도착지 : <%= "TO_WHERE" %> <%= "TO_SPEC" %></td></tr>
            <tr><td>승하차도움 : <%= "CARGO_HELP" %></td></tr>
            <tr><td>운임 : <%= "FIX_COST" %></td></tr>         
            <tr>
                <td>
                <input type="button" value="진행중인배송" class="finished" onclick="location.href='list.jsp'">
                </td>
             </tr>
            </form>
        </table>
    </section>

<footer>
    (주)트럭커 부산시 해운대구 마린시티3로 45 | 사업자번호 : 123-45-12345 | <br> 
    통신판매업 : 2021 - 부산 동래-01234 | 화물운송주선 제160236호 | <br> 
    전화번호 : 1588-3333 | 팩스 : 070-1234-1234 | 대표메일 : trucker@naver.com <br>
    화물운송약관 | 개인정보 처리방침<br>
    COPYRIGHT(C) TRUCKER LTD. ALL RIGHT RESERVED.
</footer>
</body>
</html>
