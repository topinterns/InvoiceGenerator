function FileUploaderClass()
{
console.log("hai in file upload class");	
}

FileUploaderClass.prototype.startUpload=function(){
	//$('#progress_info').show();
	//$('#upload_response').show();
	
	console.log("FileUploaderClass.prototype.startUpload");
	var file = document.getElementById('selectedFile').files[0];
	var reader = new FileReader();
	//var formDataObj = new FormData(document.getElementById('upload_form'));
	//console.log(formDataObj);
	reader.onload = function (e) {
		console.log(e);
		console.log("onloadend");
		var jsonData={};
		jsonData.name=file.name;
		jsonData.size=file.size;
		jsonData.type=file.type;
//		if(uploadType.prototype.isInvoiceUpload){
//			jsonData.uploadType="invoice";
//		}
//		else if(uploadType.prototype.isSynclioIdUpload){
//			jsonData.uploadType="synclioId";
//		}
//		else if(uploadType.prototype.isPaypalAmountUpload){
//			jsonData.uploadType="paypalAmount";
//		}
//		else if(uploadType.prototype.isPaypalPeriodUpload){
//			jsonData.uploadType="payapalPeriod";
//		}
//		else if(uploadType.prototype.isPaypalWithDESC){
//			jsonData.uploadType="paymentWithDESC";
//		}
//		else if(uploadType.prototype.isPaypalSaleTransaction){
//			jsonData.uploadType="paypalSalesTransaction";
//		}
//		else if(uploadType.prototype.isPaypalProfileDeactivation){
//			jsonData.uploadType="deactivateAccountProfile";
//		}
		var xhr, provider;
		xhr = jQuery.ajaxSettings.xhr();
		console.log(xhr);
		if (xhr.upload) {
			xhr.upload.addEventListener('progress', FileUploaderClass.prototype.uploadProgress, false);
			xhr.addEventListener('load', FileUploaderClass.prototype.uploadFinish, false);
			xhr.addEventListener('error', FileUploaderClass.prototype.uploadError, false);
			xhr.addEventListener('abort', FileUploaderClass.prototype.uploadAbort, false);
		}   

		provider = function () {
			return xhr;
		};  
		var data = e.target.result;
		data = data.substr(data.indexOf('base64') + 7); 
		jsonData.data=data;
		console.log("********************");
		console.log(jsonData);
		console.log("********************");
		$.ajax({
			url : "/fileUploadHandlerService",
			type : 'POST',
			xhr: provider,
			contentType: 'application/json',
			data : JSON.stringify(jsonData),
			success : function(response) {
				console.log(response);
			}
		}); 
	};  
	reader.readAsDataURL(file);
}