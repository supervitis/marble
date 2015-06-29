<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">HOOOOOLA QUE TAL</div>
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
</div>
	