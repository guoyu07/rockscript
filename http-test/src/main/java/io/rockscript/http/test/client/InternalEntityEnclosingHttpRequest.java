/*
 * Copyright ©2017, RockScript.io. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rockscript.http.test.client;

import java.net.URI;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;

import static org.apache.http.protocol.HTTP.EXPECT_CONTINUE;
import static org.apache.http.protocol.HTTP.EXPECT_DIRECTIVE;

@NotThreadSafe
class InternalEntityEnclosingHttpRequest extends InternalHttpRequest implements HttpEntityEnclosingRequest {

    private HttpEntity entity;

    public InternalEntityEnclosingHttpRequest(final String method, final URI requestURI) {
        super(method, requestURI);
    }

    @Override
    public HttpEntity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean expectContinue() {
        final Header expect = getFirstHeader(EXPECT_DIRECTIVE);
        return expect!=null && EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

}
