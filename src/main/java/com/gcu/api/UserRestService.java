package com.gcu.api;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gcu.business.UserBusinessService;
import com.gcu.data.entity.UserEntity;

/***
 * This class handles user authentication of login module. 
 * @author FriendZone developers
 *
 */
@RestController
@RequestMapping("/api")
public class UserRestService
{
	// VARIABLES 
	@Autowired
	UserBusinessService service;
	
	/***
	 * Get a user from the database and return an http response. 
	 * @param username
	 * @param principal
	 * @return
	 */
	@GetMapping(path="/user")
	public ResponseEntity<?> getUser(@RequestParam String username, Principal principal)
	{
		try
		{
			// retrieve user 
			UserEntity user = service.getUserByUsername(username);
			
			if (user == null)
			{
				// return not found if user object is null 
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else if (username.equals(principal.getName()))
			{
				// return an http success response 
				return new ResponseEntity<>(user, HttpStatus.OK);
			}
			else
			{
				// return a 401 unuathorized error if username does not equal principle value
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		}
		catch (Exception e)
		{
			// Error: return server error
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
