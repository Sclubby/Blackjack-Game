package Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class display extends JFrame implements ActionListener {
	private JButton hit = new JButton("HIT");
	private JButton stay = new JButton("STAY");
	private JButton dd = new JButton("Double Down");
	private JButton split = new JButton("SPLIT");
	private JButton reset = new JButton("RESET");
	private JButton bet = new JButton("BET");
	private JButton[] buttons = { hit, stay, dd, split, reset };
	private JLabel cardTotal = new JLabel();
	private JLabel cardTotal2 = new JLabel();
	private JLabel dealerCardTotal = new JLabel();
	private JLabel moneyAmount = new JLabel();
	private JLabel betAmount = new JLabel();
	private JLabel hiddenCard = new JLabel();
	private JLabel winnerText = new JLabel();
	private JLabel winnerText2 = new JLabel();
	private JLabel errorText = new JLabel();
	private JLabel CardIndicator = new JLabel();// used to show which cards are in use during a split
	private JLabel[] labels = { cardTotal, moneyAmount, betAmount, CardIndicator };
	private JTextField betInput = new JTextField("bet", 30);
	public static int numOfHits = 0;
	public static boolean secondHand = false;
	private ArrayList<Component> displayedCards = new ArrayList<Component>(); // list of every card displayed

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == hit) {
			numOfHits++;
			if (secondHand == true) {
				Blackjack.hit(numOfHits, Blackjack.secondHand);
			} else {
				Blackjack.hit(numOfHits, Blackjack.player);
			}
		} else if (e.getSource() == stay) {
			if (Blackjack.split == false || secondHand == true) { // stay is removed unless a split happened
				stay.setVisible(false);
			}
			if (secondHand == true) {
				Blackjack.stay(Blackjack.secondHand);
			} else {
				Blackjack.stay(Blackjack.player);
			}
		} else if (e.getSource() == reset) {
			numOfHits = 0;
			toggleReset(0);
			Blackjack.reset();
		} else if (e.getSource() == bet) {
			changeIndicator(0);
			int betAmount = Integer.parseInt(betInput.getText());
			if (betAmount <= Blackjack.player.getCurrentCash()) {
				CardIndicator.setVisible(true);
				errorText.setVisible(false);
				System.out.println("Bet = " + betAmount);
				System.out.println("Cash = " + Blackjack.player.getCurrentCash());
				Blackjack.player.setBet(betAmount);
				Blackjack.dealCards();
				Blackjack.playerChoice(betAmount);
			} else {
				errorText.setText("That is an invalid bet");
				errorText.setVisible(true);
			}
		} else if (e.getSource() == dd) {
			numOfHits++;
			if (secondHand == true) {
				Blackjack.hit(numOfHits, Blackjack.secondHand);
			} else {
				Blackjack.hit(numOfHits, Blackjack.player);
			}
			Blackjack.doubleDown();
		} else if (e.getSource() == split) {
			numOfHits++;
			splitDisplay();
		}
	}

	private void splitDisplay() {
		for (int y = 0; y < displayedCards.size() - 3; y++) { // all cards but dealers disappear
			displayedCards.get(y).setVisible(false);
		}
		displayCard(Blackjack.player.getFirstCard().toString(), 650, 400);
		displayCard(Blackjack.player.getSecondCard().toString(), 200, 400);
		Blackjack.split();
		changeIndicator(1);
		cardTotal2.setBounds(650, 350, 300, 25);
		cardTotal.setBounds(200, 350, 300, 25);
		cardTotal2.setFont(new Font("Serif", Font.PLAIN, 30));
		cardTotal2.setForeground(Color.red);
		cardTotal2.setText("" + Blackjack.secondHand.getHandValue());
		add(cardTotal2);
		cardTotal2.setVisible(true);
		resetdisplayValues(Blackjack.player);
	}

	public void createJpanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		setLayout(null);
		setVisible(true);
		setTitle("Black Jack");
		int y = 50;
		for (JButton x : buttons) {
			x.setBounds(y, 650, 100, 100);
			add(x);
			x.setVisible(false);
			y = y + 200;
			x.addActionListener(this);
		}
		for (JLabel x : labels) {
			x.setForeground(Color.red);
			add(x);
		}
		moneyAmount.setBounds(50, 50, 300, 25);
		moneyAmount.setFont(new Font("Serif", Font.PLAIN, 30));
		betInput.setBounds(300, 10, 75, 75);
		dealerCardTotal.setBounds(360, 60, 300, 25);
		dealerCardTotal.setFont(new Font("Serif", Font.PLAIN, 30));
		dealerCardTotal.setForeground(Color.red);
		add(dealerCardTotal);
		cardTotal.setBounds(360, 365, 300, 25);
		cardTotal.setFont(new Font("Serif", Font.PLAIN, 30));
		betAmount.setBounds(750, 50, 400, 40);
		betAmount.setFont(new Font("Serif", Font.PLAIN, 30));
		add(betInput);
		moneyAmount.setText("$1000 in the bank");
		moneyAmount.setVisible(true);
		bet.setBounds(350, 10, 75, 75);
		bet.addActionListener(this);
		add(bet);
		winnerText.setFont(new Font("Serif", Font.PLAIN, 40));
		winnerText.setBounds(550, 100, 800, 200);
		winnerText.setForeground(Color.red);
		winnerText2.setFont(new Font("Serif", Font.PLAIN, 40));
		winnerText2.setBounds(550, 200, 800, 200);
		winnerText2.setForeground(Color.red);
		reset.setVisible(false);
		errorText.setFont(new Font("Serif", Font.PLAIN, 40));
		errorText.setForeground(Color.red);
		errorText.setBounds(600, 100, 800, 200);
		add(errorText);
		errorText.setVisible(false);
		CardIndicator.setFont(new Font("Serif", Font.PLAIN, 45));
		CardIndicator.setForeground(Color.red);
		CardIndicator.setBounds(300, 550, 300, 100);
		CardIndicator.setText("_________");
		CardIndicator.setVisible(false);
	}

	public void toggleReset(int x) {
		if (x == 1) {
			for (JButton y : buttons) {
				y.setVisible(false);
			}
			reset.setVisible(true);
		} else {
			reset.setVisible(false);
			cardTotal2.setVisible(false);
		}
	}

	public void resetdisplayValues(Player x) {
		for (JButton y : buttons) {
			y.setVisible(true);
		}
		if (Blackjack.split == true) {//dont display split if the player already pressed the button
			split.setVisible(false);
		}
		reset.setVisible(false);
		if (Blackjack.player.getBet() > (Blackjack.player.getCurrentCash() + Blackjack.player.getBet()) / 2
				|| numOfHits > 0) {
			dd.setVisible(false);
		} // if bet is larger than half of the money the player is the player cannot
			// double down
		if (Blackjack.player.checkForSplit() == false) { split.setVisible(false); }
		for (JLabel y : labels) {
			y.setVisible(true);
		}
		bet.setVisible(false);
		betInput.setVisible(false);
		JLabel z = null;
		if (x != Blackjack.dealer) {
			if (x.getHandValue() <= 21) {// if its a bust wont change to num
				if (x == Blackjack.player) { // changes the jlabel depending on which hand is being played
					z = cardTotal;
				} else {
					z = cardTotal2;
				}
				if (x.getPickedAce() == true) {
					z.setText("" + x.getHandValue() + "/" + (x.getHandValue() - 10));
				} else {
					z.setText("" + x.getHandValue());
				}
			}
			betAmount.setText("Bet: " + x.getBet());
			moneyAmount.setText("$" + x.getCurrentCash() + " in the bank");
		} else {
			if (x.getHandValue() <= 21) {
			dealerCardTotal.setText("" + x.getHandValue());
			}
		}
	}

	public void betting(Player x) {
		cardTotal.setBounds(360, 365, 300, 25);
		secondHand = false;
		dealerCardTotal.setVisible(false);
		winnerText.setVisible(false);
		winnerText2.setVisible(false);
		for (JButton y : buttons) {
			y.setVisible(false);
		}
		bet.setVisible(true);
		betInput.setVisible(true);
		for (JLabel y : labels) {
			y.setVisible(false);
		}
		moneyAmount.setVisible(true);
		for (Component y : displayedCards) {
			y.setVisible(false);
		}
	}

	private ImageIcon createImg(String path) {
		ImageIcon temp = new ImageIcon(getClass().getResource(path));
		return temp;
	}

	public void displayCard(String Card, int x, int y) { // used whenever a new card is displayed
		JLabel label1 = new JLabel();
		label1.setBounds(x, y, 120, 210);
		ImageIcon image1 = createImg("/res/" + Card + ".png");
		Image scaledImage1 = image1.getImage();
		ImageIcon finalImage = new ImageIcon(
				scaledImage1.getScaledInstance(label1.getWidth(), label1.getHeight(), Image.SCALE_FAST));
		label1.setIcon(finalImage);
		if (Card.equals("red_back")) {
			hiddenCard = label1;
			add(hiddenCard);
			displayedCards.add(label1);
		} else {
			add(label1);
			displayedCards.add(label1);
		}
		repaint();
	}

	public void bust(Player y) {
		if (Blackjack.split == true && y != Blackjack.dealer) {// if split occured
			if (y == Blackjack.player) {// if first pair is a bust
				numOfHits = 0;
				changeIndicator(2);
				cardTotal.setText("BUST");
			} else { // second pair is a bust
				cardTotal2.setText("Bust");
			}
		} else { // normal bust with no split
			if (y == Blackjack.dealer) {
				dealerCardTotal.setText("Bust");
			} else {
				cardTotal.setText("Bust");
			}
		}
	}

	public void revealDealerCard() {
		hiddenCard.setVisible(false);
		dealerCardTotal.setVisible(true);
	}

	public void winDisplay(int i, int pair) {
		JLabel winner = winnerText;
		String splitText = "left - ";
		String text = null;
		if (pair == 2) {
			splitText = "right - ";
			winner = winnerText2;
		}
		if (i == 0) {
			text = "YOU WON +$" + Blackjack.player.getBet();
		} else if (i == 1) {
			if (pair >= 1) { // if the player split then it will display that the player lost half the bet if
								// that hand lost
				text = "Dealer Wins -$" + Blackjack.player.getBet();
			} else {
				text = "Dealer Wins -$" + Blackjack.player.getBet();
			}
		} else if (i == 3) {
			text = "BlackJack +$" + Blackjack.player.getBet();
		} else {
			text = "Tie";
		}
		if (pair >= 1) {
			text = splitText + text;
		}
		winner.setText(text);
		add(winnerText);
		moneyAmount.setText("$" + Blackjack.player.getCurrentCash() + " in the bank");
		winnerText.setVisible(true);
		if (pair == 2) {
			winnerText2.setVisible(true);
			add(winnerText2);
		}
		toggleReset(1);
	}

	public void broke() {
		errorText.setBounds(50, 100, 1000, 200);
		errorText.setText("Out of money, Gambling is an addiction get some help");
		errorText.setVisible(true);
		betting(Blackjack.player);
		betInput.setVisible(false);
		bet.setVisible(false);
	}

	public void changeIndicator(int x) {
		if (x == 0) {
			CardIndicator.setBounds(300, 550, 300, 100);
		} else if (x == 1) {
			CardIndicator.setBounds(150, 550, 300, 100);
		} else {
			CardIndicator.setBounds(600, 550, 300, 100);
			secondHand = true;
		}

	}
}
