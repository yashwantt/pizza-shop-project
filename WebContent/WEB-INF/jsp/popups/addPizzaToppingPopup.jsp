<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form method='post' name='addPizzaToppingForm' commandName='toppingCommand' enctype='multipart/form-data'>
	<div class="addToppingFields">
		<div class="toppingNameContainer">
			<div class="fieldName">
				<label for='name'>Topping Name:</label>
			</div>
			<div class="fieldValue">
				<form:input path='name' />
			</div>
		</div>
		<div class="toppingTypeContainer">
			<div class="fieldName">
				<label for='type'>Topping Type:</label>
			</div>
			<div class="fieldValue">
				<form:select path='type'>
					<form:options items="${toppingTypes}" />
				</form:select>
			</div>
		</div>
		<div class="toppingImageContainer">
			<div class="fieldName">
				<label for='type'>Topping Image:</label>
			</div>
			<div class="fieldValue">
				<input type='file' id='imageFile' name='imageFile'/>
			</div>
		</div>
	</div>
	<div class="buttonRegion" align="center">
		<div id="addNewToppingPopup-submit">Add</div>
		<div id="addNewToppingPopup-hider">Cancel</div>
	</div>
</form:form>