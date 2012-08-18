<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="craft"%>
<craft:craft>
	<div class="container-fluid">
		<div class="well">
			<form class="form-horizontal"
				action="${craft.getEntityContextPath(request)}/save" method="post">
				<section class="tab-pane" id="${entity.name}">
					<h1>${entity.displayName}</h1>
					<fieldset name="detail">
						<!-- <legend>Field Set Caption Goes Here</legend> -->
						<craft:foreachfield>
							<craft:field name="${field.name}" />
						</craft:foreachfield>
						<craft:entityformactions />
					</fieldset>
				</section>
			</form>
		</div>
	</div>
</craft:craft>
