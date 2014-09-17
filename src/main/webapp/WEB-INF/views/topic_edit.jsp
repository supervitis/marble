<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form:form modelAttribute="topic">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">
							<c:if test="${not empty topic.id}">
								<spring:message code='topic_edit.form.informative_message.edit' />
								<form:input type="hidden" id="id" path="id" class="form-control" />
							</c:if>
							<c:if test="${empty topic.id}">
								<spring:message code='topic_edit.form.informative_message.add' />
							</c:if>
						</p>
						<div class="form-group">
							<div class="col-lg-offset-8 col-lg-4">
								<button type="submit" id="save" class="btn btn-primary pull-right">
									<i class="fa fa-floppy-o"></i>
									<spring:message code="topic_edit.form.save" />
								</button>
								<a href="<c:url value="topic/${topic.id}"/>" class="btn btn-default pull-right"><i class="fa fa-times"></i> <spring:message
										code="topic_edit.form.cancel" /></a>

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


		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-body">

					<fieldset>
						<legend>
							<spring:message code="topic_edit.form.general_properties" />
						</legend>
						<spring:bind path="name">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="name"><spring:message code="topic.form.name.label" /></label>
								<form:input id="name" path="name" type="text" class="form-control" />
								<form:errors path="name" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>

						<spring:bind path="description">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="description"><spring:message code="topic.form.description.label" /></label>
								<form:input id="description" path="description" type="text" class="form-control" />
								<form:errors path="description" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>

					</fieldset>


				</div>
				<!-- .panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-6 -->

		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<legend>
							<spring:message code="topic_edit.form.extractor_properties" />
						</legend>
						<spring:bind path="keywords">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="keywords"><spring:message code="topic.form.keywords.label" /></label>
								<form:input id="keywords" path="keywords" type="text" class="form-control" />
								<form:errors path="keywords" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
						<spring:bind path="upperLimit">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="upperLimit"><spring:message code="topic.form.upperLimit.label" /></label>
								<form:input id="upperLimit" path="upperLimit" type="text" class="form-control" />
								<form:errors path="upperLimit" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
						<spring:bind path="lowerLimit">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="lowerLimit"><spring:message code="topic.form.lowerLimit.label" /></label>
								<form:input id="lowerLimit" path="lowerLimit" type="text" class="form-control" />
								<form:errors path="lowerLimit" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
						<spring:bind path="language">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="language"><spring:message code="topic.form.language.label" /></label>
								<form:input id="language" path="language" type="text" class="form-control" />
								<form:errors path="language" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
						<spring:bind path="statusesPerCall">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="statusesPerCall"><spring:message code="topic.form.statusesPerCall.label" /></label>
								<form:input id="statusesPerCall" path="statusesPerCall" type="text" class="form-control" />
								<form:errors path="statusesPerCall" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
						<spring:bind path="statusesPerFullExtraction">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="statusesPerFullExtraction"><spring:message code="topic.form.statusesPerFullExtraction.label" /></label>
								<form:input id="statusesPerFullExtraction" path="statusesPerFullExtraction" type="text" class="form-control" />
								<form:errors path="statusesPerFullExtraction" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
					</fieldset>
				</div>
				<!-- .panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-6 -->

		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<legend>
							<spring:message code="topic_edit.form.processor_properties" />
						</legend>
						<spring:bind path="processorPositiveBoundary">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="processorPositiveBoundary"><spring:message code="topic.form.processorPositiveBoundary.label" /></label>
								<form:input id="processorPositiveBoundary" path="processorPositiveBoundary" type="text" class="form-control" />
								<form:errors path="processorPositiveBoundary" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
						<spring:bind path="processorNegativeBoundary">
							<div class="form-group">
								<label for="processorNegativeBoundary"><spring:message code="topic.form.processorNegativeBoundary.label" /></label>
								<form:input id="processorNegativeBoundary" path="processorNegativeBoundary" type="text" class="form-control" />
								<form:errors path="processorNegativeBoundary" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>
					</fieldset>


				</div>
				<!-- .panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-6 -->

		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<legend>
							<spring:message code="topic_edit.form.plotter_properties" />
						</legend>
						<spring:bind path="plotterLeftDateBoundary">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="plotterLeftDateBoundary"><spring:message code="topic.form.plotterLeftDateBoundary.label" /></label>
								<form:input id="plotterLeftDateBoundary" path="plotterLeftDateBoundary" type="text" class="form-control" />
								<form:errors path="plotterLeftDateBoundary" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>

						<spring:bind path="plotterRightDateBoundary">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="plotterRightDateBoundary"><spring:message code="topic.form.plotterRightDateBoundary.label" /></label>
								<form:input id="plotterRightDateBoundary" path="plotterRightDateBoundary" type="text" class="form-control" />
								<form:errors path="plotterRightDateBoundary" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
							</div>
						</spring:bind>

						<spring:bind path="plotterStepSize">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="plotterStepSize"><spring:message code="topic.form.plotterStepSize.label" /></label>
								<form:input id="plotterStepSize" path="plotterStepSize" type="text" class="form-control" />
								<form:errors path="plotterStepSize" cssClass="text-danger" />
								<p class="help-block">Example block-level help text here.</p>
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

<c:if test="${not empty topic.id}">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">If you want to delete this Topic, click the Delete button below (there is no way back!).</p>
						<div class="form-group">
							<div class="col-lg-offset-10 col-lg-2">
								<a href="<c:url value="topic/delete/${topic.id}"/>" class="btn btn-danger pull-right"><i class="fa fa-trash-o"></i> Delete</a>

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

