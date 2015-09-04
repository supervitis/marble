<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form:form modelAttribute="streaming_topic">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">
							<c:if test="${not empty streaming_topic.id}">
								<spring:message code='streaming_topic_edit.form.informative_message.edit' />
								<form:input type="hidden" id="id" path="id" class="form-control" />
							</c:if>
							<c:if test="${empty streaming_topic.id}">
								<spring:message code='streaming_topic_edit.form.informative_message.add' />
							</c:if>
						</p>
						<div class="form-group">
							<div class="col-lg-offset-8 col-lg-4">
								<button type="submit" id="save" class="btn btn-primary pull-right">
									<i class="fa fa-floppy-o"></i>
									<spring:message code="streaming_topic_edit.form.save" />
								</button>
								<a href="<c:url value="streaming_topic/${streaming_topic.id}"/>" class="btn btn-default pull-right"><i class="fa fa-times"></i> <spring:message
										code="streaming_topic_edit.form.cancel" /></a>

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


		<div class="col-lg-6">
			<div class="panel panel-default">
				<div class="panel-body">

					<fieldset>
						<legend>
							<spring:message code="streaming_topic_edit.form.general_properties" />
						</legend>
						<spring:bind path="name">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="name"><spring:message code="streaming_topic.form.name.label" /></label>
								<form:input id="name" path="name" type="text" class="form-control" />
								<form:errors path="name" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.name.help" /></p>
							</div>
						</spring:bind>

						<spring:bind path="description">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="description"><spring:message code="streaming_topic.form.description.label" /></label>
								<form:input id="description" path="description" type="text" class="form-control" />
								<form:errors path="description" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.description.help" /></p>
							</div>
						</spring:bind>

					</fieldset>


				</div>
				<!-- .panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-6 -->

		<div class="col-lg-6">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<legend>
							<spring:message code="streaming_topic_edit.form.extractor_properties" />
						</legend>
						<spring:bind path="keywords">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="keywords"><spring:message code="streaming_topic.form.keywords.label" /></label>
								<form:input id="keywords" path="keywords" type="text" class="form-control" />
								<form:errors path="keywords" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.keywords.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="upperLimit">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="upperLimit"><spring:message code="streaming_topic.form.upperLimit.label" /></label>
								<form:input id="upperLimit" path="upperLimit" type="text" class="form-control" />
								<form:errors path="upperLimit" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.upperLimit.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="lowerLimit">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="lowerLimit"><spring:message code="streaming_topic.form.lowerLimit.label" /></label>
								<form:input id="lowerLimit" path="lowerLimit" type="text" class="form-control" />
								<form:errors path="lowerLimit" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.lowerLimit.help" /></p>
							</div>
						</spring:bind>
						
						<spring:bind path="sinceDate">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="sinceDate"><spring:message code="streaming_topic.form.sinceDate.label" /></label>
								<form:input id="sinceDate" path="sinceDate" type="text" class="form-control" />
								<form:errors path="sinceDate" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.sinceDate.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="untilDate">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="untilDate"><spring:message code="streaming_topic.form.untilDate.label" /></label>
								<form:input id="untilDate" path="untilDate" type="text" class="form-control" />
								<form:errors path="untilDate" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.untilDate.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="geoLatitude">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="geoLatitude"><spring:message code="streaming_topic.form.geoLatitude.label" /></label>
								<form:input id="geoLatitude" path="geoLatitude" type="text" class="form-control" />
								<form:errors path="geoLatitude" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.geoLatitude.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="geoLongitude">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="geoLongitude"><spring:message code="streaming_topic.form.geoLongitude.label" /></label>
								<form:input id="geoLongitude" path="geoLongitude" type="text" class="form-control" />
								<form:errors path="geoLongitude" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.geoLongitude.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="geoRadius">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="geoRadius"><spring:message code="streaming_topic.form.geoRadius.label" /></label>
								<form:input id="geoRadius" path="geoRadius" type="text" class="form-control" />
								<form:errors path="geoRadius" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.geoRadius.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="geoUnit">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="geoUnit"><spring:message code="streaming_topic.form.geoUnit.label" /></label>
								<form:select id="geoUnit-select" path="geoUnit" items="${geoUnits}" class="form-control" />
								<form:errors path="geoUnit" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.geoUnit.help" /></p>
							</div> 
						</spring:bind>
						<spring:bind path="language">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="language"><spring:message code="streaming_topic.form.language.label" /></label>
								<form:input id="language" path="language" type="text" class="form-control" />
								<form:errors path="language" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.language.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="email">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="email"><spring:message code="streaming_topic.form.email.label" /></label>
								<form:input id="email" path="email" type="text" class="form-control" />
								<form:errors path="email" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.email.help" /></p>
							</div>
						</spring:bind>
						<spring:bind path="statusesPerFullExtraction">
							<div class="form-group ${status.error ? 'has-error': ''}">
								<label for="statusesPerFullExtraction"><spring:message code="streaming_topic.form.statusesPerFullExtraction.label" /></label>
								<form:input id="statusesPerFullExtraction" path="statusesPerFullExtraction" type="text" class="form-control" />
								<form:errors path="statusesPerFullExtraction" cssClass="text-danger" />
								<p class="help-block"><spring:message code="streaming_topic.form.statusesPerFullExtraction.help" /></p>
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

<c:if test="${not empty streaming_topic.id}">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<fieldset>
						<p class="help-block">If you want to delete this streaming_topic, click the Delete button below (there is no way back!).</p>
						<div class="form-group">
							<div class="col-lg-offset-10 col-lg-2">
								<a href="<c:url value="streaming_topic/${streaming_topic.id}/delete"/>" class="btn btn-danger pull-right"><i class="fa fa-trash-o"></i> Delete</a>

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

