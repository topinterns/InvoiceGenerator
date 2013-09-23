package com.invoicgenerator;

import java.io.IOException;

import javax.servlet.http.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class InvoiceGeneratorServlet {


	@RequestMapping("/")
	public String homePage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("heloo Welcome");
		return "home";
	}
}
