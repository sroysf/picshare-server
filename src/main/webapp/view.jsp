<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 

    
<HTML>
<HEAD>
	<c:set var="foo" scope="session" value="5"/>  
   <TITLE>Simple UI <c:out value="${foo}"/></TITLE>
</HEAD>
<BODY>

<h2>Display centered web scale image, with editable tags form, and link to original full size image</h2>

</BODY>

</HTML>