import java.io.*;
import java.util.*;

public class FlightPlanner {
    static FlightGraph graph;
    public static void main(String[] args) {

        // build adjacency list
        HashMap<String, List<Flight>> adjList = buildAdj();

        // hand the list over to create graph
        graph = new FlightGraph(adjList);

        // find the paths for the given flights
        requestedFlights(graph);
    }

    public static HashMap<String, List<Flight>> buildAdj() {
        // hold unique cities to be referenced while building
        HashMap<String, List<Flight>> citiesAdj = new HashMap<>();

        try {
            Scanner scanner = new Scanner(new File("_flightpaths.txt"));
            int numOfFlights = Integer.parseInt(scanner.nextLine());

            // read in data and store to create Flights
            for (int i = 0; i < numOfFlights; i++) {
                String line = scanner.nextLine();
                String[] lineParts = line.split("\\|");

                String cityOne = lineParts[0];
                String cityTwo = lineParts[1];
                int cost = Integer.parseInt(lineParts[2]);
                int time = Integer.parseInt(lineParts[3]);

                Flight newFlight = new Flight(cityTwo, cost, time);

                // check to see if city exists
                if (!citiesAdj.containsKey(cityOne)) {
                    List<Flight> flights = new ArrayList<>();
                    flights.add(newFlight);
                    citiesAdj.put(cityOne, flights);
                } else {
                    citiesAdj.get(cityOne).add(newFlight);
                }

                // add reverse Flight
                Flight newFlight2 = new Flight(cityOne, cost, time);
                if (!citiesAdj.containsKey(cityTwo)) {
                    List<Flight> flights = new ArrayList<>();
                    flights.add(newFlight2);
                    citiesAdj.put(cityTwo, flights);
                } else {
                    citiesAdj.get(cityTwo).add(newFlight2);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return citiesAdj;
    }

    public static void requestedFlights(FlightGraph graph) {

        try {
            Scanner scanner = new Scanner(new File("requestedFlightPlans.txt"));
            int numFlights = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < numFlights; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");

                String cityOne = parts[0];
                String cityTwo = parts[1];
                String type = parts[2];

                // console header for getShortest output
                String typeText = type.equals("C") ? "Cost" : "Time";
                System.out.println("Flight " + (i+1) + " " + cityOne + ", " + cityTwo + " By: " + typeText);

                getShortest(cityOne, cityTwo, graph, type);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void getShortest(String start, String end, FlightGraph graph, String type) {
        Map<String, Boolean> visited = new HashMap<>();
        Stack<City> stack = new Stack<>();
        List<String> path = new ArrayList<>();

        // set all nodes to unvisited
        for (String city : graph.getNodes().keySet()) {
            visited.put(city, false);
        }

        City startNode = graph.getNodes().get(start);
        stack.push(startNode);
        visited.put(start, true);

        // traverse graph to find paths
        while (!stack.isEmpty()) {
            City currentNode = stack.peek();

            if (currentNode.getName().equals(end)) {
                break;
            }

            boolean hasUnvisitedNeighbor = false;
            int minCost = Integer.MAX_VALUE;

            City nextNode = null;

            for (Flight edge : currentNode.getDestinations()) {
                if (!visited.get(edge.getDestination())) {
                    int value = type.equals("T") ? edge.getTime() : edge.getCost();
                    if (value < minCost) {
                        hasUnvisitedNeighbor = true;
                        minCost = value;
                        nextNode = graph.getNodes().get(edge.getDestination());
                    }
                }
            }

            if (hasUnvisitedNeighbor) {
                stack.push(nextNode);
                visited.put(nextNode.getName(), true);
            } else {
                stack.pop();
            }
        }

        int totalCom = 0;
        City previousCity = null;

        // backtrack through the stack to get the path 'costs'
        while (!stack.isEmpty()) {
            City currentCity = stack.pop();
            path.add(0, currentCity.getName());

            if (previousCity != null) {
                Flight flight = previousCity.getFlightTo(currentCity.getName());
                if (type.equals("T")) {
                    totalCom += flight.getTime();
                } else if (type.equals("C")) {
                    totalCom += flight.getCost();
                }
            }
            previousCity = currentCity;
        }

        // build output by iterating the path
        String output = start;
        for (int i = 1; i < path.size(); i++) {
            output += " -> " + path.get(i);
        }

        output += type.equals("C") ? " | Cost: " + totalCom : " | Time: " + totalCom;
        System.out.println(output);
        System.out.println();
    }
}
