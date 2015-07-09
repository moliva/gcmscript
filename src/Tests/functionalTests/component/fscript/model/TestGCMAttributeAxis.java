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
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.util.AttributesHelper;
import org.objectweb.proactive.extra.component.fscript.model.GCMAttributeNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;


public class TestGCMAttributeAxis extends CommonSetup {
    private GCMComponentNode node;
    private Axis axis;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        node = new GCMComponentNode(model, serverWebService);
        axis = model.getAxis("attribute");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkAttributes() {
        AttributesHelper attrHelper = new AttributesHelper(node.getComponent());
        Set<String> attrNames = attrHelper.getAttributesNames();
        Set<Node> attrNodes = axis.selectFrom(node);
        assertNotNull(attrNodes);
        assertEquals(attrNames.size(), attrNodes.size());
        for (String attrName : attrNames) {
            assertTrue(attrNodes.contains(new GCMAttributeNode(model, attrHelper, attrName)));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addNotModifiableAttribute() {
        axis.connect(node, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeNotModifiableAttribute() {
        axis.disconnect(node, null);
    }
}
