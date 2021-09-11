package com.erik.ravendb;


import com.erik.config.ConfigurationApp;
import com.erik.config.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;

/**
 * Management the connection at RavenDB
 */
@Getter
@Builder
@Slf4j
public class RavenDBHandler {
    private RavenDBHandler(){
        //
    }
        public static class Singleton {
        private Singleton(){}
        public static IDocumentStore getConnection(ConfigurationApp properties) {
            log.info(Constants.ANSI_PURPLE + "Starting RavendDB handler" + Constants.ANSI_RESET);
            IDocumentStore store = null;
            try {
                store = new DocumentStore(new String[]{properties.getRavendbServerUrl()}, properties.getRavendbServerDatabase());

                store.initialize();
                log.info(Constants.ANSI_GREEN + "Success at starting RavenDB handler"+ Constants.ANSI_RESET);

            } catch (Exception e) {
                log.error("There was a error at open ravendb session", e);
            }
            return store;
        }

    }


}
