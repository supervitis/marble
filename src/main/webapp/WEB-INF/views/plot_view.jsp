<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">

	<div class="col-lg-8">
		<div class="panel panel-default">
			<div class="panel-heading">Chart</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<div id="plot-placeholder"></div>
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
								<th>Name</th>
								<th id="plot-name">${plot.name}</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th>Description</th>
								<td id="plot-description">${plot.description}</td>
							</tr>
							<tr>
								<th>Created At</th>
								<td id="plot-created-at">${plot.createdAt}</td>
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
								<th><a href='<c:url value="plot/delete/${plot.id}" />' class="btn btn-danger btn-block">
										<i class="fa fa-trash-o"></i> Delete
									</a></th>
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

	</div>
	<!-- /.col-lg-4 -->

</div>
<!-- /.row -->