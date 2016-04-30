package model;

public class CrystalStructure {

	String crystalStructure;
	
	public CrystalStructure(String s) {
		crystalStructure = s;
	}
	public static CrystalStructure parse(String s) {
		return new CrystalStructure(s);
	}

}
