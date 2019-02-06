package com.company;

/**
 * The Packet class represents a packet of data being sent through the network.
 * Packets have a specific id representing how many packets have been made before them.
 * They also have a size, a time of arrival, and a time until they reach the destination. The time to destination
 * is defined as the packetsize/100. The time of arrival is the time unit that the packet arrived at.
 */
public class Packet {


    private static int packetCounter = 0;
    private int id;
    private int packetSize;
    private int timeArrive;
    private int timeToDest;

    /**
     * The empty constructor for the Packet.
     */
    public Packet(){

    }

    /**
     * The constructor for the Packet that assigns the various instance variables.
     * The id is also set in the constructor, and it is equal to the packetCounter, the total number of packets made.
     * @param size
     *      The size of the packet
     * @param arrival
     *      The time of arrival of the packet
     * @param dest
     *      The time until the packet reaches the destination router.
     */
    public Packet(int size, int arrival, int dest){
        packetCounter++;
        id = packetCounter;
        packetSize = size;
        timeArrive = arrival;
        timeToDest = dest;

    }

    /**
     * @return
     *      Returns the total packet count.
     */
    public static int getPacketCounter() {
        return packetCounter;
    }

    /**
     * @return
     *      Returns the time the packet arrived.
     */
    public int getTimeArrive() {
        return timeArrive;
    }


    /**
     * @return
     *      Returns the packet's size.
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * @return
     *      Returns the time until the packet reaches its destination
     */
    public int getTimeToDest() {
        return timeToDest;
    }

    /**
     * @return
     *      Returns the id of the packet.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the packet counter for all packets
     * @param packetCounter
     *      The new value for the packet counter.
     */
    public static void setPacketCounter(int packetCounter) {
        Packet.packetCounter = packetCounter;
    }

    /**
     * Sets the packet's size
     * @param packetSize
     *      The new size of the packet.
     */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    /**
     * Sets the time that the packet arrived.
     * @param timeArrive
     *      The new time of arrival
     */
    public void setTimeArrive(int timeArrive) {
        this.timeArrive = timeArrive;
    }

    /**
     * Sets the time until the Packet reaches its destination
     * @param timeToDest
     *      The new time of destination
     */
    public void setTimeToDest(int timeToDest) {
        this.timeToDest = timeToDest;
    }

    /**
     * Gives a String representation of the Packet, containing its id, time of arrival, and time until
     * destination.
     * @return
     *      The String representation of the Packet.
     */
    @Override
    public String toString() {
        return "["+id+", "+ timeArrive+", "+timeToDest+"]";
    }
}
