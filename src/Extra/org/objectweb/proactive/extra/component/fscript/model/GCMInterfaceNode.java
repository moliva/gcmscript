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

import java.util.NoSuchElementException;

import org.etsi.uri.gcm.api.type.GCMInterfaceType;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;


/**
 * A {@link Node} which represents an interface of a GCM component. Every kinds of interface (internal or external,
 * client or server, etc.) is represented by instances of this class.
 * <br>
 * This is an extension of {@link InterfaceNode} to give a GCM personality to the node.
 *
 * @author The ProActive Team
 */
public class GCMInterfaceNode extends InterfaceNode {
    /**
     * Creates a new {@link GCMInterfaceNode}.
     *
     * @param model The GCM model the node is part of.
     * @param itf The interface the new node will represent.
     */
    public GCMInterfaceNode(FractalModel model, Interface itf) {
        super(model, itf);
    }

    /**
     * Returns the current value of one of the node's properties.
     *
     * @param name The name of the property to access.
     * @return The current value of the named property for the node.
     * @throws NoSuchElementException If the node does not have a property of the given name.
     */
    @Override
    public Object getProperty(String name) {
        if ("singleton".equals(name)) {
            return isSingleton();
        } else if ("multicast".equals(name)) {
            return isMulticast();
        } else if ("gathercast".equals(name)) {
            return isGathercast();
        } else {
            return super.getProperty(name);
        }
    }

    /**
     * Checks whether the interface is a singleton interface.
     *
     * @return <code>true</code> if the interface is a singleton interface.
     */
    public boolean isSingleton() {
        if (itfType instanceof GCMInterfaceType) {
            return ((GCMInterfaceType) itfType).isGCMSingletonItf();
        } else {
            return true;
        }
    }

    /**
     * Checks whether the interface is a multicast interface.
     *
     * @return <code>true</code> if the interface is a multicast interface.
     */
    public boolean isMulticast() {
        if (itfType instanceof GCMInterfaceType) {
            return ((GCMInterfaceType) itfType).isGCMMulticastItf();
        } else {
            return false;
        }
    }

    /**
     * Checks whether the interface is a gathercast interface.
     *
     * @return <code>true</code> if the interface is a gathercast interface.
     */
    public boolean isGathercast() {
        if (itfType instanceof GCMInterfaceType) {
            return ((GCMInterfaceType) itfType).isGCMGathercastItf();
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GCMInterfaceNode) {
            return super.equals(obj);
        } else {
            return false;
        }
    }
}
