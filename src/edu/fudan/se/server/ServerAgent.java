/**
 * 
 */
package edu.fudan.se.server;

import java.io.IOException;
import java.util.ArrayList;

import edu.fudan.se.agent.data.RequestUserInformation;
import edu.fudan.se.agent.data.UserInformation;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * @author whh
 * 
 */
public class ServerAgent extends Agent {

	private static final long serialVersionUID = 2967345614611158855L;

	private ArrayList<UserInformation> userInformationList = new ArrayList<>();

	@Override
	protected void setup() {
		super.setup();

		addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 301235591946207292L;

			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					String content = msg.getContent();
					if (msg.getPerformative() == ACLMessage.INFORM) {// 更新位置信息
						String[] location = content.split("---");
						updateUserLocation(location[0], location[1]);
					}
					if (msg.getPerformative() == ACLMessage.CFP) {// 获取位置信息

						ACLMessage reply = msg.createReply();
						reply.setPerformative(ACLMessage.PROPOSE);

						// String[] item = content.split("###"); // myGoal#bob
						String goalModelName = content.split("#")[0];
						String elementName = content.split("#")[1];
						RequestUserInformation rui = null;
						// if (item.length > 1) { // 也就是有friend nick name传过来
						// String[] friends = item[1].split("---");
						// rui = new RequestUserInformation(goalModelName,
						// elementName,
						// getFriendsUserInformations(friends));
						// } else {// 没有friend nick name传过来，把原消息内容返回
						// rui = new RequestUserInformation(goalModelName,
						// elementName, null);
						// }
						// 返回所有用户信息
						rui = new RequestUserInformation(goalModelName,
								elementName, userInformationList);
						try {
							reply.setContentObject(rui);
						} catch (IOException e) {
							e.printStackTrace();
						}
						send(reply);
					}

				}
			}
		});
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	/**
	 * 更新存储的用户位置，如果是新用户，就新建一个UserInformation
	 * 
	 * @param userAgentNickname
	 *            用户agent nickname
	 * @param location
	 *            用户位置信息
	 */
	private void updateUserLocation(String userAgentNickname, String location) {
		System.out.println("location: " + location);
		boolean isNewUse = true;
		for (UserInformation userInformation : userInformationList) {
			if (userInformation.getUserAgentNickname()
					.equals(userAgentNickname)) {
				userInformation.setLocation(location);
				isNewUse = false;
				break;
			}
		}

		if (isNewUse) {
			UserInformation userInformation = new UserInformation(
					userAgentNickname, location, 1);
			userInformationList.add(userInformation);
		}
	}

	private ArrayList<UserInformation> getFriendsUserInformations(
			String[] friends) {
		ArrayList<UserInformation> userInformations = new ArrayList<>();

		for (int i = 0; i < friends.length; i++) {
			for (UserInformation userInformation : userInformationList) {
				if (userInformation.getUserAgentNickname().equals(friends[i])) {
					userInformations.add(userInformation);
					break;
				}
			}
		}
		return userInformations;
	}

}
