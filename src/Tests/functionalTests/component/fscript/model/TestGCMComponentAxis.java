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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.AttributesHelper;
import org.objectweb.proactive.extra.component.fscript.model.GCMAttributeNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMInterfaceNode;


public class TestGCMComponentAxis extends CommonSetup {
    private GCMComponentNode node;
    private Axis axis;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        node = new GCMComponentNode(model, serverWebService);
        axis = model.getAxis("component");
    }

    @Test
    public void checkComponentOfItself() {
        Set<Node> componentNodes = axis.selectFrom(node);
        assertNotNull(componentNodes);
        assertEquals(1, componentNodes.size());
        assertTrue(componentNodes.contains(node));
    }

    @Test
    public void checkComponentOfInterface() throws NoSuchInterfaceException {
        Interface itf = (Interface) node.getComponent().getFcInterface("component");
        Set<Node> componentNodes = axis.selectFrom(new GCMInterfaceNode(model, itf));
        assertNotNull(componentNodes);
        assertEquals(1, componentNodes.size());
        assertTrue(componentNodes.contains(node));
    }

    @Test
    public void checkComponentOfAttribute() {
        AttributesHelper attrHelper = new AttributesHelper(node.getComponent());
        Set<Node> componentNodes = axis.selectFrom(new GCMAttributeNode(model, attrHelper, "separator"));
        assertNotNull(componentNodes);
        assertEquals(1, componentNodes.size());
        assertTrue(componentNodes.contains(node));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addNotModifiableComponent() {
        axis.connect(node, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeNotModifiableComponent() {
        axis.disconnect(node, null);
    }
}
