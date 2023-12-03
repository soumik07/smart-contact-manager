package com.smart.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Messege;

import net.bytebuddy.asm.Advice.Enter;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@ModelAttribute	//it will run this method for all handler and will bind the user to model object.{model.addAttribute("user", user)}will do this for all handeller, so it will be available in all urls
	public void commonData(Model model, Principal principal) {
		
		//getting the user by the email and sending the name of user to the dashboard
				String userName = principal.getName();
				User user = userRepository.getUserByUserName(userName);
				model.addAttribute("user", user);
				//System.out.println("user "+user);
	}
	
	//Dashboard page of User
	@RequestMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title", "User dashboard");
		return "normal/user_dashboard";
	}
	
	
	//Add Contact form
	@RequestMapping("/add-contact")
	public String addcontact(Model model, Principal principal) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	
	//Processing add-contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal principal,@RequestParam("profileImage") MultipartFile file , HttpSession session) {
		
		try {
			
			String userName = principal.getName();
			User user = userRepository.getUserByUserName(userName);
			
			
			
			/*if(file.isEmpty()) {
				//if the image is empty
				System.out.println("The file is empty..");
			}else {
				
				//upload the image file in "img" folder and update the name of the file in the "image" field of contact table
				
				contact.setImageurl(file.getOriginalFilename());
				
				//---------------------------------uploading file is not working---------------------------------------------------
			/*
			 * 
			 * 	File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path);
				System.out.println("Image is uploaded");
			}*/
			
			contact.setUser(user);
			user.getContacts().add(contact);
			User result = userRepository.save(user);
			
			session.setAttribute("messege", new  Messege("Your contact is saved !!", "alert-success"));
				
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR: "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("messege", new Messege("Something went wrong !","alert-danger"));
		}
		
		return "normal/add_contact_form";
	}
	
	
	//Handler for viewing all contacts of the user
	@GetMapping("/view-contact/{currentPage}")
	public String viewContact(@PathVariable("currentPage") Integer currentPage, Model model, Principal p) {
		String userName =  p.getName();
		User user = userRepository.getUserByUserName(userName);
		
		//In the pageble obj we need to provide 2 info
		//current page no.(currentPage) and item per page(5)
		Pageable pageable = PageRequest.of(currentPage, 7);
		Page<Contact> contacts=contactRepository.findContactsByUser(user.getId(), pageable);
		
		//sending it to View
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		model.addAttribute("title","View Contact");
		return "normal/view_contact";
	}
	
	
	//Handler for deleting contact
	@GetMapping("/delete-contact/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) {
		//System.out.println("CID: "+cid);
		Optional<Contact> contactOptional =  contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		contactRepository.delete(contact);
		
		session.setAttribute("messege", new Messege("Selected contact has been deleted..","alert-success"));
		return "redirect:/user/view-contact/0";
	}
	
	
	//Handler for update contact form
	@RequestMapping("/update-contact/{cid}")
	public String updateContactForm(@PathVariable("cid") Integer cid, Model model) {
		
		Optional<Contact> contactOptional =  contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		
		model.addAttribute("contact",contact);
		model.addAttribute("title","Update Contact");
		return "normal/update_contact_form";
		
	}
	
	
	//Handler for Processing Update form and save the updated values into DB
	@PostMapping("/process-update/{cid}")
	public String ProcessUpdateForm(@ModelAttribute Contact contact, @PathVariable("cid") Integer cid, HttpSession session, Principal principal) {
		
		try {
		//Save kora baki database e
		String userName =  principal.getName();
		User user = userRepository.getUserByUserName(userName);
		contact.setUser(user);
		this.contactRepository.save(contact);
		}
		catch(Exception e) {
			System.out.println("Some error: "+ e.getMessage());
		}
		
		session.setAttribute("messege", new Messege("Your contact has been updated successfully...!!","alert-success"));
		return "normal/update_messege";
	}
	
	
	//Your-profile page handler
	@RequestMapping("/your-profile")
	public String profilePage(Model model, Principal principal) {
		String userName =  principal.getName();
		User user = userRepository.getUserByUserName(userName);
		model.addAttribute("user",user);
		return "normal/your_profile";
	}
	
	
	//settings handller
	@RequestMapping("/settings")
	public String openSettings(Model model) {
		
		
		return "normal/settings";
	}
	
	//changPassword handler
		@PostMapping("/change-password")
		public String changePassword(@RequestParam("oldPassword") String oldPassword,
				@RequestParam("newPassword") String newPassword, @RequestParam("renewPassword") String renewPassword, 
				Model model, Principal principal, HttpSession session) {
			
			String userName = principal.getName();
			User user = userRepository.getUserByUserName(userName);
			String currentPasswordInDB = user.getPassword();
			
			
			 //matches(the raw pass i am taking from user in the CP form, encoded pass from DB);
			if(this.bCryptPasswordEncoder.matches(oldPassword, currentPasswordInDB)) { 
				
				if(newPassword.equals(renewPassword)) {
					user.setPassword(bCryptPasswordEncoder.encode(newPassword));
					@SuppressWarnings("unused")
					User updatedUser = this.userRepository .save(user);
				}
				else {
					session.setAttribute("messege", new Messege("Kindly re-Enter the new password correctly..!", "alert-danger"));
					return "normal/settings";
				}
			}
			else {
				session.setAttribute("messege", new Messege("Password does not match with the current password..!!", "alert-danger"));
				return "normal/settings";
			}
			
			session.setAttribute("messege", new Messege("Your password has been changed successfully..!!", "alert-success"));
			return "normal/settings";
		}
	
	
	
	
	
	
	
	
	
}
