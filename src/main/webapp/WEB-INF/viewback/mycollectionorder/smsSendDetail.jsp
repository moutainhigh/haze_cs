<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<title>发送短信</title>
	</head>
	<body>
		<div class="pageContent" id="dialog">
		<form id="frm" method="post" enctype="multipart/form-data" action="collectionOrder/sendMsg?loanOrderId=${loanOrderId}&originalNum=${originalNum}&userId=${userId}&orderId=${orderId}" onsubmit="return validateCallback(this, dialogAjaxDone);"
				class="pageForm required-validate">
				<input type="hidden" name="parentId" value="${params.parentId}" />
				<input type="hidden" name="id" id="id" value="${params.id }">
				<div class="pageFormContent" layoutH="56">
					<dl>
						<dt style="width: 80px;">
							<label>
								手机号:
							</label>
						</dt>
						<dd>
							<input name="phoneNumber" value="${userPhone}" maxlength="30" type="text"  size="30" class="phone"/>
						</dd>
					</dl>
					<dl>
						<dt style="width: 80px;">
							<label>
								短信名称:
							</label>
						</dt>
						<dd>
							<select name="msgId" id="msgId">
									<option value="">--请选择--</option>
								<c:forEach var="msg" items="${msgs}">
									<option value="${msg.id }" <c:if test="${msg.id eq params.msgId}">selected = "selected"</c:if>>
											${msg.name}
									<c:set var="msgId" value="${msg.id }"/>
									</option>
								</c:forEach>
							</select>
						</dd>					
					</dl>
					<dl>
						<dt style="width: 120px;">
							<label>
								该订单已发短信数:
							</label>
						</dt>
						<dd>
							${msgCount}
						</dd>
					</dl>
					<%-- <dl>	
						<dt style="width: 50px;">
							<label>
								短信详情:
							</label>
						</dt>
						<dd>
							<textarea id="msgContent" name="msgContent" readonly="readonly" value="${msgId }" style="width: 450px;height: 60px;"></textArea>
						</dd>
					</dl> --%>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="button" id="send" onclick="doCheck();">
										发送
									</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>

		</div>

	</body>

</html>
<script type="text/javascript">
	function doCheck() {
        if(confirm("您确认要发送短信吗?") == true){
            $("#frm").submit();
        }
    }
</script>