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

import java.net.URI;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumberUpdater;

/*
 * Class mediator for getting the list of incoming phone numbers.
 * For more information, see
 * http://www.twilio.com/docs/api/rest/incoming-phone-numbers#instance-post
 */
public class UpdateIncomingPhoneNumber extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: update phone number");
        // Must be provided
        String incomingPhoneNumberSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_INCOMING_PHONE_SID);

        TwilioUtil.initTwilio(messageContext);
        IncomingPhoneNumberUpdater incomingPhoneNumberUpdater = getIncomingPhoneNumberUpdater(
                messageContext, incomingPhoneNumberSid);
        IncomingPhoneNumber incomingPhoneNumber = incomingPhoneNumberUpdater.update();

        OMElement omResponse = TwilioUtil.parseResponse("phonenumber.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_INCOMING_PHONE_SID, incomingPhoneNumber.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: update phone number");
    }

    /**
     * Populates the parameters from the properties from the message context (If
     * provided)
     *
     * @param messageContext SynapseMessageContext
     */
    private IncomingPhoneNumberUpdater getIncomingPhoneNumberUpdater(MessageContext messageContext,
                                                                     String incomingPhoneNumberSid) {
        IncomingPhoneNumberUpdater incomingPhoneNumberUpdater = IncomingPhoneNumber.updater(incomingPhoneNumberSid);
        // Parameters to be updated
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);
        String apiVersion =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_API_VERSION);
        String voiceUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_VOICEURL);
        String voiceMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_VOICEMETHOD);
        String voiceFallbackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_VOICEFALLBACKURL);
        String voiceFallbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_VOICEFALLBACKMETHOD);
        String statusCallback =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS_CALLBACK);
        String statusCallbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS_CALLBACK_METHOD);
        String voiceCallerIdLookup =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_VOICE_CALLERID_LOOKUP);
        String voiceApplicationSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_VOICE_APPLICATION_SID);
        String smsUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_URL);
        String smsMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_METHOD);
        String smsFallbackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_FALLBACKURL);
        String smsFallbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_FALLBACKMETHOD);
        String smsApplicationSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_APPLICATION_SID);
        String accountSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_ACCOUNT_SID);

        if (friendlyName != null) {
            incomingPhoneNumberUpdater.setFriendlyName(friendlyName);
        }

        if (apiVersion != null) {
            incomingPhoneNumberUpdater.setApiVersion(apiVersion);
        }
        if (voiceUrl != null) {
            incomingPhoneNumberUpdater.setVoiceUrl(URI.create(voiceUrl));
        }
        if (voiceMethod != null) {
            incomingPhoneNumberUpdater.setVoiceMethod(HttpMethod.valueOf(voiceMethod));
        }
        if (voiceFallbackUrl != null) {
            incomingPhoneNumberUpdater.setVoiceFallbackUrl(URI.create(voiceFallbackUrl));
        }
        if (voiceFallbackMethod != null) {
            incomingPhoneNumberUpdater.setVoiceFallbackMethod(HttpMethod.valueOf(voiceFallbackMethod));
        }
        if (statusCallback != null) {
            incomingPhoneNumberUpdater.setStatusCallback(URI.create(statusCallback));
        }
        if (statusCallbackMethod != null) {
            incomingPhoneNumberUpdater.setStatusCallbackMethod(HttpMethod.valueOf(statusCallbackMethod));
        }
        if (voiceCallerIdLookup != null) {
            incomingPhoneNumberUpdater.setVoiceCallerIdLookup(Boolean.parseBoolean(voiceCallerIdLookup));
        }
        if (voiceApplicationSid != null) {
            incomingPhoneNumberUpdater.setVoiceApplicationSid(voiceApplicationSid);
        }
        if (smsUrl != null) {
            incomingPhoneNumberUpdater.setSmsUrl(URI.create(smsUrl));
        }
        if (smsMethod != null) {
            incomingPhoneNumberUpdater.setSmsMethod(HttpMethod.valueOf(smsMethod));
        }
        if (smsFallbackUrl != null) {
            incomingPhoneNumberUpdater.setSmsFallbackUrl(URI.create(smsFallbackUrl));
        }
        if (smsFallbackMethod != null) {
            incomingPhoneNumberUpdater.setSmsFallbackMethod(HttpMethod.valueOf(smsFallbackMethod));
        }
        if (smsApplicationSid != null) {
            incomingPhoneNumberUpdater.setSmsApplicationSid(smsApplicationSid);
        }
        if (accountSid != null) {
            incomingPhoneNumberUpdater.setAccountSid(accountSid);
        }
        return incomingPhoneNumberUpdater;
    }
}
