/*
 * Copyright 2018 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.quarkus.it.panache;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@XmlRootElement
@Entity(name = "Person2")
public class Person extends PanacheEntity {

    public String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Address address;

    public Status status;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Dog> dogs = new ArrayList<>();

    // note that this annotation is automatically added for mapped fields, which is not the case here
    // so we do it manually to emulate a mapped field situation
    @XmlTransient
    @Transient
    public int serialisationTrick;

    public static List<Dog> findOrdered() {
        return find("ORDER BY name").list();
    }

    // For JAXB: both getter and setter are required
    // Here we make sure the field is not used by Hibernate, but the accessor is used by jaxb and jsonb
    public int getSerialisationTrick() {
        return ++serialisationTrick;
    }

    public void setSerialisationTrick(int serialisationTrick) {
        this.serialisationTrick = serialisationTrick;
    }
}
