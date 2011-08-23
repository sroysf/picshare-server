<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 

<!-- http://www.javascriptkit.com/jsref/text.shtml -->
    
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

<a href='<c:out value="http://www.cnn.com"/>'><img src="http://farm4.static.flickr.com/3261/2538183196_8baf9a8015_s.jpg" alt="Title #0" /></a>
<img src="http://farm3.static.flickr.com/2404/2538171134_2f77bc00d9_s.jpg" alt="Title #1" />
<img src="http://farm3.static.flickr.com/2093/2538168854_f75e408156_s.jpg" alt="Title #2" />
<img src="http://farm4.static.flickr.com/3150/2538167224_0a6075dd18_s.jpg" alt="Title #4" />
<img src="http://farm4.static.flickr.com/3204/2537348699_bfd38bd9fd_s.jpg" alt="Title #5" />
<img src="http://farm4.static.flickr.com/3124/2538164582_b9d18f9d1b_s.jpg" alt="Title #6" />
<img src="http://farm4.static.flickr.com/3205/2538164270_4369bbdd23_s.jpg" alt="Title #7" />
<img src="http://farm4.static.flickr.com/3211/2538163540_c2026243d2_s.jpg" alt="Title #8" />
<img src="http://farm3.static.flickr.com/2315/2537343449_f933be8036_s.jpg" alt="Title #9" />
<img src="http://farm3.static.flickr.com/2167/2082738157_436d1eb280_s.jpg" alt="Title #10" />
<img src="http://farm3.static.flickr.com/2342/2083508720_fa906f685e_s.jpg" alt="Title #11" />
<img src="http://farm3.static.flickr.com/2132/2082721339_4b06f6abba_s.jpg" alt="Title #12" />
<img src="http://farm3.static.flickr.com/2139/2083503622_5b17f16a60_s.jpg" alt="Title #13" />

<FORM>
Prev | Page <INPUT type="text" size="2" name="pageNum"/> of 25 | Next  </br>
</FORM>

</BODY>

</HTML>