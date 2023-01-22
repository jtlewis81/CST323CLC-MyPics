package com.gcu.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gcu.business.UserBusinessService;
import com.gcu.data.entity.UserEntity;

@Controller
@RequestMapping("/profile")
public class ProfileController
{
	// VARIABLES 
    @Autowired
    private UserBusinessService userService;
	
    /**
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/")
    public String displayOwnProfile(Model model, Principal principal)
    {
    	UserEntity user = userService.getUserByUsername(principal.getName());
    	
    	model.addAttribute("title", "MyProfile");
    	model.addAttribute("pageName", "My Profile");
    	model.addAttribute("username", principal.getName());
    	model.addAttribute("user", user);
    	return "profile";
    }
}
