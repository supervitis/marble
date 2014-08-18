<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Topics</div>
			<!-- .panel-heading -->
			<div class="panel-body">
				<c:if test="${not empty topics}">
					<div class="table-responsive">
						<table class="table data-table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th scope="col">Name</th>
									<th scope="col">Actions</th>
								</tr>
							</thead>
							<c:forEach var="topic" items="${topics}">
								<tr>
									<td>${topic.name}</td>
									<td><a href="<c:url value="/topic/edit/${topic.id}"/>" class="btn btn-default"><i class="fa fa-pencil"></i> Edit</a>
									<a href="<c:url value="#"/>" class="btn btn-default btn-light-green"><i class="fa fa-sign-in"></i> Extract</a>
									<a href="<c:url value="#"/>" class="btn btn-default btn-blue"><i class="fa fa-scissors"></i> Process</a>
									<a href="<c:url value="#"/>" class="btn btn-default btn-orange"><i class="fa fa-bar-chart-o"></i> Plot</a>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${empty topics}">
					<p>There are no topics defined.</p>
				</c:if>
			</div>
			<!-- .panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->