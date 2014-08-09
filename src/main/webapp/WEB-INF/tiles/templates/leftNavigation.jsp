<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="navbar-default sidebar" role="navigation">
	<div class="sidebar-nav navbar-collapse">
		<ul class="nav" id="side-menu">
			<li class="sidebar-search">
				<div class="input-group custom-search-form">
					<input type="text" class="form-control" placeholder="Search..." /> <span class="input-group-btn">
						<button class="btn btn-default" type="button">
							<i class="fa fa-search"></i>
						</button>
					</span>
				</div> <!-- /input-group -->
			</li>
			<li><a href="controlPanel.xhtml"><i class="fa fa-cog fa-fw"></i> Control Panel</a></li>
			<li><a href="topicsPanel.xhtml"><i class="fa fa-tasks fa-fw"></i> Topics</a></li>
			<li><a href="#"><i class="fa fa-tags fa-fw"></i> Topics<span class="fa arrow"></span></a>
				<ul class="nav nav-second-level">
					<li><a href="panels-wells.html">Topic 1</a></li>
					<li><a href="buttons.html">Topic 2</a></li>
					<li><a href="notifications.html">Topic 3</a></li>
					<li><a href="typography.html">Topic 4</a></li>
					<li><a href="grid.html">Grid</a></li>
				</ul> <!-- /.nav-second-level --></li>
		</ul>
	</div>
	<!-- /.sidebar-collapse -->
</div>
<!-- /.navbar-static-side -->