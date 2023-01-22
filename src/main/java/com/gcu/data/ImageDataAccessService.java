package com.gcu.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import com.gcu.data.entity.ImageEntity;
import com.gcu.data.entity.UserEntity;

@Service
public class ImageDataAccessService implements ImageDataAccessInterface
{
	@SuppressWarnings("unused")
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplateObject;
	
	// Constructor
	public ImageDataAccessService(DataSource dataSource)
	{
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	/**
	 * Get all images by a user and return them as a list of image objects.
	 * @param userEntity
	 * @return
	 */
	@Override
	public List<ImageEntity> getAllImagesByUser(UserEntity userEntity)
	{
		String sql = "SELECT * FROM images WHERE User_ID = '" + userEntity.getId() + "'"; 
		
		List<ImageEntity> images = new ArrayList<ImageEntity>();
		
		try
		{
			SqlRowSet record = jdbcTemplateObject.queryForRowSet(sql);
			
			while (record.next()){
				images.add(new ImageEntity(
						record.getInt("ID"),
						record.getString("Description"),
						record.getString("File"),
						record.getString("Timestamp"),
						record.getInt("User_ID")));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Collections.reverse(images);
		
		return images; 
	}
	
	/**
	 * Get an image by its Id. Return it as an image object.
	 * @param imageId
	 * @return
	 */
	@Override
	public ImageEntity getImageById(int imageId)
	{
		String sql = "SELECT * FROM images WHERE ID = '" + imageId + "'"; 
		
		ImageEntity image = null;
		
		try
		{
			SqlRowSet record = jdbcTemplateObject.queryForRowSet(sql);
			
			while (record.next()){
				image = new ImageEntity(
						record.getInt("ID"),
						record.getString("Description"),
						record.getString("File"),
						record.getString("Timestamp"),
						record.getInt("User_ID"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * Create a new image
	 * @param imageEntity
	 * @return
	 */
	@Override
	public boolean add(ImageEntity imageEntity)
	{
		LocalDateTime timestamp = LocalDateTime.now();   
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY, hh:mm:ss a");
        imageEntity.setTimestamp(timestamp.format(formatter));
        
		String sql = "INSERT INTO images (ID, Description, File, Timestamp, User_ID) VALUES (?, ?, ?, ?, ?)";
		
		try
		{
			int rows = jdbcTemplateObject.update(
				sql,
				null,
				imageEntity.getDescription(),
				imageEntity.getFile(),
				imageEntity.getTimestamp(),
				imageEntity.getUserId()
			);
			return rows == 1 ? true: false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Update an image
	 * @param imageEntity
	 * @return
	 */
	@Override
	public boolean update(ImageEntity imageEntity)
	{ 
		LocalDateTime timestamp = LocalDateTime.now();   
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY, hh:mm:ss a");
        imageEntity.setTimestamp(timestamp.format(formatter));
        
		String sql = "UPDATE images SET Description = '" + imageEntity.getDescription() +
						"', Timestamp = '" + imageEntity.getTimestamp() +
						"' WHERE ID = '" + imageEntity.getId() + "'";
		try
		{
			int rows = jdbcTemplateObject.update(sql);
			
			if (rows == 1)
			{
				System.out.println("IMAGE UPDATE SUCCESS");
				return true;
			}
		}
		catch (Exception e)
		{
			System.out.println("IMAGE UPDATE FAILURE");
			e.printStackTrace();
		}
		return false;
	}
	
	/**  
	 * Delete an image 
	 * @param imageId
	 * @return
	 */
	@Override
	public boolean delete(int imageId)
	{
		String sql = "DELETE FROM images WHERE ID = '" + imageId + "'";
		try
		{
			int rows = jdbcTemplateObject.update(sql);
			
			if (rows == 1)
			{
				System.out.println("IMAGE DELETE SUCCESS");
				return true;
			}
		}
		catch (Exception e)
		{
			System.out.println("IMAGE DELETE FAILURE");
			e.printStackTrace();
		}
		return false;
	}

	public ImageEntity getLastUserImage(UserEntity userEntity)
	{
		return getAllImagesByUser(userEntity).get(0);
	}
}
