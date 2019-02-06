package com.company;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The main driver class. The Simulator takes user input for various variables and creates a simulation
 * of various packets entering different intermediate routers before being sent to their destination.
 *
 * @author Kirat Singh, ID: 112320621, E-mail: kirat.singh@stonybrook.edu
 */
public class Simulator {
    public static final int MAX_PACKETS = 3;
   static  Router dispatcher;
    static ArrayList<Router> routers;
    static int totalServiceTime;
    static int totalPacketsArrived;
    static int packetsDropped;
    static double arrivalProb;
    static int numIntRouters;
    public static int maxBufferSize;
    static int minPacketSize;
    static int maxPacketSize;
    static int bandwidth;
    static int duration;

    /**
     * Returns a random integer value between the given minimum and maximum values.
     * @param minVal
     *      The minimum value of the random integer
     * @param maxVal
     *      The maximum value of the random integer
     * @return
     *      Returns a random integer between minVal and maxVal
     */
    private static int randInt(int minVal, int maxVal){
        return (int)(minVal + Math.random()*(maxVal-minVal));
    }


    /**
     * Simulates the random creation and sending of packets through various routers. The simulation runs for
     * a certain time, duration, and uses different variables to influence its outcome. Packets first arrive
     * at the dispatcher, before being sent to the intermediate routers, and finally the destination. If the
     * intermediate routers are full, the packets are dropped. If the destination has reached its bandwidth, then
     * the packet will be sent at a later time(and will be given priority so that other packets are not sent before
     * it).
     * @return
     *      Returns the average time a packet takes before reaching its destination.
     */
    public static double simulate(){
        int currentTime = 1;
        totalPacketsArrived = 0;
        totalServiceTime = 0;
        packetsDropped = 0;
        ArrayList<Router> priorityList = new ArrayList<Router>();

        while(currentTime <= duration){
            System.out.println("Current Time: " + currentTime);
            System.out.println("-----------------------------\n");
            int numAcceptedPackets = 0;
            for(int i = 0; i < MAX_PACKETS; i++){
                if(Math.random() < arrivalProb){
                    int packetSize = randInt(minPacketSize, maxPacketSize);
                    Packet p = new Packet(packetSize, currentTime, (packetSize/100)+1);
                    dispatcher.enqueue(p);
                    System.out.println("Packet " + Packet.getPacketCounter() + " arrives at dispatcher with size "
                    + packetSize+".");
                }
            }
            System.out.println();

            //Send packets to interm Routers
            if(dispatcher.size() > 0){
                while(dispatcher.size() > 0){
                    try {
                        int index = Router.sendPacketTo(routers);
                        Packet p = dispatcher.peek();
                        routers.get(index).enqueue(p);
                        System.out.println("Packet " + p.getId() + " sent to Router " + (index+1));
                        dispatcher.dequeue();
                    }
                    catch(RouterOverFlowException e){
                        System.out.println("Packet " + dispatcher.dequeue().getId() +" has been dropped" +
                                " due to a congested network");
                        packetsDropped++;
                    }
                }
            }
            System.out.println();
            //Priority List - Remove packets given priority first due to not being sent prior.
            if(!priorityList.isEmpty()){
                for(int i = 0; i < priorityList.size(); i++){
                    Router r = priorityList.get(i);
                    if(r != null){
                        if(r.peek() != null){
                            if(r.peek().getTimeToDest() < 1){

                                if (numAcceptedPackets < bandwidth) {
                                    System.out.println("Packet " + r.peek().getId() + " has been given priority" +
                                            " due to prior bandwidth issues.");
                                    System.out.println("Packet " + r.peek().getId() + " has successfully reached the destination " +
                                            "router! +" + (currentTime - r.peek().getTimeArrive()));

                                    numAcceptedPackets++;
                                    totalServiceTime += (currentTime - r.peek().getTimeArrive());
                                    totalPacketsArrived++;
                                    r.dequeue();
                                    priorityList.remove(r);
                                } else {
                                    System.out.println("Packet " + r.peek().getId() + " could not reach the destination," +
                                            " it will be sent later.");
                                }
                            }
                        }
                    }
                }
            }
            //Decrement interm Routers
            for(Router r : routers){
                if(r != null) {
                    if (r.peek() != null) {
                        if(r.peek().getTimeToDest() >= 1)
                            r.peek().setTimeToDest(r.peek().getTimeToDest() - 1);
                        if (r.peek().getTimeToDest() < 1) {
                            if (numAcceptedPackets < bandwidth) {
                                System.out.println("Packet " + r.peek().getId() + " has successfully reached the destination " +
                                        "router! +" + (currentTime - r.peek().getTimeArrive()));

                                numAcceptedPackets++;
                                totalServiceTime += (currentTime - r.peek().getTimeArrive());
                                totalPacketsArrived++;
                                r.dequeue();
                            } else {
                                if(!priorityList.contains(r)) {
                                    System.out.println("Packet " + r.peek().getId() + " could not reach the destination," +
                                            " it will be sent later.");
                                    priorityList.add(r);
                                }
                            }
                        }
                    }
                }
            }
            System.out.println();
            //Print things
            for(Router r : routers){
                if(r != null) {
                    System.out.println("R" + (routers.indexOf(r)+1) + ": " + r.toString());
                }
            }
            System.out.println();
            currentTime++;

        }
        return (((double)totalServiceTime/(double)totalPacketsArrived));
    }

    /**
     * The main method. Users input different value for the variables given and are given a simulation using
     * these exact parameters.
     */
    public static void main(String[] args) {
        System.out.println("Starting Simulator...");
        String option = "y";
        Scanner s = new Scanner(System.in);


        while(!option.equalsIgnoreCase("n")){
            //TODO ERROR HANDLING
            try {
                System.out.print("Enter the number of intermediate routers: ");
                numIntRouters = s.nextInt();
                s.nextLine();
                System.out.print("Enter the arrival probability: ");
                arrivalProb = s.nextDouble();
                s.nextLine();
                System.out.print("Enter the maximum buffer size of the routers: ");
                maxBufferSize = s.nextInt();
                s.nextLine();
                System.out.print("Enter the minimum size of a packet: ");
                minPacketSize = s.nextInt();
                s.nextLine();
                System.out.print("Enter the maximum size of a packet: ");
                maxPacketSize = s.nextInt();
                s.nextLine();
                System.out.print("Enter the bandwidth size: ");
                bandwidth = s.nextInt();
                s.nextLine();
                System.out.print("Enter the duration of the simulation: ");
                duration = s.nextInt();
                s.nextLine();

                //Error Handling

                if(numIntRouters <= 0 || maxBufferSize <= 0 || bandwidth <= 0 || duration <= 0){
                    throw new IllegalArgumentException("Values must be greater than 0!");
                }
                if(arrivalProb <= 0 || arrivalProb > 1.0){
                    throw new IllegalArgumentException("The arrival probability must be greater than 0 and less than" +
                            " or equal to 1!");
                }
                if(minPacketSize <= 0 || maxPacketSize <= 0 || minPacketSize > maxPacketSize){
                    throw new IllegalArgumentException("Those values for the minimum and/or maximum packet sizes" +
                            " are invalid!");
                }

                //Pre conditions for simulate()
                routers = new ArrayList<Router>();
                dispatcher = new Router();
                Packet.setPacketCounter(0);
                for (int i = 0; i < numIntRouters; i++) {
                    Router r = new Router();
                    routers.add(r);
                }
                //Call and return simulate()
                double avgTime = simulate();
                System.out.println("Simulation ending...");
                System.out.println("Total Service Time: " + totalServiceTime);
                System.out.println("Total Packets Served: " + totalPacketsArrived);
                System.out.println("Average Service Time: " + (double) Math.round(avgTime * 100.0) / 100.0);
                System.out.println("Total Packets Dropped: " + packetsDropped);
                do {
                    System.out.println("Simulation finished, would you like to try another? (y/n)");
                    option = s.nextLine();
                    if (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n")) {
                        System.out.println("Please enter a valid option!");
                    }
                } while (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n"));
            }catch(InputMismatchException e){
                System.out.println("That input is not valid!");
                s.nextLine();
                continue;
            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
                continue;
            }
        }

        System.out.println("Program terminating...");
    }
}
