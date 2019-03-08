package VO;

public class Experiments {
	int chemicalID;
	int modelID;
	String td50;
	String additional_information;
	String route;
	int totalexptime;
	int exposuretime;
	double lc;
	double uc;
	String source;
	String reference;
	String cpdb_idnum;
	
	public String getCpdb_idnum() {
		return cpdb_idnum;
	}
	public void setCpdb_idnum(String cpdb_idnum) {
		this.cpdb_idnum = cpdb_idnum;
	}
	public int getChemicalID() {
		return chemicalID;
	}
	public void setChemicalID(int chemicalID2) {
		this.chemicalID = chemicalID2;
	}
	public int getModelID() {
		return modelID;
	}
	public void setModelID(int modelID) {
		this.modelID = modelID;
	}
	public String getTd50() {
		return td50;
	}
	public void setTd50(String td50) {
		this.td50 = td50;
	}
	public String getAdditional_information() {
		return additional_information;
	}
	public void setAdditional_information(String additional_information) {
		this.additional_information = additional_information;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public int getTotalexptime() {
		return totalexptime;
	}
	public void setTotalexptime(int totalexptime) {
		this.totalexptime = totalexptime;
	}
	public int getExposuretime() {
		return exposuretime;
	}
	public void setExposuretime(int exposuretime) {
		this.exposuretime = exposuretime;
	}
	public double getLc() {
		return lc;
	}
	public void setLc(double lc) {
		this.lc = lc;
	}
	public double getUc() {
		return uc;
	}
	public void setUc(double uc) {
		this.uc = uc;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	@Override
	public String toString() {
		return "Experiments [chemicalID=" + chemicalID + ", modelID=" + modelID + ", td50=" + td50
				+ ", additional_information=" + additional_information + ", route=" + route + ", totalexptime="
				+ totalexptime + ", exposuretime=" + exposuretime + ", lc=" + lc + ", uc=" + uc + ", source=" + source
				+ ", reference=" + reference + ", cpdb_idnum=" + cpdb_idnum + "]";
	}
	
	public Experiments(int chemicalID, int modelID) {
		super();
		this.chemicalID = chemicalID;
		this.modelID = modelID;
	}
	public Experiments() {
		super();
	}
	
}
