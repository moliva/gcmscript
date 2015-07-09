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
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.ComponentAxis;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;


/**
 * Implements the <code>component</code> axis which connects interface and attribute nodes to their owner
 * component. This axis is not modifiable.
 * <br>
 * This is an extension of {@link ComponentAxis} to instantiate {@link GCMComponentNode} instead of
 * {@link ComponentNode} if the component is a GCM component.
 *
 * @author The ProActive Team
 */
public class GCMComponentAxis extends ComponentAxis {
    /**
     * Creates a new {@link GCMComponentAxis}.
     *
     * @param model The GCM model the axis is part of.
     */
    public GCMComponentAxis(FractalModel model) {
        super(model);
    }

    /**
     * Returns the component node representing the component owner of the element represented by the given source
     * node.
     *
     * @param source The source node representing the element.
     * @return The component node representing the component owner of the element represented by the given source
     * node.
     */
    @Override
    public Set<Node> selectFrom(Node source) {
        Component comp = null;
        if (source instanceof GCMComponentNode) {
            comp = ((GCMComponentNode) source).getComponent();
        } else if (source instanceof GCMAttributeNode) {
            comp = ((GCMAttributeNode) source).getOwner();
        } else if (source instanceof GCMInterfaceNode) {
            comp = ((GCMInterfaceNode) source).getInterface().getFcItfOwner();
        } else {
            return super.selectFrom(source);
        }
        GCMNodeFactory nf = (GCMNodeFactory) model;
        return Collections.singleton((Node) nf.createGCMComponentNode(comp));
    }
}
