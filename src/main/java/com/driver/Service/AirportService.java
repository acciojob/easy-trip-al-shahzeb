package com.driver.Service;

import com.driver.Repository.AirportRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import javax.xml.crypto.Data;
import java.util.Date;

public class AirportService {

    AirportRepository airportRepository=new AirportRepository();

    public void addAirport(Airport airport){
        airportRepository.addAirport(airport);
    }
    public void addFlight(Flight flight){
        airportRepository.addFlight(flight);
    }
    public void addPassenger(Passenger passenger){
        airportRepository.addPassenger(passenger);
    }

    public String getLargestAirportName(){
        return airportRepository.getLargestAirportName();
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City city1, City city2){
        return airportRepository.getShortestDurationOfPossibleBetweenTwoCities(city1,city2);
    }

    public int getNumberOfPeopleOn(Date date, String aiportName){
        return airportRepository.getNumberOfPeopleOn(date, aiportName);
    }
    public int calculateFlightFare(int flightId){
        return airportRepository.calculateFlightFare(flightId);
    }
    public String bookATicket(int passenger, int flight){
        return airportRepository.bookATicket(passenger, flight);
    }
    public String cancelATicket(int passenger, int flight){
        return airportRepository.cancelATicket(passenger, flight);
    }

    public int countOfBookingsDoneByPassengerAllCombined(int pid){
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(pid);
    }
    public String getAirportNameFromFlightId(int fid){
        return airportRepository.getAirportNameFromFlightId(fid);
    }
    public int calculateRevenueOfAFlight(int fid){
        return airportRepository.calculateRevenueOfAFlight(fid);
    }
}
