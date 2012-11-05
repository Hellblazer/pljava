/*
 * Copyright (c) 2004, 2005, 2006 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root folder of this project or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava;

import java.sql.SQLException;

/**
 * An exception specially suited to be thrown from within a method designated to
 * be a trigger function. The message generated by this exception will contain
 * information on what trigger and what relation it was that caused the
 * exception
 * 
 * @author Thomas Hallgren
 */
public class TriggerException extends SQLException {
    public static final String TRIGGER_ACTION_EXCEPTION = "09000";

    private static boolean     s_recursionLock          = false;

    private static final long  serialVersionUID         = 5543711707414329116L;

    private static final String makeMessage(TriggerData td, String message) {
        StringBuffer bld = new StringBuffer();
        bld.append("In Trigger ");
        if (!s_recursionLock) {
            s_recursionLock = true;
            try {
                bld.append(td.getName());
                bld.append(" on relation ");
                bld.append(td.getTableName());
            } catch (SQLException e) {
                bld.append("(exception while generating exception message)");
            } finally {
                s_recursionLock = false;
            }
        }
        if (message != null) {
            bld.append(": ");
            bld.append(message);
        }
        return bld.toString();
    }

    /**
     * Create an exception based on the <code>TriggerData</code> that was passed
     * to the trigger method.
     * 
     * @param td
     *            The <code>TriggerData</code> that was passed to the trigger
     *            method.
     */
    public TriggerException(TriggerData td) {
        super(makeMessage(td, null), TRIGGER_ACTION_EXCEPTION);
    }

    /**
     * Create an exception based on the <code>TriggerData</code> that was passed
     * to the trigger method and an additional message.
     * 
     * @param td
     *            The <code>TriggerData</code> that was passed to the trigger
     *            method.
     * @param reason
     *            An additional message with info about the exception.
     */
    public TriggerException(TriggerData td, String reason) {
        super(makeMessage(td, reason), TRIGGER_ACTION_EXCEPTION);
    }
}
