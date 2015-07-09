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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;


public class TestRemoteGetGlobalsAction extends CommonSetup {
    private GCMComponentNode node;
    private GCMComponentNode nodePrimitive;
    private NativeProcedure remoteGetGlobals;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        node = new GCMComponentNode(model, composite);
        nodePrimitive = new GCMComponentNode(model, serverWebService);
        remoteGetGlobals = model.getNativeProcedure("remote-get-globals");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void remoteGetGlobals() throws ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        args.add(node);
        Set<String> variableNames = (Set<String>) remoteGetGlobals.apply(args, null);
        assertNotNull(variableNames);
        assertEquals(1, variableNames.size());
        assertTrue(variableNames.contains("this"));
    }

    @Test(expected = ScriptExecutionError.class)
    public void remoteGetGlobalsNoPAReconfigurationController() throws ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        args.add(nodePrimitive);
        remoteGetGlobals.apply(args, null);
    }
}