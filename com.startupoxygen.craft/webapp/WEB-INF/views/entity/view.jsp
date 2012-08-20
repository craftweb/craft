<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="craft"%>
<craft:craft>
	<div class="container-fluid">
		<div class="well">
			<form class="form-horizontal"
				action="${craft.getEntityContextPath(request)}/save" method="post">
				<div class="tab-pane" id="${entity.name}">
					<h1>${entity.displayName}</h1>
					<section>
						<fieldset name="detail">
							<!-- <legend>Field Set Caption Goes Here</legend> -->
							<craft:foreachfield>
								<craft:field name="${field.name}" edit="false" />
							</craft:foreachfield>
						</fieldset>
					</section>
				</div>
			</form>
		</div>
	</div>
</craft:craft>
