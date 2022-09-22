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

import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.ShortCode;
import com.twilio.rest.api.v2010.account.ShortCodeUpdater;

/*
 * Class mediator for updating the properties of a short code.
 * For more information, see http://www.twilio.com/docs/api/rest/short-codes
 */
public class UpdateShortCodeProperties extends AbstractConnector {

    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: update short code properties");

        String shortCodeSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SHORTCODE_SID);
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);
        String apiVersion =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_API_VERSION);
        String smsUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_URL);
        String smsMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_METHOD);
        String smsFallBackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_FALLBACKMETHOD);
        String smsFallBackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SMS_FALLBACKURL);

        TwilioUtil.initTwilio(messageContext);
        ShortCodeUpdater shortCodeUpdater = ShortCode.updater(shortCodeSid);
        if (friendlyName != null) {
            shortCodeUpdater.setFriendlyName(friendlyName);
        }
        if (apiVersion != null) {
            shortCodeUpdater.setApiVersion(apiVersion);
        }
        if (smsUrl != null) {
            shortCodeUpdater.setSmsUrl(URI.create(smsUrl));
        }
        if (smsMethod != null) {
            shortCodeUpdater.setSmsMethod(HttpMethod.valueOf(smsMethod));
        }
        if (smsFallBackUrl != null) {
            shortCodeUpdater.setSmsFallbackUrl(URI.create(smsFallBackUrl));
        }
        if (smsFallBackMethod != null) {
            shortCodeUpdater.setSmsFallbackMethod(HttpMethod.valueOf(smsFallBackMethod));
        }
        ShortCode shortCode =shortCodeUpdater.update();
        OMElement omResponse = TwilioUtil.parseResponse("shortcode.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_SHORTCODE_SID, shortCode.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: update short code properties");
    }
}
