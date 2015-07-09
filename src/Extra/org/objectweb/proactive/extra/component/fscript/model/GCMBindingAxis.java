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
package org.objectweb.proactive.extra.component.fscript.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.etsi.uri.gcm.api.control.MulticastController;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.BindingAxis;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;


/**
 * Implements the <code>binding</code> axis in FPath. This axis connects client interfaces to the server
 * interface(s) they are bound to. The {@link #connect(Node, Node)} and {@link #disconnect(Node, Node)}
 * operations on this axis are used to create and destroy bindings.
 * <br>
 * This is an extension of {@link BindingAxis} to instantiate {@link GCMInterfaceNode} instead of
 * {@link InterfaceNode} if the component is a GCM component and to take care of collective interface bindings.
 *
 * @author The ProActive Team
 */
public class GCMBindingAxis extends BindingAxis {
    /**
     * Creates a new {@link GCMBindingAxis}.
     *
     * @param model The GCM model the axis is part of.
     */
    public GCMBindingAxis(FractalModel model) {
        super(model);
    }

    /**
     * Returns the interface nodes representing all the server interfaces bound to the client interface represented
     * by the given source node.
     *
     * @param source The source node representing the client interface.
     * @return The interface nodes representing all the server interfaces bound to the client interface represented
     * by the given source node.
     */
    @Override
    public Set<Node> selectFrom(Node source) {
        if (source instanceof GCMInterfaceNode) {
            GCMInterfaceNode itfNode = (GCMInterfaceNode) source;

            Component comp = itfNode.getInterface().getFcItfOwner();
            if (!itfNode.isClient()) {
                try {
                    GCM.getContentController(comp);
                } catch (NoSuchInterfaceException nsie) {
                    return Collections.emptySet();
                }
            }

            BindingController bc = null;
            try {
                bc = GCM.getBindingController(comp);
            } catch (NoSuchInterfaceException nsie) {
                throw new AssertionError(
                    "Component with a client interface does not implement binding-controller");
            }

            if (itfNode.isCollection()) {
                Set<Node> result = new HashSet<Node>();
                for (Object rawCandidate : getCandidateInstances(itfNode, comp)) {
                    Interface itfCandidate = (Interface) rawCandidate;
                    if (isInstance(itfCandidate, itfNode)) {
                        Node serverNode = getServerInterface(bc, itfCandidate.getFcItfName());
                        if (serverNode != null) {
                            result.add(serverNode);
                        }
                    }
                }
                return Collections.unmodifiableSet(result);
            } else if (itfNode.isMulticast()) {
                MulticastController mc = null;
                try {
                    mc = GCM.getMulticastController(comp);
                } catch (NoSuchInterfaceException nsie) {
                    throw new AssertionError(
                        "Component with a multicast client interface does not implement multicast-controller");
                }
                return getServerInterfaces(mc, itfNode.getName());
            } else {
                Node serverNode = getServerInterface(bc, itfNode.getName());
                if (serverNode != null) {
                    return Collections.singleton(serverNode);
                } else {
                    return Collections.emptySet();
                }
            }
        } else {
            return super.selectFrom(source);
        }
    }

    /**
     * Returns the interface nodes representing all the server interfaces bound to the given multicast client
     * interface.
     *
     * @param mc {@link MulticastController} of the component owner of the multicast client interface.
     * @param multicastItfName Multicast client interface name.
     * @return The interface nodes representing all the server interfaces bound to the given multicast client
     * interface.
     */
    protected Set<Node> getServerInterfaces(MulticastController mc, String multicastItfName) {
        try {
            Object[] serverItfs = mc.lookupGCMMulticast(multicastItfName);
            List<GCMInterfaceNode> serverItfNodes = new ArrayList<GCMInterfaceNode>();
            GCMNodeFactory nf = (GCMNodeFactory) model;
            for (int i = 0; i < serverItfs.length; i++) {
                serverItfNodes.add((GCMInterfaceNode) nf.createInterfaceNode((Interface) serverItfs[i]));
            }
            return new HashSet<Node>(serverItfNodes);
        } catch (NoSuchInterfaceException nsie) {
            throw new AssertionError("Node references non-existing interface");
        }
    }

    /**
     * Unbinds the client interface represented by the given source node from the server interface represented by
     * the given destination node.
     *
     * @param source The source node representing the client interface.
     * @param dest The destination node representing the server interface.
     * @throws IllegalArgumentException If it is not possible to unbind the specified client interface from the
     * specified server interface.
     */
    @Override
    public void disconnect(Node source, Node dest) {
        if ((source instanceof GCMInterfaceNode) && ((GCMInterfaceNode) source).isMulticast()) {
            Interface clientItf = ((GCMInterfaceNode) source).getInterface();
            try {
                MulticastController mc = GCM.getMulticastController(clientItf.getFcItfOwner());
                mc.unbindGCMMulticast(clientItf.getFcItfName(), ((GCMInterfaceNode) dest).getInterface());
            } catch (NoSuchInterfaceException nsie) {
                throw new IllegalArgumentException(nsie);
            } catch (IllegalBindingException ibe) {
                throw new IllegalArgumentException(ibe);
            } catch (IllegalLifeCycleException ilce) {
                throw new IllegalArgumentException(ilce);
            }
        } else {
            super.disconnect(source, dest);
        }
    }
}
