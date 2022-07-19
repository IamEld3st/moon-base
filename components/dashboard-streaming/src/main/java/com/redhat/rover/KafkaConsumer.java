package com.redhat.rover;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class KafkaConsumer {

    @Inject
    CarEventSocket carEventSocket;
    @Inject
    CarMetricsSocket carMetricsSocket;
    @Inject
    ZoneChangeSocket zoneChangeSocket;

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumer.class.getName());

    @Incoming("rover-gps")
    public void consumeGps(String carEvent) {
        LOGGER.info("Received Kafka gps data:" + carEvent);
        carEventSocket.broadcast(carEvent);
    }

    @Incoming("rover-metrics")
    public void consumeMetrics(String metrics) {
        LOGGER.info("Received Kafka engine data:" + metrics);
        carMetricsSocket.broadcast(metrics);
    }

    @Incoming("rover-zonechange")
    public void consumeZoneChange(String zoneChangeEvent) {
        LOGGER.info("Received Kafka zone change event:" + zoneChangeEvent);
        zoneChangeSocket.broadcast(zoneChangeEvent);
    }

}


