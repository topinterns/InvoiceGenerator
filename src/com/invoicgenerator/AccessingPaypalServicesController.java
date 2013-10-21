//package com.invoicgenerator;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class AccessingPaypalServicesController {
//
//	@RequestMapping("/test")
//	public void readingEntries(HttpServletRequest request, HttpServletResponse response) throws IOException 
//	{
//		// Check that we have a file upload request
//	      boolean isMultipart = readingEntries.isMultipartContent(request);
//	      response.setContentType("text/html");
//	      java.io.PrintWriter out = response.getWriter( );
//	      if( !isMultipart ){
//	         out.println("<html>");
//	         out.println("<head>");
//	         out.println("<title>Servlet upload</title>");  
//	         out.println("</head>");
//	         out.println("<body>");
//	         out.println("<p>No file uploaded</p>"); 
//	         out.println("</body>");
//	         out.println("</html>");
//	         return;
//	      }
//	      DiskFileItemFactory factory = new DiskFileItemFactory();
//	      // maximum size that will be stored in memory
//	      factory.setSizeThreshold(maxMemSize);
//	      // Location to save data that is larger than maxMemSize.
//	      factory.setRepository(new File("c:\\temp"));
//
//	      // Create a new file upload handler
//	      ServletFileUpload upload = new ServletFileUpload(factory);
//	      // maximum file size to be uploaded.
//	      upload.setSizeMax( maxFileSize );
//
//	      try{ 
//	      // Parse the request to get file items.
//	      List fileItems = upload.parseRequest(request);
//		
//	      // Process the uploaded file items
//	      Iterator i = fileItems.iterator();
//
//	      out.println("<html>");
//	      out.println("<head>");
//	      out.println("<title>Servlet upload</title>");  
//	      out.println("</head>");
//	      out.println("<body>");
//	      while ( i.hasNext () ) 
//	      {
//	         FileItem fi = (FileItem)i.next();
//	         if ( !fi.isFormField () )	
//	         {
//	            // Get the uploaded file parameters
//	            String fieldName = fi.getFieldName();
//	            String fileName = fi.getName();
//	            String contentType = fi.getContentType();
//	            boolean isInMemory = fi.isInMemory();
//	            long sizeInBytes = fi.getSize();
//	            // Write the file
//	            if( fileName.lastIndexOf("\\") >= 0 ){
//	               file = new File( filePath + 
//	               fileName.substring( fileName.lastIndexOf("\\"))) ;
//	            }else{
//	               file = new File( filePath + 
//	               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
//	            }
//	            fi.write( file ) ;
//	            out.println("Uploaded Filename: " + fileName + "<br>");
//	         }
//	      }
//	}
//	catch(Exception e){
//		
//	}
//	//@RequestMapping("/creatingListOfEntries")
//	
//}