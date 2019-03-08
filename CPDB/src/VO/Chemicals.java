package VO;

import java.util.UUID;

public class Chemicals {
	private String chemicals_name;
	private String CAS;
	
	public String getChemicals_name() {
		return chemicals_name.replace("\"", "");
	}
	
	public void setChemicals_name(String chemicals_name) {
		this.chemicals_name = chemicals_name;
	}
	public String getCAS() {
		return CAS;
	}
	public void setCAS(String cAS) {
		CAS = cAS;
	}
	
	public Chemicals(String chemicals_name, String cAS) {
		super();
		this.chemicals_name = chemicals_name;
		CAS = cAS;
	}
	
	public Chemicals() {
		super();
	}
	
	@Override
	public String toString() {
		return "Chemicals [chemicals_name=" + chemicals_name + ", CAS=" + CAS + "]";
	}
}
