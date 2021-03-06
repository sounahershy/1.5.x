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

package org.mifos.reports.business.service;

import java.util.Date;
import java.util.List;

import org.mifos.framework.exceptions.ServiceException;
import org.mifos.reports.ui.DateSelectionItem;
import org.mifos.reports.ui.SelectionItem;

public interface IReportsParameterService {

    public abstract List<SelectionItem> getActiveBranchesUnderUser(String officeSearchId) throws ServiceException;

    public abstract List<SelectionItem> getActiveLoanOfficersUnderOffice(Integer branchId) throws ServiceException;

    public abstract List<SelectionItem> getActiveCentersUnderUser(Integer branchId, Integer loanOfficerId)
            throws ServiceException;

    public abstract List<DateSelectionItem> getMeetingDates(Integer branchId, Integer loanOfficerId, Integer centerId,
            Date from) throws ServiceException;

    public boolean invalidate();
}
