<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>스터디의 시작, 페이퍼민트</title>
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/datepickr.css">
<link rel="stylesheet" href="/css/markdown.css">
<script src="/js/datepickr.js"></script>

<!-- 노트 캘린더 -->
<link rel="stylesheet" href="/css/dateRangePickerForBootstrap.css">
<link rel="stylesheet" href="/css/daterangepicker-bs3.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="/js/moment.js"></script>
<script src="/js/daterangepicker.js"></script>

</head>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<input type="hidden" id="sessionUserId" name="sessionUserId" value="${sessionUser.userId}">
	<h1 id="empty-message"
		style="position: absolute; color: #888; top: 300px; width: 100%; text-align: center;">새
		노트를 작성해주세요</h1>
	<div id='note-list-container' class='content wrap'>
		<a href="/g/${groupId}"><span id="group-name"></span></a>
		<div id='create-note'>
			<a href='/notes/editor/g/${groupId}'>
				<button id='create-new-button'>
					<i class="fa fa-plus-circle"></i>
				</button>
			</a>
		</div>

		<div id="left-menu-container">
			<div id='calendar-container'>
				<div id="defaultCalendar" ></div>
				<input class="inputBtn" id="allShow" type="submit" value="전체보기" onclick="reloadNoteList()" />
			</div>
			<div id='summary-container' style=" position: absolute; top: 320px;">
				<span id="summaryShow">공지 모음</span>
				<div class='leftsideContainer' id='attention-container'>
					<ul id='attention-list'></ul>
				</div>
				<span id="summaryShow" style="margin-top: 3px;">질문 모음</span>
				<div class='leftsideContainer' id='question-container' style=" margin-top: 25px;">
					<ul id='question-list'></ul>
				</div>
			</div>
		</div>

		<div id='group-member-container'>
			<form id="addMemberForm">
				<span style="font-weight:bold;">멤버추가</span><br/>
				<input type="hidden" name="groupId">
				<input class="inputText" type="text" name="userId">
				<input class="inputBtn" type="submit" value="초대">
				<span class="addMemberAlert" style="visibility:hidden;">멤버추가메세지</span>
			</form>
			<div id='group-member-list'>
				<span style="font-weight:bold;">멤버관리</span><br/>
				<table id='group-member'>
				</table>
			</div>
			<div style="padding:10px;">
				<a href="#"><span id="leave-group" style="font-weight:bold;" onclick="confirmLeave()">그룹탈퇴하기</span></a>
			</div>
		</div>
	</div>
	<template id="view-note-template">
	<div class="markdown-body">
		<div class="note-content"></div>
		<div id="commentListUl"></div>
		<form id="commentForm" method="post">
			<textarea id='commentText' name='commentText' rows='5' cols='50'></textarea>
			<br>
			<button id='submitComment' class='btn btn-pm'>확인</button>
		</form>
	</div>
	</template>
	<template id="comment-template">
	<li><img class='avatar' class='avatar' src='/img/profile/avatar-default.png'>
		<div class='comment-container'>
			<div class='comment-info'>
				<span class='comment-user'></span> <span class='comment-date'></span>
			</div>
			<div class='comment'></div>
			<div class='comment-util'></div>
		</div></li>
	</template>
	<template id="member-template">
		<tr>
			<td class="member-info" style="width:140px; display:inline-block;">
				<div class="member-name" style="font-weight:bold;">멤버이름</div>
				<div class="member-id" style="color:#888; font-size:9px;">멤버아이디</div>
			</td>
			<td class="member-util" style="font-size:15px; display:inline-block;">
				<ul>
					<li>
						<i class="fa fa-eye"></i>
						<span class="info">노트 숨기기</span>
					</li>
					<input style="display:none;" type='checkbox' class='memberChk' checked=true value="">
					<li>
						<i class="fa fa-times"></i>
						<span class="info">멤버제외</span>
					</li>
				</ul>
			</td>
		</tr>
	</template>
        
    <script type="template" >
            
    </script>    
    
	<script>
	document.title = "${groupName}";
	var groupName = ("${groupName}".replace(/</g, "&lt;")).replace(/>/g, "&gt;");
	document.querySelector('#group-name').innerHTML = groupName;
	var bJoinedUser = false;
	const groupId = window.location.pathname.split("/")[2];
	window.addEventListener('load', function() {
		document.querySelector("#addMemberForm input[name='groupId']").value = groupId;
		readMember(groupId);
		document.querySelector("#addMemberForm").addEventListener("submit", function(e) { e.preventDefault(); addMember(); }, false);
		document.title = "${groupName}";
		var groupName = ("${groupName}".replace(/</g, "&lt;")).replace(/>/g, "&gt;");
		document.querySelector('#group-name').innerHTML = groupName;

		appendNoteList(${noteList});
		appendMarkList(${markList});
		var elCreateBtn = document.querySelector("#create-new-button");
	}, false);
	
	window.addEventListener('scroll', function() {
		infiniteScroll();
	}, false);
	
	
	</script>
	<script src="/js/note.js"></script>
	<script type="text/javascript">
		$(function() {
		    $("#defaultCalendar").daterangepicker({
		        singleDatePicker: true,
		        showDropdowns: true
		    },
		    function(start, end, label) {
		    	console.log(start.toISOString(), end.toISOString(), label);
		        $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
		    }); 
		});

		document.querySelector('#calendar-container').addEventListener("click", function(e) {
			var noteTargetDate = $('#defaultCalendar').data('daterangepicker').startDate._d.toISOString().substring(0,10);
			reloadNoteList(noteTargetDate);
		}, false);
	</script>
</body>
</html>
