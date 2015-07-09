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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.etsi.uri.gcm.api.control.MulticastController;
import org.etsi.uri.gcm.api.type.GCMInterfaceType;
import org.etsi.uri.gcm.util.GCM;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.model.Axis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.proactive.extra.component.fscript.Utils;
import org.objectweb.proactive.extra.component.fscript.model.GCMInterfaceNode;


public class TestGCMBindingAxis extends CommonSetup {
    private Component comp;
    private Map<Interface, GCMInterfaceNode> itfs = new HashMap<Interface, GCMInterfaceNode>();
    private Axis axis;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        comp = composite;
        for (Object rawItf : GCM.getContentController(comp).getFcInternalInterfaces()) {
            Interface itf = (Interface) rawItf;
            if (!Utils.isControllerItfName(itf.getFcItfName())) {
                itfs.put(itf, new GCMInterfaceNode(model, itf));
            }
        }
        for (Component child : GCM.getContentController(comp).getFcSubComponents()) {
            for (Object rawItf : child.getFcInterfaces()) {
                Interface itf = (Interface) rawItf;
                GCMInterfaceType itfType = (GCMInterfaceType) itf.getFcItfType();
                if (itfType.isFcClientItf() && !Utils.isControllerItfName(itf.getFcItfName())) {
                    itfs.put(itf, new GCMInterfaceNode(model, itf));
                }
            }
        }
        axis = model.getAxis("binding");
    }

    @Test
    public void checkBindings() throws NoSuchInterfaceException {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            Interface itf = entry.getKey();
            Object[] realServerItfs = new Object[0];
            if (((GCMInterfaceType) itf.getFcItfType()).isGCMMulticastItf()) {
                realServerItfs = GCM.getMulticastController(itf.getFcItfOwner()).lookupGCMMulticast(
                        itf.getFcItfName());
            } else {
                realServerItfs = new Object[] { GCM.getBindingController(itf.getFcItfOwner()).lookupFc(
                        itf.getFcItfName()) };
            }
            Set<Node> serverItfNodes = axis.selectFrom(entry.getValue());
            int nbRealServerItfs = 0;
            for (int i = 0; i < realServerItfs.length; i++) {
                if (realServerItfs[i] != null) {
                    assertTrue(serverItfNodes.contains(new GCMInterfaceNode(model,
                        ((Interface) realServerItfs[i]))));
                    nbRealServerItfs++;
                }
            }
            assertEquals(nbRealServerItfs, serverItfNodes.size());
        }
    }

    @Test
    public void addBindings() throws NoSuchInterfaceException, IllegalLifeCycleException,
            IllegalBindingException {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            Interface itf = entry.getKey();
            Object[] realServerItfs = new Object[0];
            if (((GCMInterfaceType) itf.getFcItfType()).isGCMMulticastItf()) {
                MulticastController mc = GCM.getMulticastController(itf.getFcItfOwner());
                realServerItfs = mc.lookupGCMMulticast(itf.getFcItfName());
                for (int i = 0; i < realServerItfs.length; i++) {
                    mc.unbindGCMMulticast(itf.getFcItfName(), realServerItfs[i]);
                }
            } else {
                BindingController bc = GCM.getBindingController(itf.getFcItfOwner());
                realServerItfs = new Object[] { bc.lookupFc(itf.getFcItfName()) };
                if (realServerItfs[0] != null) {
                    bc.unbindFc(itf.getFcItfName());
                }
            }
            for (int i = 0; i < realServerItfs.length; i++) {
                if (realServerItfs[i] != null) {
                    axis
                            .connect(entry.getValue(), new GCMInterfaceNode(model,
                                (Interface) realServerItfs[i]));
                }
            }
            if (((GCMInterfaceType) itf.getFcItfType()).isGCMMulticastItf()) {
                assertArrayEquals(realServerItfs, GCM.getMulticastController(itf.getFcItfOwner())
                        .lookupGCMMulticast(itf.getFcItfName()));
            } else {
                assertEquals(realServerItfs[0], GCM.getBindingController(itf.getFcItfOwner()).lookupFc(
                        itf.getFcItfName()));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBindingNotStopped() throws NoSuchInterfaceException, IllegalLifeCycleException {
        GCM.getLifeCycleController(comp).startFc();
        axis.connect(new GCMInterfaceNode(model, ((Interface) comp.getFcInterface("service-client"))),
                new GCMInterfaceNode(model, ((Interface) comp.getFcInterface("service-server"))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBindingAlreadyBound() throws NoSuchInterfaceException {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            Interface itf = entry.getKey();
            Object realServerItf = GCM.getBindingController(itf.getFcItfOwner()).lookupFc(itf.getFcItfName());
            if (realServerItf != null) {
                axis.connect(entry.getValue(), new GCMInterfaceNode(model, (Interface) realServerItf));
            }
        }
    }

    @Test
    public void removeBindings() throws NoSuchInterfaceException {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            Interface itf = entry.getKey();
            Object[] realServerItfs = new Object[0];
            if (((GCMInterfaceType) itf.getFcItfType()).isGCMMulticastItf()) {
                realServerItfs = GCM.getMulticastController(itf.getFcItfOwner()).lookupGCMMulticast(
                        itf.getFcItfName());
            } else {
                realServerItfs = new Object[] { GCM.getBindingController(itf.getFcItfOwner()).lookupFc(
                        itf.getFcItfName()) };
            }
            for (int i = 0; i < realServerItfs.length; i++) {
                if (realServerItfs[i] != null) {
                    axis.disconnect(entry.getValue(), new GCMInterfaceNode(model,
                        (Interface) realServerItfs[i]));
                }
            }
            if (((GCMInterfaceType) itf.getFcItfType()).isGCMMulticastItf()) {
                assertArrayEquals(new Object[] {}, GCM.getMulticastController(itf.getFcItfOwner())
                        .lookupGCMMulticast(itf.getFcItfName()));
            } else {
                assertNull(GCM.getBindingController(itf.getFcItfOwner()).lookupFc(itf.getFcItfName()));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeBindingNotStopped() throws NoSuchInterfaceException, IllegalLifeCycleException {
        GCM.getLifeCycleController(comp).startFc();
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            Interface itf = entry.getKey();
            Object[] realServerItfs = new Object[0];
            if (((GCMInterfaceType) itf.getFcItfType()).isGCMMulticastItf()) {
                MulticastController mc = GCM.getMulticastController(itf.getFcItfOwner());
                realServerItfs = mc.lookupGCMMulticast(itf.getFcItfName());
            } else {
                BindingController bc = GCM.getBindingController(itf.getFcItfOwner());
                realServerItfs = new Object[] { bc.lookupFc(itf.getFcItfName()) };
            }
            for (int i = 0; i < realServerItfs.length; i++) {
                if (realServerItfs[i] != null) {
                    axis.disconnect(entry.getValue(), new GCMInterfaceNode(model,
                        (Interface) realServerItfs[i]));
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeBindingNotBound() throws NoSuchInterfaceException, IllegalLifeCycleException,
            IllegalBindingException {
        for (Map.Entry<Interface, GCMInterfaceNode> entry : itfs.entrySet()) {
            Interface itf = entry.getKey();
            Object[] realServerItfs = new Object[0];
            if (((GCMInterfaceType) itf.getFcItfType()).isGCMMulticastItf()) {
                MulticastController mc = GCM.getMulticastController(itf.getFcItfOwner());
                realServerItfs = mc.lookupGCMMulticast(itf.getFcItfName());
                for (int i = 0; i < realServerItfs.length; i++) {
                    mc.unbindGCMMulticast(itf.getFcItfName(), realServerItfs[i]);
                }
            } else {
                BindingController bc = GCM.getBindingController(itf.getFcItfOwner());
                realServerItfs = new Object[] { bc.lookupFc(itf.getFcItfName()) };
                if (realServerItfs[0] != null) {
                    bc.unbindFc(itf.getFcItfName());
                }
            }
            for (int i = 0; i < realServerItfs.length; i++) {
                if (realServerItfs[i] != null) {
                    axis.disconnect(entry.getValue(), new GCMInterfaceNode(model,
                        (Interface) realServerItfs[i]));
                }
            }
        }
    }
}
