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

import java.util.HashSet;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.AttributeAxis;
import org.objectweb.fractal.fscript.model.fractal.AttributeNode;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.fractal.util.AttributesHelper;


/**
 * Implements the <code>attribute</code> axis in FPath. This axis connects a component to its attributes (if any),
 * as defined by its <code>attribute-controller</code> interface. This axis is not modifiable.
 * <br>
 * This is an extension of {@link AttributeAxis} to instantiate {@link GCMAttributeNode} instead of
 * {@link AttributeNode} if the component is a GCM component.
 *
 * @author The ProActive Team
 */
public class GCMAttributeAxis extends AttributeAxis {
    /**
     * Creates a new {@link GCMAttributeAxis}.
     *
     * @param model The GCM model the axis is part of.
     */
    public GCMAttributeAxis(FractalModel model) {
        super(model);
    }

    /**
     * Returns the attribute nodes representing all the attributes related to the element represented by the given
     * source node.
     *
     * @param source The source node representing the element.
     * @return The attribute nodes representing all the attributes related to the element represented by the given
     * source node.
     */
    @Override
    public Set<Node> selectFrom(Node source) {
        Component comp = null;
        if (source instanceof GCMComponentNode) {
            comp = ((GCMComponentNode) source).getComponent();
        } else if (source instanceof GCMInterfaceNode) {
            comp = ((GCMInterfaceNode) source).getInterface().getFcItfOwner();
        } else {
            return super.selectFrom(source);
        }
        AttributesHelper attrHelper = new AttributesHelper(comp);
        Set<Node> result = new HashSet<Node>();
        for (Object attrName : attrHelper.getAttributesNames()) {
            // Here we explicitly do not use the NodeFactory interface in order to reuse
            // the AttributeHelper, as these can be costly to create
            Node node = new GCMAttributeNode((FractalModel) model, attrHelper, (String) attrName);
            result.add(node);
        }
        return result;
    }
}
