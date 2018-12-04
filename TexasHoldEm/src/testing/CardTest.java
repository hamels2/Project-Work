package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Card;

public class CardTest {
	Card a = new Card(4,'C');
	Card b = new Card(9,'D');
	Card c = new Card(12,'S');
	Card d = new Card(4,'H');
	
	@Test
	public void compareCardRanks() {
		assertEquals("Different ranked cards",-1,a.compareTo(b));
		assertEquals("Different ranked cards",-1,b.compareTo(c));
		assertEquals("Different ranked cards",-1,a.compareTo(c));
		assertEquals("Different ranked cards",0,d.compareTo(a));
		}
	
	@Test
	public void getRankTest(){
		assertEquals("4",4,a.getRank());
		assertEquals("9",9,b.getRank());
		assertEquals("12",12,c.getRank());
		assertEquals("4",4,d.getRank());
	}
	
	@Test
	public void getSuitTest(){
		assertEquals("Clubs",'C',a.getSuit());
		assertEquals("Diamonds",'D',b.getSuit());
		assertEquals("Spades",'S',c.getSuit());
		assertEquals("Hearts",'H',d.getSuit());
	}
	
	@Test
	public void rankToStringTest(){
		assertEquals("4","Four",a.rankToString());
		assertEquals("9","Nine",b.rankToString());
		assertEquals("12","Queen",c.rankToString());
		assertEquals("4","Four",d.rankToString());
	}
	
	@Test
	public void suitToStringTest(){
		assertEquals("Clubs","Clubs",a.suitToString());
		assertEquals("Diamonds","Diamonds",b.suitToString());
		assertEquals("Spades","Spades",c.suitToString());
		assertEquals("Hearts","Hearts",d.suitToString());
	}

}
