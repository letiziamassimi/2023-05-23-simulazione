package it.polito.tdp.baseball.model;

public class Coppia {
	
	String playerId1;
	String playerId2;
	
	public Coppia(String playerId1, String playerId2) {
		super();
		this.playerId1 = playerId1;
		this.playerId2 = playerId2;
	}

	public String getPlayerId1() {
		return playerId1;
	}

	public void setPlayerId1(String playerId1) {
		this.playerId1 = playerId1;
	}

	public String getPlayerId2() {
		return playerId2;
	}

	public void setPlayerId2(String playerId2) {
		this.playerId2 = playerId2;
	}
	
	

}
