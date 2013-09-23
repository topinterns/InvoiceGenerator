<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.css">
<!-- <script src="http://code.jquery.com/jquery-latest.js"></script> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="/bootstrap/css/style.css" />
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






<script type="text/javascript" src="/js/loadDetails.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body >

<center>
<table class="tableIndividual" style="display:none">
<thead id="headersIndividual">
<tr  bgcolor="#DCDCDC"><td >Account Number</td><td>Phone Number</td><td>Invoice Date</td><td>Date Due</td><td>Address</td><td> Charges</td>  <td>2</td> <td>3</td> <td>4</td> <td>5</td> <td>6</td> <td>7</td> <td>8</td> <td>9</td> <td>10</td> <td>Summary of charges</td><td>Total Charges </td><td>From Address</td><td>Account Name</td><td>Summary of charges(page2)</td> <td>Generate Pdfs </td>
</thead>
<tbody id="bodyIndividual" >

</tbody>
</table>
</center>






 <!-- <input type="button" value="test" onclick="return test()"/>  -->
<div class="container">
<table class="tableForDetails" ></table>
<form class="form-horizontal"  method="post">
<div>
<h4 class='heading'><span>Statement Summary </span><span class='pull-right' style='margin-right:37%'>Address</span></h4>
<span class="summary">
  <div class="control-group">
    <label class="control-label" >Account Number</label>
    <div class="controls">
      <input type="text" id="acc_no" name="acc_no" placeholder="Account Number">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Phone Number</label>
    <div class="controls">
      <input type="text" id="phone_number" name="phone_number" placeholder="Phone Number">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Invoice Date</label>
    <div class="controls">
      <input type="text" id="invoice_date" name="invoice_date" placeholder="Invoice Date">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Date Due</label>
    <div class="controls">
      <input type="text" id="date_due" name="date_due" placeholder="Date Due">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Total Due</label>
    <div class="controls">
      <input type="text" id="total_due" name="total_due" placeholder="Total Due">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Amount Enclosed</label>
    <div class="controls">
      <input type="text" id="amount_enclosed" name="amount_enclosed" placeholder="Amount Enclosed">
    </div>
  </div>
   <div class="control-group" style="margin-left: 449px;margin-top: -241px;">
   <label class="control-label" style="margin-right: -151px;margin-top: -200px;" >Address</label>
    <div class="controls">
     
     <textarea rows="4" cols="50" placeholder="Enter your address" style="margin-top: -18px;">
</textarea>
    </div>
  </div>
  
  </span>
<h4 class='heading'><span>Summary Of Account History </span><span class='pull-right' style='margin-right:25%'>Summary Of Charges</span></h4>  <span class='summary'>
  <div class="control-group">
    <label class="control-label" >Payments Received</label>
    <div class="controls">
      <input type="text" id="payment_rec" name="payment_rec" placeholder="Payments Received">
    </div>
  </div>
  
  <div class="control-group">
    <label class="control-label" >Debits and Credits</label>
    <div class="controls">
      <input type="text" id="debit_credit" name="debit_credit" placeholder="Debits and Credits">
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Balance Forward</label>
    <div class="controls">
      <input type="text" id="bal_fwd" name="bal_fwd" placeholder="Balance Forward">
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Current Charges</label>
    <div class="controls">
      <input type="text" id="curent_charge" name="curent_charge" placeholder="Current Charges">
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Total Due By</label>
    <div class="controls">
      <input type="text" id="total_due_by" name="total_due_by" placeholder="Total Due By">
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Total Due After</label>
    <div class="controls">
      <input type="text" id="total_due_after" name="total_due_after" placeholder="Total Due After">
    </div>
  </div>
  
   <input class='btn btnModified' type="button" onclick="saveDetails()" value="Submit"></input>
  
  </span>
  
  <!-- <span><h4  class='heading'>Summary of Charges</h4></span> -->
  <div class="summary" style="margin-top: -319px;margin-right: 5px;">
   <div class="control-group">
    <label class="control-label" >Plan Overage Charges</label>
    <div class="controls">
      <input type="text" id="poc" name="poc" placeholder="Plan Overage Charges">
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Other Charges</label>
    <div class="controls">
      <input type="text" id="ocharges" name="ocharges" placeholder="Other Charges">
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Total Current Charges</label>
    <div class="controls">
      <input type="text" id="tcc" name="tcc" placeholder="Total Current Charges">
    </div>
 </div>
  </div>
  </div>

</form>
  </div>
</body>
</html>