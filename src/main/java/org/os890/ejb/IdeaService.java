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
package org.os890.ejb;

import org.os890.jpa.Idea;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class IdeaService {
    @PersistenceContext(unitName = "demoPU")
    private EntityManager entityManager;

    public void save(Idea idea) {
        entityManager.persist(idea);
    }

    public Idea findById(long id) {
        return entityManager.find(Idea.class, id);
    }

    public Idea update(Idea idea) {
        return entityManager.merge(idea);
    }

    public void delete(Idea idea) {
        entityManager.remove(findById(idea.getId()));
    }

    public List<Idea> findAll() {
        return entityManager.createQuery("select idea from " + Idea.class.getSimpleName() + " idea").getResultList();
    }
}
