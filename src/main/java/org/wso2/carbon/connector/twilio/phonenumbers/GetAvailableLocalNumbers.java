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
 * Class mediator for getting available local numbers in an account.
 * For more information,
 * http://www.twilio.com/docs/api/rest/available-phone-numbers
 */
public class GetAvailableLocalNumbers extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) {
        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: get available local numbers");

        String country =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_COUNTRY);

        TwilioUtil.initTwilio(messageContext);
        TwilioRestClient twilioRestClient = Twilio.getRestClient();
        Request request = new Request(HttpMethod.GET, Domains.API.toString(),
                TwilioUtil.API_URL +
                        "/" +
                        twilioRestClient.getAccountSid() +
                        "/" +
                        TwilioUtil.API_AVAILABLE_PHONE_NUMBERS +
                        "/" + country + "/" +
                        TwilioUtil.API_LOCAL
        );
        addQueryParams(request, messageContext);
        Response response = twilioRestClient.request(request);

        OMElement omResponse = TwilioUtil.parseResponse(response);
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: get available local numbers");
    }
    private void addQueryParams(Request request, MessageContext messageContext) {
        String areaCode =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_AREACODE);
        String contains =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CONTAINS);
        String region =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_IN_REGION);
        String postalCode =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_IN_POSTAL_CODE);
        String nearLat =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_NEAR_LAT_LONG);
        String nearNumber =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_NEAR_NUMBER);
        String inLata =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_IN_LATA);
        String inRateCenter =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_IN_RATE_CENTER);
        String distance =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_DISTANCE);

        if (areaCode != null) {
            request.addQueryParam(TwilioUtil.TWILIO_AREACODE, areaCode);
        }
        if (contains != null) {
            request.addQueryParam(TwilioUtil.TWILIO_CONTAINS, contains);
        }
        if (region != null) {
            request.addQueryParam(TwilioUtil.TWILIO_IN_REGION, region);
        }
        if (postalCode != null) {
            request.addQueryParam(TwilioUtil.TWILIO_IN_POSTAL_CODE, postalCode);
        }
        if (nearLat != null) {
            request.addQueryParam(TwilioUtil.TWILIO_NEAR_LAT_LONG, nearLat);
        }
        if (nearNumber != null) {
            request.addQueryParam(TwilioUtil.TWILIO_NEAR_NUMBER, nearNumber);
        }
        if (inLata != null) {
            request.addQueryParam(TwilioUtil.TWILIO_IN_LATA, inLata);
        }
        if (inRateCenter != null) {
            request.addQueryParam(TwilioUtil.TWILIO_IN_RATE_CENTER, inRateCenter);
        }
        if (distance != null) {
            request.addQueryParam(TwilioUtil.TWILIO_DISTANCE, distance);
        }

    }
}
