<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">

	<div class="col-lg-8">
		<div class="panel panel-default">
			<div class="panel-heading">Execution Log</div>
			<!-- /.panel-heading -->

			<div class="panel-body">

				<div class="form-group">
					<textarea id="execution-log" class="form-control resize-vertical" rows="20" disabled="disabled"></textarea>
					<p class="help-block">The log is displayed backwards, and only the recent entries are shown. Also, the log is
						refreshed periodically, so you don't have to refresh the whole page.</p>
				</div>

			</div>

			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->

	<div class="col-lg-4">
		<div class="panel panel-default">
			<div class="panel-heading">Information</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>Status</th>
								<th id="execution-status">${execution.status }</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th>Type</th>
								<td id="execution-type">Unknown</td>
							</tr>
							<tr>
								<th>Created At</th>
								<td id="execution-created-at">Unknown</td>
							</tr>
							<tr>
								<th>Last Updated At</th>
								<td id="execution-updated-at">Unknown</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
		<div class="panel panel-default">
			<div class="panel-heading">Actions</div>
			<!-- /.panel-heading -->

			<div class="panel-body">

				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th><div data-toggle="tooltip" title="Actions are not available right now.">
										<a id="send-command-stop" class="btn btn-default btn-block disabled">Stop</a>
									</div></th>
							</tr>
						</thead>
						<!-- <tbody>
							<tr>
								<td></td>
							</tr>
						</tbody>-->
					</table>
				</div>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
		<div class="panel panel-default">
			<div class="panel-body">
				<div id="updated" class="alert alert-success"></div>
				<div id="notifications"></div>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->

</div>
<!-- /.row -->