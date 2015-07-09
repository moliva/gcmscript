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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.etsi.uri.gcm.util.GCM;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;


public class TestGCMChildAxis extends CommonSetup {
    private GCMComponentNode node;
    private GCMComponentNode nodePrimitive;
    private Axis axis;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        node = new GCMComponentNode(model, composite);
        nodePrimitive = new GCMComponentNode(model, serverWebService);
        axis = model.getAxis("child");
    }

    @Test
    public void checkChildren() throws NoSuchInterfaceException {
        Component[] realChildren = getSubComponents();
        Set<Node> childNodes = axis.selectFrom(node);
        assertNotNull(childNodes);
        assertEquals(realChildren.length, childNodes.size());
        for (int i = 0; i < realChildren.length; i++) {
            assertTrue(childNodes.contains(new GCMComponentNode(model, realChildren[i])));
        }
    }

    @Test
    public void checkPrimitiveChildren() {
        Set<Node> childNodes = axis.selectFrom(nodePrimitive);
        assertNotNull(childNodes);
        assertEquals(0, childNodes.size());
    }

    @Test
    public void addChild() throws ADLException {
        Factory factory = FactoryFactory.getFactory();
        Component master = (Component) factory.newComponent(
                "functionalTests.component.fscript.model.components.adl.Master", null);
        GCMComponentNode masterNode = new GCMComponentNode(model, master);
        axis.connect(node, masterNode);
        Set<Node> childNodes = axis.selectFrom(node);
        assertNotNull(childNodes);
        assertTrue(childNodes.contains(masterNode));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPrimitiveChild() throws ADLException {
        Factory factory = FactoryFactory.getFactory();
        Component master = (Component) factory.newComponent(
                "functionalTests.component.fscript.model.components.adl.Master", null);
        GCMComponentNode masterNode = new GCMComponentNode(model, master);
        axis.connect(nodePrimitive, masterNode);
    }

    @Test
    public void removeChild() throws NoSuchInterfaceException, IllegalLifeCycleException,
            IllegalBindingException {
        Component[] children = getSubComponents();
        Component master = null;
        Component worker1 = null;
        Component worker2 = null;
        for (int i = 0; i < children.length; i++) {
            String compName = GCM.getNameController(children[i]).getFcName();
            if (compName.equals("master")) {
                master = children[i];
            } else if (compName.equals("worker1")) {
                worker1 = children[i];
            } else if (compName.equals("worker2")) {
                worker2 = children[i];
            }
        }
        GCM.getBindingController(node.getComponent()).unbindFc("service-server");
        GCM.getMulticastController(master).unbindGCMMulticast("service-client",
                worker1.getFcInterface("service-server"));
        GCM.getMulticastController(master).unbindGCMMulticast("service-client",
                worker2.getFcInterface("service-server"));
        GCMComponentNode masterNode = new GCMComponentNode(model, master);
        axis.disconnect(node, masterNode);
        Set<Node> childNodes = axis.selectFrom(node);
        assertNotNull(childNodes);
        assertFalse(childNodes.contains(masterNode));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePrimitiveChild() throws ADLException {
        Factory factory = FactoryFactory.getFactory();
        Component master = (Component) factory.newComponent(
                "functionalTests.component.fscript.model.components.adl.Master", null);
        GCMComponentNode masterNode = new GCMComponentNode(model, master);
        axis.disconnect(nodePrimitive, masterNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeInvalidChild() {
        axis.disconnect(node, nodePrimitive);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeChildWithoutUnbinding() throws NoSuchInterfaceException {
        Component[] children = getSubComponents();
        Component master = null;
        for (int i = 0; i < children.length; i++) {
            if (GCM.getNameController(children[i]).getFcName().equals("master")) {
                master = children[i];
                break;
            }
        }
        GCMComponentNode masterNode = new GCMComponentNode(model, master);
        axis.disconnect(node, masterNode);
    }

    private Component[] getSubComponents() throws NoSuchInterfaceException {
        return GCM.getContentController(node.getComponent()).getFcSubComponents();
    }
}
