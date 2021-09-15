package com.erik.jobs;

import com.erik.config.ConfigurationApp;
import com.erik.config.DeviceRegistry;
import com.erik.ravendb.RavenDBHandler;
import com.erik.ravendb.RavenDBHeartbeatWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.ravendb.client.documents.IDocumentStore;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.nio.charset.StandardCharsets;

@Slf4j
public class HeartbeatJob implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("Running Job");
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        ConfigurationApp properties = (ConfigurationApp) dataMap.get("config");
        IDocumentStore ravendbConnection = RavenDBHandler.Singleton.getConnection(properties);

        IMqttClient client = DeviceRegistry.getMqttClient();
        String heartbeaTopicListen = buildHeartbeatURL(properties.getMqttServerHeartbeat(),properties.getMqttServerSeparator());



                client.subscribe(properties.getMqttServerHeartbeat(), (topic, message) -> {
                    log.info("Received operation " + topic);
                    try {
                        new Thread(RavenDBHeartbeatWriter.
                                builder().
                                ravendbConnection(ravendbConnection).
                                properties(properties).
                                topic(topic).
                                message(message).build(), "RavenDBHeartbeatWriter").start();


                    } catch (Exception e) {
                        log.error("Error at mqtt handler message", e);
                    }
                });




        DeviceRegistry.getMqttDevicesList().forEach(sensor -> {
            String heartbeatUrlIN = heartbeaTopicListen + properties.getMqttServerSeparator()+ sensor;

            try {
                log.info("Try send heartbeat message");

                client.publish(heartbeatUrlIN, new MqttMessage("1".getBytes(StandardCharsets.UTF_8)));

            } catch (MqttException e) {
                log.error("Error at send message heartbeat",e);
            }

        });

    }



    String buildHeartbeatURL(String baseURL,String separator ){
        String[] split = baseURL.split(separator);
        String heartbeatUrlBase = split[0]+separator+split[1];
        return heartbeatUrlBase;
    }


}
