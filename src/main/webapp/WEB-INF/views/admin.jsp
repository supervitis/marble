<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Social Networks</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<p>Take a look and edit your Twitter API Keys</p>
				<div class="col-lg-offset-10 col-lg-2">
					<a href="<c:url value="admin/keys/twitter"/>" class="btn btn-primary pull-right"><i class="fa fa-key"></i>
						View Keys</a>
				</div>
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
			<div class="panel-heading">Undoable Actions</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<p>So, little padawan. Before taking this route, think carefully you must.</p>
				<!-- Button trigger modal -->
				<div class="col-lg-offset-10 col-lg-2">
					<button class="btn btn-primary pull-right" data-toggle="modal" data-target="#myModal">Reset Everything</button>
				</div>
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
								<a href="<c:url value="admin/reset/rebase"/>" class="btn btn-primary">Reset</a>
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
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Upload SenticNet Data</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<p>Select the file containing the senticnet data in the original rdf format and click upload. Previous senticnet
					data will be deleted.</p>
				<c:url value='/admin/upload/sentic?${_csrf.parameterName}=${_csrf.token}' var="uploadSenticUrl" />
				<form:form enctype="multipart/form-data" action="${uploadSenticUrl}">
					<div class="form-group">
						<input name="file" type="file" class="form-control" />
					</div>
					<div class="col-lg-offset-10 col-lg-2">
						<button type="submit" id="save" class="btn btn-primary pull-right">
							<i class="fa fa-upload"></i> Upload
						</button>
					</div>
				</form:form>
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
			<div class="panel-heading">Upload Validation Data</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<p>Select the file containing the validation data, that is, data that will be used to validate the polarity 
				 processors. It must conform to the format <code>"&lt;polarity&gt;","&lt;text&gt;"</code> where polarity could take the following values:</p>
				 <ul>
					 <li>-1: Negative</li>
					 <li>0: Neutral</li>
					 <li>1: Positive</li>
				 </ul>
				<c:url value='/admin/upload/validationData?${_csrf.parameterName}=${_csrf.token}' var="uploadValidationDataUrl" />
				<form:form enctype="multipart/form-data" action="${uploadValidationDataUrl}">
					<div class="form-group">
						<input name="file" type="file" class="form-control" />
					</div>
					<div class="col-lg-offset-10 col-lg-2">
						<button type="submit" id="save" class="btn btn-primary pull-right">
							<i class="fa fa-upload"></i> Upload
						</button>
					</div>
				</form:form>
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
		<!-- 
		<div class="panel panel-default">
			<div class="panel-heading">"The Special"</div>
			<!-- /.panel-heading -- >

			<div class="panel-body">
				<p>You know...</p>
				<div class="col-lg-offset-10 col-lg-2">
					<a href="<c:url value="admin/reset/special"/>" class="btn btn-primary pull-right"><i class="fa fa-magic"></i>
						Run it!</a>
				</div>
				
			</div>

			<!-- .panel-body -- >
		</div>
		-->
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->