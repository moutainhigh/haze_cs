<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
%>

<form id="pagerForm" onsubmit="return navTabSearch(this);" action="mmanLoanCollectionOrder/technologyOrderList?myId=${params.myId}" method="post">	
	<div class="pageHeader">
		<div class="searchBar">
			<table class="searchContent" >
				<tr>
					<%-- <td>
						催 收 时 间: 
						<input type="text" id="collectionBeginTime" name="collectionBeginTime" value="${params.collectionBeginTime}" class="date textInput readonly" datefmt="yyyy-MM-dd"  readonly="readonly"/>
						至
						<input type="text" id="collectionEndTime" name="collectionEndTime"  value="${params.collectionEndTime}" class="date textInput readonly" datefmt="yyyy-MM-dd"  readonly="readonly"/>
					</td> --%>
					<td colspan="2">
						派 单 时  间:
						 <input type="text" id="dispatchBeginTime" name="dispatchBeginTime" value="${params.dispatchBeginTime}" class="date textInput readonly" datefmt="yyyy-MM-dd"  readonly="readonly"/>
						至
						 <input type="text" id="dispatchEndTime" name="dispatchEndTime" value="${params.dispatchEndTime}" class="date textInput readonly" datefmt="yyyy-MM-dd"  readonly="readonly"/>
					</td>
					<td>逾 期 天 数: <input type="text" id="overDueDaysBegin" name="overDueDaysBegin" value="${params.overDueDaysBegin}"/>至<input type="text" id="overDueDaysEnd" name="overDueDaysEnd" value="${params.overDueDaysEnd}"/></td>
					<td>借款人姓名: <input type="text" id="loanRealName" name="loanRealName" value="${params.loanRealName}"/></td>
					<td>当前催收员: <input type="text" id="collectionRealName" name="collectionRealName" value="${params.collectionRealName}"/></td>
				</tr>
				<tr>
					<td>借款人手机: <input type="text" id="loanUserPhone" name="loanUserPhone" value="${params.loanUserPhone}"/></td>
					<%-- <td>催收人手机: <input type="text" id="collectionPhone" name="collectionPhone" value="${params.collectionPhone}"/></td> --%>
					<td>借  款 编 号: <input type="text" id="loanId" name="loanId" value="${params.loanId}"/></td>
					<c:if test="${userGropLeval ne '10021'}">
						<c:choose>
							<c:when test="${not empty params.CompanyPermissionsList}">
								<td>催 收 公 司: 
									<select name="companyId" id="companyId">
										<c:forEach var="company" items="${ListMmanLoanCollectionCompany }">
											<c:forEach var="companyViw" items="${params.CompanyPermissionsList}">
												<c:if test="${companyViw.companyId eq company.id}">
													<option value="${company.id }" <c:if test="${company.id eq params.companyId}">selected = "selected"</c:if>>
															${company.title}
													</option>
												</c:if>
											</c:forEach>
										</c:forEach>
									</select>
								</td>
							</c:when>
							<c:otherwise>
								<td>催 收 公 司: 
								<select name="companyId" id="companyId">
									<option value="">全部</option>
									<c:forEach var="company" items="${ListMmanLoanCollectionCompany }">
										<option value="${company.id }" <c:if test="${company.id eq params.companyId}">selected = "selected"</c:if>>
												${company.title}
										</option>
									</c:forEach>
								</select>
						</td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<td>催&nbsp;&nbsp;&nbsp;&nbsp;收&nbsp;&nbsp;&nbsp;组: 
						<select name="collectionGroup" id="orderCollectionGroup">
								<option value="">全部</option>
								<c:forEach var="group" items="${dictMap }">
									<option value="${group.key }" <c:if test="${group.key eq params.collectionGroup}">selected = "selected"</c:if>>
										${group.value}
									</option>
								</c:forEach>
								
						</select>
					</td>				
					<td>
						催 收 状 态:
						<select id="status" name="status">
							<option value="">全部</option>
							<option value="0" <c:if test="${params.status eq '0'}">selected = "selected"</c:if>> 待催收 </option>
							<option value="1" <c:if test="${params.status eq '1'}">selected = "selected"</c:if>> 催收中 </option>
							<option value="2" <c:if test="${params.status eq '2'}">selected = "selected"</c:if>> 承诺还款 </option>
							<option value="3" <c:if test="${params.status eq '3'}">selected = "selected"</c:if>> 待催收（委外） </option>
							<option value="4" <c:if test="${params.status eq '4'}">selected = "selected"</c:if>> 催收成功 </option>
							<option value="6" <c:if test="${params.status eq '6'}">selected = "selected"</c:if>> 减免申请 </option>
							<option value="7" <c:if test="${params.status eq '7'}">selected = "selected"</c:if>> 减免审核成功 </option>
							<option value="8" <c:if test="${params.status eq '8'}">selected = "selected"</c:if>> 减免审核拒绝 </option>
						</select>
					</td>
					<td>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">
									查询
								</button>
							</div>
						</div>
					</td>
				</tr>	
				<tr>
				</tr>
			</table>
		</div>
	</div>
	<div class="pageContent" heigth="100%">
		<jsp:include page="${BACK_URL}/rightSubList">
			<jsp:param value="${params.myId}" name="parentId"/>
		</jsp:include>
			<table class="table" style="width: 100%;" nowrapTD="false" layoutH="135">
				<thead>
					<tr>
					<th align="center" width="50">
						序号
					</th>
					<th align="center" width="50">
						借款编号
					</th>
					<th align="center" width="50">
						催收公司
					</th>
					<th align="center" width="50">
						催收组
					</th>
					<th align="center" width="50">
						借款人姓名
					</th>
					<th align="center" width="100">
						借款人手机号
					</th>
					<th align="center" width="50">
						借款金额
					</th>
					<th align="center" width="50">
						逾期天数
					</th>
					<th align="center" width="50">
						滞纳金
					</th>
					<th align="center" width="50">
							减免滞纳金
					</th>
					<th align="center" width="50">
						催收状态
					</th>
					<th align="center" width="50">
						应还时间
					</th>
					<th align="center" width="50">
						已还金额
					</th>
					<th align="center" width="50">
						最新还款时间
					</th>
					<th align="center" width="50">
						派单时间
					</th>
					<th align="center" width="50">
						当前催收员
					</th>
				</tr>
				</thead>
				<tbody >
					<c:forEach var="order" items="${pm.items}" varStatus="status">
					<tr target="id" rel="${order.id }">
						<td align="center" width="50">
							${status.count}
						</td>
						<td align="center" width="50">
							${order.loanId}
						</td>
						<td align="center" width="50">
							${order.companyTile}
						</td>
						<td align="center" width="50">
							${dictMap[order.collectionGroup]}
						</td>
						<td align="center" width="50">
							 ${order.realName}
						</td>
						<td align="center" width="50">
							 ${order.phoneNumber}
						</td>
						<td align="center" width="50">
							 ${order.loanMoney}
						</td>
						<td align="center" width="50">
							 ${order.overdueDays}
						</td>
						<td align="center" width="50">
							 ${order.loanPenlty}
						</td>
						<td align="center" width="50">
								${order.reductionMoney}
						</td>
						<td align="center" width="50">
							<c:choose>
								<c:when test="${order.collectionStatus eq '0'}">待催收</c:when>
								<c:when test="${order.collectionStatus eq '1'}"> 催收中</c:when>
								<c:when test="${order.collectionStatus eq '2'}">承诺还款</c:when>
								<c:when test="${order.collectionStatus eq '3'}">待催收（委外）</c:when>
								<c:when test="${order.collectionStatus eq '4'}">催收成功</c:when>
								<c:when test="${order.collectionStatus eq '5'}">续期</c:when>
								<c:when test="${order.collectionStatus eq '6'}">减免申请</c:when>
								<c:when test="${order.collectionStatus eq '7'}">减免审核成功</c:when>
								<c:when test="${order.collectionStatus eq '8'}">减免审核拒绝</c:when>
							</c:choose>
						</td>
						<td align="center" width="50">
							 <fmt:formatDate value="${order.loanEndTime}" pattern="yyyy-MM-dd"/> 
						</td>
						<!--<td align="center" width="50">
							 还款状态（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+）
							<c:if test="${order.returnStatus == '3'}">S1</c:if>
							<c:if test="${order.returnStatus == '4'}">S2</c:if>
							<c:if test="${order.returnStatus == '5'}">M1-M2</c:if>
							<c:if test="${order.returnStatus == '6'}">M2-M3</c:if>
							<c:if test="${order.returnStatus == '7'}">M3+</c:if>
						</td>-->
						<td align="center" width="50">
							 ${order.returnMoney}
						</td>
						<td align="center" width="50">
							<c:if test="${order.returnMoney>0.0}">
							 	<fmt:formatDate value="${order.currentReturnDay}" pattern="yyyy-MM-dd HH:mm:ss"/> 
							 </c:if>
						</td>
						<td align="center" width="50">
						 	<fmt:formatDate value="${order.dispatchTime}" pattern="yyyy-MM-dd HH:mm:ss"/> 
						</td>
						<td align="center" width="50">
							 ${order.currUserName}
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<c:set var="page" value="${pm }"></c:set>
		<!-- 分页 -->
		<%@ include file="../page.jsp"%>
	</div>
</form>