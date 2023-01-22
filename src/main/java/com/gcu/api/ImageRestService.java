package com.gcu.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gcu.business.ImageBusinessService;
import com.gcu.business.UserBusinessService;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;

@RestController
@RequestMapping("/api")
public class ImageRestService
{
	@Autowired
	UserBusinessService userService;
	@Autowired
	ImageBusinessService imageService;
	
	/**
	 * Get all user posts by a requested username.
	 * @param username
	 * @return 
	 */
	@GetMapping(path="/allUserImages")
	public ResponseEntity<?> getAllUserImages(@RequestParam String username)
	{
		try
		{
			UserEntity user = userService.getUserByUsername(username);
			
			if (user == null)
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else
			{
				List<ImageEntity> images = imageService.getAllImagesByUser(user);
				return new ResponseEntity<>(images, HttpStatus.OK);
			}
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Get latest post for a requested username. 
	 * @param username
	 * @return
	 */
	@GetMapping(path="/lastUserImage")
	public ResponseEntity<?> getLastUserImage(@RequestParam String username)
	{
		try
		{
			UserEntity user = userService.getUserByUsername(username);
			
			if (user == null)
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else
			{
				ImageEntity image = imageService.getLastUserImage(user);
				return new ResponseEntity<>(image, HttpStatus.OK);
			}
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}