package com.erik.ravendb;


import com.erik.config.ConfigurationApp;
import com.erik.config.Constants;
import com.erik.model.Sensor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.session.ISessionDocumentTimeSeries;

import java.time.Instant;
import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class RavenDBHandler {
    public static class Singleton {
        public static IDocumentStore getConnection(ConfigurationApp properties) {
            log.info(Constants.ANSI_PURPLE + "Starting RavendDB handler" + Constants.ANSI_RESET);
            IDocumentStore store = null;
            try {
                store = new DocumentStore(new String[]{properties.getRavendbServerUrl()}, properties.getRavendbServerDatabase());

                store.initialize();
            } catch (Exception e) {
                log.error("There was a error at open ravendb session", e);
            }
            return store;
        }

        public static void writeSensorTemperature(ConfigurationApp properties, IDocumentStore store, Sensor sensor) {

            try (IDocumentSession session = store.openSession()) {
                ISessionDocumentTimeSeries timeSeries = session.timeSeriesFor(properties.getRavendbServerDocument(), sensor.getId());
                timeSeries.append(Date.from(Instant.now()), sensor.getValue());
                session.saveChanges();
            } catch (Exception e) {
                log.error("There was a error at save data at ravendb ", e);
            }
        }
    }

}
