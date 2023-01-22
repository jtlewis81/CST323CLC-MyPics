package com.gcu.model;

public class LoginModel {

	/**
	 * The LoginModel consists of "username" and "password" String variables.
	 */
    private String username;    
    private String password;
    
    /**
     * username setter
     * 
     * @param user
     */
    public void setUsername(String user) { username = user; }
    
    /**
     * username getter
     * 
     * @return username
     */
    public String getUsername() { return username; }
    
    /**
     * password setter
     * 
     * @param pass
     */
    public void setPassword(String pass) { password = pass; }
    
    /**
     * password getter
     * 
     * @return password
     */
    public String getPassword() { return password; }
}
