<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.css">
 <script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="/bootstrap/css/style.css" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script type='text/javascript' src='/js/loadDetails.js'></script>
<script type='text/javascript'>
function isNumberKey1(evt)
{
   var charCode = (evt.which) ? evt.which :event.keyCode
   if (charCode > 31 && (charCode < 48 || charCode > 57 || charCode == 189 || charCode== 190))
      return false;
   return true;
}
function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which :event.keyCode;
   if (charCode > 31 && ((charCode!= 47 && charCode< 45)|| charCode > 57))
      return false;
   return true;
}
$('#date_entry').on('mouseleave',function(){
	alert("left");
});
var numberForSummary=1;
var numberForUser=1;
function addTwo(){
	$('#removeForUser').show();
	numberForSummary=numberForSummary+1;
	var content="";
	/* content+='<span class= "pull-left"><li ><input placeholder="PlaceHolder1" type="text"></li></span><span class="pull-left"> <li><input  placeholder="PlaceHolder2"  type="text"></li></span>'; */
	content+='<div id="spanSummaryId'+numberForSummary+'\"><table ><tr><td><input placeholder="PlaceHolder1" type="text"></td><td><td><input  placeholder="PlaceHolder2"  type="text"></td></tr></table><div>';
	$('#addTwoFields').append(content);
}

function removeTwo(){
	$('#spanSummaryId'+numberForSummary).remove();
	numberForSummary=numberForSummary-1;
	if(numberForSummary==1){
		$('#removeForUser').hide();
	}
}
$('#answerConnect').live('click',function(){
	 $("#fromAddress").html("<h5>AnswerConnect,<br>P.O. Box 80040,<br>Portland,OR,<br>972801040405</h5>");
	 $("#fromAddressText").val("AnswerConnect,P.O. Box 80040,Portland, OR,972801040405");
	  $('#imageForLogo').attr('src','/bootstrap/img/ansc.png');
	   $('#navigationForLogo').children().each(function() {
	     var kid = $(this);
	     if (kid.hasClass('active'))
	      $(this).removeClass('active');

	    });
	  $(this).addClass('active');
	  
	 });
	 $('#answerForce').live('click',function(){
		 $("#fromAddress").html("<h5>Answer Force,<br>P.O. Box 2115,<br>Tualatin, OR,<br>970622115151</h5>");
		 $("#fromAddressText").val("Answer Force,P.O. Box 2115,Tualatin, OR,970622115151"); 
	  $('#imageForLogo').attr('src','/bootstrap/img/af.png');
	   $('#navigationForLogo').children().each(function() {
	     var kid = $(this);
	     if (kid.hasClass('active'))
	      $(this).removeClass('active');

	    });
	  $(this).addClass('active');
	  
	 });
	 $('#memoSent').live('click',function(){
		 $("#fromAddress").html("<h5>Memosent,<br>P.O. Box 3304,<br>Tualatin, OR,<br>970623304042</h5>");
		 $("#fromAddressText").val("Memosent,P.O. Box 3304,Tualatin, OR,970623304042");
	  $('#imageForLogo').attr('src','/bootstrap/img/memo2.png');
	  $('#imageForLogo').attr('style','height:37px');
	   $('#navigationForLogo').children().each(function() {
	     var kid = $(this);
	     if (kid.hasClass('active'))
	      $(this).removeClass('active');

	    });
	  $(this).addClass('active');
	  
	 });
	 $('#lexReception').live('click',function(){
		 $("#fromAddress").html("<h5>Lex Reception,<br>P.O. Box 80040,<br>Portland, OR,<br>972801040405</h5>"); 
		 $("#fromAddressText").val("Lex Reception,P.O. Box 80040,Portland, OR,972801040405");
	  $('#imageForLogo').attr('src','/bootstrap/img/lex2.png');
	   $('#navigationForLogo').children().each(function() {
	     var kid = $(this);
	     if (kid.hasClass('active'))
	      $(this).removeClass('active');

	    });
	  $(this).addClass('active');
	  
	 });

	 $(function() {
	     $('.datepicker').datepicker({
	       changeMonth: true,
	       changeYear: true,
	       dateFormat: "dd/mm/yy"
	     });
	   });
</script>
<!-- <script type="text/javascript">
 function test()
{
	alert("ajax call started");
	$.ajax({
		url:"/test",
		type:"POST",
		data : "hello",
			 contentType: "application/json",
			  beforeSend: function ( jqXHR )
			  {
			   jqXHR.setRequestHeader("Connection", "close");
			  },
			  processData: false,
			   success: function response(resultObject)
			  {

			  },
			  dataType: ''
	});
	alert("ajax call ended");
	} 
</script> -->
<!-- <script>
</script> -->
<script type="text/javascript" src="/js/loadDetails.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Receipt</title>
</head>
<body >
<center>
<!-- <table class="intable" >
<thead id="inHeaders">
<tr  bgcolor="#DCDCDC"><td >Account Number</td><td>Phone Number</td><td>Invoice Date</td><td>Date Due</td><td>Address</td><td> Charges</td>  <td>2</td> <td>3</td> <td>4</td> <td>5</td> <td>6</td> <td>7</td> <td>8</td> <td>9</td> <td>10</td> <td>Summary of charges</td><td>Total Charges </td><td>From Address</td><td>Account Name</td><td>Summary of charges(page2)</td> <td>Generate Pdfs </td>
</thead>
<tbody id="inBody" >
</tbody>
</table> -->
</center>
<!--  <input type="button" value="test" onclick="return test()"/> --> 
<div class="container1">
<ul class="nav nav-tabs" id='navigationForLogo'>
  <li class="active" id='answerConnect'><a>Answer Connect</a></li>
  <li id='answerForce'><a>Answer Force</a></li>
  <li id='memoSent'><a>Memo Sent</a></li>
  <li id='lexReception'><a>Lex Reception</a></li>
  <img id='imageForLogo' class='pull-right' src='/bootstrap/img/ansconnlogo.gif' height='100px' width='100px'/>
</ul>
<table class="tableForDetails" ></table>
<form class="form-horizontal"  method="post">
<div>
<h4 class='heading'><span>Statement Summary </span><span class='pull-right' style='margin-right:37%'>Address</span></h4>
<span class="summary">
  <div class="control-group">
    <label class="control-label" >Account Number</label>
    <div class="controls">													
      <input type="text" id="accountNumberP" name="accountNumberP" placeholder="Account Number" onkeypress="return isNumberKey1(event)">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Phone Number</label>
    <div class="controls">
      <input type="text" id="phoneNumberP" name="phoneNumberP" placeholder="Phone Number" onkeypress="return isNumberKey(event)">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Date</label>
    <div class="controls">
      <input type="text" class= "datepicker" id="invoiceDateP" name="invoiceDateP" placeholder="Invoice Date">
    </div>
  </div>
   <div class="control-group" style="margin-left: 449px;margin-top: -241px;">
   <label class="control-label" style="margin-right: -151px;margin-top: -95px;" >To Address</label>
    <div class="controls">
         <textarea rows="4" cols="50" placeholder="Enter To address here" style="margin-top: 76px;" id="toAddressP">
</textarea>
    </div>  </div>
    <div class="control-group" style="margin-left: 449px;margin-top: -241px;">
   <label class="control-label" style="margin-left: 4px;margin-top: 11px;">From Address</label>
    <div class="controls">
	<div class="controls" id="fromAddress" style="margin-top: 90%; margin-left: 4%; "><h5>AnswerConnect,<br>P.O. Box 80040,<br>Portland, OR,<br>972801040405</h5></div>
	<input id="fromAddressText" name="fromAddressText" type="hidden" value="AnswerConnect,P.O. Box 80040,Portland, OR,972801040405" />     
    </div>
  </div>
    
    </span>
    </span>
  <!-- <span><h4  class='heading'>Summary of Charges</h4></span> -->
    <div class='TwoDiv clearPadding' >
  <h4 class='heading'><span>Summary Of Charges </span><span class='pull-right' style='margin-right:25%'></span></h4>  
  <span class='summaryNew' >
   <ul class="addTwoFields" id='addTwoFields'  style="height:200px;overflow-y:auto;" >
		<span id='spanSummaryId'>
           <div>
            <span class='pull-left' > <li ><input class='sizeIncrease'  id='date_entry'  placeholder="PlaceHolder1"  type="text"></li></span>
         	<span class='pull-left'> <li><input class='sizeIncrease' placeholder="PlaceHolder2" type="text"></li></span>
            <span class='addIcon'> <i class='icon-plus pull-right' onclick="addTwo()"></i><i style='display:none' id='removeForUser' class='icon-minus' onclick="removeTwo()"></i></span>
           </div>
            </span>
            <br>
            </ul>
		</span>
	</div>
  </div>
   <input class="btn btnModified" type="button" onclick="paymentDetails()" value="Generate Pdf &amp; Send Mail" style="margin-left:40%; margin-top:-4%"/>
</form>
  </div>
<form id="frmReportIframe" name="frmReportIframe"
		target="pdfdownloadIframe" action="/admin/generatepdf" method="post">
		<input id="allStaffDetailsJson" name="allStaffDetailsJson"
			type="hidden" value="" /> <input id="btnSubmit" name="btnSubmit"
			type="button" style="display: none;" />
	</form>
	<iframe style="display: none" id="pdfdownloadIframe"
		name="pdfdownloadIframe"></iframe>

</body>
</html>