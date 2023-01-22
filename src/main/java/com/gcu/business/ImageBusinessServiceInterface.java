package com.gcu.business;

import java.util.List;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;

public interface ImageBusinessServiceInterface
{	
	public List<ImageEntity> getAllImagesByUser(UserEntity userEntity);
	public boolean addImage(ImageEntity postEntity);
	public boolean updateImage(ImageEntity postEntity);
	public boolean deleteImage(int postId);
	public ImageEntity getImageById(int postId);
	ImageEntity getLastUserImage(UserEntity userEntity);
}
