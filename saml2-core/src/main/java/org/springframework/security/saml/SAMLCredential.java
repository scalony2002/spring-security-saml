/* Copyright 2009 Vladimir Schafer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package org.springframework.security.saml;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.NameID;
import org.springframework.security.saml.parser.SAMLCollection;
import org.springframework.security.saml.parser.SAMLObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Object is a storage for entities parsedd from SAML2 response during it's authentication. The object is stored
 * as credential object inside the Authentication returned after the authentication success.
 * <p/>
 * The SAML entities (NameID, Assertion) are internally stored in SAMLObject to permit their serialization.
 *
 * @author Vladimir Schafer
 */
public class SAMLCredential implements Serializable {

    private SAMLObject<NameID> nameID;
    private SAMLObject<Assertion> authenticationAssertion;
    private String IDPEntityID;

    /**
     * Collection of attributes received from assertions.
     */
    private SAMLCollection<Attribute> attributes;

    /**
     * Created unmodifiable SAML credential object.
     *
     * @param nameID                  name ID of the authenticated entity
     * @param authenticationAssertion assertion used to validate the entity
     * @param IDPEntityID             identifier of IDP where the assertion came from
     */
    public SAMLCredential(NameID nameID, Assertion authenticationAssertion, String IDPEntityID) {
        this(nameID, authenticationAssertion, IDPEntityID, Collections.<Attribute>emptyList());
    }

    /**
     * Created unmodifiable SAML credential object.
     *
     * @param nameID                  name ID of the authenticated entity
     * @param authenticationAssertion assertion used to validate the entity
     * @param IDPEntityID             identifier of IDP where the assertion came from
     * @param attributes              attributes collected from received assertions
     */
    public SAMLCredential(NameID nameID, Assertion authenticationAssertion, String IDPEntityID, List<Attribute> attributes) {    
        this.nameID = new SAMLObject<NameID>(nameID);
        this.authenticationAssertion = new SAMLObject<Assertion>(authenticationAssertion);
        this.IDPEntityID = IDPEntityID;
        this.attributes = new SAMLCollection<Attribute>(attributes);
    }

    /**
     * NameID returned from IDP as part of the authentication process.
     *
     * @return name id
     */
    public NameID getNameID() {
        return nameID.getObject();
    }

    /**
     * Assertion issued by IDP as part of the authentication process.
     *
     * @return assertion
     */
    public Assertion getAuthenticationAssertion() {
        return authenticationAssertion.getObject();
    }

    /**
     * Entity ID of the IDP which issued the assertion.
     *
     * @return IDP entity ID
     */
    public String getIDPEntityID() {
        return IDPEntityID;
    }

    /**
     * Method searches for the first occurrence of the attribute with given name and returns it.
     * Name comparing is only done by "name" attribute, disregarding "friendly-name" and "name-format".
     * Attributes are searched in order as received in SAML message.
     *
     * @param name name of attribute to find
     * @return the first occurrence of the attribute with the given name or null if not found
     */
    public Attribute getAttributeByName(String name) {
        for (Attribute attribute : getAttributes()) {
            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Unmodifiable list of all attributes loaded from the assertions received during SSO.
     * Attributes with the same name might be contained multiple times if received from different assertions.
     * Order of attributes is the same as declared in the received SAML message.
     *
     * @return unmodifiable list of users attributes
     */
    public List<Attribute> getAttributes() {
        return Collections.unmodifiableList(attributes.getObject());
    }

}