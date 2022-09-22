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
package org.wso2.carbon.connector.twilio.phonenumbers;

import java.net.URI;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.ValidationRequest;
import com.twilio.rest.api.v2010.account.ValidationRequestCreator;
import com.twilio.type.PhoneNumber;

/*
 * Class mediator for purchasing a phone numbers.
 * For more information, see
 * http://www.twilio.com/docs/api/rest/incoming-phone-numbers#list-post
 */
public class AddOutgoingPhoneNumber extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: add outgoing phone number");

        TwilioUtil.initTwilio(messageContext);
        ValidationRequestCreator validationRequestCreator = getValidationRequestCreator(messageContext);
        ValidationRequest validationRequest = validationRequestCreator.create();

        OMElement omResponse = TwilioUtil.parseResponse("outgoingphonenumber.create.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_PHONENUMBER, validationRequest.getPhoneNumber().toString());
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_CALL_SID, validationRequest.getCallSid());
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_VERIFICATION_CODE, validationRequest.getValidationCode());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: add outgoing phone number");
    }

    private ValidationRequestCreator getValidationRequestCreator(MessageContext messageContext) {

        String phoneNumber =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_PHONENUMBER);
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);
        String callDelay =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CALL_DELAY);
        String extension =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_EXTENSION);
        String callback =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS_CALLBACK);
        String callbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS_CALLBACK_METHOD);

        ValidationRequestCreator validationRequestCreator = new ValidationRequestCreator(new PhoneNumber(phoneNumber));

        if (friendlyName != null) {
            validationRequestCreator.setFriendlyName(friendlyName);
        }
        if (callDelay != null) {
            validationRequestCreator.setCallDelay(Integer.parseInt(callDelay));
        }
        if (extension != null) {
            validationRequestCreator.setExtension(extension);
        }
        if (callback != null) {
            validationRequestCreator.setStatusCallback(URI.create(callback));
        }
        if (callbackMethod != null) {
            validationRequestCreator.setStatusCallbackMethod(HttpMethod.valueOf(callbackMethod));
        }
        return validationRequestCreator;
    }
}
