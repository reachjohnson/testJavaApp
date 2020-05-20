
<html>
<head>
   <title><%= AppConstants.APPTITLE %>
   </title>

   <script type="text/javascript" language="JavaScript">
      function frmSetValues() {
         document.WebForm.hidRefNo.value = parent.opener.WebForm.hidRefNo.value;
         document.WebForm.action = "loadUpdateLocation";
         frmReadSubmit();
      }
   </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="WebForm" method="POST">
   <% include ../common/popup_banner.ejs %>
   <input type="hidden" name="hidRefNo">
</form>
</body>
</html>