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
import org.objectweb.proactive.gcmdeployment.GCMApplication;


/**
 * A {@link Node} which represents a {@link GCMApplication}.
 *
 * @author The ProActive Team
 */
public class GCMApplicationNode extends AbstractNode {
    /** The {@link GCMApplication} the node represents. */
    private final GCMApplication gcma;

    /**
     * Creates a new {@link GCMApplicationNode}.
     *
     * @param model The GCM model the node is part of.
     * @param gcma The {@link GCMApplication} the new node will represent.
     */
    public GCMApplicationNode(FractalModel model, GCMApplication gcma) {
        this(model, gcma, "gcmapplication");
    }

    /**
     * Creates a new {@link GCMApplicationNode}.
     *
     * @param model The GCM model the node is part of.
     * @param gcma The {@link GCMApplication} the new node will represent.
     * @param nodekind The kind of node declared in the model.
     */
    protected GCMApplicationNode(FractalModel model, GCMApplication gcma, String nodekind) {
        super(model.getNodeKind(nodekind));
        checkNotNull(gcma);
        this.gcma = gcma;
    }

    /**
     * Returns the underlying {@link GCMApplication} represented by the node.
     *
     * @return The {@link GCMApplication} represented by the node.
     */
    public GCMApplication getGCMApplication() {
        return gcma;
    }

    /**
     * Returns the current value of one of the node's properties.
     *
     * @param name The name of the property to access.
     * @return The current value of the named property for the node.
     * @throws NoSuchElementException If the node does not have a property of the given name.
     */
    public Object getProperty(String name) {
        if ("descriptor-path".equals(name)) {
            return getDescriptorPath();
        } else if ("state".equals(name)) {
            return "UNKNOW";
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
        if ("state".equals(name)) {
            setState((String) value);
        } else {
            throw new NoSuchElementException("Invalid property name '" + name + "'");
        }
    }

    /**
     * Returns the descriptor path associated to the {@link GCMApplication}.
     *
     * @return The descriptor path associated to the {@link GCMApplication}.
     */
    public String getDescriptorPath() {
        return gcma.getDescriptorURL().getPath();
    }

    /**
     * Sets the {@link GCMApplication} to the requested new state. The only value currently supported is
     * <code>"KILLED"</code>: this terminates all the runtimes that have been started by the
     * {@link GCMApplication}.
     *
     * @param state The requested new state of the {@link GCMApplication}. The only value currently supported is
     * <code>"KILLED"</code>.
     * @throws IllegalArgumentException If the requested state is not supported.
     */
    public void setState(String state) {
        if ("KILLED".equals(state)) {
            gcma.kill();
        } else {
            throw new IllegalArgumentException("Invalid value for 'state': '" + state + "'");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GCMApplicationNode) {
            GCMApplicationNode other = (GCMApplicationNode) obj;
            return this.gcma.equals(other.getGCMApplication());
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return gcma.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "#<gcmapplication: " + getDescriptorPath() + ">";
    }
}
