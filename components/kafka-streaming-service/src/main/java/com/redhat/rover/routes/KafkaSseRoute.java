package com.redhat.rover.routes;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.vertx.websocket.VertxWebsocketConstants;

public class KafkaSseRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {

		// expose Rover GPS positions from Kafka as Websocket
		from("kafka:{{com.redhat.rover.camelk.kafka.topic.gps}}?clientId=kafkaSseCamelClientx&brokers={{com.redhat.rover.camelk.kafka.brokers}}")
			//.log("GPS position received from Kafka : ${body}")
			.setHeader(VertxWebsocketConstants.SEND_TO_ALL, constant("true"))
			//.log(simple("${headers[CamelVertxWebsocket.sendToAll]}").toString())
			.to("vertx-websocket://0.0.0.0:8080/api/carevents?lazyStartProducer=true");
		 from("vertx-websocket://0.0.0.0:8080/api/carevents?")
			.log("GPS position received from Websocket : ${body}");

		// expose Rover engine metrics from Kafka as Websocket
		from("kafka:{{com.redhat.rover.camelk.kafka.topic.metrics}}?clientId=kafkaSseMetricsClientx&brokers={{com.redhat.rover.camelk.kafka.brokers}}")
			.log("Metric received from Kafka : ${body}")
			.setHeader(VertxWebsocketConstants.SEND_TO_ALL, constant(true))
			.to("vertx-websocket://0.0.0.0:8080/api/carmetrics");
		from("vertx-websocket://0.0.0.0:8080/api/carmetrics")
			.log("Metric received from Websocket : ${body}");

		// expose Rover zone change events from Kafka as Websocket
		from("kafka:{{com.redhat.rover.camelk.kafka.topic.zonechange}}?clientId=kafkaSseZoneChangeClientx&brokers={{com.redhat.rover.camelk.kafka.brokers}}")
			.log("Zone change event received from Kafka : ${body}")
			.setHeader(VertxWebsocketConstants.SEND_TO_ALL, constant(true))
			.to("vertx-websocket://0.0.0.0:8080/api/zonechange");
		from("vertx-websocket://0.0.0.0:8080/api/zonechange")
			.log("Zone change event received from Websocket : ${body}");
		
	}
	
	
}
