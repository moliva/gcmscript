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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.etsi.uri.gcm.util.GCM;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;
import org.objectweb.proactive.extensions.webservices.WSConstants;
import org.objectweb.proactive.extra.component.fscript.Utils;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;
import org.objectweb.proactive.extra.component.fscript.model.GCMInterfaceNode;

import functionalTests.component.fscript.model.components.ServiceMulticast;


public class TestWSAction extends CommonSetup {
    private GCMComponentNode node;
    private GCMComponentNode primitiveNode;
    private NativeProcedure wsExpose;
    private NativeProcedure wsUnexpose;
    private NativeProcedure wsBind;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        GCM.getNameController(serverWebService).setFcName("ServerWS");
        node = new GCMComponentNode(model, composite);
        primitiveNode = new GCMComponentNode(model, serverWebService);
        wsExpose = model.getNativeProcedure("ws-expose");
        wsUnexpose = model.getNativeProcedure("ws-unexpose");
        wsBind = model.getNativeProcedure("ws-bind");
    }

    @Test
    public void exposeInterface() throws NoSuchInterfaceException, ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        GCMInterfaceNode itfNode = new GCMInterfaceNode(model, (Interface) serverWebService
                .getFcInterface("service1"));
        args.add(itfNode);
        wsExpose.apply(args, null);
        wsUnexpose.apply(args, null);
    }

    @Test
    public void exposeInterfaces() throws NoSuchInterfaceException, ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        Set<Node> nodes = new HashSet<Node>();
        GCMInterfaceNode itfNode = new GCMInterfaceNode(model, (Interface) serverWebService
                .getFcInterface("service1"));
        nodes.add(itfNode);
        itfNode = new GCMInterfaceNode(model, (Interface) serverWebService.getFcInterface("service2"));
        nodes.add(itfNode);
        args.add(nodes);
        wsExpose.apply(args, null);
        wsUnexpose.apply(args, null);
    }

    @Test
    public void exposeAllInterfaces() throws ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        args.add(primitiveNode);
        wsExpose.apply(args, null);
        wsUnexpose.apply(args, null);
    }

    @Test
    public void exposeInterfaceAndBind() throws NoSuchInterfaceException, ScriptExecutionError,
            IllegalBindingException, IllegalLifeCycleException {
        List<Object> args = new ArrayList<Object>();
        GCMInterfaceNode itfNode = new GCMInterfaceNode(model, (Interface) serverWebService
                .getFcInterface("service1"));
        args.add(itfNode);
        wsExpose.apply(args, null);
        args.remove(itfNode);

        Component[] subComponents = GCM.getContentController(composite).getFcSubComponents();
        for (int i = 0; i < subComponents.length; i++) {
            if (GCM.getNameController(subComponents[i]).getFcName().equals("collector")) {
                GCM.getBindingController(subComponents[i]).bindFc("service-client",
                        composite.getFcInterface("service-client"));
            }
        }
        String url = Utils.getPAWebServicesController(serverWebService).getUrl() + WSConstants.SERVICES_PATH +
            GCM.getNameController(serverWebService).getFcName() + "_service1(DynamicCXF)";
        args.add(new GCMInterfaceNode(model, (Interface) composite.getFcInterface("service-client")));
        args.add(url);
        wsBind.apply(args, null);

        GCM.getLifeCycleController(serverWebService).startFc();
        GCM.getLifeCycleController(composite).startFc();
        List<String> messages = new ArrayList<String>();
        messages.add("msg1");
        messages.add("msg2");
        List<StringWrapper> processedMessages = ((ServiceMulticast) composite
                .getFcInterface("service-server")).process(messages);
        assertEquals(messages.size(), processedMessages.size());
        for (int i = 0; i < processedMessages.size(); i++) {
            assertEquals("S>C>W>M>" + messages.get(i), processedMessages.get(i).getStringValue());
        }
    }

    @Test(expected = ScriptExecutionError.class)
    public void exposeNoPAWebServicesController() throws ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        args.add(node);
        wsExpose.apply(args, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void exposeInterfacesNotSameComponent() throws NoSuchInterfaceException, ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        Set<Node> nodes = new HashSet<Node>();
        GCMInterfaceNode itfNode = new GCMInterfaceNode(model, (Interface) composite
                .getFcInterface("service-server"));
        nodes.add(itfNode);
        itfNode = new GCMInterfaceNode(model, (Interface) serverWebService.getFcInterface("service2"));
        nodes.add(itfNode);
        args.add(nodes);
        wsExpose.apply(args, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void unexposeNoPAWebServicesController() throws ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        args.add(node);
        wsUnexpose.apply(args, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void unexposeInterfacesNotSameComponent() throws NoSuchInterfaceException, ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        Set<Node> nodes = new HashSet<Node>();
        GCMInterfaceNode itfNode = new GCMInterfaceNode(model, (Interface) composite
                .getFcInterface("service-server"));
        nodes.add(itfNode);
        itfNode = new GCMInterfaceNode(model, (Interface) serverWebService.getFcInterface("service2"));
        nodes.add(itfNode);
        args.add(nodes);
        wsUnexpose.apply(args, null);
    }

    @Test(expected = ScriptExecutionError.class)
    public void bindURLError() throws NoSuchInterfaceException, ScriptExecutionError {
        List<Object> args = new ArrayList<Object>();
        String url = "URLError";
        args
                .add(new GCMInterfaceNode(model, (Interface) node.getComponent().getFcInterface(
                        "service-client")));
        args.add(url);
        wsBind.apply(args, null);
    }
}
