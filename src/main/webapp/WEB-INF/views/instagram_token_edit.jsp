<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form:form modelAttribute="instagram_token">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">
							<c:if test="${not empty instagram_token.id}">
								<spring:message code='instagram_token_edit.form.informative_message.edit' />
								<form:input type="hidden" id="id" path="id" class="form-control" />
							</c:if>
							<c:if test="${empty instagram_token.id}">
								<spring:message code='instagram_token_edit.form.informative_message.add' />
							</c:if>
						</p>
						<div class="form-group">
							<div class="col-lg-offset-8 col-lg-4">
								<button type="submit" id="save" class="btn btn-primary pull-right">
									<i class="fa fa-floppy-o"></i>
									<spring:message code="instagram_token_edit.form.save" />
								</button>
								<a href="<c:url value="admin/keys/instagram"/>" class="btn btn-default pull-right">
									<i class="fa fa-times"></i>
									<spring:message code="instagram_token_edit.form.cancel" />
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
				<div class="panel-body">

					<fieldset>
						<legend>
							<spring:message code="instagram_token_edit.form.general_properties" />
						</legend>

						<spring:bind path="description">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="description"><spring:message code="instagram_token.form.description.label" /></label>
								<form:input id="description" path="description" type="text" class="form-control" />
								<form:errors path="description" cssClass="text-danger" />
								<p class="help-block">
									<spring:message code="instagram_token.form.description.help" />
								</p>
							</div>
						</spring:bind>



						<spring:bind path="accessToken">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="accessToken"><spring:message code="instagram_token.form.accessToken.label" /></label>
								<form:input id="accessToken" path="accessToken" type="text" class="form-control" />
								<form:errors path="accessToken" cssClass="text-danger" />
								<p class="help-block">
									<spring:message code="instagram_token.form.accessToken.help" />
								</p>
							</div>
						</spring:bind>

						<spring:bind path="enabled">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<fieldset>
									<legend>
										<spring:message code="instagram_token.form.other" />
									</legend>
									<div class="checkbox">
										<label for="enabled"> <form:checkbox path="enabled" /> <spring:message
												code="instagram_token.form.enabled.label" />
										</label>
									</div>
									<form:errors path="enabled" cssClass="text-danger" />
									<p class="help-block">
										<spring:message code="instagram_token.form.enabled.help" />
									</p>
								</fieldset>
							</div>
						</spring:bind>

					</fieldset>


				</div>
				<!-- .panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-6 -->
	</div>
	<!-- /.row -->
</form:form>

<c:if test="${not empty instagram_token.id}">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">If you want to delete this Instagram Token, click the Delete button below (there is no
							way back!).</p>
						<div class="form-group">
							<div class="col-lg-offset-10 col-lg-2">
								<a href="<c:url value="admin/keys/instagram/delete/${instagram_token.id}"/>" class="btn btn-danger pull-right">
									<i class="fa fa-trash-o"></i> Delete
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
</c:if>

