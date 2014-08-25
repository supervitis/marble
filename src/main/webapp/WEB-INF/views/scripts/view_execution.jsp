<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	(function refreshLog() {
		$.ajax({
			dataType : "json",
			url : "<c:url value="/rest/execution/${execution.id}" />",
			success : function(data) {
				$("#log-container").html(data.log);
			}
			})
			.done( function() {
				// Schedule the next request when the current one's complete
				setTimeout(refreshLog, 5000);
			})
		;
	})();
</script>