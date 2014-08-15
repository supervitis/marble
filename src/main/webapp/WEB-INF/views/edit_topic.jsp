<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">
			<spring:message code="edit_topic.form.title" />
		</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<form:form modelAttribute="topic" enctype="multipart/form-data">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<fieldset>
						<p class="help-block"><spring:message code='edit_topic.form.informative_message' /></p>
						<div class="form-group">
							<div class="col-lg-offset-10 col-lg-2">
								<form:input type="hidden" id="id" path="id" class="form-control" />
								<input type="submit" id="btnAdd" class="btn btn-primary pull-right" value="<spring:message code='edit_topic.form.save' />" /> <a
									href="<c:url value="/topic"/>" class="btn btn-default pull-right"><spring:message code="edit_topic.form.cancel" /></a>

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
					<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<fieldset>
						<legend>
							<spring:message code="edit_topic.form.general_properties" />
						</legend>
						<div class="form-group">
							<label for="name"><spring:message code="topic.form.name.label" /></label>
							<form:input id="name" path="name" type="text" class="form-control" />
							<form:errors path="name" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="description"><spring:message code="topic.form.description.label" /></label>
							<form:input id="description" path="description" type="text" class="form-control" />
							<form:errors path="description" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

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
					<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<fieldset>
						<legend>
							<spring:message code="edit_topic.form.extractor_properties" />
						</legend>

						<div class="form-group">
							<label for="keywords"><spring:message code="topic.form.keywords.label" /></label>
							<form:input id="keywords" path="keywords" type="text" class="form-control" />
							<form:errors path="keywords" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="upperLimit"><spring:message code="topic.form.upperLimit.label" /></label>
							<form:input id="upperLimit" path="upperLimit" type="text" class="form-control" />
							<form:errors path="upperLimit" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="lowerLimit"><spring:message code="topic.form.lowerLimit.label" /></label>
							<form:input id="lowerLimit" path="lowerLimit" type="text" class="form-control" />
							<form:errors path="lowerLimit" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="language"><spring:message code="topic.form.language.label" /></label>
							<form:input id="language" path="language" type="text" class="form-control" />
							<form:errors path="language" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="statusesPerCall"><spring:message code="topic.form.statusesPerCall.label" /></label>
							<form:input id="statusesPerCall" path="statusesPerCall" type="text" class="form-control" />
							<form:errors path="statusesPerCall" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="statusesPerFullExtraction"><spring:message code="topic.form.statusesPerFullExtraction.label" /></label>
							<form:input id="statusesPerFullExtraction" path="statusesPerFullExtraction" type="text" class="form-control" />
							<form:errors path="statusesPerFullExtraction" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>
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
					<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<fieldset>
						<legend>
							<spring:message code="edit_topic.form.processor_properties" />
						</legend>
						<div class="form-group">
							<label for="processorPositiveBoundary"><spring:message code="topic.form.processorPositiveBoundary.label" /></label>
							<form:input id="processorPositiveBoundary" path="processorPositiveBoundary" type="text" class="form-control" />
							<form:errors path="processorPositiveBoundary" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="processorNegativeBoundary"><spring:message code="topic.form.processorNegativeBoundary.label" /></label>
							<form:input id="processorNegativeBoundary" path="processorNegativeBoundary" type="text" class="form-control" />
							<form:errors path="processorNegativeBoundary" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>
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
					<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<fieldset>
						<legend>
							<spring:message code="edit_topic.form.plotter_properties" />
						</legend>
						<div class="form-group">
							<label for="plotterLeftDateBoundary"><spring:message code="topic.form.plotterLeftDateBoundary.label" /></label>
							<form:input id="plotterLeftDateBoundary" path="plotterLeftDateBoundary" type="text" class="form-control" />
							<form:errors path="plotterLeftDateBoundary" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="plotterRightDateBoundary"><spring:message code="topic.form.plotterRightDateBoundary.label" /></label>
							<form:input id="plotterRightDateBoundary" path="plotterRightDateBoundary" type="text" class="form-control" />
							<form:errors path="plotterRightDateBoundary" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
						</div>

						<div class="form-group">
							<label for="plotterStepSize"><spring:message code="topic.form.plotterStepSize.label" /></label>
							<form:input id="plotterStepSize" path="plotterStepSize" type="text" class="form-control" />
							<form:errors path="plotterStepSize" cssClass="text-danger" />
							<p class="help-block">Example block-level help text here.</p>
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
