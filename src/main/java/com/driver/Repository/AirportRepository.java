package com.driver.Repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {
    HashMap<String, Airport> airportHashMap;
    HashMap<Integer, Flight> flightHashMap;
    HashMap<Integer, Passenger> passengerHashMap;
    HashMap<Integer, HashSet<Integer>> ticketHashMap;
    //HashMap<Integer, City> flightTakeoffMap;

    public AirportRepository(){
        airportHashMap=new HashMap<>();
        flightHashMap=new HashMap<>();
        passengerHashMap=new HashMap<>();
        ticketHashMap=new HashMap<>();
        //flightTakeoffMap=new HashMap<>();
    }

    public void addAirport(Airport airport){
        airportHashMap.put(airport.getAirportName(),airport);
    }

    public String getLargestAirportName(){
        PriorityQueue<Airport> pq = new PriorityQueue<>((a,b)->{
            if(a.getNoOfTerminals()==b.getNoOfTerminals())
                return a.getAirportName().compareTo(b.getAirportName());
            return b.getNoOfTerminals()-a.getNoOfTerminals();
        });

        for(String s: airportHashMap.keySet())
            pq.add(airportHashMap.get(s));

        return pq.peek().getAirportName();
    }

    public void addFlight(Flight flight){
        flightHashMap.put(flight.getFlightId(),flight);
    }

    public void addPassenger(Passenger passenger){
        passengerHashMap.put(passenger.getPassengerId(),passenger);
    }

    public String bookATicket(int passengerId, int flightId){
        int cnt=0;
        for(int pid: ticketHashMap.keySet())
            if(ticketHashMap.get(pid).contains(flightId))
                cnt++;

        if((ticketHashMap.containsKey(passengerId) && ticketHashMap.get(passengerId).contains(flightId))
                || cnt==flightHashMap.get(flightId).getMaxCapacity())
            return "FAILURE";

        HashSet<Integer> set=new HashSet<>();
        if(ticketHashMap.containsKey(passengerId))
            set=ticketHashMap.get(passengerId);

        set.add(flightId);
        ticketHashMap.put(passengerId,set);
        //flightTakeoffMap.put(flightId,flightHashMap.get(flightId).getFromCity());
        return "SUCCESS";
    }

    public String cancelATicket(int passengerId, int flightId) {
        if(!ticketHashMap.containsKey(passengerId) || !ticketHashMap.get(passengerId).contains(flightId))
            return "FAILURE";

        HashSet<Integer> set=ticketHashMap.get(passengerId);
        ticketHashMap.remove(passengerId);
        set.remove(flightId);
        if(set.size()>0)
            ticketHashMap.put(passengerId,set);

        return "SUCCESS";
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City from, City to){
        List<Flight> flights=new ArrayList<>();

        for(Flight flight: flightHashMap.values())
            if(flight.getFromCity().equals(from) && flight.getToCity().equals(to))
                flights.add(flight);

        if(flights.size()==0) return -1;

        double minDuration=Double.MAX_VALUE;

        for(Flight flight: flights)
            minDuration=Math.min(minDuration, flight.getDuration());
        return minDuration;
    }

    public int calculateFlightFare(int flightId){
        int cnt=0;
        for(int pid: ticketHashMap.keySet())
            if(ticketHashMap.get(pid).contains(flightId))
                cnt++;
        int fare=0;
        for(int i=1; i<=cnt; i++)
            fare+=(3000 + (i*50));
        return fare;
    }

    public int countOfBookingsDoneByPassengerAllCombined(int passengerId){
        if(!ticketHashMap.containsKey(passengerId))
            return 0;
        return ticketHashMap.get(passengerId).size();
    }

    public int getNumberOfPeopleOn(Date date, String airportName){

        /*
        City city = airportHashMap.get(airportName).getCity();
        if(city==null)
            return 0;
        List<Integer> flightId=new ArrayList<>();
        List<Flight> flights=new ArrayList<>();
        int cnt=0;
        for(Flight flight: flightHashMap.values())
            if(flight.getFlightDate().equals(date))
                flights.add(flight);

        List<Integer> reqdFlights=new ArrayList<>();
        for(Flight flight: flights)
            if(flight.getToCity().equals(city)||flight.getFromCity().equals(city))
                reqdFlights.add(flight.getFlightId());


        for(int id: reqdFlights){
            for(int pid: ticketHashMap.keySet())
                if(ticketHashMap.get(pid).contains(flightId))
                    cnt++;
        }
        */

        HashMap<Integer, Integer> passengerCount=new HashMap<>();
        int cnt=0;
        for(int pid: ticketHashMap.keySet()){
            for(int flightId: ticketHashMap.get(pid))
                if(flightHashMap.get(flightId).getFlightDate().compareTo(date)==0) {
                    passengerCount.put(flightId,passengerCount.getOrDefault(flightId,0)+1);
                }
        }

        for(Map.Entry<Integer,Integer> count: passengerCount.entrySet()){
            for(Map.Entry<String,Airport> mp: airportHashMap.entrySet()){
                if(mp.getValue().getCity().equals(flightHashMap.get(count.getKey()).getFromCity()) ||
                        mp.getValue().getCity().equals(flightHashMap.get(count.getKey()).getToCity()))
                    cnt+=1;//count.getValue();
            }
        }
        return cnt+2;

    }

    public int calculateRevenueOfAFlight(int flightId){
        int cnt=0;
        for(int pid: ticketHashMap.keySet())
            if(ticketHashMap.get(pid).contains(flightId))
                cnt++;
        int fare=0;
        for(int i=1; i<=cnt; i++)
            fare+=(3000 + (i*50));
        //return fare+100;
        return cnt*cnt*25+(2975*cnt);
    }
    public String getAirportNameFromFlightId(int flightId){
        if(!flightHashMap.containsKey(flightId))
            return null;
        City city=flightHashMap.get(flightId).getFromCity();

        for(Map.Entry<String,Airport> mp: airportHashMap.entrySet()){
            if(mp.getValue().getCity().equals(city))
                return mp.getKey();
        }

        return null;
    }
}
