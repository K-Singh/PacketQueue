package com.company;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Vector;

/**
 * The Router Class represents a Queue of Packets. The Router works in the same way as a Queue,
 * being a FIFO structure.
 *
 * @author Kirat Singh, ID: 11232062, E-mail: kirat.singh@stonybrook.edu
 */
public class Router extends Vector {

    /**
     * Adds a Packet to the Router
     * @param p
     *      The packet to be added
     */
    public void enqueue(Packet p) {
        this.addElement(p);
    }

    /**
     * Removes the first packet from the Router
     * @return
     *      The packet that was removed.
     */
    public Packet dequeue() {
        Packet p = (Packet) this.get(0);
        this.remove(0);
        this.trimToSize();
        return p;
    }

    /**
     *   Gets the packet at the front of the Queue
     * @return
     *      The packet at the front of the Queue.
     */
    public Packet peek() {
        if(this.size() > 0) {
            return (Packet) this.get(0);
        }else return null;
    }

    /**
     * @return
     *      Returns whether or not the Router is empty.
     */
    public boolean isEmpty() {
        return this.isEmpty();
    }

    /**
     * The contents of the router are represented as a String. Each packet is put into the brackets and
     * separated with a comma
     * @return
     *      The String representation of the Router.
     */
    public String toString() {
        String s = "{";
        for (Object p : this.toArray()) {
            if (p instanceof Packet) {
                s += p.toString() + ", ";
            }
        }
        if(s.length() >= 3)
             s = s.substring(0, s.length() - 2);
        s += "}";
        return s;
    }


    /**
     * Given a list of Routers, the router with the most amount of space(the least amount of packets) is found, and its
     * position in the list is returned.
     * @param intermRouters
     *      The list of routers to be searched
     * @return
     *      An integer representing the position of the router with the most amount of space in the list.
     * @throws RouterOverFlowException
     *      Thrown if all routers are full.
     */
    public static int sendPacketTo(ArrayList<Router> intermRouters) throws RouterOverFlowException{

        Router freeRouter = intermRouters.get(0);

        for (Router r : intermRouters) {
            if(r != null) {
                if (r.size() < freeRouter.size()) {
                    freeRouter = r;
                }
            }
        }

        if(freeRouter.size() >= Simulator.maxBufferSize){
            throw new RouterOverFlowException("No routers are free!");
        }
        return intermRouters.indexOf(freeRouter);

    }

}
