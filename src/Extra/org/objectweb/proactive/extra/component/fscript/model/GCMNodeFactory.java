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

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.gcmdeployment.GCMApplication;
import org.objectweb.proactive.gcmdeployment.GCMVirtualNode;


/**
 * A <em>factory</em> for Fractal/GCM-specific {@link org.objectweb.fractal.fscript.model.Node nodes}. This
 * interface can be used by clients to wrap objects from the Fractal or GCM APIs in
 * {@link org.objectweb.fractal.fscript.model.Node nodes} appropriate for FPath to manipulate in a uniform way.
 * <br>
 * This is an extension of {@link NodeFactory} to add GCM-specific
 * {@link org.objectweb.fractal.fscript.model.Node nodes}.
 *
 * @author The ProActive Team
 */
public interface GCMNodeFactory extends NodeFactory {
    /**
     * Creates a {@link GCMApplicationNode} to represent a {@link GCMApplication}.
     *
     * @param gcma A {@link GCMApplication}.
     * @return A {@link GCMApplicationNode} representing the {@link GCMApplication}.
     */
    public GCMApplicationNode createGCMApplicationNode(GCMApplication gcma);

    /**
     * Creates a {@link GCMVirtualNodeNode} to represent a {@link GCMVirtualNode}.
     *
     * @param gcmvn A {@link GCMVirtualNode}.
     * @return A {@link GCMVirtualNodeNode} representing the {@link GCMVirtualNode}.
     */
    public GCMVirtualNodeNode createGCMVirtualNodeNode(GCMVirtualNode gcmvn);

    /**
     * Creates a {@link GCMNodeNode} to represent a GCM {@link Node}.
     *
     * @param gcmnode A GCM {@link Node}.
     * @return A {@link GCMNodeNode} representing the GCM {@link Node}.
     */
    public GCMNodeNode createGCMNodeNode(Node gcmnode);

    /**
     * Creates a {@link GCMComponentNode} to represent a GCM component.
     *
     * @param comp A GCM component.
     * @return A {@link GCMComponentNode} representing the GCM component.
     */
    public GCMComponentNode createGCMComponentNode(Component comp);

    /**
     * Creates a {@link GCMAttributeNode} to represent an attribute of a GCM component.
     *
     * @param comp A GCM component.
     * @param attr The name of the attribute to represent.
     * @return A {@link GCMAttributeNode} representing the attribute of the GCM component.
     */
    public GCMAttributeNode createGCMAttributeNode(Component comp, String attr);
}
