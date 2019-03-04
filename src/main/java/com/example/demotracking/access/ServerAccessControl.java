package com.example.demotracking.access;

import java.io.IOException;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import com.example.demotracking.access.AccessControl;
import com.example.demotracking.access.CurrentUser;
import com.example.demotracking.classes.ConnectionManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class ServerAccessControl
implements AccessControl {
	
    public String signIn(ConnectionManager manager, String username, String password) {
    	
    	String auth_pw = this.generatePWHash(password);
    	String result = "";
    	
    	int indexAt;
    	indexAt = username.indexOf("@");
    	
    	//connect to server
    	LdapConnection auth = new LdapNetworkConnection("172.16.1.4", 389);
    	
    	System.out.println("-- signIn --");
    	try {
			auth.bind("CN=Administrator, CN=Users, DC=mgenesis, DC=local", "JpxL0F92");
			
			EntryCursor cursor = auth.search( 
					"OU=User, OU=Logistics, DC=mgenesis, DC=local", 
					"(objectclass=*)", SearchScope.ONELEVEL 
					);
			
			for ( Entry entry : cursor )
		    {
				if (entry.get("mailNickname") != null 
						&& entry.get("mailNickname").contains(username.substring(0, indexAt))) {
					System.out.println("Found it!");
					
					//success
					result = "Admin";
					CurrentUser.set(result, "", username);
					break;
				}
		    }
			
		} catch (LdapException e) {
			//failure
			System.out.println("Failed to authenticate with LDAP server");
			e.printStackTrace();
		}
    	
    	try {
    		//close the connection
			auth.unBind();
			auth.close();
		} catch (LdapException e) {
			System.out.println("Failed to unbind from LDAP server");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to close connection with LDAP server");
			e.printStackTrace();
		}
        
        return result;
    }

    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    public boolean isUserInRole(String role) {
        return this.isRoleValid(role);
    }

    public boolean isRoleValid(String role) {
        return "Admin".equals(role) | "Viewer".equals(role);
    	//return true;
    }

    public String getPrincipalName() {
        return CurrentUser.get();
    }
    
    private String generatePWHash(String pw) {
    	String auth_pw = pw;
    	MessageDigest md;
    	
    	try {
			md = MessageDigest.getInstance("MD5");
			md.update(auth_pw.getBytes());
	    	return DatatypeConverter.printHexBinary(md.digest());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return "";
		}	
    }
}

