package com.gcu.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.gcu.business.UserBusinessService;
import com.gcu.data.entity.UserEntity;

@Controller
@RequestMapping("/registration")
public class RegistrationController 
{
	 @Autowired
	 private UserBusinessService userService;
    
	/**
	 * Display Registration page
	 * 
	 * @param model
	 * @return
	 */
    @GetMapping("/")
    public String displayRegistration(Model model) 
    {
        model.addAttribute("title", "Registration");       
        model.addAttribute("pageName", "Create Account");
        model.addAttribute("userEntity", new UserEntity());
        
        return "registration";
    }
    
    /**
     * performs a submission of new user
     * 
     * @return
     */
    @PostMapping("/submitRegistration")
    public ModelAndView submitRegistration(@Valid UserEntity userEntity, BindingResult bindingResult, Model model) 
    {      
        ModelAndView mv = new ModelAndView(); 
        
        boolean existingUserError = false;
        if (userService.getUserByUsername(userEntity.getUsername()) != null)
        {
        	existingUserError = true;
        }
        
        if (bindingResult.hasErrors() || existingUserError) 
        {
        	if (existingUserError)
        		mv.addObject("existingUserError", "Username already exists!"); 
        	
            mv.addObject("title", "Registration");
            mv.addObject("pageName", "Registration");
            mv.setViewName("registration");   
            return mv;
        }

        UserEntity newUser = null;
        boolean insertionResult = userService.addUser(userEntity);
        if (insertionResult)
        {
            newUser = userService.getUserByUsername(userEntity.getUsername());
        	System.out.println("New user successfully added to Users table!"); 
        	System.out.println("ID = " + newUser.getId() + ", Username = " + newUser.getUsername());
        }
        
        mv.addObject("title", "Login");
        mv.addObject("pageName", "Login");
        mv.setViewName("redirect:/");
        return mv;
    }
}
