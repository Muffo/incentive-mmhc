package test.multipleNode;

import test.support.StringTxServer;

public class NodeC {

	public static void main(String[] args) {

		StringTxServer strTx = new StringTxServer(true);
		Thread strTxThread = new Thread(strTx);
		strTxThread.start();
	}
}
