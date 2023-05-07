import java.util.ArrayList;
import java.util.List;

class City {
    private String name;
    int cost;
    private List<Flight> flights; // edges to other cities

    public City(String name, int cost) {
        this.name = name;
        this.cost = cost;
        this.flights = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public List<Flight> getDestinations() {
        return this.flights;
    }

    public void addFlights(String city, int cost, int time) {
        this.flights.add(new Flight(city, cost, time));
    }

    // provides data object for making comparisons
    public Flight getFlightTo(String destination) {
        for (Flight flight : this.flights) {
            if (flight.getDestination().equals(destination)) {
                return flight;
            }
        }
        return null;
    }
}