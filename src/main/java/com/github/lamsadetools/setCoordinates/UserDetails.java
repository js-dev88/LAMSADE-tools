package com.github.lamsadetools.setCoordinates;

public class UserDetails {

	private String email;
	private String firstName;
	private String function;
	private String name;
	private String number;

	public UserDetails() {
		this("unknown", "unknown", "unknown", "unknown", "unknown");
	}

	public UserDetails(String name, String firstName, String function, String number, String email) {
		this.name = name;
		this.firstName = firstName;
		this.function = function;
		this.number = number;
		this.email = email;
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

	public boolean isFilled() {
		if (email != null && firstName != null && function != null && name != null && number != null)
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

}
