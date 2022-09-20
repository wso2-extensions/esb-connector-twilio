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
package org.wso2.carbon.connector.twilio.application;

import java.net.URI;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Application;
import com.twilio.rest.api.v2010.account.ApplicationUpdater;

/*
* Class mediator for updating an application instance with optional parameters
* For more information, see http://www.twilio.com/docs/api/rest/applications
*/
public class updateApplication extends AbstractConnector {

    public void connect(MessageContext messageContext) {
        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: update application");
        String applicationSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_APPLICATION_SID);

        TwilioUtil.initTwilio(messageContext);
        ApplicationUpdater applicationUpdater = getApplicationUpdater(messageContext, applicationSid);
        Application application = applicationUpdater.update();
        OMElement omResponse = TwilioUtil.parseResponse("application.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_APPLICATION_SID,
                application.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: update application");
    }

    /**
     * Create a map containing the parameters required to update the
     * application, which has been defined
     *
     * @return The map containing the defined parameters
     */
    private ApplicationUpdater getApplicationUpdater(MessageContext messageContext, String applicationSid) {
        ApplicationUpdater applicationUpdater = Application.updater(applicationSid);
        // optional parameters for creating the application
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
        String smsUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_URL);
        String smsMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_METHOD);
        String smsFallbackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_FALLBACK_URL);
        String smsFallbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_FALLBACKMETHOD);
        String smsStatusCallback =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_STATUS_CALLBACK);

        // null-checking and addition to map
        if (friendlyName != null) {
            applicationUpdater.setFriendlyName(friendlyName);
        }
        if (apiVersion != null) {
            applicationUpdater.setApiVersion(apiVersion);
        }
        if (voiceUrl != null) {
            applicationUpdater.setVoiceUrl(URI.create(voiceUrl));
        }
        if (voiceMethod != null) {
            applicationUpdater.setVoiceMethod(HttpMethod.valueOf(voiceMethod));
        }
        if (voiceFallbackUrl != null) {
            applicationUpdater.setVoiceFallbackUrl(URI.create(voiceFallbackUrl));
        }
        if (voiceFallbackMethod != null) {
            applicationUpdater.setVoiceFallbackMethod(HttpMethod.valueOf(voiceFallbackMethod));
        }
        if (statusCallback != null) {
            applicationUpdater.setStatusCallback(URI.create(statusCallback));
        }
        if (statusCallbackMethod != null) {
            applicationUpdater.setStatusCallbackMethod(HttpMethod.valueOf(statusCallbackMethod));
        }
        if (voiceCallerIdLookup != null) {
            applicationUpdater.setVoiceCallerIdLookup(Boolean.parseBoolean(voiceCallerIdLookup));
        }
        if (smsUrl != null) {
            applicationUpdater.setSmsUrl(URI.create(smsUrl));
        }
        if (smsMethod != null) {
            applicationUpdater.setSmsMethod(HttpMethod.valueOf(smsMethod));
        }
        if (smsFallbackUrl != null) {
            applicationUpdater.setSmsFallbackUrl(URI.create(smsFallbackUrl));
        }
        if (smsFallbackMethod != null) {
            applicationUpdater.setSmsFallbackMethod(HttpMethod.valueOf(smsFallbackMethod));
        }
        if (smsStatusCallback != null) {
            applicationUpdater.setSmsStatusCallback(URI.create(smsStatusCallback));
        }
        return applicationUpdater;
    }
}
