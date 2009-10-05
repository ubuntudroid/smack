/**
 * $RCSfile$
 * $Revision$
 * $Date$
 *
 * Copyright 2009 Jive Software.
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

package org.jivesoftware.smack;

import java.net.URI;
import java.net.URISyntaxException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.proxy.ProxyInfo;

public class BOSHConfiguration extends ConnectionConfiguration {

    public enum Protocol { http, https };

    private Protocol protocol;
    private String file;

    public BOSHConfiguration(String xmppDomain) {
        super(xmppDomain, 7070);
        setSASLAuthenticationEnabled(true);
        protocol = Protocol.http;
        file = "/http-bind/";
    }

    public BOSHConfiguration(String xmppDomain, int port) {
        super(xmppDomain, port);
        setSASLAuthenticationEnabled(true);
        protocol = Protocol.http;
        file = "/http-bind/";
    }

    public boolean isProxyEnabled() {
        return (proxy != null && proxy.getProxyType() != ProxyInfo.ProxyType.NONE);
    }

    public ProxyInfo getProxyInfo() {
        return proxy;
    }

    public String getProxyAddress() {
        return (proxy != null ? proxy.getProxyAddress() : null);
    }

    public int getProxyPort() {
        return (proxy != null ? proxy.getProxyPort() : 8080);
    }

    public URI getURI() throws URISyntaxException {
        if (file.charAt(0) != '/') {
            file = '/' + file;
        }
        return new URI(protocol + "://" + getHost() + ":" + getPort() + file);
    }
}
