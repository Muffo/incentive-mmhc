package test.localReputationManager;

import test.support.StringTxServer;


public class ServerTest {

	public static void main(String[] args) {

		StringTxServer strTx = new StringTxServer(false);
		Thread strTxThread = new Thread(strTx);
		strTxThread.start();

	}

}
