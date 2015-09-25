/**
 * 
 */
package edu.fudan.se.server;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * @author whh
 *
 */
public class StartServerAgent {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AgentController serverAgent = newContainer().createNewAgent("ServerAgent", "edu.fudan.se.server.ServerAgent", args);
			System.out.println("create new agent!");
			serverAgent.start();
			System.out.println("server agent start!");
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static AgentContainer newContainer() {

		Runtime rt = Runtime.instance();// 鑾峰彇jade杩愯鏃�

		// 鍒涘缓涓�涓厤缃枃浠讹紝鍙傛暟鏄鍔犲叆鐨勫钩鍙扮殑鍙傛暟
		Profile profile = new ProfileImpl("127.0.0.1", 1099, null, false);

		System.out.println("Launching the agent container ..." + profile);
		// 鏍规嵁profile鍐呭鍒涘缓涓�涓猚ontainer锛屽姞鍏ュ埌profile鎸囧畾鐨勫钩鍙�
		AgentContainer newContainer = rt.createAgentContainer(profile);

		return newContainer;
	}

}
