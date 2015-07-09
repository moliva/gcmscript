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

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extra.component.fscript.model.GCMNodeNode;


public class TestGCMNodeNode extends CommonSetup {
    private Node gcmnode;
    private GCMNodeNode node;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        gcmnode = gcmaLocal.getVirtualNode("VN1").getCurrentNodes().get(0);
        node = new GCMNodeNode(model, gcmnode);
    }

    @Test(expected = NullPointerException.class)
    public void createNullGCMNode() {
        new GCMNodeNode(model, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void readInvalidProperty() {
        node.getProperty("invalid");
    }

    @Test(expected = NoSuchElementException.class)
    public void writeInvalidProperty() {
        node.setProperty("invalid", null);
    }

    @Test
    public void readGCMNodeName() {
        String realName = gcmnode.getNodeInformation().getName();
        assertEquals(realName, node.getName());
        assertEquals(realName, node.getProperty("name"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyGCMNodeName() {
        node.setProperty("name", "none");
    }

    @Test
    public void readGCMNodeURL() {
        String realURL = gcmnode.getNodeInformation().getURL();
        assertEquals(realURL, node.getURL());
        assertEquals(realURL, node.getProperty("url"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyGCMNodeState() {
        node.setProperty("url", "none");
    }
}
