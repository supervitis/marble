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
							code="instagram_topics_list.form.informative_message" />
					</p>
					<div class="form-group">
						<div class="col-lg-offset-10 col-lg-2">
							<a href="<c:url value="instagram_topic/create"/>"
								class="btn btn-primary pull-right"> <i class="fa fa-file-o"></i>
								<spring:message code="instagram_topics_list.form.create" />
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
				<spring:message code="instagram_topics_list.form.instagram_topics" />
			</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if test="${not empty instagram_topics}">
					<div class="table-responsive">
						<table
							class="table data-table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th scope="col"><spring:message
											code="instagram_topics_list.form.name" /></th>
									<th scope="col"><spring:message
											code="instagram_topics_list.form.actions" /></th>
								</tr>
							</thead>
							<c:forEach var="instagram_topic" items="${instagram_topics}">
								<tr>
									<td>${instagram_topic.name}</td>
									<td><a
										href="<c:url value="instagram_topic/${instagram_topic.id}"/>"
										class="btn btn-default"> <i class="fa fa-info-circle"></i><span
											class="hidden-xs hidden-sm"> <spring:message
													code="instagram_topics_list.form.info" /></span>

									</a> 
									<a
										href="<c:url value="instagram_topic/${instagram_topic.id}/execution/extract"/>"
										class="btn btn-default btn-light-green"> <i
										class="fa fa-sign-in"></i><span class="hidden-xs hidden-sm">
											<spring:message code="instagram_topics_list.form.extract" />
										</span>
									</a>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${empty instagram_topics}">
					<p>
						<spring:message code="instagram_topics_list.form.empty_message" />
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