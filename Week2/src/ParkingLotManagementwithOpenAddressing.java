import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    public ParkingSpot() {
        this.licensePlate = null;
        this.entryTime = 0;
        this.occupied = false;
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int capacity;
    private int occupiedSpots = 0;

    private int totalProbes = 0;
    private int totalParks = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {

            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedSpots++;

        totalProbes += probes;
        totalParks++;

        System.out.println(
                "Vehicle " + licensePlate +
                        " parked at spot #" + index +
                        " (" + probes + " probes)"
        );
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long duration =
                        (System.currentTimeMillis() - table[index].entryTime) / 1000;

                double hours = duration / 3600.0;

                double fee = hours * 5.0; // $5 per hour

                System.out.println(
                        "Vehicle " + licensePlate +
                                " exited from spot #" + index +
                                " Duration: " + String.format("%.2f", hours) + " hours" +
                                " Fee: $" + String.format("%.2f", fee)
                );

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedSpots--;

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found");
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy =
                (occupiedSpots * 100.0) / capacity;

        double avgProbes =
                totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("\nParking Statistics:");
        System.out.println("Total Spots: " + capacity);
        System.out.println("Occupied Spots: " + occupiedSpots);
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Average Probes: " + String.format("%.2f", avgProbes));
    }
}

public class ParkingLotManagementwithOpenAddressing {

    public static void main(String[] args) throws Exception {

        ParkingLot lot = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}