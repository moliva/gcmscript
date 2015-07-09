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
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.FScriptEngine;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;
import org.objectweb.proactive.extra.component.fscript.GCMScript;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;

import functionalTests.component.fscript.model.components.ServiceMulticast;


public class TestGCMScript extends CommonSetup {
    protected FScriptEngine engine;

    @Before
    public void setUp() throws Exception {
        System.setProperty("fractal.provider", "org.objectweb.fractal.julia.Julia");
        engine = FScript.getFScriptEngine(GCMScript.newEngineFromAdl());
    }

    @Test
    public void reconfigure() throws FScriptException, NoSuchInterfaceException {
        engine.execute("gcma = deploy-gcma(\"" + this.getClass().getResource("GCMA.xml").getPath() + "\");");
        composite = ((GCMComponentNode) engine
                .execute("composite = gcm-new(\"functionalTests.component.fscript.model.components.adl.Composite\", $gcma);"))
                .getComponent();

        List<String> expectedChildNames = new ArrayList<String>();
        expectedChildNames.add("master");
        expectedChildNames.add("worker1");
        expectedChildNames.add("worker2");
        expectedChildNames.add("collector");
        checkChildNames(expectedChildNames);
        String expectedHeader = "C>W>M>";
        executeComposite(expectedHeader);

        engine
                .execute("master2 = gcm-new(\"functionalTests.component.fscript.model.components.adl.Master\", $gcma);");
        engine.execute("set-name($master2, \"master2\");");
        engine.execute("replace($composite/child::master, $master2);");

        engine.execute("gcmvn = $gcma/gcmvn::VN2;");
        engine
                .execute("worker3 = gcm-new(\"functionalTests.component.fscript.model.components.adl.Worker1\", $gcmvn);");
        engine.execute("set-name($worker3, \"worker3\");");
        engine.execute("replace($composite/child::worker1, $worker3);");

        engine.execute("gcmn = $gcma/gcmvn::VN1/gcmnode::*;");
        engine
                .execute("collector2 = gcm-new(\"functionalTests.component.fscript.model.components.adl.Collector\", $gcmn);");
        engine.execute("set-name($collector2, \"collector2\");");
        engine.execute("replace($composite/child::collector, $collector2);");

        expectedChildNames = new ArrayList<String>();
        expectedChildNames.add("master2");
        expectedChildNames.add("worker2");
        expectedChildNames.add("worker3");
        expectedChildNames.add("collector2");
        checkChildNames(expectedChildNames);
        engine.execute("start($composite);");
        executeComposite(expectedHeader);
        engine.execute("stop($composite);");

        engine.execute("kill($composite/child::*;");
        engine.execute("kill($composite)");
    }

    @SuppressWarnings("unchecked")
    private void checkChildNames(List<String> realChildNames) throws FScriptException {
        Set<Node> childNodes = (Set<Node>) engine.execute("$composite/child::*;");
        assertNotNull(childNodes);
        assertEquals(realChildNames.size(), childNodes.size());
        for (Node childNode : childNodes) {
            assertTrue(realChildNames.contains(((GCMComponentNode) childNode).getName()));
        }
    }

    protected void executeComposite(String expectedHeader) throws FScriptException, NoSuchInterfaceException {
        engine.execute("start($composite);");
        List<String> messages = new ArrayList<String>();
        messages.add("msg1");
        messages.add("msg2");
        List<StringWrapper> processedMessages = ((ServiceMulticast) composite
                .getFcInterface("service-server")).process(messages);
        assertEquals(messages.size(), processedMessages.size());
        for (int i = 0; i < processedMessages.size(); i++) {
            assertEquals(expectedHeader + messages.get(i), processedMessages.get(i).getStringValue());
        }
        engine.execute("stop($composite);");
    }
}
