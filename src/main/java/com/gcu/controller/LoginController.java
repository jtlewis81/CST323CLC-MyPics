package com.gcu.controller;

import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.gcu.business.ImageBusinessService;
import com.gcu.business.UserBusinessService;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;
import com.gcu.model.RegistrationModel;

@Component
@Controller
public class LoginController
{
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	 @Autowired
	 private UserBusinessService userService;
	 @Autowired
	 private ImageBusinessService imageService;
	
	/**
	* Display Login page
	* @param model
	* @return
	*/
	@GetMapping("/")
	public String displayLogin(Model model)	
	{
		logger.info("[LOGGER] - loaded login page");
		
		model.addAttribute("title", "Login Form");
		return "login";
	}

	/**
	 * takes the user to the registration form
	 * @param model
	 * @return
	 */
	@GetMapping("/registration")
	public String newUser(Model model)
	{
		logger.info("[LOGGER] - loaded registration page");
		
	    model.addAttribute("title", "New User Registration");
	    model.addAttribute("registrationModel", new RegistrationModel());
	    return "registration";
	} 

	/**
	* return the home view
	* @param model
	* @param principal
	* @return
	*/
	@GetMapping("/home")
	public ModelAndView home(Model model, Principal principal)
	{		
		logger.info("[LOGGER] - logging in user: {}", principal.getName());
		
		UserEntity user = userService.getUserByUsername(principal.getName());
    	List<ImageEntity> images = imageService.getAllImagesByUser(user);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("userEntity", userService.getUserByUsername(principal.getName()));
		mv.addObject("username", principal.getName());        
		mv.addObject("images", images);
		mv.addObject("title", "Home");
		mv.addObject("pageName", "Home");
		mv.setViewName("home");
		return mv;
	}
	
}
