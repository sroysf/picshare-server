<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 

<!-- http://www.javascriptkit.com/jsref/text.shtml -->

<jsp:useBean id="imageList"
             scope="request"
             type="java.util.Collection" />
                 
<HTML>
<HEAD>
	<c:set var="foo" scope="session" value="5"/>  
   <TITLE>Simple UI <c:out value="${foo}"/></TITLE>
   
   <script language="Javascript">
   		function onFormKey(event) {
   			if (event.keyCode == 13) {
   				var i = document.getElementById('pageNumBox').value;
   				alert('Enter Pressed : ' + i);
   			}
   		}
   </script>
</HEAD>
<BODY>

<h3><c:out value="${ tag }"/></h3>

<FORM>
Thumbnails per page:
<select id="numPerPagePulldown" name="numPerPage">
	<option value="10">10</option>
	<option value="25">25</option>
	<option value="50" selected="true">50</option>
</select>

<br/>

Prev | Page <INPUT id="pageNumBox" type="text" size="2" name="pageNum" onkeydown="onFormKey(event)"/> of 25 | Next  <br/>
</FORM>

        
<c:forEach var="image" items="${ imageList }">
	<a href="/picshare/image/web?id=<c:out value='${ image.id }'/>"><img src="<c:out value='${ image.thumbUrl }'/>"/></a>
</c:forEach>
             

</BODY>

</HTML>