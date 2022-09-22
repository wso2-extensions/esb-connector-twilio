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
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumberCreator;

/*
 * Class mediator for purchasing a phone numbers.
 * For more information, see
 * http://www.twilio.com/docs/api/rest/incoming-phone-numbers#list-post
 */
public class PurchasePhoneNumber extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: purchase phone number");

        TwilioUtil.initTwilio(messageContext);
        IncomingPhoneNumberCreator incomingPhoneNumberCreator = getIncomingPhoneNumberCreator(messageContext);
        IncomingPhoneNumber incomingPhoneNumber = incomingPhoneNumberCreator.create();

        OMElement omResponse = TwilioUtil.parseResponse("phonenumber.purchase.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_INCOMING_PHONE_SID, incomingPhoneNumber.getSid());
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_FRIENDLY_NAME, incomingPhoneNumber.getFriendlyName());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: purchase phone number");
    }

    private IncomingPhoneNumberCreator getIncomingPhoneNumberCreator(MessageContext messageContext) {
        IncomingPhoneNumberCreator incomingPhoneNumberCreator;

        String phoneNumber =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_PHONENUMBER);
        String areaCode =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_AREACODE);
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);
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
        String apiVersion =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.API_VERSION);

        if (phoneNumber != null) {
            incomingPhoneNumberCreator = IncomingPhoneNumber.creator(new PhoneNumber(phoneNumber));
        } else {
            incomingPhoneNumberCreator = IncomingPhoneNumber.creator(areaCode);
        }
        if (friendlyName != null) {
            incomingPhoneNumberCreator.setFriendlyName(friendlyName);
        }
        if (voiceUrl != null) {
            incomingPhoneNumberCreator.setVoiceUrl(URI.create(voiceUrl));
        }
        if (voiceMethod != null) {
            incomingPhoneNumberCreator.setVoiceMethod(HttpMethod.valueOf(voiceMethod));
        }
        if (voiceFallbackUrl != null) {
            incomingPhoneNumberCreator.setVoiceFallbackUrl(URI.create(voiceFallbackUrl));
        }
        if (voiceFallbackMethod != null) {
            incomingPhoneNumberCreator.setVoiceFallbackMethod(HttpMethod.valueOf(voiceFallbackMethod));
        }
        if (statusCallback != null) {
            incomingPhoneNumberCreator.setStatusCallback(URI.create(statusCallback));
        }
        if (statusCallbackMethod != null) {
            incomingPhoneNumberCreator.setStatusCallbackMethod(HttpMethod.valueOf(statusCallbackMethod));
        }
        if (voiceCallerIdLookup != null) {
            incomingPhoneNumberCreator.setVoiceCallerIdLookup(Boolean.parseBoolean(voiceCallerIdLookup));
        }
        if (voiceApplicationSid != null) {
            incomingPhoneNumberCreator.setVoiceApplicationSid(voiceApplicationSid);
        }
        if (smsUrl != null) {
            incomingPhoneNumberCreator.setSmsUrl(URI.create(smsUrl));
        }
        if (smsMethod != null) {
            incomingPhoneNumberCreator.setSmsMethod(HttpMethod.valueOf(smsMethod));
        }
        if (smsFallbackUrl != null) {
            incomingPhoneNumberCreator.setSmsFallbackUrl(URI.create(smsFallbackUrl));
        }
        if (smsFallbackMethod != null) {
            incomingPhoneNumberCreator.setSmsFallbackMethod(HttpMethod.valueOf(smsFallbackMethod));
        }
        if (smsApplicationSid != null) {
            incomingPhoneNumberCreator.setSmsApplicationSid(smsApplicationSid);
        }
        if (apiVersion != null) {
            incomingPhoneNumberCreator.setApiVersion(apiVersion);
        }
        return incomingPhoneNumberCreator;
    }
}
