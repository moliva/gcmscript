/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2013 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package functionalTests.component.fscript.model;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.etsi.uri.gcm.api.type.GCMInterfaceType;
import org.etsi.uri.gcm.util.GCM;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.proactive.extra.component.fscript.model.GCMInterfaceNode;


public class TestGCMInterfaceNode extends CommonSetup {
    private Component comp;
    private Map<Interface, GCMInterfaceNode> itfs = new HashMap<Interface, GCMInterfaceNode>();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        comp = composite;
        Interface componentItf = (Interface) comp.getFcInterface("component");
        itfs.put(componentItf, new GCMInterfaceNode(model, componentItf));
        for (Object rawItf : GCM.getContentController(comp).getFcInternalInterfaces()) {
            Interface itf = (Interface) rawItf;
            itfs.put(itf, new GCMInterfaceNode(model, itf));
        }
        for (Component child : GCM.getContentController(comp).getFcSubComponents()) {
            for (Object rawItf : child.getFcInterfaces()) {
                Interface itf = (Interface) rawItf;
                itfs.put(itf, new GCMInterfaceNode(model, itf));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void createNullInterface() {
        new GCMInterfaceNode(model, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void readInvalidProperty() {
        itfs.get(comp).getProperty("invalid");
    }

    @Test(expected = NoSuchElementException.class)
    public void writeInvalidProperty() {
        itfs.get(comp).setProperty("invalid", null);
    }

    @Test
    public void readInterfaceName() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            String realName = entry.getKey().getFcItfName();
            assertEquals(realName, entry.getValue().getName());
            assertEquals(realName, entry.getValue().getProperty("name"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceName() {
        itfs.get(comp).setProperty("name", "none");
    }

    @Test
    public void readInterfaceIsInternal() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            boolean realIsInternal = entry.getKey().isFcInternalItf();
            assertEquals(realIsInternal, entry.getValue().isInternal());
            assertEquals(realIsInternal, entry.getValue().getProperty("internal"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceIsInternal() {
        itfs.get(comp).setProperty("internal", true);
    }

    @Test
    public void readInterfaceSignature() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            GCMInterfaceType itfType = (GCMInterfaceType) entry.getKey().getFcItfType();
            String realSignature = itfType.getFcItfSignature();
            assertEquals(realSignature, entry.getValue().getSignature());
            assertEquals(realSignature, entry.getValue().getProperty("signature"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceSignature() {
        itfs.get(comp).setProperty("signature", "none");
    }

    @Test
    public void readInterfaceIsCollection() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            GCMInterfaceType itfType = (GCMInterfaceType) entry.getKey().getFcItfType();
            boolean realIsCollection = itfType.isFcCollectionItf();
            assertEquals(realIsCollection, entry.getValue().isCollection());
            assertEquals(realIsCollection, entry.getValue().getProperty("collection"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceIsCollection() {
        itfs.get(comp).setProperty("collection", true);
    }

    @Test
    public void readInterfaceIsOptional() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            GCMInterfaceType itfType = (GCMInterfaceType) entry.getKey().getFcItfType();
            boolean realIsOptional = itfType.isFcOptionalItf();
            assertEquals(realIsOptional, entry.getValue().isOptional());
            assertEquals(realIsOptional, entry.getValue().getProperty("optional"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceIsOptional() {
        itfs.get(comp).setProperty("optional", true);
    }

    @Test
    public void readInterfaceIsClient() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            GCMInterfaceType itfType = (GCMInterfaceType) entry.getKey().getFcItfType();
            boolean realIsClient = itfType.isFcClientItf();
            assertEquals(realIsClient, entry.getValue().isClient());
            assertEquals(realIsClient, entry.getValue().getProperty("client"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceIsClient() {
        itfs.get(comp).setProperty("client", true);
    }

    @Test
    public void readInterfaceIsSingleton() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            GCMInterfaceType itfType = (GCMInterfaceType) entry.getKey().getFcItfType();
            boolean realIsSingleton = itfType.isGCMSingletonItf();
            assertEquals(realIsSingleton, entry.getValue().isSingleton());
            assertEquals(realIsSingleton, entry.getValue().getProperty("singleton"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceIsSingleton() {
        itfs.get(comp).setProperty("singleton", true);
    }

    @Test
    public void readInterfaceIsMulticast() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            GCMInterfaceType itfType = (GCMInterfaceType) entry.getKey().getFcItfType();
            boolean realIsMulticast = itfType.isGCMMulticastItf();
            assertEquals(realIsMulticast, entry.getValue().isMulticast());
            assertEquals(realIsMulticast, entry.getValue().getProperty("multicast"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceIsMulticast() {
        itfs.get(comp).setProperty("multicast", true);
    }

    @Test
    public void readInterfaceIsGathercast() {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            GCMInterfaceType itfType = (GCMInterfaceType) entry.getKey().getFcItfType();
            boolean realIsGathercast = itfType.isGCMGathercastItf();
            assertEquals(realIsGathercast, entry.getValue().isGathercast());
            assertEquals(realIsGathercast, entry.getValue().getProperty("gathercast"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyInterfaceIsGathercast() {
        itfs.get(comp).setProperty("gathercast", true);
    }
}