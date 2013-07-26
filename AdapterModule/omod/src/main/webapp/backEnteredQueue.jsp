<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%-- <%@ taglib prefix="form" uri="resources/spring-form.tld"%> --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<br />

<%@ include file="template/menu.jsp"%>
<br />
<script type="text/javascript">
	var $j = jQuery.noConflict();
	$j(document).ready(function() {
		
		/*$j( ".dialog" ).dialog({ autoOpen: false });
        $j( ".opener" ).click(function() {
          var idt = "dl"+$j(this).attr("id");
          //window.alert(idx);
          //var idt = "dl" + idx;
          //window.alert(idt);
          $j( "#"+idt).dialog( "open" );
        });*/
		

		$j('#myTable').dataTable({
			"sPaginationType":"full_numbers",
            "bJQueryUI":true,
            "aaSorting":[[0, "desc"]],
            "aoColumns": [{ "bSortable": false },{ "bSortable": false },{ "bSortable": false },{ "bSortable": false }]
		});
	});
</script>
<%-- <div>
		<form method =" post " action = "${pageContext.request.contextPath}/module/rheapocadapter/filterData.form">
			<span style="vertical-align: bottom;">Date From:<input
				type="date" name="dateFrom" id="date_from" value="${dateFrom}"> Date To:<input
				type="date" name="dateTo" id="date_to" value="${dateTo}"></span><span
				style="vertical-align: bottom;">
				<input type="hidden" value="Backentered" name="qType">
				<input type="image"
				src="${pageContext.request.contextPath}/moduleResources/rheapocadapter/media/images/refresh.png"
				alt="Submit"></span>
		</form>
</div> --%>
<div id="encounterNotSent" style="text-align: center;">
	<b><u>Back Entered Data</u></b>
	<div style="clear: both">

		<form action="sentBackEnteredData.form" method="post">
			<span style="float: right;"><input type="submit"
				value="Process Back Entered Queue Data" /></span>
		</form>
	</div>
	
		<table id="myTable" style="width: 100%;">
			<thead>
				<tr>
					<th>Encounter Id</th>
					<th>Encounter date</th>
					<th>Patient Id</th>
					<th>Encounter Type</th>

				</tr>
			</thead>
			<tbody>
				<c:forEach var="encounter" items="${encounterNotSent}"
					varStatus="num">
					<tr>
						<td>${encounter.encounterId}</td>
						<td><openmrs:formatDate date="${encounter.encounterDatetime}"
								type="medium" /></td>
						<td>${encounter.patientId}</td>
						<td>${encounter.encounterType.encounterTypeId}</td>

					</tr>
				</c:forEach>
			</tbody>
		</table>
	
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>