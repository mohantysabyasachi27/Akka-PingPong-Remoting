package com.azuga.akkaping;

import com.azuga.common.Message;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * Hello world!
 *
 */
public class AkkaPing {

	static ActorSystem pingSystem;
	static ActorSelection pongSelection;
	static ActorRef pingActor;

	public AkkaPing() {
		pingSystem = ActorSystem.create("PingApplication", ConfigFactory.load().getConfig("PingConfig"));
		pongSelection = pingSystem.actorSelection("akka.tcp://PongApplication@127.0.0.1:5152/user/PongActor");
		System.out.println("*************Sending the first Ping***********");
		pongSelection.tell(new Message("sunny"), ActorRef.noSender());
		ActorRef pingActor = pingSystem.actorOf(Props.create(PingActor.class), "PingActor");

	}

	static class PingActor extends UntypedAbstractActor {
		public PingActor() {
			super();
		}

		@Override
		public void onReceive(Object message) {

			if (message instanceof Message) {
				Message recMsg = (Message) message;
				System.out.println("Received Message: " + recMsg.toString());
				pongSelection.tell(recMsg, getSelf());
			} else {
				System.out.println("UnHandled Message Received");
				unhandled(message);
			}
		}
	}

	public static void main(String[] args) {
		new AkkaPing();
	}
}
