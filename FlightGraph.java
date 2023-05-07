import java.util.HashMap;
import java.util.List;

public class FlightGraph {
    private HashMap<String, City> cities;

    public FlightGraph(HashMap<String, List<Flight>> flights) {
        this.cities = new HashMap<>();

        // add unique cities and their flight paths as Nodes and Edges
        for (String name: flights.keySet()) {
            City city = new City(name, 0);
            cities.put(name, city);

            List<Flight> flightList = flights.get(name);
            for (Flight flight : flightList) {
                city.addFlights(flight.getDestination(), flight.getCost(), flight.getTime());

                if (!cities.containsKey(flight.getDestination())) {
                    cities.put(flight.getDestination(), new City(flight.getDestination(), flight.getCost()));
                }
            }
        }
    }

    public HashMap<String, City> getNodes() {
        return cities;
    }
}
