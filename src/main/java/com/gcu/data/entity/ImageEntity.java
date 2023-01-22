package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Images")
public class ImageEntity
{    
	@Id
	private int id; 
	@Column("Title")
    private String description;
	@Column("File")
    private String file;    
	@Column("Timestamp")
    private String timestamp; 
	@Column("User_ID")
    private int userId;
    
	public ImageEntity(int id, String description, String file, String timestamp, int userId)
    {
    	this.id = id; 
    	this.description = description; 
    	this.file = file;
    	this.timestamp = timestamp; 
    	this.userId = userId;
    }
    
    public ImageEntity(){}

    
    public int getId() { return this.id; }
    public String getDescription() { return this.description; }
    public String getFile() { return this.file; }
    public String getTimestamp() { return this.timestamp; }
    public int getUserId() { return this.userId; }
    
    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setFile(String file) { this.file = file; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }   
    public void setUserId(int userId) { this.userId = userId; }
}