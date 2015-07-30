<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message code="datasets_list.form.dataset_informative_message" />
					</p>
					<div class="form-group">
						<div class="col-lg-offset-10 col-lg-2">
							<a href="<c:url value="datasets/create"/>"
								class="btn btn-primary pull-right"><i class="fa fa-file-o"></i>
								<spring:message code="datasets_list.form.create" /></a>

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
<!-- Datasets -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="datasets_list.form.datasets" />
			</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if test="${not empty datasets}">
					<div class="table-responsive">
						<table
							class="table data-table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th scope="col"><spring:message
											code="datasets_list.form.name" /></th>
									<th scope="col"><spring:message
											code="datasets_list.form.description" /></th>
									<th scope="col"><spring:message
											code="datasets_list.form.actions" /></th>
								</tr>
							</thead>
							<c:forEach var="dataset" items="${datasets}">
								<tr>
									<td>${dataset.name}</td>
									<td>${dataset.description}</td>
									<td><a href="<c:url value="datasets/edit/${dataset.id}"/>"
										class="btn btn-default"><i class="fa fa-pencil"></i> <spring:message
												code="datasets_list.form.edit" /></a><a
										href="<c:url value="datasets/download/${dataset.id}"/>"
										class="btn btn-default"><i class="fa fa-download"></i> <spring:message
												code="datasets_list.form.download" /></a></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${empty datasets}">
					<p>
						<spring:message code="datasets_list.form.empty_message" />
					</p>
				</c:if>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- Topics -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message code="datasets_list.form.topic_informative_message" />
					</p>
					<div class="form-group">
						<div class="col-lg-offset-10 col-lg-2">
							<a href="<c:url value="topic/create"/>"
								class="btn btn-primary pull-right"><i class="fa fa-file-o"></i>
								<spring:message code="datasets_list.form.create" /></a>

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
				<spring:message code="datasets_list.form.topics" />
			</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if test="${not empty topics}">
					<div class="table-responsive">
						<table
							class="table data-table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th scope="col"><spring:message
											code="datasets_list.form.name" /></th>
									<th scope="col"><spring:message
											code="datasets_list.form.description" /></th>
									<th scope="col"><spring:message
											code="datasets_list.form.actions" /></th>
								</tr>
							</thead>
							<c:forEach var="topic" items="${topics}">
								<tr>
									<td>${topic.name}</td>
									<td>${topic.description}</td>
									<td><a href="<c:url value="topic/${topic.id}/edit"/>"
										class="btn btn-default"><i class="fa fa-pencil"></i> <spring:message
												code="datasets_list.form.edit" /></a><a
										href="<c:url value="topic/${topic.id}/download"/>"
										class="btn btn-default"><i class="fa fa-download"></i> <spring:message
												code="datasets_list.form.download" /></a></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${empty topics}">
					<p>
						<spring:message code="topics_list.form.empty_message" />
					</p>
				</c:if>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- Streaming Topics -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message code="datasets_list.form.streaming_topic_informative_message" />
					</p>
					<div class="form-group">
						<div class="col-lg-offset-10 col-lg-2">
							<a href="<c:url value="streaming_topic/create"/>"
								class="btn btn-primary pull-right"><i class="fa fa-file-o"></i>
								<spring:message code="datasets_list.form.create" /></a>

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
				<spring:message code="datasets_list.form.streaming_topics" />
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
											code="datasets_list.form.name" /></th>
									<th scope="col"><spring:message
											code="datasets_list.form.description" /></th>
									<th scope="col"><spring:message
											code="datasets_list.form.actions" /></th>
								</tr>
							</thead>
							<c:forEach var="streaming_topic" items="${streaming_topics}">
								<tr>
									<td>${streaming_topic.name}</td>
									<td>${streaming_topic.description}</td>
									<td><a href="<c:url value="streaming_topic/${streaming_topic.id}/edit"/>"
										class="btn btn-default"><i class="fa fa-pencil"></i> <spring:message
												code="datasets_list.form.edit" /></a><a
										href="<c:url value="streaming_topic/${streaming_topic.id}/download"/>"
										class="btn btn-default"><i class="fa fa-download"></i> <spring:message
												code="datasets_list.form.download" /></a></td>
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