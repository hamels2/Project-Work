package userInterface;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import com.sun.glass.events.KeyEvent;

import controller.AIOpponent;
import controller.Game;
import controller.Pot;
import model.Player;


public class TexasHoldEm extends JFrame implements ActionListener  {
	private ImageIcon background;
	private Player player1 = null;
	private Player player2 = null;
	private AIOpponent AIplayer;
	private Game game;
	private Pot pot;
	
	private static TexasHoldEm board;
	private JMenuBar menuBar;
	private JMenu menu, aboutMenu;
	private JMenuItem newGameItem, saveItem, loadItem, exitItem, infoItem, ruleItem;

	private JPanel dealerPanel, communityPanel, btnPanel, playerPanel;
	private JButton betBtn, callBtn, dealBtn, foldBtn;
	private JLabel lblDealer, lblCommunity, lblPlayerCards, lblPlayer1Chips, lblPlayer2Chips, lblPot, lblConsole;
	private JLabel[] playerCards, dealerCards, communityCards;
	private JTextField betField;
	private JTextArea console;
	private JScrollPane scroll;
	private Border sunkIn = BorderFactory.createBevelBorder(BevelBorder.LOWERED); 
	
	private boolean dealt = false;
	private int communityCardCount = 0;
	private int communityWidth = 5;
	
	
	//constructor
	public TexasHoldEm(){
		super("Texas Hold Em");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(590,430);
		setLocationRelativeTo(null);
		setBackground(Color.GRAY);
		setResizable(false);
		
		background = new ImageIcon("src/res/Cards/b.gif");
		
		player1 = new Player();
		
		//2 player game
//		player2 = new Player();
//		game = new Game(player1, player2, this);
		
		//AI game
		AIplayer = new AIOpponent();
		game = new Game(player1, AIplayer, this);
	
		pot = game.getPot();
		
		addMenu();
		addPanel();
		
		//add the components onto the frame
		setJMenuBar(menuBar);
		getContentPane().setLayout(null);
		getContentPane().add(lblDealer);
		getContentPane().add(dealerPanel);
		getContentPane().add(lblCommunity);
		getContentPane().add(communityPanel);
		getContentPane().add(btnPanel);
		getContentPane().add(playerPanel);
		getContentPane().add(scroll);
		getContentPane().add(lblConsole);
		getContentPane().add(lblPlayerCards);
		getContentPane().add(lblPlayer2Chips);
		getContentPane().add(lblPlayer1Chips);
		getContentPane().add(lblPot);
		trackCommunity();
		
	}
	
	/**
	 * Method to initialize and setup the menu bar along with the menu items within
	 */
	private void addMenu(){
		menuBar = new JMenuBar();
		
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		menuBar.add(menu);
		
		aboutMenu = new JMenu("About");
		aboutMenu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(aboutMenu);
		
		newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.addActionListener(this);
		menu.add(newGameItem);
		
		menu.addSeparator();
		
//		saveItem = new JMenuItem("Save", KeyEvent.VK_S);
//		saveItem.addActionListener(this);
//		menu.add(saveItem);
//		
//		loadItem = new JMenuItem("Load", KeyEvent.VK_L);
//		loadItem.addActionListener(this);
//		menu.add(loadItem);
		
//		menu.addSeparator();
		
		exitItem = new JMenuItem("Exit", KeyEvent.VK_E);
		exitItem.addActionListener(this);
		menu.add(exitItem);
		
		infoItem = new JMenuItem("App Info", KeyEvent.VK_I);
		infoItem.addActionListener(this);
		aboutMenu.add(infoItem);
		
		ruleItem = new JMenuItem("Game Rules", KeyEvent.VK_R);
		ruleItem.addActionListener(this);
		aboutMenu.add(ruleItem);
	}
	
	/**
	 * Method that adds the components for player, community and dealer
	 */
	private void addPanel(){
		lblDealer = new JLabel();
		lblCommunity = new JLabel();
		
		//create panel to hold hold buttons
		btnPanel = new JPanel(); 
		btnPanel.setLayout(new GridLayout(1, 5));
		btnPanel.setBorder(sunkIn);
		
		//create buttons for the button panel
		dealBtn = new JButton("Deal");
		dealBtn.addActionListener(this);
		btnPanel.add(dealBtn);
		
		callBtn = new JButton("Call/Check");
		callBtn.addActionListener(this);
		callBtn.setEnabled(false);
		btnPanel.add(callBtn);
		
		foldBtn = new JButton("Fold");
		foldBtn.addActionListener(this);
		foldBtn.setEnabled(false);
		btnPanel.add(foldBtn);
		
		betBtn = new JButton("Bet");
		betBtn.addActionListener(this);
		betBtn.setEnabled(false);
		btnPanel.add(betBtn);
		
		betField = new JTextField("0");
		btnPanel.add(betField);
		
		//create a dealer panel to show dealer's card back
		dealerPanel = new JPanel();
		dealerPanel.setLayout(null);
		dealerPanel.setBorder(sunkIn);
		
		dealerPanel.setBounds(5, 30, 161, 107);
		
		lblDealer.setText("Dealer Cards");
		lblDealer.setHorizontalAlignment(JLabel.CENTER);
		lblDealer.setBounds(5, 5, 161, 20);
		
		dealerCards = new JLabel[2];
		
		//create community panel that shows the community cards
		communityPanel = new JPanel();
		communityPanel.setBorder(sunkIn);
		communityPanel.setBounds(181, 30, 395, 107);
		
		lblCommunity.setText("Community Cards");
		lblCommunity.setHorizontalAlignment(JLabel.CENTER);
		lblCommunity.setBounds(166, 5, 395, 20);

		btnPanel.setBounds(5, 164, 571, 30);

		//create a player panel that shows the player's cards
		playerPanel = new JPanel();
		playerPanel.setLayout(null);
		playerPanel.setBorder(sunkIn);
		playerPanel.setBounds(339, 228, 161, 107);
		
		lblPlayerCards = new JLabel("Player Cards");
		lblPlayerCards.setBounds(386, 205, 79, 14);

		playerCards = new JLabel[2];

		communityCards = new JLabel[5];
		
		//console 
		console = new JTextArea();
		console.setEditable(false);
		
		lblConsole = new JLabel("Console");
		lblConsole.setBounds(103, 203, 46, 14);
		
		scroll = new JScrollPane(console);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll.setBounds(10, 228, 234, 107);
		
		//chip and pot labels
		lblPlayer2Chips = new JLabel("Chips: ");
		lblPlayer2Chips.setBounds(15, 139, 116, 14);
		
		lblPlayer1Chips = new JLabel("Chips: ");
		lblPlayer1Chips.setBounds(377, 341, 88, 14);
		
		lblPot = new JLabel("Pot: ");
		lblPot.setBounds(300, 139, 155, 14);
	}
	
	/**
	 * Method to set icons for the player's cards
	 */
	private void createPlayerCards(){
		playerPanel.removeAll();
		ImageIcon playerFirstCardIcon = new ImageIcon(player1.getHand().get(0).getFileName());
		ImageIcon playerSecondCardIcon = new ImageIcon(player1.getHand().get(1).getFileName());
		playerCards[0] = new JLabel();
		playerCards[0].setIcon(playerFirstCardIcon);
		playerCards[1] = new JLabel();
		playerCards[1].setIcon(playerSecondCardIcon);
		playerCards[0].setBounds(5, 5, playerFirstCardIcon.getIconWidth(), playerFirstCardIcon.getIconHeight());
		playerCards[1].setBounds(playerFirstCardIcon.getIconWidth() + 10, 5, playerSecondCardIcon.getIconWidth(), playerSecondCardIcon.getIconHeight());
		playerPanel.add(playerCards[0]);
		playerPanel.add(playerCards[1]);
		playerPanel.updateUI();
	}
	
	/**
	 * Method that sets the dealer's cards' icons
	 */
	private void createDealerCards() {
		dealerPanel.removeAll();
		String imagePath = "src/res/Cards/back.gif";
		ImageIcon dealerFirstCardIcon = new ImageIcon(imagePath);
		ImageIcon dealerSecondCardIcon = new ImageIcon(imagePath);
		dealerCards[0] = new JLabel();
		dealerCards[0].setIcon(dealerFirstCardIcon);
		dealerCards[1] = new JLabel();
		dealerCards[1].setIcon(dealerSecondCardIcon);
		dealerCards[0].setBounds(5, 5, dealerFirstCardIcon.getIconWidth(), dealerFirstCardIcon.getIconHeight());
		dealerCards[1].setBounds(dealerFirstCardIcon.getIconWidth() + 10, 5, dealerSecondCardIcon.getIconWidth(), dealerSecondCardIcon.getIconHeight());
		dealerPanel.add(dealerCards[0]);
		dealerPanel.add(dealerCards[1]);
		dealerPanel.updateUI();
	}
	
	/** 
	 * Method to set the community cards' icons
	 */
	public void createCommunityCard(){
		communityCards[communityCardCount] = new JLabel();
		communityCards[communityCardCount].setVisible(false);
		ImageIcon communityCardIcon = new ImageIcon(player1.getHand().get(communityCardCount + 2).getFileName());
		communityCards[communityCardCount].setIcon(communityCardIcon);
		communityCards[communityCardCount].setBounds(communityWidth, 5, communityCardIcon.getIconWidth(), communityCardIcon.getIconHeight());
		communityPanel.add(communityCards[communityCardCount]);
		communityWidth += communityCards[communityCardCount].getWidth() + 5;
		communityCardCount++;
		communityPanel.updateUI();
	}
	
	/**
	 * This method is used to display errors and messages that are relevant to the user
	 * @param option int - The type of error
	 */
	public static void displayMessage(int option){
		switch (option){
		//0 represents raise error
		case 0:	JOptionPane.showMessageDialog(board, "Invalid input, try again", "Raise Error", JOptionPane.ERROR_MESSAGE); break;
		//1 represents incorrect button order
		case 1: JOptionPane.showMessageDialog(board, "Deal First", "Error", JOptionPane.ERROR_MESSAGE); break;	
		//2 represents negative value in bet field
		case 2: JOptionPane.showMessageDialog(board, "Cannot enter negative value", "Error", JOptionPane.ERROR_MESSAGE); break;
		//3 represents empty field
		case 3: JOptionPane.showMessageDialog(board, "Bet amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE); break;
		//4 represents that the dealer wins
		case 4: JOptionPane.showMessageDialog(board, "Dealer Wins!", "Winner!", JOptionPane.INFORMATION_MESSAGE); break;
		//5 represents that the player wins
		case 5: JOptionPane.showMessageDialog(board, "You Win!", "Winner!", JOptionPane.INFORMATION_MESSAGE); break;
		}
	}
	
	/**
	 * Method for outputting messages to the console
	 * @param s String - Text to be shown on console
	 */
	public void log(String s){
		console.append(s + "\n");
	}
	
	/**
	 * Method resets the graphic components
	 */
	public void reset(){
		for (int i=0; i < 2; i++){
			dealerCards[i] = null;
			playerCards[i] = null;
		}
		
		for (int i=5; i < 5; i++){
			communityCards[i] = null;
		}
		
		communityCardCount = 0;
		communityPanel.removeAll();
		
		dealt = false;
		dealBtn.setEnabled(true);
		callBtn.setEnabled(false);
		foldBtn.setEnabled(false);
		betBtn.setEnabled(false);
	}
	
	/**
	 * This method reveals the community cards
	 */
	public void showCommunity(){
		int i = 0;
		while (communityCards[i] != null && i < 5){
			communityCards[i++].setVisible(true);
			if (i>=5) return;
		}
		
	}
	
	/**
	 * Method creates every card in community and display it
	 */
	public void showAll(){
		for (int i = player1.getHand().size(); i <= 7 ; i++){
			createCommunityCard();
		}
	}
	
	/**
	 * Method that updates the chip and pot labels
	 */
	public void updateChipLabels(){
		lblPlayer2Chips.setText("Chips: " + game.getChipsP2());
		lblPot.setText("Pot: " + pot.getPot());
		lblPlayer1Chips.setText("Chips " + game.getChipsP1());
	}
	
	/**
	 * Method flips dealer's cards to reveal the dealer's hand
	 */
	public void showDealerCard(){
		dealerPanel.removeAll();
		ImageIcon dealerFirstCardIcon = new ImageIcon(AIplayer.getHand().get(0).getFileName());
		ImageIcon dealerSecondCardIcon = new ImageIcon(AIplayer.getHand().get(1).getFileName());
		dealerCards[0] = new JLabel();
		dealerCards[0].setIcon(dealerFirstCardIcon);
		dealerCards[1] = new JLabel();
		dealerCards[1].setIcon(dealerSecondCardIcon);
		dealerCards[0].setBounds(5, 5, dealerFirstCardIcon.getIconWidth(), dealerFirstCardIcon.getIconHeight());
		dealerCards[1].setBounds(dealerFirstCardIcon.getIconWidth() + 10, 5, dealerSecondCardIcon.getIconWidth(), dealerSecondCardIcon.getIconHeight());
		dealerPanel.add(dealerCards[0]);
		dealerPanel.add(dealerCards[1]);
		dealerPanel.updateUI();
	}
	
	/**
	 * Method updates the bet button label
	 * @param t String - the text to set on the button
	 */
	public void changeBetBtnLabel(String t){
		betBtn.setText(t);
		betBtn.updateUI();
	}
	/**
	 * Method that keeps track of how many community cards are currently on the board
	 * When the card count reaches 5, end the round and evaluate both player's hands
	 */
	public void trackCommunity(){
		Thread th = new Thread(){
			@Override
			public void run(){
				while(true){
					if (communityCardCount == 5 && game.isTurnEnd()) game.evaluateRound();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		th.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == infoItem){
			AboutFrame abtFrm= new AboutFrame('h');
			abtFrm.setVisible(true);
		}else if (e.getSource() == ruleItem){
			AboutFrame abtFrm = new AboutFrame('r');
			abtFrm.setVisible(true);
		}else if (e.getSource() == newGameItem){
			game.startNewGame();
			log("New game");
			updateChipLabels();
			dealerPanel.removeAll();
			playerPanel.removeAll();
			communityPanel.updateUI();
			dealerPanel.updateUI();
			playerPanel.updateUI();
		}else if (e.getSource() == exitItem){
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else{
			if (e.getSource() == dealBtn){
				game.deal();
				communityCardCount = 0;
				createPlayerCards();
				createDealerCards();
				createCommunityCard();
				createCommunityCard();
				pot.ante();
				updateChipLabels();
				dealt = true;
				dealBtn.setEnabled(false);
				callBtn.setEnabled(true);
				foldBtn.setEnabled(true);
				betBtn.setEnabled(true);
			}
			else if (!dealt) {
				TexasHoldEm.displayMessage(1);
			}
			else{
				if (e.getSource() == callBtn){
					pot.call();
					communityPanel.updateUI();
				}else if (e.getSource() == betBtn){
					try{
						pot.raise(Integer.parseInt(betField.getText()));
					}catch (Exception ex){
						displayMessage(3);
					}
					communityPanel.updateUI();
				}else if (e.getSource() == foldBtn){
					pot.fold();
					reset();
				}
			}
		}
	}
	
	public static void main(String args[]){
		board = new TexasHoldEm();
		board.setVisible(true);
	}
}
