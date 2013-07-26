<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%-- <%@ taglib prefix="form" uri="resources/spring-form.tld"%> --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<br />
<%@ include file="template/menu.jsp"%>
<br />
<div>
		<form method =" post " action = "${pageContext.request.contextPath}/module/rheapocadapter/filterData.form">
			<span style="vertical-align: bottom;">Date From:<input
				type="date" name="dateFrom" id="date_from" value="${dateFrom}"> Date To:<input
				type="date" name="dateTo" id="date_to" value="${dateTo}"></span><span
				style="vertical-align: bottom;">
				<input type="hidden" value="Processing" name="qType">
				<input type="image"
				src="${pageContext.request.contextPath}/moduleResources/rheapocadapter/media/images/refresh.png"
				alt="Submit"></span>
		</form>
</div>
<script type="text/javascript">
	var $j = jQuery.noConflict();
	$j(document).ready(function() {
       
		$j(".dialog").dialog({
			autoOpen : false
		});
		$j(".opener").click(function() {
			var idt = "dl" + $j(this).attr("id");
			//window.alert(idx);
			//var idt = "dl" + idx;
			//window.alert(idt);
			$j( ".dialog" ).dialog({ width: 500 });
			$j( ".dialog" ).dialog({ modal: true });
			$j("#" + idt).dialog("open");
			
		});

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
			},{
				"bSortable" : false
			}
			]
		});
	});
</script>
<div id="processingTransactions" style="text-align: center;">
	<b><u>Processing Transactions</u></b>
</div>
<div>


	<table id="myTable" style="width: 100%;">
		<thead>
			<tr>
				<th>ID</th>
				<th>Time Request Sent</th>
				<th>Message Type</th>
				<th>Message</th>
				<th>Sender</th>
				<th>End Point</th>

			</tr>
		</thead>
		<tbody>
			<c:forEach var="processingTransactions"
				items="${processingTransactions}" varStatus="num">
				<tr>
					<td>${processingTransactions.id}</td>
					<td>${processingTransactions.timeRequestSent}</td>
					<td>${processingTransactions.method}</td>
					<td><a href="#" class="opener" id="${num.count}">View
							Message</a>
						<p id="url${num.count}" style="display: none;">${processingTransactions.url}</p>
						<textarea id="txa${num.count}" rows="" cols=""
							style="display: none;">${processingTransactions.message}</textarea></td>
					<td>${processingTransactions.sender}</td>
                    <td><c:choose>
							<c:when test="${fn:contains(processingTransactions.url, 'encounter')}">
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