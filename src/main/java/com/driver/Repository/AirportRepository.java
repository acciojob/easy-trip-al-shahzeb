package com.driver.Repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {


    public HashMap<String,Airport> airportHashMap = new HashMap<>();

    public HashMap<Integer, Flight> flightHashMap = new HashMap<>();

    public HashMap<Integer,List<Integer>> flightTopassengerHashMap = new HashMap<>();

    public HashMap<Integer,Passenger> passengerHashMap = new HashMap<>();


    public String addAirport(Airport airport){

        //Simply add airport details to your database
        //Return a String message "SUCCESS"
        airportHashMap.put(airport.getAirportName(),airport);

        return "SUCCESS";
    }

    public String getLargestAirportName(){

        //Largest airport is in terms of terminals 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName

        String ans = "";
        int terminals = 0;
        for(Airport airport : airportHashMap.values()){

            if(airport.getNoOfTerminals()>terminals){
                ans = airport.getAirportName();
                terminals = airport.getNoOfTerminals();
            }else if(airport.getNoOfTerminals()==terminals){
                if(airport.getAirportName().compareTo(ans)<0){
                    ans = airport.getAirportName();
                }
            }
        }
        return ans;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){

        //Find the duration by finding the shortest flight that connects these 2 cities directly
        //If there is no direct flight between 2 cities return -1.

        double distance = 1000000000;

        for(Flight flight:flightHashMap.values()){
            if((flight.getFromCity().equals(fromCity))&&(flight.getToCity().equals(toCity))){
                distance = Math.min(distance,flight.getDuration());
            }
        }

        if(distance==1000000000){
            return -1;
        }
        return distance;

    }

    public int getNumberOfPeopleOn(Date date,String airportName){

        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight



        Airport airport = airportHashMap.get(airportName);
        if(Objects.isNull(airport)){
            return 0;
        }
        City city = airport.getCity();
        int count = 0;
        for(Flight flight:flightHashMap.values()){
            if(date.equals(flight.getFlightDate()))
                if(flight.getToCity().equals(city)||flight.getFromCity().equals(city)){

                    int flightId = flight.getFlightId();
                    count = count + flightTopassengerHashMap.get(flightId).size();
                }
        }
        return count;
    }

    public int calculateFlightFare(Integer flightId){

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be jsut checking price

        int noOfPeopleBooked = flightTopassengerHashMap.get(flightId).size();
        return noOfPeopleBooked*50 + 3000;

    }



    public String bookATicket(Integer flightId,Integer passengerId){

        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"


        if(Objects.nonNull(flightTopassengerHashMap.get(flightId)) &&(flightTopassengerHashMap.get(flightId).size()<flightHashMap.get(flightId).getMaxCapacity())){


            List<Integer> passengers =  flightTopassengerHashMap.get(flightId);

            if(passengers.contains(passengerId)){
                return "FAILURE";
            }

            passengers.add(passengerId);
            flightTopassengerHashMap.put(flightId,passengers);
            return "SUCCESS";
        }
        else if(Objects.isNull(flightTopassengerHashMap.get(flightId))){
            flightTopassengerHashMap.put(flightId,new ArrayList<>());
            List<Integer> passengers =  flightTopassengerHashMap.get(flightId);

            if(passengers.contains(passengerId)){
                return "FAILURE";
            }

            passengers.add(passengerId);
            flightTopassengerHashMap.put(flightId,passengers);
            return "SUCCESS";

        }
        return "FAILURE";
    }


    public String cancelATicket(Integer flightId,Integer passengerId){

        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId

        List<Integer> passengers = flightTopassengerHashMap.get(flightId);
        if(passengers == null){
            return "FAILURE";
        }


        if(passengers.contains(passengerId)){
            passengers.remove(passengerId);
            return "SUCCESS";
        }
        return "FAILURE";
    }


    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){

        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        HashMap<Integer,List<Integer>> passengerToflightHashMap = new HashMap<>();
        //We have a list from passenger To flights database:-
        int count = 0;
        for(Map.Entry<Integer,List<Integer>> entry: flightTopassengerHashMap.entrySet()){

            List<Integer> passengers  = entry.getValue();
            for(Integer passenger : passengers){
                if(passenger==passengerId){
                    count++;
                }
            }
        }
        return count;
    }


    public String addFlight(Flight flight){

        //Return a "SUCCESS" message string after adding a flight.
        flightHashMap.put(flight.getFlightId(),flight);
        return "SUCCESS";
    }



    public String getAirportNameFromFlightId(Integer flightId){

        //We need to get the starting aiport from where the flight will be taking off
        //return null incase the flightId is invalid or you are not able to find the airportName

        if(flightHashMap.containsKey(flightId)){
            City city = flightHashMap.get(flightId).getFromCity();
            for(Airport airport:airportHashMap.values()){
                if(airport.getCity().equals(city)){
                    return airport.getAirportName();
                }
            }
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId){

        int noOfPeopleBooked = flightTopassengerHashMap.get(flightId).size();
        int variableFare = (noOfPeopleBooked*(noOfPeopleBooked+1))*25;
        int fixedFare = 3000*noOfPeopleBooked;
        int totalFare = variableFare + fixedFare;

        return totalFare;
    }

    public String addPassenger(Passenger passenger){

        passengerHashMap.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }
    
    /**
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

        //
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
        //

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
        return cnt;

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
    
    **/
}
