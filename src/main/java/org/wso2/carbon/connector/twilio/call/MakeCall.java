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

import com.twilio.type.PhoneNumber;
import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;

/*
 * Class mediator for making a call.
 * For more information, see http://www.twilio.com/docs/api/rest/making-calls
 */
public class MakeCall extends AbstractConnector {
    // Parameter details. For specifications and formats, see
    // http://www.twilio.com/docs/api/rest/making-calls#post-parameters-required
    // and
    // http://www.twilio.com/docs/api/rest/making-calls#post-parameters-optional.

    @Override
    public void connect(MessageContext messageContext) {
        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: Make Call");

        TwilioUtil.initTwilio(messageContext);
        CallCreator callCreator = getCallCreator(messageContext);
        Call call = callCreator.create();
        OMElement omResponse = TwilioUtil.parseResponse("call.create.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_CALL_SID, call.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: Make Call");
    }

    private CallCreator getCallCreator(MessageContext messageContext) {
        // These are compulsory
        String to =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext, TwilioUtil.PARAM_TO);
        String from =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FROM);
        // One of the below
        String callUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_URL);
        String applicationSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_APPLICATION_SID);
        // Optional parameters
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
        String sendDigits =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_SEND_DIGITS);
        String machineDetection =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_MACHINE_DETECTION);
        String timeout =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_IF_TIMEOUT);
        String record =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_IF_RECORD);

        CallCreator callCreator;
        if (callUrl != null) {
            callCreator = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                    URI.create(callUrl));
        } else {
            callCreator = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                    applicationSid);
        }
        // These are optional parameters. Need to check whether the parameters
        // have been defined
        if (method != null) {
            callCreator.setMethod(HttpMethod.valueOf(method));
        }
        if (fallbackUrl != null) {
            callCreator.setFallbackUrl(URI.create(fallbackUrl));
        }
        if (fallbackMethod != null) {
            callCreator.setFallbackMethod(HttpMethod.valueOf(fallbackMethod));
        }
        if (statusCallback != null) {
            callCreator.setStatusCallback(URI.create(statusCallback));
        }
        if (statusCallbackMethod != null) {
            callCreator.setStatusCallbackMethod(HttpMethod.valueOf(statusCallbackMethod));
        }
        if (sendDigits != null) {
            callCreator.setSendDigits(sendDigits);
        }
        if (machineDetection != null) {
            callCreator.setMachineDetection(machineDetection);
        }
        if (timeout != null) {
            callCreator.setTimeout(Integer.parseInt(timeout));
        }
        if (record != null) {
            callCreator.setRecord(Boolean.parseBoolean(record));
        }
        return callCreator;
    }
}