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

import com.twilio.rest.api.v2010.account.conference.Participant;
import com.twilio.rest.api.v2010.account.conference.ParticipantUpdater;

/*
 * Class mediator for updating a particulars of a participant in a given
 * conference.
 * For more information, seehttp://www.twilio.com/docs/api/rest/participant
 */
public class UpdateParticipant extends AbstractConnector {

    public void connect(MessageContext messageContext) {

        SynapseLog log = getLog(messageContext);
        log.auditLog("Start: update participant");

        String conferenceSid =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CONFERENCE_SID);
        String callSidOfParticipant =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_CALL_SID);
        String muted =
                (String) ConnectorUtils.lookupTemplateParamater(messageContext,
                        TwilioUtil.PARAM_MUTED);

        TwilioUtil.initTwilio(messageContext);
        ParticipantUpdater participantUpdater = Participant.updater(conferenceSid, callSidOfParticipant);

        if (muted != null) {
            participantUpdater.setMuted(Boolean.parseBoolean(muted));
        }

        Participant participant = participantUpdater.update();
        OMElement omResponse = TwilioUtil.parseResponse("participant.update.success");
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_CONFERENCE_SID, conferenceSid);
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_CALL_SID, participant.getCallSid());
        TwilioUtil.addElement(omResponse, TwilioUtil.PARAM_MUTED, participant.getMuted());
        TwilioUtil.preparePayload(messageContext, omResponse);

        log.auditLog("End: update participant");

    }
}