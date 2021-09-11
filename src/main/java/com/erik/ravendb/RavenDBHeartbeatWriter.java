package com.erik.ravendb;

import com.erik.config.ConfigurationApp;
import com.erik.model.Sensor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.session.ISessionDocumentTimeSeries;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.Instant;
import java.util.Date;

@Slf4j
public class RavenDBHeartbeatWriter extends  RavenDBWriter implements Runnable{
    public RavenDBHeartbeatWriter(ConfigurationApp properties, IDocumentStore ravendbConnection, String topic, MqttMessage message) {
        super(properties, ravendbConnection, topic, message);
    }
    @Override
    public void run() {

        log.info("Writing HeartBeatMessage");
        try (IDocumentSession session = ravendbConnection.openSession()) {
            ISessionDocumentTimeSeries timeSeries = session.timeSeriesFor(properties.getRavendbServerDocument(), topic);
            timeSeries.append(Date.from(Instant.now()), 1);
            session.saveChanges();
        } catch (Exception e) {
            log.error("There was a error at save data at ravendb ", e);
        }
    }


}
