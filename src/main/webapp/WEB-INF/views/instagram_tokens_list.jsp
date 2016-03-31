<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message code="instagram_tokens_list.form.informative_message" />
					</p>
					<div class="form-group">
						<div class="col-lg-offset-10 col-lg-2">
							<a href="<c:url value="admin/keys/instagram/create"/>" class="btn btn-primary pull-right"><i class="fa fa-file-o"></i> <spring:message
									code="instagram_tokens_list.form.add" /></a>
									
							<a href="<c:url value="https://api.instagram.com/oauth/authorize/?client_id=${client_id}&redirect_uri=${redirect_uri}&response_type=code"/>" class="btn btn-primary pull-right"><i class="fa fa-file-o"></i> <spring:message
									code="instagram_tokens_list.form.generate" /></a>

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
				<spring:message code="instagram_tokens_list.form.keys" />
			</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if test="${not empty instagram_tokens}">
					<div class="table-responsive">
						<table class="table data-table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th scope="col"><spring:message code="instagram_tokens_list.form.description" /></th>
									<th scope="col"><spring:message code="instagram_tokens_list.form.actions" /></th>
								</tr>
							</thead>
							<c:forEach var="instagram_token" items="${instagram_tokens}">
								<tr>
									<td>${instagram_token.description}</td>
									<td><a href="<c:url value="admin/keys/instagram/edit/${instagram_token.id}"/>" class="btn btn-default"><i
											class="fa fa-pencil"></i> <spring:message code="instagram_tokens_list.form.edit" /></a></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${empty instagram_tokens}">
					<p>
						<spring:message code="instagram_tokens_list.form.empty_message" />
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