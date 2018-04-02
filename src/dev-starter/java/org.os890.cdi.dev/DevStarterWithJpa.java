/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.cdi.dev;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * tested with intellij
 */
public class DevStarterWithJpa {
    public static void main(String[] args) throws Exception {
        System.setProperty("faces.PROJECT_STAGE", "Development");
        System.setProperty("use_jpa", "true");

        Swarm container = new Swarm()
                //part only in case of JPA - start
                .fraction(new DatasourcesFraction()
                    .dataSource("DemoDS", (ds) -> {
                        ds.driverName("h2");
                        ds.connectionUrl("jdbc:h2:mem:demoDB;DB_CLOSE_DELAY=-1"); //DB_CLOSE_ON_EXIT=FALSE
                        ds.userName("sa");
                        ds.password("sa");
                    })
                )
                //part only in case of JPA - end
        ;

        System.setProperty("swarm.http.port", ConfigResolver.getProjectStageAwarePropertyValue("httpPort"));

        container.start();

        String context = ConfigResolver.getProjectStageAwarePropertyValue("serviceRoot");
        WARArchive warArchive = container.createDefaultDeployment().as(WARArchive.class).setContextRoot(context);

        container.deploy(warArchive);
    }
}
