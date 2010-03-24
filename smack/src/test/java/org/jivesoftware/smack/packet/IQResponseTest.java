/**
 * $RCSfile$
 * $Revision$
 * $Date$
 *
 * Copyright 2010 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smack.packet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests that verifies the correct behavior of creating result and error IQ packets.
 * 
 * @see <a href="http://xmpp.org/rfcs/rfc3920.html#stanzas-semantics-iq">IQ Semantics</a>
 * @author Guenther Niess
 */
public class IQResponseTest {

    final static private String childElement = "<child xmlns=\"http://igniterealtime.org/protocol/test\"/>";

    /**
     * Test creating a simple and empty IQ response.
     */
    @Test
    public void testGeneratingSimpleResponse() {
        final IQ request = new IQ() {
            public String getChildElementXML() {
                return childElement;
            }
        };
        request.setFrom("sender@test/Smack");
        request.setTo("receiver@test/Smack");

        final IQ result = IQ.createResultIQ(request);

        assertEquals(IQ.Type.RESULT, result.getType());
        assertNotNull(result.getPacketID());
        assertEquals(request.getPacketID(), result.getPacketID());
        assertEquals(request.getFrom(), result.getTo());
        assertEquals(request.getTo(), result.getFrom());
        assertNull(result.getChildElementXML());
    }

    /**
     * Test creating a error response based on an IQ request.
     */
    @Test
    public void testGeneratingValidErrorResponse() {
        final XMPPError error = new XMPPError(XMPPError.Condition.bad_request);
        final IQ request = new IQ() {
            public String getChildElementXML() {
                return childElement;
            }
        };
        request.setType(IQ.Type.SET);
        request.setFrom("sender@test/Smack");
        request.setTo("receiver@test/Smack");

        final IQ result = IQ.createErrorResponse(request, error);

        assertEquals(IQ.Type.ERROR, result.getType());
        assertNotNull(result.getPacketID());
        assertEquals(request.getPacketID(), result.getPacketID());
        assertEquals(request.getFrom(), result.getTo());
        assertEquals(error, result.getError());
        assertEquals(childElement, result.getChildElementXML());
    }

    /**
     * According to <a href="http://xmpp.org/rfcs/rfc3920.html#stanzas-semantics-iq"
     * >RFC3920: IQ Semantics</a> we shouldn't respond to an IQ of type result.
     */
    @Test
    public void testGeneratingResponseBasedOnResult() {
        final IQ request = new IQ() {
            public String getChildElementXML() {
                return childElement;
            }
        };
        request.setType(IQ.Type.RESULT);
        request.setFrom("sender@test/Smack");
        request.setTo("receiver@test/Smack");

        try {
            IQ.createResultIQ(request);
        }
        catch (IllegalArgumentException e) {
            return;
        }

        fail("It shouldn't be possible to generate a response for a result IQ!");
    }

    /**
     * According to <a href="http://xmpp.org/rfcs/rfc3920.html#stanzas-semantics-iq"
     * >RFC3920: IQ Semantics</a> we shouldn't respond to an IQ of type error.
     */
    @Test
    public void testGeneratingErrorBasedOnError() {
        final XMPPError error = new XMPPError(XMPPError.Condition.bad_request);
        final IQ request = new IQ() {
            public String getChildElementXML() {
                return childElement;
            }
        };
        request.setType(IQ.Type.ERROR);
        request.setFrom("sender@test/Smack");
        request.setTo("receiver@test/Smack");
        request.setError(error);

        try {
            IQ.createErrorResponse(request, error);
        }
        catch (IllegalArgumentException e) {
            return;
        }

        fail("It shouldn't be possible to generate a response for a error IQ!");
    }
}
