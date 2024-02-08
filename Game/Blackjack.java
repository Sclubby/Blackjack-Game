package Game;

import java.util.ArrayList;

public class Blackjack {
	public static String[] symbols = { "D", "H", "S", "C" };
	public static display gui = new display();
	public static ArrayList<Card> fullDeck = new ArrayList<Card>(); // complete 52 cards
	public static ArrayList<Card> playingDeck = new ArrayList<Card>(); // deck used during game
	public static Card mysteryCard = new Card(null, 0);
	public static Player player = new Player(1000, 1000, "Carl", new ArrayList<Card>(), 0, false);
	public static Player secondHand = new Player(0, 0, "Hand2", new ArrayList<Card>(), 0, false); // if the player
																									// splits uses this
																									// new player object
																									// as the other hand
	public static Player dealer = new Player(0, 0, "dealer", new ArrayList<Card>(), 0, false);
	public static boolean split = false;
	public static boolean dd = false;

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		gui.createJpanel();
		BuildDeck();
	}

//	public static void printDeck() {
//		for (Card x : playingDeck) {
//			System.out.println(x.toString());
//		}
//	}

	public static void playerChoice(int bet) {// waits for player input unless the player busts
		gui.resetdisplayValues(player);
	}

	public static void dealCards() { // gives cards out at the start of every round and hides the dealers second card
		player.subBet();
		Integer[] xValues = { 350, 300, 350, 300 };
		Integer[] yValues = { 400, 400, 100, 100 };

		for (int x = 0; x < xValues.length; x++) {
			Card pickedCard = PickRandCard();

			if (x < 2) {
				player.addCards(pickedCard);
				player.addHandValue(pickedCard.getValue());
			} else {
				dealer.addCards(pickedCard);
				dealer.addHandValue(pickedCard.getValue());
			}

			if (x == 3) {
				mysteryCard = pickedCard;
				gui.displayCard("red_back", xValues[x], yValues[x]); // this will display the back of the card infront
																		// of the hidden dealers card
			} // This way the program simply removes the back of the card picture to reveal
				// the actual card
			gui.displayCard(pickedCard.toString(), xValues[x], yValues[x]);
		}
	}

	public static void BuildDeck() { // builds the deck initially at the beginning of the game
		int cardValue = 2, suitNum = 0;
		for (int i = 0; i < 52; i++) {
			fullDeck.add(new Card(symbols[suitNum], cardValue));
			if (cardValue == 14) {
				cardValue = 2;
				suitNum++;
			} else {
				cardValue++;
			}
		}
		for (Card x : fullDeck) {
			playingDeck.add(x);
		} // makes fulldeck = to playingdeck
	}

	public static Card PickRandCard() { // picks a random card from the playing deck
		int randCard = (int) (Math.random() * playingDeck.size() - 1);
		Card card = playingDeck.get(randCard);
		playingDeck.remove(randCard);
		return card;
	}

	public static void hit(int numOfHits, Player x) { // when the player or dealer hits
		int Xpos = 300;
		if (split == true && x != dealer) { // changes x pos of display card if the player splits
			if (x == player) {
				Xpos = 200;
			} else {
				Xpos = 600;
			}
		}
		Card pickedCard = PickRandCard();
		if (pickedCard.getValue() == 14) {
			x.setPickedAce(true);
		}
		if (x == player || x == secondHand) {
			gui.displayCard(pickedCard.toString(), Xpos - (numOfHits * 50), 400);
		} else {// if the dealer hits
			gui.displayCard(pickedCard.toString(), 300 - (numOfHits * 50), 100);
		}
		// adds to hand value and checks ace, bust, and blackjack
		x.addHandValue(pickedCard.getValue());
		gui.resetdisplayValues(x);
		if (x.getHandValue() > 21) {
			if (x.getPickedAce() == true) { // negates the ace if it goes over 21
				x.setHandValue(x.getHandValue() - 10);
				x.setPickedAce(false);
			} // decreases value of ace if over 21
			else {
				gui.bust(x);
			}
		}
		gui.resetdisplayValues(x);
		if (x.getHandValue() >= 21 && x != dealer) { // if player goes over 21 automatically stays
			stay(x);
		}
	}

	public static void stay(Player x) { // every time a player presses the stay button, bust, or dd stay is ran
		if (x != dealer) { // transitioning to the next part of the game (stay does nothing if the dealer runs it)
			if (split == true && x == player) { // split and stay on first pair
				gui.changeIndicator(2);
				display.numOfHits = 0;
			} else { // everything else
				gui.revealDealerCard();
				dealerMoves();
				decideWinner();
			}
		}
	}

	private static void dealerMoves() { // controls the dealers play
		int numOfHits = 1;
		int CardTotal = dealer.getHandValue();
		gui.resetdisplayValues(dealer);
		while (CardTotal < 17) {// dealer will keep playing until > or = 17
			hit(numOfHits, dealer);
			System.out.println(dealer.getHandValue());
			CardTotal = dealer.getHandValue();
			numOfHits++;
		}
	}

	private static void decideWinner() { // used to decide who won for one or two hands
		int x = 0; // used to show winDisplay which pair it is displaying for a split
		boolean loop = true;
		int playerTotal = player.getHandValue();
		int dealerTotal = dealer.getHandValue();
		if (split == true) {
			x = 1;
		}
		while (loop == true) {
			if (dealerTotal > 21 && playerTotal > 21) { // if both the dealer and player bust = tie
				player.tie();
				gui.winDisplay(2, x);
			} else {
				if (playerTotal > 21) {// player bust
					gui.winDisplay(1, x);
				} else {
					if (dealerTotal > 21) { // dealer bust
						player.addWinnings();
						gui.winDisplay(0, x);
					} else {
						if (dealerTotal > playerTotal) {// dealer bigger total
							gui.winDisplay(1, x);
						} else {
							if (dealerTotal == playerTotal) { // tie
								player.tie();
								gui.winDisplay(2, x);
							} else {
								if (playerTotal == 21) { // blackjack for player
									player.addWinnings();
									gui.winDisplay(3, x);
								} else { // player bigger
									player.addWinnings();
									gui.winDisplay(0, x);
								}
							}
						}
					}
				}
			}
			if (split == true) {
				playerTotal = secondHand.getHandValue();
				x = 2;
				split = false;
			} else {
				loop = false;
			}
		}
	}

	public static void reset() {// when the reset button is pressed resets all the variables for a new round
		split = false;
		dd = false;
		Player[] list = { player, dealer, secondHand };
		for (int y = 0; y < 2; y++) { // resets all player object variables
			list[y].setHandValue(0);
			list[y].setPickedAce(false);
		}
		playingDeck.clear();
		for (Card x : fullDeck) {
			playingDeck.add(x);
		} // resets playing deck
		if (player.getCurrentCash() == 0) {
			gui.broke();
		} else {
			gui.betting(player);
		}
	}

	public static void doubleDown() { // runs if the dd button is pressed
		dd = true;
		player.subBet();
		player.setBet(player.getBet() * 2);
		if (display.secondHand == true) {
			stay(secondHand);
		} else {
			stay(player);
		}
	}

	public static void split() {
		split = true;

		player.setHandValue(0); // this chunk correctly resets the hand values of both hands before the new
								// cards are added
		secondHand.setHandValue(0);
		player.addHandValue(player.getSecondCard().getValue());
		secondHand.addHandValue(player.getFirstCard().getValue());

		int xPos = 150;
		Card newCard = null;
		Player z = player;
		for (int y = 0; y < 2; y++) { // adds and displays the newly drawn cards
			newCard = PickRandCard();
			gui.displayCard(newCard.toString(), xPos, 400);
			z.addCards(newCard);
			z.addHandValue(newCard.getValue());
			xPos = 600;
			if (newCard.getValue() == 14) {// if the new picked card is an ace that is seen
				z.setPickedAce(true);
			}
			z = secondHand;
		}
		secondHand.setBet(player.getBet()); // basically doubles the bet and sets the cash correctly
		player.subBet();
		secondHand.setCurrentCash(player.getCurrentCash());
	}
}
