package com.erik.ravendb;

import com.erik.config.ConfigurationApp;
import com.erik.model.Sensor;
import com.erik.model.SensorField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.session.ISessionDocumentTimeSeries;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;


/** This class process the received mqtt message at a isolated thread to allows the main flow process be free and write the sensor's value at ravenDB*/
@Data
@Builder
@AllArgsConstructor
@Slf4j
public class RavenDBWriter implements Runnable {

    ConfigurationApp properties;
    IDocumentStore ravendbConnection;
    String topic;
    MqttMessage message;

    @Override
    public void run() {
        final String payload = new String(message.getPayload());
        Sensor sensor = getSensor(properties, topic, payload);

        try (IDocumentSession session = ravendbConnection.openSession()) {
            ISessionDocumentTimeSeries timeSeries = session.timeSeriesFor(properties.getRavendbServerDocument(), sensor.getId());
            timeSeries.append(Date.from(Instant.now()), sensor.getValues());
            session.saveChanges();
        } catch (Exception e) {
            log.error("There was a error at save data at ravendb ", e);
        }

    }

    /**
     *
     * @param properties see {@link com.erik.config.ConfigurationApp}
     * @param topic MQTT received topic from mqtt message
     * @param payload value of sensor from mqtt message
     * @return a instance of Sensor, see {@link com.erik.model.Sensor}
     */
    private Sensor getSensor(ConfigurationApp properties, String topic, String payload) {
        Sensor sensor = Sensor.builder().build();
        try {
            String[] split = topic.split(properties.getMqttServerSeparator());
            String[] splitPayload = payload.split(properties.getMqttServerSeparator());

            sensor.setId(split[1].toLowerCase()+split[2]);
            sensor.setValues(Arrays.stream(splitPayload).mapToDouble(Double::parseDouble).toArray());
        } catch (Exception e) {
            log.error("Error at build sensor");
        }
        return sensor;
    }
}
