package test;

import net.sourceforge.jnlp.cache.UpdatePolicy;
import localReputationManager.RepCollection;
import localReputationManager.RepUpdater;

public class LrmTest {

	public static void main(String[] args) {

		RepCollection repCollection = RepCollection.getInstance();

		repCollection.AddNode("192.168.0.5");
		repCollection.AddNode("192.168.0.6");
		repCollection.AddNode("192.168.0.7");
		repCollection.AddNode("192.168.0.8");

		System.out.println(repCollection);

		for (int i = 0; i < 2; i++) {

			for (int k = 0; k < 100; k++)
				RepUpdater.UpdateRep("192.168.0.5", true);

			System.out.println("\n***************\n");
			System.out.println(repCollection);
		}

		for (int i = 0; i < 2; i++) {

			for (int k = 0; k < 100; k++)
				RepUpdater.UpdateRep("192.168.0.5", false);

			System.out.println("\n***************\n");
			System.out.println(repCollection);
		}

	}
}
