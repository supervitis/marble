<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    $(function() {
        var modules = {};
        <c:forEach var="module" items="${modules}">
        modules["${module.name}"] = {};
        modules["${module.name}"]["simpleName"] = "${module.simpleName}";
        modules["${module.name}"]["operations"] = {};
        <c:forEach var="operation" items="${module.operations}">
        modules["${module.name}"]["operations"]["${operation.key}"] = "${operation.value}";
        </c:forEach>
        modules["${module.name}"]["parameters"] = {};
        <c:forEach var="parameter" items="${module.parameters}">
        modules["${module.name}"]["parameters"]["${parameter.key}"] = "${parameter.value}";
        </c:forEach>
        </c:forEach>

        var $moduleSelect = $("#modules-select");
        console.log(modules);
        // Fill up the main select options
        $moduleSelect.append(function() {
            var output = '<option value="">Select a module...</option>';
            $.each(modules, function(key, value) {
                output += '<option value="' + key + '">' + value["simpleName"] + '</option>';
            });
            return output;
        });
        $moduleSelect.change(function() {
            console.log("<"+$(this).val()+">");
            if (!$(this).val()) {
                $("#operations-div").empty();
            }
            else {
	            // Create the operation select
	            $("#operations-div").empty().append('<fieldset><legend>Operation</legend> '+
	            '<select name="operation" id="operations-select" class="form-control"></select></fieldset>');
	            $("#operations-select").empty().append(function() {
	                var output = '<option value="">Select an operation...</option>';
	                $.each(modules[$moduleSelect.val()]["operations"], function(key, value) {
	                    output += '<option value="' + key + '">' + value + '</option>';
	                });
	                return output;
	            });
	            
	         	// Create the parameters configuration
	            $("#parameters-div").empty().append(function() {
	                var output = '<fieldset><legend>Custom Parameters</legend>';
	                $.each(modules[$moduleSelect.val()]["parameters"], function(key, value) {
	                    output += '<div class="form-group">';
	                    output += '<label for="' + key + '">' + value + '</label>';
	                    output += '<input id="' + key + '" name="parameters[' + key + ']" class="form-control" type="text">';
	                    output += '<p class="help-block">' + value + '</p>';
	                    output += '</div>';
	                });
	                output += '</fieldset>';
	                return output;
	            });
            }
        });
    });
</script>