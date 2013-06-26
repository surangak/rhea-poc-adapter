<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%-- <%@ taglib prefix="form" uri="resources/spring-form.tld"%> --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="template/menu.jsp"%>
<div>
		<form method =" post " action = "${pageContext.request.contextPath}/module/rheapocadapter/filterData.form">
			<span style="vertical-align: bottom;">Date From:<input
				type="date" name="dateFrom" id="date_from" value="${dateFrom}"> Date To:<input
				type="date" name="dateTo" id="date_to" value="${dateTo}"></span><span
				style="vertical-align: bottom;">
				<input type="hidden" value="Error" name="qType">
				<input type="image"
				src="${pageContext.request.contextPath}/moduleResources/rheapocadapter/media/images/refresh.png"
				alt="Submit"></span>
		</form>
</div>
<br />
<script type="text/javascript">
	var $j = jQuery.noConflict();
	$j(document).ready(function() {
		$j('#myTable').dataTable({
			"sPaginationType":"full_numbers",
            "bJQueryUI":true,
            "aaSorting":[[0, "desc"]],
            "aoColumns": [{ "bSortable": false },{ "bSortable": false },{ "bSortable": false },{ "bSortable": false },{ "bSortable": false },
                          { "bSortable": false }]
		});
	});
</script>
<div id="errorTransactions" style="text-align: center;">
	<b><u>Error Transactions</u></b>
</div><div>	
		<table id="myTable" style="width: 100%;">
			<thead>
				<tr>
					
					<th>ID</th>
					<th>Time RequestSent</th>
					<th>Message Type</th>
					<th>Message</th>
					<th>Sender ID</th>
					<th>End Point</th>

				</tr>
				</thead>
        <tbody>
				<c:forEach var="errorTransactions" items="${errorTransactions}"
					varStatus="num">
					<tr>
						<td>${errorTransactions.id}</td>
						<td>${errorTransactions.timeRequestSent}</td>
						<td><c:choose>
							<c:when test="${fn:contains(errorTransactions.url, 'PUT')}">
                                PUT
							</c:when>
							<c:when test="${fn:contains(archiveTransactions.message, 'Update')}">
                                PUT
							</c:when>
							<c:when test="${fn:contains(errorTransactions.url, 'POST')}">
                                POST
							</c:when>
							<c:when test="${fn:contains(errorTransactions.message, 'Encounter')}">
                                POST
							</c:when>
							<c:when test="${fn:contains(archiveTransactions.message, 'Sav')}">
                                POST
							</c:when>
							<c:when test="${fn:contains(errorTransactions.url, 'GET')}">
                                GET
							</c:when>
							<c:when test="${fn:contains(errorTransactions.message, 'Get')}">
                                GET
							</c:when>
							<c:otherwise>
                                N/A
                            </c:otherwise>
						</c:choose></td>
						<td><a href="#" class="opener" id="${num.count}">View
							Message</a>
						<p id="url${num.count}" style="display: none;">${errorTransactions.url}</p>
						<textarea id="txa${num.count}" rows="" cols=""
							style="display: none;">${errorTransactions.message}</textarea></td>			
						<td>${errorTransactions.sender}</td>
						<td><c:choose>
							<c:when test="${fn:contains(errorTransactions.url, 'encounter')}">
                                SHR
							</c:when>
							<c:otherwise>
                                CR
                            </c:otherwise>
						</c:choose></td>

					</tr>
				</c:forEach>
			</tbody>
		</table>


</div>
<%@ include file="template/modal.jsp"%>

<%@ include file="/WEB-INF/template/footer.jsp"%>