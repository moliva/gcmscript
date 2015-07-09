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
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.proactive.extra.component.fscript.model.GCMApplicationNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMVirtualNodeNode;
import org.objectweb.proactive.gcmdeployment.GCMVirtualNode;


public class TestGCMVirtualNodeNodeAxis extends CommonSetup {
    private GCMApplicationNode node;
    private Axis axis;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        node = new GCMApplicationNode(model, gcmaLocal);
        axis = model.getAxis("gcmvn");
    }

    @Test
    public void checkGCMVirtualNodes() {
        GCMVirtualNode[] realGCMVNs = node.getGCMApplication().getVirtualNodes().values().toArray(
                new GCMVirtualNode[] {});
        Set<Node> gcmvnNodes = axis.selectFrom(node);
        assertNotNull(gcmvnNodes);
        assertEquals(realGCMVNs.length, gcmvnNodes.size());
        for (Node gcmvnNode : gcmvnNodes) {
            GCMVirtualNode gcmvn = ((GCMVirtualNodeNode) gcmvnNode).getGCMVirtualNode();
            boolean found = false;
            for (int i = 0; i < realGCMVNs.length; i++) {
                if (gcmvn.getName().equals(realGCMVNs[i].getName()) &&
                    (gcmvn.getNbCurrentNodes() == realGCMVNs[i].getNbCurrentNodes())) {
                    found = true;
                }
            }
            if (!found) {
                fail();
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addNotModifiableGCMVirtualNode() {
        axis.connect(node, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeNotModifiableGCMVirtualNode() {
        axis.disconnect(node, null);
    }
}
