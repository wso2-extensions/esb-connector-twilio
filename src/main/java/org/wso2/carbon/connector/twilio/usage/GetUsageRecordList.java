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
package org.wso2.carbon.connector.twilio.usage;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.connector.twilio.util.TwilioUtil;

import com.twilio.Twilio;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;

/*
 * Class mediator for getting a USAGE resources
 * For more information, see http://www.twilio.com/docs/api/rest/usage-records
 */
public class GetUsageRecordList extends AbstractConnector {

    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: get usage record List");

        TwilioUtil.initTwilio(messageContext);
        TwilioRestClient twilioRestClient = Twilio.getRestClient();
        Request request = new Request(HttpMethod.GET, Domains.API.toString(),
                TwilioUtil.API_URL +
                        "/" +
                        twilioRestClient.getAccountSid() +
                        "/" +
                        TwilioUtil.API_USAGE +
                        "/" +
                        TwilioUtil.API_USAGE_RECORDS
        );
        this.addQueryParams(request, messageContext);
        Response response = twilioRestClient.request(request);

        OMElement omResponse = TwilioUtil.parseResponse(response);
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: get usage record List");
    }

    private void addQueryParams(Request request, MessageContext messageContext) {
        String category =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_USAGE_CATEGORY);
        String startDate =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_START_DATE);
        String endDate =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_END_DATE);

        if (category != null) {
            request.addQueryParam(TwilioUtil.TWILIO_CATEGORY, category);
        }
        if (startDate != null) {
            request.addQueryParam(TwilioUtil.TWILIO_START_DATE, startDate);
        }
        if (endDate != null) {
            request.addQueryParam(TwilioUtil.TWILIO_END_DATE, endDate);
        }
    }

}
