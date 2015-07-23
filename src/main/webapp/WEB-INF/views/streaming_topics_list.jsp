<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message
							code="streaming_topics_list.form.informative_message" />
					</p>
					<div class="form-group">
						<div class="col-lg-offset-10 col-lg-2">
							<a href="<c:url value="streaming_topic/create"/>"
								class="btn btn-primary pull-right"> <i class="fa fa-file-o"></i>
								<spring:message code="streaming_topics_list.form.create" />
							</a>
						</div>
					</div>
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
				<spring:message code="streaming_topics_list.form.streaming_topics" />
			</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if test="${not empty streaming_topics}">
					<div class="table-responsive">
						<table
							class="table data-table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th scope="col"><spring:message
											code="streaming_topics_list.form.name" /></th>
									<th scope="col"><spring:message
											code="streaming_topics_list.form.actions" /></th>
								</tr>
							</thead>
							<c:forEach var="streaming_topic" items="${streaming_topics}">
								<tr>
									<td>${streaming_topic.name}</td>
									<td><a
										href="<c:url value="streaming_topic/${streaming_topic.id}"/>"
										class="btn btn-default"> <i class="fa fa-info-circle"></i><span
											class="hidden-xs hidden-sm"> <spring:message
													code="streaming_topics_list.form.info" /></span>

									</a> <c:choose>
											<c:when test="${not streaming_topic.active}">
												<a
													href="<c:url value="streaming_topic/${streaming_topic.id}/execution/extract"/>"
													class="btn btn-default btn-light-green"> <i
													class="fa fa-sign-in"></i><span class="hidden-xs hidden-sm">
														<spring:message code="streaming_topics_list.form.extract" />
												</span>
												</a>
											</c:when>
											<c:otherwise>
												<a
													href="<c:url value="streaming_topic/${streaming_topic.id}/execution/stop"/>"
													class="btn btn-default btn-light-red"> <i
													class="fa fa-stop"></i><span class="hidden-xs hidden-sm">
														<spring:message code="streaming_topics_list.form.stop" />
												</span>
												</a>
											</c:otherwise>
										</c:choose></td>

								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${empty streaming_topics}">
					<p>
						<spring:message code="streaming_topics_list.form.empty_message" />
					</p>
				</c:if>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->