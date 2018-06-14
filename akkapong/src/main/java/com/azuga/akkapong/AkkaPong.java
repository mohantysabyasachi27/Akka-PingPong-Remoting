package com.azuga.akkapong;

import com.azuga.common.Message;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class AkkaPong {
	static ActorSystem pongSystem;
	static ActorSelection pingSelection;
	static ActorRef pongActor;

	public AkkaPong() {
		pongSystem = ActorSystem.create("PongApplication", ConfigFactory.load().getConfig("PongConfig"));
		pingSelection = pongSystem.actorSelection("akka.tcp://PingApplication@127.0.0.1:5153/user/PingActor");
		pongActor = pongSystem.actorOf(Props.create(PongActor.class), "PongActor");

	}

	static class PongActor extends UntypedAbstractActor {

		public PongActor() {
			super();

		}

		@Override
		public void onReceive(Object message) {

			if (message instanceof Message) {
				Message recMsg = (Message) message;
				System.out.println("Received Message: " + recMsg.toString());
				pingSelection.tell(recMsg, getSelf());
			} else {
				System.out.println("UnHandled Message Received");
				unhandled(message);
			}
		}
	}

	public static void main(String[] args) {
		new AkkaPong();
	}
}
