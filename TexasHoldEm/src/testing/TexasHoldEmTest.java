package testing;

import static org.junit.Assert.*;

import java.awt.event.WindowEvent;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.BeforeClass;
import org.junit.Test;

import userInterface.TexasHoldEm;

public class TexasHoldEmTest {
	private TexasHoldEm texasH;
	private StopWatch stopwatch;
	
	@Test
	public void testWindowCreationSpeed() {
		int sum = 0;
		stopwatch = new StopWatch();
		for (int i = 0; i < 10; i++){
			stopwatch.start();
			texasH = new TexasHoldEm();
			texasH.setVisible(true);
			stopwatch.stop();
//			texasH.dispatchEvent(new WindowEvent(texasH, WindowEvent.WINDOW_CLOSING));

			long millis = stopwatch.getTime();
			sum += millis;
			assertTrue(millis < 3000);
			System.out.println(millis);
			stopwatch.reset();
		}
		assertTrue((sum/10.0) < 3000.0);
		System.out.println((sum/10.0));
	}

}
