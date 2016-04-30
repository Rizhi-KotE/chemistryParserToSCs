package model;

public class ElectronicConfiguration {

	private String configuration;
	
	public ElectronicConfiguration(String s) {
		configuration = s;
	}
	public static ElectronicConfiguration parse(String s) {
		
		return new ElectronicConfiguration(s);
	}

}
