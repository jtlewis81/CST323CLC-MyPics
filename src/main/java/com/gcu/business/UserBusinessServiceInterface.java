package com.gcu.business;

import com.gcu.data.entity.UserEntity;

public interface UserBusinessServiceInterface
{
	public UserEntity getUserByUsername(String username);
	public boolean addUser(UserEntity userEntity);
	public boolean updateUser(UserEntity userEntity);
	public boolean deleteUser(UserEntity userEntity);
}
