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
import org.springframework.web.bind.annotation.RequestMapping;
import com.gcu.business.ImageBusinessService;
import com.gcu.business.UserBusinessService;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;

@Component
@Controller
@RequestMapping("/home")
public class HomeController 
{
	Logger logger = LoggerFactory.getLogger(HomeController.class);

	// VARIABLES 
    @Autowired
    private UserBusinessService userService;     
    @Autowired
    private ImageBusinessService imageService;
    
    // display home page 
    @GetMapping("/")
    public String display(Model model, Principal principal) 
    {   
		logger.info("[LOGGER] - loaded home page for user: {}", principal.getName());
		
    	UserEntity user = userService.getUserByUsername(principal.getName());
    	List<ImageEntity> images = imageService.getAllImagesByUser(user);
    	
        model.addAttribute("title", "Home");
        model.addAttribute("pageName", "Home");
        model.addAttribute("username", user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("images", images);
        model.addAttribute("userEntity", user);
        
        return "home";
    }
}
