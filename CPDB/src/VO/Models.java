package VO;

public class Models {
	String species;
	String gender = ".";
	String mutagencity = ".";
	String strain = ".";
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMutagencity() {
		return mutagencity;
	}
	public void setMutagencity(String mutagencity) {
		this.mutagencity = mutagencity;
	}
	public String getStrain() {
		return strain;
	}
	public void setStrain(String strain) {
		this.strain = strain;
	}
	
	public Models(String species, String gender, String mutagencity, String strain) {
		super();
		this.species = species;
		this.gender = gender;
		this.mutagencity = mutagencity;
		this.strain = strain;
	}
	public Models() {
		super();
	}
	
	public Models(String mutagencity) {
		super();
		this.mutagencity = mutagencity;
	}
	
	public Models(String gender, String mutagencity) {
		super();
		this.gender = gender;
		this.mutagencity = mutagencity;
	}
	
	@Override
	public String toString() {
		return "Models [species=" + species + ", gender=" + gender + ", mutagencity=" + mutagencity + ", strain="
				+ strain + "]";
	}
	
	
}
