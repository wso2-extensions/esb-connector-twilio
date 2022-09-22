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
import com.twilio.rest.api.v2010.account.ConnectApp;
import com.twilio.rest.api.v2010.account.ConnectAppUpdater;

/*
 * Class mediator for updating a connect app instance with optional parameters
 * For more information, see http://www.twilio.com/docs/api/rest/connect-apps
 */
public class updateConnectApp extends AbstractConnector {

    public void connect(MessageContext messageContext) {
        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: update connect application");
        String connectSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CONNECT_APP_SID);

        TwilioUtil.initTwilio(messageContext);
        ConnectAppUpdater connectAppUpdater = getConnectAppUpdater(messageContext, connectSid);
        ConnectApp connectApp = connectAppUpdater.update();
        OMElement omResponse = TwilioUtil.parseResponse("conapp.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_CONNECT_APP_SID, connectApp.getSid());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: update connect application");
    }

    /**
     * Create a map containing the parameters required to update the
     * application, which has been defined
     *
     * @return The map containing the defined parameters
     */
    private ConnectAppUpdater getConnectAppUpdater(MessageContext messageContext, String connectSid) {
        ConnectAppUpdater connectAppUpdater = ConnectApp.updater(connectSid);
        String friendlyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_FRIENDLY_NAME);
        String authorizedRedirectUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_AUTHORIZED_REDIRECT_URL);
        String deauthorizedCallbackUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_DEAUTHORIZE_CALLBACK_URL);
        String deauthorizedCallbackMethod =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_DEAUTHORIZE_CALLBACK_METHOD);
        String permissions =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_PERMISSIONS);
        String description =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_DESCRIPTION);
        String companyName =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_COMPANY_NAME);
        String homepageUrl =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_HOMEPAGE_URL);

        // null-checking and addition to map
        if (friendlyName != null) {
            connectAppUpdater.setFriendlyName(friendlyName);
        }
        if (authorizedRedirectUrl != null) {
            connectAppUpdater.setAuthorizeRedirectUrl(URI.create(authorizedRedirectUrl));
        }
        if (deauthorizedCallbackUrl != null) {
            connectAppUpdater.setDeauthorizeCallbackUrl(URI.create(deauthorizedCallbackUrl));
        }
        if (deauthorizedCallbackMethod != null) {
            connectAppUpdater.setDeauthorizeCallbackMethod(HttpMethod.valueOf(deauthorizedCallbackMethod));
        }
        if (permissions != null) {
            connectAppUpdater.setPermissions(ConnectApp.Permission.valueOf(permissions));
        }
        if (description != null) {
            connectAppUpdater.setDescription(description);
        }
        if (companyName != null) {
            connectAppUpdater.setCompanyName(companyName);
        }
        if (homepageUrl != null) {
            connectAppUpdater.setHomepageUrl(URI.create(homepageUrl));
        }
        return connectAppUpdater;
    }
}
