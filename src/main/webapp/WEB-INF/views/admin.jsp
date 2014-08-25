<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">

	<div class="col-lg-3">
		<div class="panel panel-default">
			<div class="panel-heading">Social Networks</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<p>Twitter API Keys</p>
					<div class="col-lg-offset-10 col-lg-2">
						<a href="<c:url value="/admin/keys/twitter"/>" class="btn btn-primary pull-right"><i class="fa fa-key"></i>
							View Keys</a>
					</div>
			</div>

			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-3 -->

	<div class="col-lg-3">
		<div class="panel panel-default">
			<div class="panel-heading">Undoable Actions</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<!-- Button trigger modal -->
				<button class="btn btn-primary" data-toggle="modal" data-target="#myModal">Reset Everything</button>
				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">Reset Everything</h4>
							</div>
							<div class="modal-body">Are you really, really sure?</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
								<a href="<c:url value="/admin/reset/rebase"/>" class="btn btn-primary">Reset</a>
							</div>
						</div>
						<!-- /.modal-content -->
					</div>
					<!-- /.modal-dialog -->
				</div>
				<!-- /.modal -->
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-3 -->
	
	<div class="col-lg-3">
		<div class="panel panel-default">
			<div class="panel-heading">"The Special"</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<p>You know...</p>
					<div class="col-lg-offset-10 col-lg-2">
						<a href="<c:url value="/admin/reset/special"/>" class="btn btn-primary pull-right"><i class="fa fa-magic"></i>
							Run it!</a>
					</div>
			</div>

			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-3 -->
</div>
<!-- /.row -->