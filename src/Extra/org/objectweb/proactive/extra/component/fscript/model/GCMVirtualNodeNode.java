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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.NoSuchElementException;

import org.objectweb.fractal.fscript.model.AbstractNode;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.proactive.gcmdeployment.GCMVirtualNode;


/**
 * A {@link Node} which represents a {@link GCMVirtualNode}.
 *
 * @author The ProActive Team
 */
public class GCMVirtualNodeNode extends AbstractNode {
    /** The {@link GCMVirtualNode} the node represents. */
    private final GCMVirtualNode gcmvn;

    /**
     * Creates a new {@link GCMVirtualNode}.
     *
     * @param model The GCM model the node is part of.
     * @param gcmvn The {@link GCMVirtualNode} the new node will represent.
     */
    public GCMVirtualNodeNode(FractalModel model, GCMVirtualNode gcmvn) {
        this(model, gcmvn, "gcmvirtualnode");
    }

    /**
     * Creates a new {@link GCMVirtualNode}.
     *
     * @param model The GCM model the node is part of.
     * @param gcmvn The {@link GCMVirtualNode} the new node will represent.
     * @param nodekind The kind of node declared in the model.
     */
    protected GCMVirtualNodeNode(FractalModel model, GCMVirtualNode gcmvn, String nodekind) {
        super(model.getNodeKind(nodekind));
        checkNotNull(gcmvn);
        this.gcmvn = gcmvn;
    }

    /**
     * Returns the underlying {@link GCMVirtualNode} represented by the node.
     *
     * @return The {@link GCMVirtualNode} represented by the node.
     */
    public GCMVirtualNode getGCMVirtualNode() {
        return gcmvn;
    }

    /**
     * Returns the current value of one of the node's properties.
     *
     * @param name The name of the property to access.
     * @return The current value of the named property for the node.
     * @throws NoSuchElementException If the node does not have a property of the given name.
     */
    public Object getProperty(String name) {
        if ("name".equals(name)) {
            return getName();
        } else if ("state".equals(name)) {
            return getState();
        } else {
            throw new NoSuchElementException("Invalid property name '" + name + "'");
        }
    }

    /**
     * Changes the value of one of the node's properties.
     *
     * @param name The name of the property to change.
     * @param value The new value to set for the property.
     * @throws NoSuchElementException If the node does not have a property of the given name.
     * @throws IllegalArgumentException If the requested value is invalid for the named property.
     * @throws UnsupportedOperationException If the specified property exists, but is not modifiable.
     */
    public void setProperty(String name, Object value) {
        checkSetRequest(name, value);
    }

    /**
     * Returns the name of the {@link GCMVirtualNode}.
     *
     * @return The name of the {@link GCMVirtualNode}.
     */
    public String getName() {
        return gcmvn.getName();
    }

    /**
     * Returns the state of the {@link GCMVirtualNode}.
     *
     * @return The state of the {@link GCMVirtualNode}.
     */
    public String getState() {
        if (gcmvn.isReady()) {
            return "READY";
        } else {
            return "UNREADY";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GCMVirtualNodeNode) {
            GCMVirtualNodeNode other = (GCMVirtualNodeNode) obj;
            return this.gcmvn.equals(other.getGCMVirtualNode());
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return gcmvn.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "#<gcmvirtualnode: " + getName() + ">";
    }
}
