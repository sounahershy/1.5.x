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
package org.mifos.application.collectionsheet.persistence;

import org.mifos.accounts.fees.business.AmountFeeBO;
import org.mifos.application.meeting.business.MeetingBO;
import org.mifos.customers.business.CustomerMeetingEntity;
import org.mifos.customers.center.business.CenterBO;
import org.mifos.customers.office.business.OfficeBO;
import org.mifos.customers.personnel.business.PersonnelBO;
import org.mifos.customers.util.helpers.CustomerLevel;
import org.mifos.customers.util.helpers.CustomerStatus;
import org.mifos.framework.util.helpers.Constants;

/**
 *
 */
public class CenterBuilder {

    private CenterBO center;
    private final CustomerAccountBuilder customerAccountBuilder = new CustomerAccountBuilder();
    private String name = "Test Center";
    private MeetingBO meeting = new MeetingBuilder().customerMeeting().weekly().every(1).startingToday().build();
    private OfficeBO office;
    private PersonnelBO loanOfficer;
    private String searchId = "1.1";
    private final Short updatedFlag = Constants.NO;
    private final CustomerStatus customerStatus = CustomerStatus.CENTER_ACTIVE;

    public CenterBO build() {

        final CustomerMeetingEntity customerMeeting = new CustomerMeetingEntity(meeting, updatedFlag);
        center = new CenterBO(CustomerLevel.CENTER, customerStatus, name, office, loanOfficer, customerMeeting,
                searchId);

        // add relationship between customer account and customer.
        customerAccountBuilder.withCustomer(center).withOffice(office).withLoanOfficer(loanOfficer).buildForIntegrationTests();

        return center;
    }

    public CenterBuilder withName(final String withName) {
        this.name = withName;
        return this;
    }

    public CenterBuilder withMeeting(final MeetingBO withMeeting) {
        this.meeting = withMeeting;
        return this;
    }

    public CenterBuilder withOffice(final OfficeBO withOffice) {
        this.office = withOffice;
        return this;
    }

    public CenterBuilder withLoanOfficer(final PersonnelBO withLoanOfficer) {
        this.loanOfficer = withLoanOfficer;
        return this;
    }

    public CenterBuilder withFee(final AmountFeeBO withFee) {
        customerAccountBuilder.withFee(withFee);
        return this;
    }

    public CenterBuilder withSearchId(final String withSearchId) {
        this.searchId = withSearchId;
        return this;
    }
}
