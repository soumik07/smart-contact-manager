package com.smart.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Messege;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "signin - Smart Contact Manager");
		return "login";
	}
	
	//handler for registering user
	@RequestMapping(value="/do_register", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,  BindingResult result1, @RequestParam(value="agreement", defaultValue="false")
	boolean agreement, Model model, HttpSession session) {
		
		try {
			if(!agreement) {
				System.out.println("You haven't agreed the terms and conditions.");
				throw new Exception("You haven't agreed the terms and conditions.");
			}
			
			if(result1.hasErrors()) {
				System.out.println("Result1Errors: "+ result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("default.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		
			System.out.println("User: " + user);
			System.out.println("Agreement: " + agreement);
			model.addAttribute("title", "Login - Smart Contact Manager");
			model.addAttribute("user", user);
		
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("messege", new Messege("Your registration is done successfully!!","alert-success"));
			return "signup";
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("messege", new Messege(e.getMessage(),"alert-danger"));
			return "signup";
		}
			
	}
}
