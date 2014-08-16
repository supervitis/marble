<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="row topics-panel">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Topics</div>
			<!-- /.panel-heading -->

			<div class="panel-body">
				<div class="table-responsive">
					<table class="table data-table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th scope="col">Name</th>
								<th scope="col">Extractor Status</th>
								<th scope="col">Last Extractor Message</th>
								<th scope="col">Processor Status</th>
								<th scope="col">Last Processor Message</th>
								<th scope="col">Actions</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><a href="/Marble-web/topic.xhtml?name=Blackberry" class="name">Blackberry</a></td>
								<td><span id="j_idt32:0:extractorStatus"></span></td>
								<td><span class="text-info">-</span></td>
								<td><span id="j_idt32:0:processorStatus"></span></td>
								<td><span class="text-info">-</span></td>
								<td>
									<form id="j_idt32:0:actions" name="j_idt32:0:actions" method="post" action="/Marble-web/topicsPanel.xhtml"
										enctype="application/x-www-form-urlencoded" role="form">
										<input type="hidden" name="j_idt32:0:actions" value="j_idt32:0:actions">

										<div>
											<input id="j_idt32:0:actions:extract" type="submit" name="j_idt32:0:actions:extract" value="Extract" class="btn btn-default"><input
												id="j_idt32:0:actions:extractStop" type="submit" name="j_idt32:0:actions:extractStop" value="Stop Extract"
												class="btn btn-default"><input id="j_idt32:0:actions:process" type="submit" name="j_idt32:0:actions:process"
												value="Process" class="btn btn-default"><a href="plotter.xhtml?name=Blackberry" class="btn btn-default">Plot</a><a
												href="statistics.xhtml?name=Blackberry" class="btn btn-default">Stats</a>
										</div>
										<input type="hidden" name="javax.faces.ViewState" id="j_id1:javax.faces.ViewState:0"
											value="-5224325943024160020:-794346146508309816" autocomplete="off">
									</form>
								</td>
							</tr>
							<tr>
								<td><a href="/Marble-web/topic.xhtml?name=Whatsapp" class="name">Whatsapp</a></td>
								<td><span id="j_idt32:1:extractorStatus"></span></td>
								<td><span class="text-info">-</span></td>
								<td><span id="j_idt32:1:processorStatus"></span></td>
								<td><span class="text-info">-</span></td>
								<td>
									<form id="j_idt32:1:actions" name="j_idt32:1:actions" method="post" action="/Marble-web/topicsPanel.xhtml"
										enctype="application/x-www-form-urlencoded" role="form">
										<input type="hidden" name="j_idt32:1:actions" value="j_idt32:1:actions">

										<div>
											<input id="j_idt32:1:actions:extract" type="submit" name="j_idt32:1:actions:extract" value="Extract" class="btn btn-default"><input
												id="j_idt32:1:actions:extractStop" type="submit" name="j_idt32:1:actions:extractStop" value="Stop Extract"
												class="btn btn-default"><input id="j_idt32:1:actions:process" type="submit" name="j_idt32:1:actions:process"
												value="Process" class="btn btn-default"><a href="plotter.xhtml?name=Whatsapp" class="btn btn-default">Plot</a><a
												href="statistics.xhtml?name=Whatsapp" class="btn btn-default">Stats</a>
										</div>
										<input type="hidden" name="javax.faces.ViewState" id="j_id1:javax.faces.ViewState:0"
											value="-5224325943024160020:-794346146508309816" autocomplete="off">
									</form>
								</td>
							</tr>
						</tbody>
					</table>


				</div>
				<!-- /.table-responsive -->

				<div class="well">
					<form id="autoupdate" name="autoupdate" method="post" action="#"
						enctype="application/x-www-form-urlencoded">
						
						<div class="checkbox">
							<label> <input id="autoupdateCheck" type="checkbox"> Auto-update
								status (to update the other fields you need to refresh this page).
							</label>
						</div>
					</form>
				</div>
				<!-- /.well -->

			</div>
			<!-- /.panel-body -->

		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->


<div class="row">
	<div class="col-lg-12"></div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->


<hr>
<div class="row">
	<div class="col-lg-12">
		<p>© Marble Initiative</p>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
