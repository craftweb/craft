<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% String contextPath = (String) request.getAttribute("contextPath"); %>
<title>Craft: <%=contextPath %></title>
<link rel="stylesheet" type="text/css"
	href="/assets/css/bootstrap-blue.css" />
<link rel="stylesheet" type="text/css"
	href="/assets/css/bootstrap-responsive.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/docs.css" />

<script type="text/javascript" src="/assets/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/assets/js/bootstrap.js"></script>
<script type="text/javascript" src="/assets/js/docs.js"></script>
</head>
<body data-spy="scroll" data-target=".subnav" data-offset="50">
	<div class="container">
		<header class="jumbotron">
		<div class="control-group">
			<div class="controls pull-right error">
				<form class="form-inline" action="#login">
					<input type="email" class="input-small" placeholder="Email">
					<input type="password" class="input-small" placeholder="Password">
					<label class="checkbox"> <input type="checkbox">
						Remember me
					</label>
					<button type="submit" class="btn btn-primary">Sign in</button>
				</form>
			</div>
		</div>
		<h1>Optamaze</h1>
		<div class="control-group">
			<div class="controls pull-right">
				<form action="#search">
					<div class="input-append">
						<input class="span2" id="appendedInputButton" size="16"
							type="text">
						<button class="btn btn-primary" type="submit">Go!</button>
					</div>
				</form>
			</div>
		</div>
		<p class="lead">A preview of Optamaze...</p>
		</header>
		<div class="well">
			<div class="navbar">
				<div class="navbar-inner">
					<div class="container">
						<a class="btn btn-navbar" data-toggle="collapse"
							data-target=".nav-collapse"> <span class="icon-bar"></span> <span
							class="icon-bar"></span> <span class="icon-bar"></span>
						</a>
						<div class="nav-collapse">
							<ul class="nav" id="mainTabs">
								<% if(contextPath.equalsIgnoreCase("a")) { %>
										<li class="active">
								<% } else { %>
										<li>
								<% } %>
								<a href="<%=request.getContextPath()%>/a">A</a></li>
								<% if(contextPath.equalsIgnoreCase("b")) { %>
										<li class="active">
								<% } else { %>
										<li>
								<% } %>
								<a href="<%=request.getContextPath()%>/b">B</a></li>
								<% if(contextPath.equalsIgnoreCase("c")) { %>
										<li class="active">
								<% } else { %>
										<li>
								<% } %>
								<a href="<%=request.getContextPath()%>/c">C</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<h1>Hello World!</h1>
			<h2>Welcome to Page <%=contextPath %></h2>
		</div>
	</div>
	<br> <br>
	<footer class="footer">
	<p class="pull-right">
		<a href="#">Back to top</a>
	</p>
	</footer>
</body>
</html>