<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-lg-4 col-md-6">
		<div class="panel panel-theme-green">
			<div class="panel-heading">
				<div class="row">
					<div class="col-xs-3">
						<i class="fa fa-tags fa-5x"></i>
					</div>
					<div class="col-xs-9 text-right">
						<div class="huge" id="info-topics">${info.topics}</div>
						<div>Topics</div>
					</div>
				</div>
			</div>
			<a href='<c:url value="topic" />'>
				<div class="panel-footer">
					<span class="pull-left">View Details</span> <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
					<div class="clearfix"></div>
				</div>
			</a>
		</div>
	</div>
	<div class="col-lg-4 col-md-6">
		<div class="panel panel-theme-orange">
			<div class="panel-heading">
				<div class="row">
					<div class="col-xs-3">
						<i class="fa fa-rocket fa-5x"></i>
					</div>
					<div class="col-xs-9 text-right">
						<div class="huge" id="info-executions">${info.executions}</div>
						<div>Executions</div>
					</div>
				</div>
			</div>
			<a href='<c:url value="topic" />'>
				<div class="panel-footer">
					<span class="pull-left">View Details</span> <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
					<div class="clearfix"></div>
				</div>
			</a>
		</div>
	</div>
	<div class="col-lg-4 col-md-6">
		<div class="panel panel-theme-blue">
			<div class="panel-heading">
				<div class="row">
					<div class="col-xs-3">
						<i class="fa fa-twitter fa-5x"></i>
					</div>
					<div class="col-xs-9 text-right">
						<div class="huge" id="info-statuses">${info.statuses}</div>
						<div>Statuses</div>
					</div>
				</div>
			</div>
			<a href="#">
				<div class="panel-footer">
					<span class="pull-left">View Details</span> <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
					<div class="clearfix"></div>
				</div>
			</a>
		</div>
	</div>
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-8">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-info-circle fa-fw"></i> About this demo
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<p>This is an special installation of the Marble Initiative to showcase the capabilities of the system.</p>
				
				<p>Configuration is reestablished from time to time, and some default test topics are defined and extracted automatically.</p>
				
				<p>As a guest user, you will be able to test some of the most basic features, but you will be limited to the data available
				in the system at the moment of the test. If you would like to test the extraction modules or test with a different set of data,
				Please feel free to contact me through my <a href="http://miguelfernandes.com/contact/">contact form</a> and I will provide 
				you with the appropriated credentials for you to test the system.</p> 
				
				<p>In the meantime, feel free to navigate the site, and if you encounter any problems in the way, please open a 
				<a href="https://github.com/miguelfc/marble/issues/new">github issue</a>
				with a short description of what happened and how I can reproduce it. Thank you!</p>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->

	</div>
	<!-- /.col-lg-8 -->
	<div class="col-lg-4">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-envelope fa-fw"></i> About this project
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<p>The Marble Initiative is an opinion mining platform, modular and open sourced.</p>
				<p>It is part of a PhD project, being developed by Miguel Fernandes for the University of Vigo, under the
					supervision of Ana Fernández Vilas and Rebeca Díaz Redondo.</p>
				<p>If you want to get more information about this project, there are some useful links below this lines.</p>
				<div class="list-group">
					<a class="list-group-item" href="http://marble.miguelfc.com"><i class="fa fa-circle-o-notch fa-fw fa-lg"></i> Marble Initiative Website</a>
					<a class="list-group-item" href="http://github.com/miguelfc/marble"><i class="fa fa-github fa-fw fa-lg"></i> Github repository</a>
					<a class="list-group-item" href="http://linkedin.com/in/miguelfernandes"><i class="fa fa-linkedin fa-fw fa-lg"></i> Miguel Fernandes LinkedIn</a>
					<a class="list-group-item" href="http://iclab.det.uvigo.es/marbleproject.html"><i class="fa fa-building fa-fw fa-lg"></i> UVigo
							Information &amp; Computing Laboratory</a>
				</div>

			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->
</div>
<!-- /.row -->