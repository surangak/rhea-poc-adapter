<div id="divDlg" title="Message"></div>

<br />
<script>
	var $k = jQuery.noConflict();

	$k(".opener").click(function() {
		var m = "txa" + $k(this).attr("id");
		var u = "url" + $k(this).attr("id");
		showDialog(m, u);

	});
	function showDialog(id, url) {
		$k("#divDlg").dialog({
			zIndex : 980,
			bgiframe : true,
			height : 600,
			width : 708,
			modal : true
		});

		var msg = $k("#" + id).val();
		var fullUrl = $k("#" + url).text();

		$k("#divDlg")
				.html(
						"<span style = 'font-size : 10px;'> <strong>URL: &nbsp;</strong>"
								+ fullUrl
								+ "</span></ br><textarea rows='30' cols='95' style='border: solid; border-color: aqua;' readonly='readonly'>"
								+ msg + "</textarea>");
	}
	$k(document).ready(function() {
	});
</script>