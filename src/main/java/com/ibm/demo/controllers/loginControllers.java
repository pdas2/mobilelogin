package com.ibm.demo.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.ibm.demo.model.Account;
import com.ibm.demo.model.offerService;
import com.ibm.demo.producer.Producer;
import com.ibm.demo.repository.AccountSearchRepository;
import com.ibm.demo.repository.offerMongoRepository;
import com.ibm.demo.repository.offerMongoRepository;
import com.ibm.demo.repository.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class loginControllers {
	
	public static final Logger logger = LoggerFactory.getLogger(loginControllers.class);
	public String userName;
	public String Error;
	public String userAccId;
	public String userPhone;
	
	@Autowired
	AccountSearchRepository accountSearchRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	offerMongoRepository offerRepository;
	
	@Autowired
	Producer producer;
	
	@RequestMapping("/home")
	public String home1(Model model) {
		
		 model.addAttribute("userName", userName);
		 model.addAttribute("userAccId", userAccId);
		 model.addAttribute("userPhone", userPhone);
		return "home";
	}
	
	
	@RequestMapping("/login" )
	public String home(Model model) {
		
		model.addAttribute("userList", Error);
		return "login";
	}
	
	@RequestMapping("/success" )
	public String home2(Model model) {
		
		model.addAttribute("userList", "Thank you "+userName+" for choosing the offer." );
		model.addAttribute("userList1", "The offer have been successfully added to your account" );
		model.addAttribute("userList2", "Account would be updated shortly" );
		return "success";
	}
	
	@RequestMapping(value = "/addOffer", method = RequestMethod.POST)
	public String addOffer(@ModelAttribute offerService offer) {
		
		String msg="Offer successfully ordered to Account No."+offer.getuserAccId();
		
		offerRepository.save(offer);
		
		 Gson gson = new Gson();
	     String json = gson.toJson(offer);
		producer.produceMsg(json);
		logger.info(json);
		
		userName=offer.getuserName();
		
		logger.info("Offer will be activated within 24 hrs for User: "+offer.getuserName());
		return "redirect:success";
		//returns "Offer will be activated within 24 hrs for User: "+offer.getuserName();
	}
	
	
	 @RequestMapping(value = "/login" , method = RequestMethod.POST )
	    public String loginSubmit(Model model, @RequestParam String userid,@RequestParam String password) {
		 // model.addAttribute("userList", accountSearchRepository.searchuid(userid,password));
		  
		 Account acc =accountSearchRepository.searchuid(userid,password);
			//model.addAttribute("search", userid);
		 ModelMap model1 = new ModelMap();
		 
		
		// model1.put("userList", "prosenjit");
		 
			if(acc==null)
			{
				
				Error="No User details found";
				
				logger.info(Error);
				
			return "redirect:login";
			}
			else
			{
				userName=acc.getname();
				userAccId=acc.getaccId();
				userPhone=acc.getPhone();
				
				 model.addAttribute("userList", acc.getname());
				System.out.println("Name="+acc.getname());
				
				logger.info(userName+" logged in successfully");
			return "redirect:home";
			}
	    }

}
