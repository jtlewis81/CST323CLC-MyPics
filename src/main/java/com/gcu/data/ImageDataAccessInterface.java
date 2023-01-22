package com.gcu.data;

import java.util.List;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;

public interface ImageDataAccessInterface
{
	public List<ImageEntity> getAllImagesByUser(UserEntity userEntity);
	public ImageEntity getImageById(int imageId);
	public boolean add(ImageEntity imageEntity);
	public boolean update(ImageEntity imageEntity);
	public boolean delete(int imageId);
	public ImageEntity getLastUserImage(UserEntity userEntity);
}
