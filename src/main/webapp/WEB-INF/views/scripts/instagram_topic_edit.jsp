<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
$(function() {
$("#sinceDate").datetimepicker().on('change', function(ev){
	
	$("#sinceDate").datetimepicker('hide');
});
$("#untilDate").datetimepicker().on('change', function(ev){
	$("#upperLimit").val(Date.parse($("#untilDate").val())/1000);
	$("#untilDate").datetimepicker('hide');
});
})
</script>