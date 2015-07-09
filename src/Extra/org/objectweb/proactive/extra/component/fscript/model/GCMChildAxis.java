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

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.ChildAxis;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;


/**
 * Implements the <code>child</code> axis in FPath. This axis connects composite components to their direct
 * subcomponents. The {@link #connect(Node, Node)} and {@link #disconnect(Node, Node)} operations map to the
 * addition and removal of sub-components from composites.
 * <br>
 * This is an extension of {@link ChildAxis} to instantiate {@link GCMComponentNode} instead of
 * {@link ComponentNode} if the component is a GCM component.
 *
 * @author The ProActive Team
 */
public class GCMChildAxis extends ChildAxis {
    /**
     * Creates a new {@link GCMChildAxis}.
     *
     * @param model The GCM model the axis is part of.
     */
    public GCMChildAxis(FractalModel model) {
        super(model);
    }

    /**
     * Returns the component nodes representing all the subcomponents of the component represented by the given
     * source node.
     *
     * @param source The source node representing the component.
     * @return The component nodes representing all the subcomponents of the component represented by the given
     * source node.
     */
    @Override
    public Set<Node> selectFrom(Node source) {
        if (source instanceof GCMComponentNode) {
            Component parent = ((ComponentNode) source).getComponent();
            try {
                ContentController cc = GCM.getContentController(parent);
                Set<Node> result = new HashSet<Node>();
                GCMNodeFactory nf = (GCMNodeFactory) model;
                for (Component child : cc.getFcSubComponents()) {
                    result.add(nf.createGCMComponentNode(child));
                }
                return result;
            } catch (NoSuchInterfaceException nsie) {
                // Not a composite, no children
                return Collections.emptySet();
            }
        } else {
            return super.selectFrom(source);
        }
    }
}
