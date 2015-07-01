<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:if test="${not empty dataset.id}">
	<c:url value='/datasets/${dataset.id}/edit?${_csrf.parameterName}=${_csrf.token}' var="uploadDatasetUrl" />
</c:if>
<c:if test="${empty dataset.id}">
	<c:url value='/datasets/create?${_csrf.parameterName}=${_csrf.token}' var="uploadDatasetUrl" />
</c:if>

<form:form modelAttribute="dataset" enctype="multipart/form-data" action='${uploadDatasetUrl}'>
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">
							<c:if test="${not empty dataset.id}">
								<spring:message code='dataset_edit.form.informative_message.edit' />
								<form:input type="hidden" id="id" path="id" class="form-control" />
							</c:if>
							<c:if test="${empty dataset.id}">
								<spring:message code='dataset_edit.form.informative_message.add' />
							</c:if>
						</p>
						<div class="form-group">
							<div class="col-lg-offset-8 col-lg-4">
								<button type="submit" id="save" class="btn btn-primary pull-right">
									<i class="fa fa-floppy-o"></i>
									<spring:message code="dataset_edit.form.save" />
								</button>
								<a href="<c:url value="datasets"/>" class="btn btn-default pull-right">
									<i class="fa fa-times"></i>
									<spring:message code="dataset_edit.form.cancel" />
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
							<spring:message code="dataset_edit.form.general_properties" />
						</legend>

						<spring:bind path="description">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="description"><spring:message code="dataset.form.description.label" /></label>
								<form:input id="description" path="description" type="text" class="form-control" />
								<form:errors path="description" cssClass="text-danger" />
								<p class="help-block">
									<spring:message code="dataset.form.description.help" />
								</p>
							</div>
						</spring:bind>

						<spring:bind path="name">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="name"><spring:message code="dataset.form.name.label" /></label>
								<form:input id="name" path="name" type="text" class="form-control" />
								<form:errors path="name" cssClass="text-danger" />
								<p class="help-block">
									<spring:message code="dataset.form.name.help" />
								</p>
							</div>
						</spring:bind>
						
						<div class="form-group">
						<p>Select the file containing the data in json format and click upload. Previous data on this dataset will be removed</p>
						<input name="file" type="file" class="form-control" />
						</div>

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

<c:if test="${not empty dataset.id}">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">If you want to delete this Dataset, click the Delete button below (there is no
							way back!).</p>
						<div class="form-group">
							<div class="col-lg-offset-10 col-lg-2">
								<a href="<c:url value="datasets/delete/${dataset.id}"/>" class="btn btn-danger pull-right">
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

