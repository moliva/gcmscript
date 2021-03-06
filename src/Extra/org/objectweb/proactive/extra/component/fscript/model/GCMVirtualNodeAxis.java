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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.fscript.model.AbstractAxis;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.proactive.gcmdeployment.GCMApplication;
import org.objectweb.proactive.gcmdeployment.GCMVirtualNode;


/**
 * Implements the <code>gcmvn</code> axis in FPath. This axis connects a {@link GCMApplication} to its
 * {@link GCMVirtualNode} (if any). This axis is not modifiable.
 *
 * @author The ProActive Team
 */
public class GCMVirtualNodeAxis extends AbstractAxis {
    /**
     * Creates a new {@link GCMVirtualNodeAxis}.
     *
     * @param model The GCM model the axis is part of.
     */
    public GCMVirtualNodeAxis(FractalModel model) {
        super(model, "gcmvn", "gcmapplication", "gcmvirtualnode");
    }

    /**
     * Indicates that the axis is primitive, i.e. not implemented in terms of another axis.
     *
     * @return <code>true</code> to indicate that the axis is not implemented in terms of another axis.
     */
    public boolean isPrimitive() {
        return true;
    }

    /**
     * Indicates that the axis does not support direct manipulation by the user, i.e. actions are not available in
     * FScript programs to connect and disconnect arcs belonging to the axis.
     *
     * @return <code>false</code> to indicate that the axis does not support direct manipulation by the user.
     */
    public boolean isModifiable() {
        return false;
    }

    /**
     * Returns the GCM virtual node nodes representing all the GCM virtual nodes of the GCM application represented
     * by the given source node.
     *
     * @param source The source node representing the GCM application.
     * @return The GCM virtual node nodes representing all the GCM virtual nodes of the GCM application represented
     * by the given source node.
     */
    public Set<Node> selectFrom(Node source) {
        GCMApplication gcma = ((GCMApplicationNode) source).getGCMApplication();
        GCMVirtualNode[] gcmvns = gcma.getVirtualNodes().values().toArray(new GCMVirtualNode[] {});
        Set<Node> result = new HashSet<Node>();
        GCMNodeFactory nf = (GCMNodeFactory) model;
        for (GCMVirtualNode gcmvn : gcmvns) {
            result.add(nf.createGCMVirtualNodeNode(gcmvn));
        }
        return Collections.unmodifiableSet(result);
    }
}
