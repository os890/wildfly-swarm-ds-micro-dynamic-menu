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
package org.os890.cdi;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.os890.jaxrs.IdeaResource;
import org.os890.jaxrs.RestApp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import java.util.logging.Logger;

public class StartupLogger {
    private static final Logger LOG = Logger.getLogger(StartupLogger.class.getName());

    @Inject
    @ConfigProperty(name = "serviceName")
    private String serviceName;

    @Inject
    @ConfigProperty(name = "serviceVersion")
    private String serviceVersion;

    @Inject
    @ConfigProperty(name = "serviceRoot")
    private String serviceRoot;

    @Inject
    @ConfigProperty(name = "httpPort")
    private Integer httpPort;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object startupEvent) {
        LOG.info("service '" + serviceName + "' started in version " + this.serviceVersion + "");
        LOG.info("info-page at http://localhost:" + httpPort + "/" + this.serviceRoot + "");

        if ("true".equalsIgnoreCase(System.getProperty("use_jpa"))) {
            LOG.info("jpa-test at http://localhost:" + httpPort + "/" + this.serviceRoot + "/" + RestApp.class.getAnnotation(ApplicationPath.class).value() + "/" + IdeaResource.class.getAnnotation(Path.class).value());
        }
    }
}
