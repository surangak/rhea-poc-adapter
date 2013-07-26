<openmrs:require privilege="View Patients" otherwise="/login.htm"
	redirect="/index.htm" />
<openmrs:htmlInclude
	file="/moduleResources/rheapocadapter/media/js/jquery-1.7.2.js" />

<openmrs:htmlInclude
	file="/moduleResources/rheapocadapter/media/js/jquery-ui.js" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />
	

<openmrs:htmlInclude
	file="/moduleResources/rheapocadapter/media/css/demo_table_jui.css" />
<openmrs:htmlInclude
	file="/moduleResources/rheapocadapter/media/themes/smoothness/jquery-ui-1.8.4.custom.css" />

<openmrs:htmlInclude
	file="/moduleResources/rheapocadapter/media/themes/smoothness/jquery-ui.css" />

<openmrs:htmlInclude
	file="/moduleResources/rheapocadapter/media/js/jquery.dataTables.columnFilter.js" />

<script></script>
<div style="text-align: center; ">
	<ul id="menu">
		<li
			<c:if test='<%=request.getRequestURI().contains("manageQueue")%>'>class="active"</c:if>><a
			href="${pageContext.request.contextPath}/module/rheapocadapter/manageQueue.form"><spring:message
					code="rheapocadapter.label.archive" /></a></li>
		<li
			<c:if test='<%=request.getRequestURI().contains("processingQueue")%>'>class="active"</c:if>><a
			href="${pageContext.request.contextPath}/module/rheapocadapter/processingQueue.form"><spring:message
					code="rheapocadapter.label.processing" /></a></li>
		<li
			<c:if test='<%=request.getRequestURI().contains("errorQueue")%>'>class="active"</c:if>><a
			href="${pageContext.request.contextPath}/module/rheapocadapter/errorQueue.form"><spring:message
					code="rheapocadapter.label.error" /></a></li>
		<li
			<c:if test='<%=request.getRequestURI().contains("backEnteredQueue")%>'>class="active"</c:if>><a
			href="${pageContext.request.contextPath}/module/rheapocadapter/backEnteredQueue.form"><spring:message
					code="rheapocadapter.label.backEnteredQueue" /></a></li>
	</ul>
	
</div>
<!-- ${pageContext.request.contextPath}/module/adaptermodule/registerPatientForm.form -->