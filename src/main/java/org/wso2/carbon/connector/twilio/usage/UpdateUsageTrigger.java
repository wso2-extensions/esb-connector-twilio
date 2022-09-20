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
package org.wso2.carbon.connector.twilio.usage;

import java.net.URI;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.usage.Trigger;
import com.twilio.rest.api.v2010.account.usage.TriggerUpdater;
/*
 * Class mediator for getting a USAGE triggers
 * For more information, see http://www.twilio.com/docs/api/rest/usage-triggers
 */
public class UpdateUsageTrigger extends AbstractConnector {

    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: update usage trigger");
        String triggerSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_USAGE_TRIGGER_SID);
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);
        String callbackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CALLBACK_URL);
        String callbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CALLBACK_METHOD);

        TwilioUtil.initTwilio(messageContext);
        TriggerUpdater triggerUpdater = Trigger.updater(triggerSid);
        if (friendlyName != null) {
            triggerUpdater.setFriendlyName(friendlyName);
        }
        if (callbackUrl != null) {
            triggerUpdater.setCallbackUrl(URI.create(callbackUrl));
        }
        if (callbackMethod != null) {
            triggerUpdater.setCallbackMethod(HttpMethod.valueOf(callbackMethod));
        }
        Trigger trigger = triggerUpdater.update();

        OMElement omResponse = TwilioUtil.parseResponse("usagetrigger.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_USAGE_TRIGGER_SID, trigger.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: update usage trigger");
    }
}
