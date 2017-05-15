package com.github.lantoine.lamsadetools.setCoordinates;

public class UserDetails {

	private String email;
	private String firstName;
	private String function;
	private String name;
	private String number;
	private String group; 
	private String fax;
	private String office;

	public UserDetails() {
		this("unknown", "unknown", "unknown", "unknown", "unknown", "unknown", "unknown","unknown");
	}

	public UserDetails(String name, String firstName, String function, String number, String email, String group, String fax, String office) {
		this.name = name;
		this.firstName = firstName;
		this.function = function;
		this.number = number;
		this.email = email;
		this.group = group;
		this.fax = fax;
		this.office = office;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFunction() {
		return function;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getFax() {
		return fax;
	}
	
	public String getOffice() {
		return office;
	}

	public boolean isFilled() {
		if (email != null && firstName != null && function != null && name != null && number != null && group != null && fax != null && office != null)
			return true;
		return false;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setGroup(String group) {
		this.number = group;
	}
	
	public void setFax(String fax) {
		this.number = fax;
	}
	
	public void setOffice(String office) {
		this.number = office;
	}

}
