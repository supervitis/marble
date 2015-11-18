<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message code="instagram_topic_info.informative_message" />
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
	<div class="col-lg-8">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="instagram_topic_info.information" />
			</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if
					test="${instagram_topicInfo.totalStatusesExtracted == 0 && instagram_topicInfo.totalStatusesProcessed == 0}">
					<spring:message
						code='instagram_topic_info.no_information_available' />
				</c:if>
				<c:if
					test="${instagram_topicInfo.totalStatusesExtracted != 0 || instagram_topicInfo.totalStatusesProcessed != 0}">

					<div class="list-group">
						<a class="list-group-item"> <i class="fa fa-sign-in fa-fw"></i>
							<spring:message
								code="instagram_topic_info.total_statuses_extracted" /> <span
							class="pull-right text-muted small"><em>
									${instagram_topicInfo.totalStatusesExtracted}</em> </span>
						</a> <a class="list-group-item"> <i class="fa fa-calendar fa-fw"></i>
							<spring:message code="instagram_topic_info.oldest_status_date" />
							<span class="pull-right text-muted small"><em>
									${instagram_topicInfo.oldestStatusDate}</em> </span>
						</a> <a class="list-group-item"> <i class="fa fa-barcode fa-fw"></i>
							<spring:message code="instagram_topic_info.oldest_status_id" />
							<span class="pull-right text-muted small"><em>
									${instagram_topicInfo.oldestStatusId}</em> </span>
						</a> <a class="list-group-item"> <i class="fa fa-calendar fa-fw"></i>
							<spring:message code="instagram_topic_info.newest_status_date" />
							<span class="pull-right text-muted small"><em>
									${instagram_topicInfo.newestStatusDate}</em> </span>
						</a> <a class="list-group-item"> <i class="fa fa-barcode fa-fw"></i>
							<spring:message code="instagram_topic_info.newest_status_id" />
							<span class="pull-right text-muted small"><em>
									${instagram_topicInfo.newestStatusId}</em> </span>
						</a> <a class="list-group-item"> <i class="fa fa-scissors fa-fw"></i>
							<spring:message
								code="instagram_topic_info.total_statuses_processed" /> <span
							class="pull-right text-muted small"><em>
									${instagram_topicInfo.totalStatusesProcessed}</em> </span>
						</a>

					</div>

				</c:if>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->

	<div class="col-lg-4">
		<div class="panel panel-default">
			<div class="panel-heading">Actions</div>
			<!-- /.panel-heading -->

			<div class="panel-body">

				<div class="table-responsive">
					<table class="table">
						<tbody>
							<tr>
								<td><a
									href="<c:url value="instagram_topic/${instagram_topicInfo.topicId}/edit"/>"
									class="btn btn-default btn-block"> <i class="fa fa-pencil"></i>
										<spring:message code="instagram_topic_info.actions.edit" />
								</a></td>
							</tr>
							<c:choose>
								<c:when test="${not instagram_topicInfo.active}">
									<tr>
										<td><a
											href="<c:url value="instagram_topic/${instagram_topicInfo.topicId}/execution/extract"/>"
											class="btn btn-default btn-block"> <i
												class="fa fa-sign-in"></i>
													<spring:message code="instagram_topics_list.form.extract" />
										</a></td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td><a
											href="<c:url value="instagram_topic/${instagram_topicInfo.topicId}/execution/stop"/>"
											class="btn btn-default btn-block"> <i
												class="fa fa-stop"></i>
													<spring:message code="instagram_topics_list.form.stop" />
										</a></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
			<!-- .panel-body -->
		</div>
	</div>
	<!-- /.col-lg-4 -->
</div>
<!-- /.row -->