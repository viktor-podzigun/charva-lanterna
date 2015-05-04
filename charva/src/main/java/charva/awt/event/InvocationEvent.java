/* class InvocationEvent
 *
 * Copyright (C) 2001, 2002  R M Pitman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charva.awt.event;


/**
 * An event which executes the <code>run()</code> method on a 
 * <code>Runnable</code> when dispatched by the AWT event dispatcher thread
 */
public class InvocationEvent extends AWTEvent {
    
    private static final long serialVersionUID = 5130067377369294864L;
    
    /**
     * Marks the first integer id for the range of invocation event ids.
     */
    public static final int INVOCATION_FIRST    = 1200;

    /**
     * The default id for all InvocationEvents.
     */
    public static final int INVOCATION_DEFAULT  = INVOCATION_FIRST;

    /**
     * Marks the last integer id for the range of invocation event ids.
     */
    public static final int INVOCATION_LAST     = INVOCATION_DEFAULT;


    /** The Runnable whose run() method will be called      */
    protected final Runnable    runnable;

    /**
     * The (possibly null) object whose notifyAll() method will be called
     * as soon as the Runnable's run() method has returned
     */
    protected final Object      notifier;
    
    /**
     * Set to true if dispatch() catches Exception and stores it in the
     * exception instance variable. If false, Exceptions are propagated up
     * to the EventDispatchThread's dispatch loop.
     */
    protected final boolean     catchExceptions;

    /**
     * The (potentially null) Exception thrown during execution of the
     * Runnable.run() method. This variable will also be null if a particular
     * instance does not catch exceptions.
     */
    private Exception           exception;

    
    /**
     * Constructs an InvocationEvent with the specified source which
     * will execute the Runnable's run() method when dispatched by
     * the AWT dispatch thread.
     *
     * @param source    the source of this event
     * @param runnable  the Runnable whose run() method will be called
     *                  when the run() method has returned
     */
    public InvocationEvent(Object source, Runnable runnable) {
        this(source, runnable, null, false);
    }

    /**
     * Constructs an InvocationEvent with the specified source which will
     * execute the Runnable's run() method when dispatched by the AWT dispatch
     * thread. After the run() method has returned, the notifier's notifyAll()
     * method will be called.
     * 
     * @param source    the source of this event
     * @param runnable  the Runnable whose run() method will be called
     * @param notifier  the object whose notifyAll() method will be called when 
     *                  the run() method has returned
     * @param catchExceptions
     *            specifies whether <code>dispatch</code> should catch Exception
     *            when executing the <code>Runnable</code>'s <code>run</code>
     *            method, or should instead propagate those Exceptions to the
     *            EventDispatchThread's dispatch loop
     */
    public InvocationEvent(Object source, Runnable runnable, 
            Object notifier, boolean catchExceptions) {
	
        super(source, INVOCATION_DEFAULT);
    	
        this.runnable = runnable;
        this.notifier = notifier;
        this.catchExceptions = catchExceptions;
    }

    /**
     * Executes the runnable's run() method and then (if the notifier
     * is non-null) calls the notifier's notifyAll() method.
     */
    public void dispatch() {
        try {
            if (catchExceptions) {
                try {
                    runnable.run();
                } 
                catch (Exception x) {
                    exception = x;
                }
            } else {
                runnable.run();
            }
        } finally {
        	if (notifier != null) {
        	    synchronized (notifier) {
        	        notifier.notifyAll();
        	    }
        	}
        }
    }

    public Exception getException() {
        return (catchExceptions ? exception : null);
    }
    
    /**
     * Returns a parameter string identifying this event.
     * This method is useful for event-logging and for debugging.
     *
     * @return  A string identifying the event and its attributes
     */
    public String paramString() {
        String typeStr;
        switch(id) {
        case INVOCATION_DEFAULT:
            typeStr = "INVOCATION_DEFAULT";
            break;
        default:
            typeStr = "unknown type";
        }

        return typeStr + ",runnable=" + runnable 
                + ",notifier=" + notifier;
    }
}
