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
package org.os890.jaxrs;

import org.os890.ejb.IdeaService;
import org.os890.jpa.Idea;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Stateless
@Path("test-jpa")
public class IdeaResource {
    private static final Logger LOG = Logger.getLogger(IdeaResource.class.getName());

    @Inject
    private IdeaService ideaService;

    @GET
    public Response runTest() {
        try {
            Idea newIdea = new Idea();
            newIdea.setTopic("ORM");
            newIdea.setDescription("Mapping objects to tables");
            ideaService.save(newIdea);

            List<Idea> foundIdeas = ideaService.findAll();
            LOG.info("number of found ideas: " + foundIdeas.size());

            long id = foundIdeas.iterator().next().getId();
            Idea loadedIdea = ideaService.findById(id);
            LOG.info("loaded idea: " + loadedIdea);

            loadedIdea.setDescription("Updating data in the DB");
            ideaService.update(loadedIdea);

            Idea updatedIdea = ideaService.findById(id);
            LOG.info("updated idea: " + updatedIdea);

            ideaService.delete(updatedIdea);

            foundIdeas = ideaService.findAll();
            LOG.info("number of found ideas: " + foundIdeas.size());
        } catch (Throwable t) {
            return Response.serverError().build();
        }
        return Response.ok("finished successfully").build();
    }
}
