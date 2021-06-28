package it.polito.tdp.crimes.model;

public class Adiacenza {
	
	private String t1;
	private String t2;
	private int peso;
	
	public Adiacenza(String t1, String t2, int peso) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.peso = peso;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return t1 + " VS " + t2 + " (" + peso + ")";
	}
	
	
	

}
