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
package org.wso2.carbon.connector.twilio.call;

import java.net.URI;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallUpdater;

/*
 * Class mediator for modifying a live call.
 * For more information, see
 * http://www.twilio.com/docs/api/rest/change-call-state
 */
public class ModifyLiveCall extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) {
        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: Update Live Call");

        TwilioUtil.initTwilio(messageContext);
        CallUpdater callUpdater = getCallUpdater(messageContext);
        Call call = callUpdater.update();
        OMElement omResponse = TwilioUtil.parseResponse("call.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_CALL_SID, call.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: Update Live Call");
    }

    private CallUpdater getCallUpdater(MessageContext messageContext) {

        String callSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CALL_SID);
        String url =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_URL);
        String status =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS);
        String method =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_METHOD);

        String fallbackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FALLBACKURL);
        String fallbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FALLBACK_METHOD);
        String statusCallback =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS_CALLBACK);
        String statusCallbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS_CALLBACK_METHOD);

        CallUpdater callUpdater = Call.updater(callSid);
        if (url != null) {
            callUpdater.setUrl(URI.create(url));
        }
        if (status != null) {
            callUpdater.setStatus(Call.UpdateStatus.valueOf(status));
        }
        if (method != null) {
            callUpdater.setMethod(HttpMethod.valueOf(method));
        }
        if (fallbackUrl != null) {
            callUpdater.setFallbackUrl(URI.create(fallbackUrl));
        }
        if (fallbackMethod != null) {
            callUpdater.setFallbackMethod(HttpMethod.valueOf(fallbackMethod));
        }
        if (statusCallback != null) {
            callUpdater.setStatusCallback(URI.create(statusCallback));
        }
        if (statusCallbackMethod != null) {
            callUpdater.setStatusCallbackMethod(HttpMethod.valueOf(statusCallbackMethod));
        }
        return callUpdater;
    }
}
