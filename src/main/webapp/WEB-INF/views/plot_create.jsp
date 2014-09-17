<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<fieldset>
					<p class="help-block">
						<spring:message code="plot_create.form.informative_message" />
					</p>
				</fieldset>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>

<form:form modelAttribute="plot_create">
	<div class="row">
		<div class="col-lg-8">
			<div class="panel panel-default">

				<!-- /.panel-heading -->

				<div class="panel-body">
					<div class="form-group" id="modules-div">
						<fieldset>
							<legend>Module</legend>
							<select id="modules-select" class="form-control">
							</select>
						</fieldset>
					</div>
					<div class="form-group" id="operations-div"></div>
					<div id="parameters-div"></div>
				</div>
				<!-- .panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
</form:form>