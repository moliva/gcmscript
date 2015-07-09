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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.extra.component.fscript.model.GCMApplicationNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMNodeNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMVirtualNodeNode;


public class TestGCMNewAction extends CommonSetup {
    private NativeProcedure gcmNew;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        model.bindFc("gcm-factory", FactoryFactory.getFactory());
        gcmNew = model.getNativeProcedure("gcm-new");
    }

    @Test
    public void gcmNewWithGCMApplication() throws ScriptExecutionError, NodeException {
        List<Object> args = new ArrayList<Object>();
        args.add("functionalTests.component.fscript.model.components.adl.ServerWebService");
        args.add(new GCMApplicationNode(model, gcmaLocal));
        GCMComponentNode node = (GCMComponentNode) gcmNew.apply(args, null);
        assertTrue(gcmaLocal.getVirtualNode("VN1").getCurrentNodes().contains(
                PAActiveObject.getActiveObjectNode(node.getComponent())));
    }

    @Test
    public void gcmNewWithGCMVirtualNode() throws ScriptExecutionError, NodeException {
        List<Object> args = new ArrayList<Object>();
        args.add("functionalTests.component.fscript.model.components.adl.ServerWebService");
        Set<Node> nodes = new HashSet<Node>();
        nodes.add(new GCMVirtualNodeNode(model, gcmaLocal.getVirtualNode("VN1")));
        args.add(nodes);
        GCMComponentNode node = (GCMComponentNode) gcmNew.apply(args, null);
        assertTrue(gcmaLocal.getVirtualNode("VN1").getCurrentNodes().contains(
                PAActiveObject.getActiveObjectNode(node.getComponent())));
    }

    @Test
    public void gcmNewWithGCMNode() throws ScriptExecutionError, NodeException {
        List<Object> args = new ArrayList<Object>();
        args.add("functionalTests.component.fscript.model.components.adl.ServerWebService");
        Set<Node> nodes = new HashSet<Node>();
        org.objectweb.proactive.core.node.Node gcmnode = gcmaLocal.getVirtualNode("VN1").getCurrentNodes()
                .get(0);
        nodes.add(new GCMNodeNode(model, gcmnode));
        args.add(nodes);
        GCMComponentNode node = (GCMComponentNode) gcmNew.apply(args, null);
        assertEquals(gcmnode, PAActiveObject.getActiveObjectNode(node.getComponent()));
    }

    @Test(expected = ScriptExecutionError.class)
    public void gcmNewWithADLError() throws ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        args.add("ADLError");
        GCMApplicationNode gcmaNode = new GCMApplicationNode(model, gcmaLocal);
        args.add(gcmaNode);
        gcmNew.apply(args, null);
    }
}
