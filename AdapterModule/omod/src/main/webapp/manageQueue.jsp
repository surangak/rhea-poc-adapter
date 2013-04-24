<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/menu.jsp"%>

<div>
	<form method=" post "
		action="${pageContext.request.contextPath}/module/rheapocadapter/filterData.form">
		<span style="vertical-align: bottom;">Date From:<input
			type="date" name="dateFrom" id="date_from" value="${dateFrom}">
			Date To:<input type="date" name="dateTo" id="date_to"
			value="${dateTo}"></span><span style="vertical-align: bottom;">
			<input type="hidden" value="Archive" name="qType"> <input
			type="image"
			src="${pageContext.request.contextPath}/moduleResources/rheapocadapter/media/images/refresh.png"
			alt="Submit">
		</span>
	</form>
</div>
<br />
<script type="text/javascript">
	var $j = jQuery.noConflict();
	$j(document).ready(function() {
		$j('#myTable').dataTable({
			"sPaginationType" : "full_numbers",
			"bJQueryUI" : true,
			"aaSorting" : [ [ 0, "desc" ] ],
			"aoColumns" : [ {
				"bSortable" : false
			}, {
				"bSortable" : false
			}, {
				"bSortable" : false
			}, {
				"bSortable" : false
			}, {
				"bSortable" : false
			}, {
				"bSortable" : false
			} ]
		});

	});
</script>
<div id="archiveTransactions" style="text-align: center;">

	<strong><u>Archive Transactions</u></strong>
</div>
<div>
	<table id="myTable" style="width: 100%;">
		<thead>
			<tr>

				<th>ID</th>
				<th>Time Request Sent</th>
				<th>Message Type</th>
				<th>Message</th>
				<th>Time Response Received</th>
				<th>End Point</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="archiveTransactions" items="${archiveTransactions}"
				varStatus="num">
				<tr>

					<td>${archiveTransactions.id}</td>
					<td>${archiveTransactions.timeRequestSent}</td>
					<td><c:if test="${num.count%2!=0}">
				POST
				</c:if> <c:if test="${num.count%2==0}">
				PUT
				</c:if></td>
					<!--<td><a href="#" class="opener" id="${num.count}" onclick="showMessageDialog('${archiveTransactions.message}');">Show Message</a>-->
					<td><a href="#" class="opener" id="${num.count}">Show
							Message</a>
						<p id="url${num.count}" style="display: none;">${archiveTransactions.url}</p>
						<textarea id="txa${num.count}" rows="" cols=""
							style="display: none;">${archiveTransactions.message}</textarea>
					</td>
					<td>${archiveTransactions.timeResponseReceived}</td>
					<!--<td>${archiveTransactions.relatedEncounter}</td>-->
					<td><c:choose>
							<c:when test="${fn:contains(archiveTransactions.url, 'encounter')}">
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