/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
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
package org.wso2.carbon.connector.twilio.account;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.rest.api.v2010.Account;
import com.twilio.rest.api.v2010.AccountCreator;

/*
 * Class mediator for creating a sub-account
 * For more information, see http://www.twilio.com/docs/api/rest/subaccounts
 */
public class CreateSubAccount extends AbstractConnector {

    public void connect(MessageContext messageContext) {
        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: create account");
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);

        TwilioUtil.initTwilio(messageContext);
        AccountCreator accountCreator = Account.creator();

        if (friendlyName != null) {
            accountCreator.setFriendlyName(friendlyName);
        }

        Account account = accountCreator.create();
        OMElement omResponse = TwilioUtil.parseResponse("account.create.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.TWILIO_ACCOUNT_SID, account.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: create account");
    }
}
