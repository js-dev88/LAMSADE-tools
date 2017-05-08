package com.github.lantoine.lamsadetools.yearbookInfos;

@SuppressWarnings("serial")
public class YearbookDataException extends Exception{

	/**
	 * YearbookDataException occurs when some data are missing or when the firstname and / or the surname of the professor is wrong
	 */
	
	public YearbookDataException(String message){
		 super(message);
	}
	

}
