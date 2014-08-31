<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function () {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
	});
    
    function getTime() {
        var currentDate = new Date();
        var currentTime = ('0' + currentDate.getHours()).slice(-2) + ':'
                + ('0' + (currentDate.getMinutes() + 1)).slice(-2) + ':'
                + ('0' + (currentDate.getSeconds() + 1)).slice(-2);
        return currentTime;
    };

    function sendCommand(command) {
        $.ajax({
            type : "PUT",
            data : '{"command": "' + command + '"}',
            contentType: "application/json",
            url : "<c:url value="/rest/execution/${execution.id}/command" />"
        }).done(
                function() {
                    notification = '<div class="alert alert-success alert-dismissable"><button type="button" class="close" data-dismiss="alert" '+
                    'aria-hidden="true">×</button><i class="fa fa-crosshairs"></i> Command <'+command+'> sent.</div>';
                    $($.parseHTML(notification)).appendTo("#notifications");
        }).fail(
                function() {
                    notification = '<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert"  '+
                    'aria-hidden="true">×</button><i class="fa fa-crosshairs"></i> Command <'+command+'> could not be sent.</div>';
                    $($.parseHTML(notification)).appendTo("#notifications");
                });

    };
    
    $("#send-command-stop").click(function() {
        sendCommand("stop");
    });

    (function refreshLog() {
        $.ajax({
            dataType : "json",
            url : "<c:url value="/rest/execution/${execution.id}" />"
        }).done(
                function(data) {
                    $("#execution-log").html(data.log);
                    $("#execution-status").html(data.status);
                    $("#execution-type").html(data.type);
                    $("#execution-command").html(data.command);
                    $("#execution-updated-at").html(data.updatedAt);

                    $("#updated").removeClass("alert-warning");
                    $("#updated").addClass("alert-success");
                    $("#updated").html(
                            "<i class='fa fa-clock-o'></i> Updated at "
                                    + getTime() + ".");
                }).fail(
                function() {
                    $("#updated").removeClass("alert-success");
                    $("#updated").addClass("alert-warning");
                    $("#updated").html(
                            "<i class='fa fa-clock-o'></i> Update failed at "
                                    + getTime() + ".");
                }).always(function() {
            // Schedule the next request when the current one's complete
            setTimeout(refreshLog, 5000);
        });
    })();
</script>