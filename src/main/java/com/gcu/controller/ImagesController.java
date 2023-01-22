package com.gcu.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.gcu.business.ImageBusinessServiceInterface;
import com.gcu.business.UserBusinessServiceInterface;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;

@Controller
@RequestMapping("/image")
public class ImagesController
{
	@Autowired
	private UserBusinessServiceInterface userService;     
	@Autowired
	private ImageBusinessServiceInterface imageService;
    
    /**
     * Display the new image page
     * @param user
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/")
    public String display(UserEntity user, Model model, Principal principal) 
    {
        model.addAttribute("title", "Add Post");     
        model.addAttribute("pageName", "Create Post");
    	model.addAttribute("username", principal.getName());
        model.addAttribute("userEntity", user);
        model.addAttribute("imageModel", new ImageEntity());
        return "newImage";
    }
    
    /**
     * Add an image to the user's account
     * @param file
     * @param redirectAttributes
     * @param description
     * @param principal
     * @param model
     * @return
     */
    @PostMapping("/addImage")
    public String addImage( @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, String description, Principal principal, Model model) 
    {
    	UserEntity user = userService.getUserByUsername(principal.getName());
    	ImageEntity imageEntity = new ImageEntity();
        imageEntity.setUserId(user.getId());
        imageEntity.setDescription(description);
        imageEntity.setFile(file.getOriginalFilename());
    	
        if (file.isEmpty() || !isImage(file))
        {
            redirectAttributes.addFlashAttribute("message", "Please select a valid image file");
            return "redirect:uploadStatus";
        }

        try
        {
        	String uploadPath = Paths.get("src", "main", "resources", "static", "images", principal.getName()).toString();
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
            {
                uploadDir.mkdir();
            }
        	
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadPath, file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }   	
    	
        if (imageService.addImage(imageEntity))
        {
        	System.out.println("Image was successfully added to Images table!");
        }
        else
        {
        	System.out.println("An error occurred inserting new Image into Images table.");
        }

        model.addAttribute("userEntity", user);
        model.addAttribute("images", imageService.getAllImagesByUser(user));
    	model.addAttribute("username", principal.getName());
        model.addAttribute("pageName", "Home");
        model.addAttribute("title", "Home");
        
        return "redirect:/home";
    }

    // helper method to check if file is image
    private boolean isImage(MultipartFile file) {
        return file.getContentType().startsWith("image");
    }
    
    /**
     * Display the edit image page
     * @param imageId
     * @param imageEntity
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/editImage")
    public String editImage(@RequestParam String imageId, ImageEntity imageEntity, Model model, Principal principal)
    {
		int id = Integer.valueOf(imageId);
		imageEntity = imageService.getImageById(id);
		UserEntity user = userService.getUserByUsername(principal.getName());

		model.addAttribute("title", "Update Image");     
		model.addAttribute("pageName", "Edit Image");
		model.addAttribute("imageId", imageId);
		model.addAttribute("updateImageEntity", imageEntity);
		model.addAttribute("deleteImageEntity", imageEntity);
		model.addAttribute("description", imageEntity.getDescription());
		model.addAttribute("userEntity", user);
    	model.addAttribute("username", principal.getName());
		return "editImage";
    }
    
    /**
     * Update an image
     * @param imageEntity
     * @param imageId
     * @param description
     * @param bindingResult
     * @param model
     * @param principal
     * @return
     */
    @PostMapping("/updateImage")
    public String updateImage(@Valid ImageEntity imageEntity, String imageId, String description, BindingResult bindingResult, Model model, Principal principal)
    {
    	imageEntity = imageService.getImageById(Integer.valueOf(imageId));
    	imageEntity.setDescription(description);
    	
    	if(!bindingResult.hasErrors())
    	{
        	if (imageService.updateImage(imageEntity))
        	{
        		System.out.println("Image was successfully updated!");
        	}
    		else
    		{
    			System.out.println("An error occurred updating Image.");
    		}
    	}

    	model.addAttribute("title", "Home");
    	model.addAttribute("pageName", "Home");
    	model.addAttribute("username", principal.getName());
    	model.addAttribute("images", imageService.getAllImagesByUser(userService.getUserByUsername(principal.getName())));    	
    	return "redirect:/home";
    }
    
    /**
     * Delete an image
     * @param imageEntity
     * @param imageId
     * @param principal
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/deleteImage")
    public String deleteImage(@Valid ImageEntity imageEntity, String imageId, Principal principal, BindingResult bindingResult, Model model)
    {
    	ImageEntity image = imageService.getImageById(Integer.valueOf(imageId));
    	
    	
    	try
    	{
    		Path filePath = Paths.get("src", "main", "resources", "static", "images", principal.getName(), image.getFile());
    		Files.delete(filePath);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	if (imageService.deleteImage(Integer.valueOf(imageId)))
    	{
    		System.out.println("Image was successfully deleted!");
    	}
		else
		{
			System.out.println("An error occurred deleting Image.");
		}
    	
    	model.addAttribute("title", "Home");
    	model.addAttribute("pageName", "Home");
    	model.addAttribute("username", principal.getName());
    	model.addAttribute("images", imageService.getAllImagesByUser(userService.getUserByUsername(principal.getName())));    	
        return "redirect:/home";
    }
}
