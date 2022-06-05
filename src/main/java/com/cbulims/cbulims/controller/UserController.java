package com.cbulims.cbulims.controller;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cbulims.cbulims.model.User;
import com.cbulims.cbulims.repository.UserRepository;

@Controller
public class UserController {
	
	private final UserRepository userRepo;
	
	public UserController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "Dashboard/dashboard";
	}
	
	@GetMapping("/")
	public String home() {
		return "Dashboard/dashboard";
	}
	
	//User Sign Up
	@GetMapping("/signup")
	public String getSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	//User Sign In
	@GetMapping("/login")
	public String getSignInForm() {
		return "login";
	}
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user")User user, Model model) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		
		user.setPassword(encodedPassword);
		
		userRepo.save(user);
	
		model.addAttribute("user",user);
		
		return "signup_success";
	}
	
	// Accessed only by application administrator
	//Show All USers
	@GetMapping("/showallusers")
	public String showAllUsers(Model model) {
		List<User> userlist = userRepo.findAll();
		model.addAttribute("alluserslist", userlist);
		return "userlist";
	}
	
	//All paths below this point should only be accessed by Developer
	//Save User
	@PostMapping("/save")
	public String saveNewUser(@ModelAttribute("newuser")User user) {
		userRepo.save(user);
		return "redirect:/userlist";	
	}
}
