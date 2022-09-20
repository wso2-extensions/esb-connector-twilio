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
package org.wso2.carbon.connector.twilio.conference;

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
 * Class mediator for getting Conference instances.
 * For more information, see http://www.twilio.com/docs/api/rest/conference
 */
public class GetConferenceList extends AbstractConnector {

    public void connect(MessageContext messageContext) {
        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: get conference list");

        TwilioUtil.initTwilio(messageContext);
        TwilioRestClient twilioRestClient = Twilio.getRestClient();
        Request request = new Request(HttpMethod.GET, Domains.API.toString(),
                TwilioUtil.API_URL +
                        "/" +
                        twilioRestClient.getAccountSid() +
                        "/" +
                        TwilioUtil.API_CONFERENCES);
        addQueryParams(request, messageContext);
        Response response = twilioRestClient.request(request);

        OMElement omResponse = TwilioUtil.parseResponse(response);
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: get conference list");

    }

    private void addQueryParams(Request request, MessageContext messageContext) {
        String status =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_STATUS);
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);
        String dateCreated =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_DATE_CREATED);
        String dateUpdated =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_DATE_UPDATED);

        if (dateCreated != null) {
            request.addQueryParam(TwilioUtil.TWILIO_DATECREATED, dateUpdated);
        }

        if (dateUpdated != null) {
            request.addQueryParam(TwilioUtil.TWILIO_DATEUPDATED, dateUpdated);
        }

        if (friendlyName != null) {
            request.addQueryParam(TwilioUtil.TWILIO_FRIENDLY_NAME, friendlyName);
        }

        if (status != null) {
            request.addQueryParam(TwilioUtil.TWILIO_STATUS, status);
        }

    }
}