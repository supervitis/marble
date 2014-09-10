<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message code="topic_info.informative_message" />
					</p>
				</fieldset>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="topic_info.information" />
			</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if test="${topicInfo.totalStatusesExtracted == 0 && topicInfo.totalStatusesProcessed == 0}">
					<spring:message code='topic_info.no_information_available' />
				</c:if>
				<c:if test="${topicInfo.totalStatusesExtracted != 0 || topicInfo.totalStatusesProcessed != 0}">

					<div class="list-group">
						<a class="list-group-item">
							<i class="fa fa-sign-in fa-fw"></i>
							<spring:message code="topic_info.total_statuses_extracted" />
							<span class="pull-right text-muted small"><em> ${topicInfo.totalStatusesExtracted}</em> </span>
						</a>
						<a class="list-group-item">
							<i class="fa fa-calendar fa-fw"></i>
							<spring:message code="topic_info.oldest_status_date" />
							<span class="pull-right text-muted small"><em> ${topicInfo.oldestStatusDate}</em> </span>
						</a>
						<a class="list-group-item">
							<i class="fa fa-barcode fa-fw"></i>
							<spring:message code="topic_info.oldest_status_id" />
							<span class="pull-right text-muted small"><em> ${topicInfo.oldestStatusId}</em> </span>
						</a>
						<a class="list-group-item">
							<i class="fa fa-calendar fa-fw"></i>
							<spring:message code="topic_info.newest_status_date" />
							<span class="pull-right text-muted small"><em> ${topicInfo.newestStatusDate}</em> </span>
						</a>
						<a class="list-group-item">
							<i class="fa fa-barcode fa-fw"></i>
							<spring:message code="topic_info.newest_status_id" />
							<span class="pull-right text-muted small"><em> ${topicInfo.newestStatusId}</em> </span>
						</a>
						<a class="list-group-item">
							<i class="fa fa-scissors fa-fw"></i>
							<spring:message code="topic_info.total_statuses_processed" />
							<span class="pull-right text-muted small"><em> ${topicInfo.totalStatusesProcessed}</em> </span>
						</a>

					</div>

				</c:if>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->