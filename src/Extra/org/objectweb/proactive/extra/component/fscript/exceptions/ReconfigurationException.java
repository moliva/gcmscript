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
package org.objectweb.proactive.extra.component.fscript.exceptions;

import org.objectweb.proactive.annotation.PublicAPI;


/**
 * Thrown when an error related to reconfigurations occurs.
 *
 * @author The ProActive Team
 */
@PublicAPI
public class ReconfigurationException extends Exception {
    /**
     * Constructs a {@link ReconfigurationException} with <code>null</code> as its detail message.
     */
    public ReconfigurationException() {
        super();
    }

    /**
     * Constructs a {@link ReconfigurationException} with the specified detail message.
     *
     * @param message Detail message.
     */
    public ReconfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a {@link ReconfigurationException} with the specified detail message and cause.
     *
     * @param message Detail message.
     * @param cause Cause.
     */
    public ReconfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@link ReconfigurationException} with the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString())</tt>.
     *
     * @param cause Cause.
     */
    public ReconfigurationException(Throwable cause) {
        super(cause);
    }
}
