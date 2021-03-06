/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */
package org.mifos.application.master.business;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COUNTRY")
public class CountryEntity implements Serializable {

    private Short countryId;
    private String countryName;
    private String countryShortName;

    public CountryEntity() {
    }

    public CountryEntity(String countryName, String countryShortName) {
        this.countryName = countryName;
        this.countryShortName = countryShortName;
    }

    @Id
    @GeneratedValue
    @Column(name = "COUNTRY_ID", nullable = false)
    public Short getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Short countryId) {
        this.countryId = countryId;
    }

    @Column(name = "COUNTRY_NAME")
    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Column(name = "COUNTRY_SHORT_NAME")
    public String getCountryShortName() {
        return this.countryShortName;
    }

    public void setCountryShortName(String countryShortName) {
        this.countryShortName = countryShortName;
    }

}
