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
package org.wso2.carbon.connector.twilio.sms;

import java.net.URI;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;

/*
 * Class mediator for sending an SMS.
 * For more information, see http://www.twilio.com/docs/api/rest/sending-sms
 */
public class SendSms extends AbstractConnector {

    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: send SMS");

        String to =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext, TwilioUtil.PARAM_TO);
        String from =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FROM);
        String body =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_BODY);
        String statusCallBackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS_CALLBACK_URL);
        String applicationSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_APPLICATION_SID);

        TwilioUtil.initTwilio(messageContext);
        MessageCreator messageCreator = Message.creator(new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(from), body);
        if (applicationSid != null) {
            messageCreator.setApplicationSid(applicationSid);
        }
        if (statusCallBackUrl != null) {
            messageCreator.setStatusCallback(URI.create(statusCallBackUrl));
        }
        Message message = messageCreator.create();

        OMElement omResponse = TwilioUtil.parseResponse("sms.create.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_MESSAGE_SID, message.getSid());
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_STATUS, message.getStatus().toString());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: send SMS");
    }
}
