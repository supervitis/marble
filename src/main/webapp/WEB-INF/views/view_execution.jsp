<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">

	<div class="col-lg-8">
		<div class="panel panel-default">
			<div class="panel-heading">Execution Log</div>
			<!-- /.panel-heading -->

			<div class="panel-body">

				<div class="form-group">
					<textarea id="log-container" class="form-control" rows="20" disabled="disabled"></textarea>
					<p class="help-block">The log is displayed backwards, and only the recent entries are shown. Also, the log
						is refreshed periodically, so you don't have to refresh the whole page.</p>
				</div>

			</div>

			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->

	<div class="col-lg-4">
		<div class="panel panel-default">
			<div class="panel-heading">Actions</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<ul>
					<li><a href="<c:url value="#"/>" class="btn btn-primary">Start</a></li>
					<li><a href="<c:url value="#"/>" class="btn">Stop</a></li>
				</ul>
			</div>

			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->

</div>
<!-- /.row -->