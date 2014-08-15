<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="container">
	<div class="row">
		<div class="col-md-4 col-md-offset-4">
			<div class="login-panel panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Please enter your credentials</h3>
				</div>
				<div class="panel-body">
					<c:if test="${not empty error}">
						<div class="alert alert-danger">
							<spring:message code="LoginController.badCredentials" />
							<br />
						</div>
					</c:if>
					<form role="form" action="<c:url value= "/login/authenticate"></c:url>" method="post">
						<fieldset>
							<div class="form-group">
								<input class="form-control" placeholder="Username" name="username" type="text" autofocus />
							</div>
							<div class="form-group">
								<input class="form-control" placeholder="Password" name="password" type="password" value="" />
							</div>
							<div class="checkbox">
								<label> <input name="_spring_security_remember_me" disabled="disabled" type="checkbox" value="Remember Me" />Remember Me
								</label>
							</div>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
							<input class="btn btn-lg btn-success btn-block" type="submit" value="Login" />
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>