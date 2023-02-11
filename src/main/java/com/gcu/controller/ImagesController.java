package com.gcu.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import javax.validation.Valid;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Component
@Controller
@RequestMapping("/image")
public class ImagesController
{
	Logger logger = LoggerFactory.getLogger(ImagesController.class);

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
		logger.info("[LOGGER] - loaded image upload page");
		
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
		logger.info("[LOGGER] - uploading image with filename: {}", file.getOriginalFilename());
		
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
        
    	FTPClient client = new FTPClient();
    	FileInputStream fis = null;
    	String filepath = "/images/" + principal.getName() + "/";    	
	    String filename = file.getOriginalFilename();

    	try {
    	    client.connect("beachblock.net");
    	    client.login("jtlewis81", "");
    	    client.setFileType(FTP.BINARY_FILE_TYPE);

    	    //
    	    // Check for existing directory or create it
    	    //
    	    try
    		{
    			if(client.changeWorkingDirectory(filepath) == false)
    			{
    				client.makeDirectory(filepath);
    			}
    		}
        	catch (IOException e)
    		{
    			e.printStackTrace();
    		}
    	    
    	    //
    	    // Create an InputStream of the file to be uploaded
    	    //
    	    fis = (FileInputStream) file.getInputStream();

    	    //
    	    // Store file to server
    	    //
    	    client.changeWorkingDirectory(filepath);
   	    	client.storeFile(filename, fis);
    	    
    	    client.logout();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} finally {
    		// disconnect
    	    try {
    	        if (fis != null) {
    	            fis.close();
    	        }
    	        client.disconnect();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
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
		logger.info("[LOGGER] - loaded edit image page");
		
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
		logger.info("[LOGGER] - editing description for image with filename: {}", imageEntity.getFile());
		
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
		logger.info("[LOGGER] - deleting image with filename: {}", imageEntity.getFile());
		
    	ImageEntity image = imageService.getImageById(Integer.valueOf(imageId));
    	
    	FTPClient client = new FTPClient();
    	FileInputStream fis = null;
    	String filepath = "/images/" + principal.getName() + "/";
	    String filename = image.getFile();

    	try {
    	    client.connect("beachblock.net");
    	    client.login("jtlewis81", "");
    	    client.setFileType(FTP.BINARY_FILE_TYPE);
    	    client.changeWorkingDirectory(filepath);
    	    client.deleteFile(filename);
    	    System.out.println(client.getReplyString());
    	    
    	    client.logout();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} finally {
    	    try {
    	        if (fis != null) {
    	            fis.close();
    	        }
    	        client.disconnect();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
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
