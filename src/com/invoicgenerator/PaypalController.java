package com.invoicgenerator;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.gson.Gson;

@Controller
public class PaypalController {
	
	static final Logger mLogger = Logger.getLogger(AdminController.class.getName());
	Gson gson=GsonUtil.getGson();
	
	@RequestMapping(value ="/uploadGen", method = RequestMethod.POST)
	 public @ResponseBody String  uploadCsvFile(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileName") String uploadedFileName) throws IOException {
		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String memcacheKey=uploadedFileName+"_"+new Date().getTime();
		BlobKey blobKey;
		System.out.println(request);
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
		blobKey = blobs.get("uploadGenFile");
		System.out.println("Got uploaded File.........");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    syncCache.put("blobKey_"+memcacheKey, blobKey);
	    
	    Queue queue    = QueueFactory.getQueue("constructDelimitFileOfGen");
	    queue.add(withUrl("/constructDelimitFileOfGen").param("blobKey","blobKey_"+memcacheKey));
	    
	    return memcacheKey;
	  }
	
	public Map<String,String> getPaypalParameterMap(List<String> list,String transactionType){
		 Map<String,String> paramMap=new HashMap<String, String>();
		 paramMap.put("TENDER", "C");
		 paramMap.put("ACCT", list.get(0));
		 paramMap.put("EXPDATE", list.get(1).replace("/", ""));
		 paramMap.put("AMT", list.get(2));
		 if(transactionType.equalsIgnoreCase("sale")){
			 paramMap.put("TRXTYPE", "S");
		 }
		 paramMap.put("COMMENT1",new StringBuffer().append(list.get(4)).append("-").append(list.get(3)).append("").toString());
		 paramMap.put("DESC", new StringBuffer().append("ACTI DBA ").append(list.get(4)).append(" TUALATIN OR 98760").toString());
		 return paramMap;
	 }



	@RequestMapping("/constructDelimitFileOfGen")
	public void uploadGenAndDownloadCsv(HttpServletRequest request, HttpServletResponse response,@RequestParam("blobKey") String blobKeyForFile)throws IOException {
			try{
			BufferedReader reader = readingUploadedFile(blobKeyForFile);
			String line;
			String result="";
			System.out.println("paypal_sales_transaction");
			StringBuffer profileString =  new StringBuffer();
			profileString.append("CreditCardNumber").append("\t").append("ExpiryDate").append("\t").append("Amount").append("\t").append("Comment").append("\t").append("Product Name").append("\t").append("ResponseMessage").append("\t").append("ProfileId").append("\t").append("PNREF Number").append("\n");
			
			List<ArrayList<String>> totalDetails = new ArrayList<ArrayList<String>>();
			Map<String, String> requestMapToPaypal;
			Map<String, Object> tempMap;
			String[] eachDetail = null;
			while ((line = reader.readLine()) != null) {
				result = line.replaceAll("\",", "\t").replaceAll("\"", "") ;
				//System.out.println("Result::"+result);
				eachDetail = result.split("\t");
				ArrayList<String> listOfDetails = new ArrayList<String>();
				listOfDetails.add(eachDetail[3]);
				listOfDetails.add(eachDetail[4]);
				listOfDetails.add(eachDetail[5]);
				listOfDetails.add(eachDetail[2]);
				listOfDetails.add(eachDetail[1]);
				totalDetails.add(listOfDetails);
			}
			System.out.println("total details::"+totalDetails);
			for(int index=0;index<totalDetails.size();index++){
				mLogger.info("list :"+totalDetails.get(index));
				Map<String,String> paramMap=getPaypalParameterMap(totalDetails.get(index), "sale");
				tempMap=new HashMap<String, Object>();
				tempMap.put("dataMap", paramMap);
				Map<String,Object> paypalResponseMap=gson.fromJson(new String(CustomHttpRequest.generatePostRequest(" http://79.live-synclio-backend.appspot.com"+"/paymentProcessing_with_DESC", tempMap)),Map.class);
				//System.out.println("Response Map from Paypal::"+paypalResponseMap);
				profileString.append( totalDetails.get(index).get(0)).append("\t").append( totalDetails.get(index).get(1)).append("\t").append( totalDetails.get(index).get(2)).append("\t").append( totalDetails.get(index).get(3)).append("\t").append( totalDetails.get(index).get(4)).append("\t").append(paypalResponseMap.get("RESPMSG")).append("\t").append(paypalResponseMap.get("PROFILEID")).append("\t").append(paypalResponseMap.get("PNREF")).append("\n");
			}
		//	response.addHeader("Content-Disposition", "attachment; filename=\"Paypal_Response.csv\"");
		//	response.getWriter().write(profileString.toString());
			//System.out.println(result);
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
			syncCache.put(blobKeyForFile.replace("blobKey_",""),profileString);
			System.out.println(profileString.length()+" stored in "+blobKeyForFile.replace("blobKey_",""));
			System.out.println(guid());
			System.out.println("2nd time::"+guid());
			}
			catch(Exception e){
				System.out.println("In exception::");
				e.printStackTrace();
			}
	}
	

	@RequestMapping(value ="/uploadPaypal", method = RequestMethod.POST)
	 public @ResponseBody String  uploadPaypalFile(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileName") String uploadedFileName) throws IOException {
		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String memcacheKey=uploadedFileName+"_"+new Date().getTime();
		BlobKey blobKey;
		
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
		blobKey = blobs.get("uploadPaypalFile");
		System.out.println("Got uploaded File.........");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    syncCache.put("blobKey_"+memcacheKey, blobKey);
	    
	    Queue queue    = QueueFactory.getQueue("constructDelimitFileOfGen");
	    queue.add(withUrl("/constructDelimitFileOfPaypal").param("blobKey","blobKey_"+memcacheKey));
	    
	    return memcacheKey;
	  }
	
	@RequestMapping("/constructDelimitFileOfPaypal")
	public void uploadPaypalAndDownloadCsv(HttpServletRequest request, HttpServletResponse response, @RequestParam("blobKey") String blobKeyFromMemcache)throws IOException, JSONException {
			String line;
			String result="";
			JSONArray arrayWithQuotes =null;
			BufferedReader reader = readingUploadedFile(blobKeyFromMemcache);
			while ((line = reader.readLine()) != null) {
				String[] readArray = line.split("\t");
				arrayWithQuotes = new JSONArray(readArray);
				result += arrayWithQuotes.toString().replaceAll("\\[", "").replaceAll("\\]", "") + "\n";
			}
			System.out.println("RESULT"+result);
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
			syncCache.put(blobKeyFromMemcache.replace("blobKey_",""),result);
		
	}

	@RequestMapping(value = "/downloadGen", method = RequestMethod.POST)
	public void downloadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ClassNotFoundException, IllegalArgumentException, JSONException {
		String keyForData = request.getParameter("keyForResponse");
		System.out.println(keyForData);
		String result = downloadRequestedFile(keyForData).toString();
		System.out.println("Result::"+result);
		response.setContentType("text/csv");
		response.addHeader("Content-Disposition", "attachment; filename=\"Paypal_Response.csv\"");
		response.getWriter().write(result);
	}
	
	
	private String downloadRequestedFile(String memCacheKeyOfFile){
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    String result = (String) syncCache.get(memCacheKeyOfFile);
	    return result;
	}
	
	private BufferedReader readingUploadedFile(String blobKeyOfFile){
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    BlobKey blobKey = (BlobKey) syncCache.get(blobKeyOfFile);
		try {
			FileService fileServices = FileServiceFactory.getFileService();
			AppEngineFile fileURL = fileServices.getBlobFile(blobKey);
			fileURL.getFileSystem();
			FileReadChannel readChannel = fileServices.openReadChannel(fileURL,true);
			BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
			return reader;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public String s4(){
		 return Double.toString(Math.floor((1 + Math.random()) * 0x10000)).substring(1);
	             
	}
	public String guid(){
		  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +s4() + '-' + s4() + s4() + s4();
	}
	
}


	
