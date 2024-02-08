package Game;

import java.util.ArrayList;

public class Player {
	private int currentCash; // cash the player has at the point
	private String name;
	public ArrayList<Card> cards = new ArrayList<Card>();// the cards the player has in front of him
	private int bet; // the amount of money a player put in during a round
	private int handValue; // the type of card combo a player has at the end of the game
	private boolean pickedAce;

	public Player(int currentCash, int bet, String name, ArrayList<Card> cards, int handValue, boolean pickedAce) {
		this.currentCash = currentCash;
		this.name = name;
		this.handValue = handValue;
		this.bet = bet;
		this.pickedAce = pickedAce;
		for (int i = 0; i < 2; i++) {
			this.cards = cards;
		}
	}

	public void setPickedAce(boolean x) {
		if (x == true) {
			this.pickedAce = true;
		} else {
			this.pickedAce = false;
		}
	}

	public boolean getPickedAce() {
		return this.pickedAce;
	}

	public int getHandValue() {
		return this.handValue;
	}

	public void setHandValue(int x) {
		this.handValue = x;
	}

	public void addHandValue(int x) {
		if (x == 14) {
			setPickedAce(true);
			x = 11;
		} else if (x > 10) {
			x = 10;
		}
		this.handValue = this.handValue + x;
		System.out.println(this.name + " Value: " + this.handValue);
	}

	public void addCards(Card x) {
		this.cards.add(x);
	}
	
	public void removeCard() { //removes last card in list
		this.cards.remove(this.cards.size()-1);
	}
	
	public void setCurrentCash(int x) {
		this.currentCash = x;
	}

	public int getCurrentCash() {
		return this.currentCash;
	}

	public void subBet() {
		this.currentCash = this.currentCash - this.bet;
	}

	public void setBet(int x) {
		this.bet = x;
	}

	public int getBet() {
		return this.bet;
	}

	public void addWinnings() {
		this.currentCash = this.currentCash + this.bet * 2;
		System.out.println("After win $" + this.currentCash);
	}

	public void tie() {
		this.currentCash = this.currentCash + this.bet;
	}

	public boolean checkForSplit() { // if the two cards chosen have the same value then a player can do a split
		if (this.cards.get(this.cards.size() - 2).getValue() == this.cards.get(this.cards.size() - 1).getValue()) {
			return true;
		}
		return false;
	}

	public Card getFirstCard() {
		return this.cards.get(this.cards.size() - 2);
	}

	public Card getSecondCard() {
		return this.cards.get(this.cards.size() - 1);
	}
}
