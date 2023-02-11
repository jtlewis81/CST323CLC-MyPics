package com.gcu.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gcu.data.ImageDataAccessService;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;

@Service
public class ImageBusinessService implements ImageBusinessServiceInterface
{
	Logger logger = LoggerFactory.getLogger(UserBusinessService.class);
	
	@Autowired
	ImageDataAccessService imageService;
	
	/**
	 * Get data on a user's Images and return all Images as a list object.
	 */
	@Override
	public List<ImageEntity> getAllImagesByUser(UserEntity userEntity)
	{
		logger.info("[LOGGER] - returning all images for username: {}", userEntity.getUsername());
		
		return imageService.getAllImagesByUser(userEntity);
	}
		
	/**
	 * Add an image to the database. 
	 */
	@Override
	public boolean addImage(ImageEntity imageEntity)
	{
		logger.info("[LOGGER] - adding image with filename: {}", imageEntity.getFile());
		
		return imageService.add(imageEntity);
	}
	
	/**
	 * Send a post model to the database to update its corresponding record columns. 
	 */
	@Override
	public boolean updateImage(ImageEntity imageEntity)
	{
		logger.info("[LOGGER] - updating description for image with filename: {}", imageEntity.getFile());
		
		return imageService.update(imageEntity);	
	}

	/**
	 * Delete a post from the database by its Id. 
	 */
	@Override
	public boolean deleteImage(int imageId)
	{
		logger.info("[LOGGER] - deleting image with filename: {}", imageService.getImageById(imageId).getFile());    	
		
		return imageService.delete(imageId);		
	}

	/**
	 * Get a post by its record Id. 
	 */
	@Override
	public ImageEntity getImageById(int imageId)
	{
		logger.info("[LOGGER] - returning imageEntity by id for image with filename: {}", imageService.getImageById(imageId).getFile());
		
		return imageService.getImageById(imageId);
	}

	/**
	 * Get the last image a user uploaded.
	 */
	@Override
	public ImageEntity getLastUserImage(UserEntity userEntity)
	{
		logger.info("[LOGGER] - returning last image for username: {}", userEntity.getUsername());
		
		return imageService.getLastUserImage(userEntity);
	}
}
