package VO;

public class Effect {
	int experimentID;
	String tissue;
	String tumor;
	public int getExperimentID() {
		return experimentID;
	}
	public void setExperimentID(int experimentID) {
		this.experimentID = experimentID;
	}
	public String getTissue() {
		return tissue;
	}
	public void setTissue(String tissue) {
		this.tissue = tissue;
	}
	public String getTumor() {
		return tumor;
	}
	public void setTumor(String tumor) {
		this.tumor = tumor;
	}
	@Override
	public String toString() {
		return "Effect [experimentID=" + experimentID + ", tissue=" + tissue + ", tumor=" + tumor + "]";
	}
	
	public Effect(int experimentID, String tissue, String tumor) {
		super();
		this.experimentID = experimentID;
		this.tissue = tissue;
		this.tumor = tumor;
	}
	public Effect(int experimentID, String tissue) {
		super();
		this.experimentID = experimentID;
		this.tissue = tissue;
	}
	
	
}

