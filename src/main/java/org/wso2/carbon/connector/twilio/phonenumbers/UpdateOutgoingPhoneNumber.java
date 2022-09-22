/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.connector.twilio.phonenumbers;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.rest.api.v2010.account.OutgoingCallerId;
import com.twilio.rest.api.v2010.account.OutgoingCallerIdUpdater;

/*
 * Class mediator for purchasing a phone numbers.
 * For more information, see
 * http://www.twilio.com/docs/api/rest/incoming-phone-numbers#list-post
 */
public class UpdateOutgoingPhoneNumber extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: update outgoing phone number");
        String outgoingCallerSId =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_OUTGOING_CALLERID);
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);

        TwilioUtil.initTwilio(messageContext);
        OutgoingCallerIdUpdater outgoingCallerIdUpdater = OutgoingCallerId.updater(outgoingCallerSId);
        if (friendlyName != null) {
            outgoingCallerIdUpdater.setFriendlyName(friendlyName);
        }
        OutgoingCallerId outgoingCallerId = outgoingCallerIdUpdater.update();
        OMElement omResponse = TwilioUtil.parseResponse("outgoingphonenumber.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_OUTGOING_PHONE_SID, outgoingCallerId.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: update outgoing phone number");
    }
}
