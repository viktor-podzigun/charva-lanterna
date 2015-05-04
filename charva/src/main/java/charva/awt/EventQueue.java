/* class EventQueue
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

package charva.awt;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import charva.awt.event.AWTEvent;
import charva.awt.event.FocusEvent;
import charva.awt.event.InvocationEvent;


/**
 * The EventQueue class queues "AWT" events, which are used to trigger
 * drawing actions. They can be enqueued using the postEvent() method
 * by any thread (e.g the keyboard reading thread enqueues KeyEvents),
 * but only the event-dispatching thread should call getNextEvent(),
 * because only the event-dispatching thread should do any drawing.
 */
public class EventQueue {

//    private static final Log LOG = LogFactory.getLog(EventQueue.class);

    private static EventQueue   instance;
    
    private final LinkedList    queue = new LinkedList();
    
    private final List          queueListeners = new ArrayList();
    
    
    /**
     * The constructor cannot be called from outside the class, making
     * this an example of the Singleton pattern.
     */
    private EventQueue() {
    }

    /**
     * Registers an EventQueueListener object for this queue.
     */
    public void addEventQueueListener(EventQueueListener listener) {
        queueListeners.add(listener);
    }

    /**
     * Unregisters an EventQueueListener object from this queue.
     */
    public void removeEventQueueListener(EventQueueListener listener) {
        queueListeners.remove(listener);
    }
    
    private void fireEventPosted() {
        for (int i = queueListeners.size() - 1; i >= 0; i--) {
            ((EventQueueListener)queueListeners.get(i)).eventPosted();
        }
    }

    public synchronized static EventQueue getInstance() {
        if (instance == null) {
            instance = new EventQueue();
        }
        
        return instance;
    }

    public synchronized void postEvent(AWTEvent evt) {
        if (evt.getID() == FocusEvent.FOCUS_GAINED) {
            Toolkit.getDefaultToolkit().setLastFocusEvent((FocusEvent) evt);
        }
        
        addLast(evt);
        
        fireEventPosted();
        
        // wake up the dequeueing thread
        //notifyAll();
    }

    /**
     * Block until an event is available, then return the event.
     *
     * @return the next available AWTEvent
     */
//    public synchronized AWTEvent waitForNextEvent() {
//
//        /* If the queue is empty, block until another thread enqueues
//         * an event.
//         */
//        while (queuesAreEmpty()) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                LOG.error("should never be interrupted!", e); // should never happen
//            }
//        }
//        return (AWTEvent) removeFirst();
//    }

    /**
     * This method is only called if we know that an event is available
     *
     * @return the first AWTEvent on the queue
     */
    synchronized AWTEvent getNextEvent() {
        return (AWTEvent) removeFirst();
    }

    synchronized void clear() {
        queue.clear();
    }

    /**
     * Causes the runnable's run() method to be called in the
     * AWT dispatch thread
     */
    public static void invokeLater(Runnable runnable) {
        getInstance().postEvent(new InvocationEvent(
                Toolkit.getDefaultToolkit(), runnable));
    }

    public static void invokeAndWait(Runnable runnable)
            throws InterruptedException, InvocationTargetException {

//        if (EventQueue.isDispatchThread()) {
//            throw new Error(
//                    "Cannot call invokeAndWait from the event dispatcher thread");
//        }

        class AWTInvocationLock {
        }
        
        Object lock = new AWTInvocationLock();

        InvocationEvent event = new InvocationEvent(
                Toolkit.getDefaultToolkit(), runnable, lock, true);

        synchronized (lock) {
            getInstance().postEvent(event);
            lock.wait();
        }

        Exception eventException = event.getException();
        if (eventException != null) {
            throw new InvocationTargetException(eventException);
        }
    }

    /**
     * Returns <tt>true</tt> if this event queue is empty 
     * and <tt>false</tt> otherwise
     */
    public synchronized boolean isEmpty() {
        return (queue.size() == 0);
    }

    /**
     * Enqueue the event onto one of two queues, depending on its type
     */
    private void addLast(AWTEvent evt) {
        queue.addLast(evt);
    }

    /**
     * This is called only if at least one of the queues is non-empty
     */
    private Object removeFirst() {
        return (queue.size() > 0 ? queue.removeFirst() : null);
    }
}
