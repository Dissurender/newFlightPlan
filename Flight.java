class Flight {
    private String destination;
    private int time;
    private int cost;

    public Flight(String destination, int time, int cost) {
        this.destination = destination;
        this.time = time;
        this.cost = cost;
    }

    public String getDestination() {
        return destination;
    }

    public int getTime() {
        return time;
    }

    public int getCost() {
        return cost;
    }
}