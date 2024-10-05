import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class Room {
    private int roomId;
    private String category;
    private double pricePerNight;
    private int capacity;
    private boolean isAvailable;

    public Room(int roomId, String category, double pricePerNight, int capacity) {
        this.roomId = roomId;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.isAvailable = true;  // All rooms are available initially
    }

    public int getRoomId() {
        return roomId;
    }

    public String getCategory() {
        return category;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void markAsBooked() {
        isAvailable = false;
    }

    @Override
    public String toString() {
        return "Room " + roomId + " (" + category + "): $" + pricePerNight + "/night, Capacity: " + capacity + " people";
    }
}

class Reservation {
    private String userName;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;
    private double totalCost;

    public Reservation(String userName, Room room, Date checkInDate, Date checkOutDate) {
        this.userName = userName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = calculateTotalCost();
    }

    private double calculateTotalCost() {
        long stayDuration = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
        return stayDuration * room.getPricePerNight();
    }

    public String getUserName() {
        return userName;
    }

    public Room getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Reservation for " + userName + ": Room " + room.getRoomId() + " (" + room.getCategory() +
                "), Check-in: " + checkInDate + ", Check-out: " + checkOutDate + ", Total Cost: $" + totalCost;
    }
}

public class HotelReservationSystem {

    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Reservation> reservations = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    static {
        // Initializing with some rooms
        rooms.add(new Room(101, "Single", 100, 1));
        rooms.add(new Room(102, "Double", 150, 2));
        rooms.add(new Room(103, "Suite", 250, 4));
        rooms.add(new Room(104, "Single", 100, 1));
        rooms.add(new Room(105, "Double", 150, 2));
        rooms.add(new Room(106, "Suite", 250, 4));
    }

    private static void displayAvailableRooms(Date checkInDate, Date checkOutDate, int requiredCapacity) {
        System.out.println("Available rooms:");
        for (Room room : rooms) {
            if (room.isAvailable() && room.getCapacity() >= requiredCapacity) {
                System.out.println(room);
            }
        }
    }

    private static void makeReservation() {
        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkInDateStr = scanner.nextLine();
        Date checkInDate = parseDate(checkInDateStr);

        if (checkInDate == null) {
            System.out.println("Invalid check-in date format. Please try again.");
            return;
        }

        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOutDateStr = scanner.nextLine();
        Date checkOutDate = parseDate(checkOutDateStr);

        if (checkOutDate == null) {
            System.out.println("Invalid check-out date format. Please try again.");
            return;
        }

        System.out.print("Enter the required capacity (number of people): ");
        int requiredCapacity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Display available rooms
        displayAvailableRooms(checkInDate, checkOutDate, requiredCapacity);

        System.out.print("Select a room by entering the room ID: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Room selectedRoom = null;
        for (Room room : rooms) {
            if (room.getRoomId() == roomId && room.isAvailable()) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Invalid room selection or room not available.");
            return;
        }

        // Create the reservation
        Reservation reservation = new Reservation(userName, selectedRoom, checkInDate, checkOutDate);
        reservations.add(reservation);

        // Mark room as unavailable
        selectedRoom.markAsBooked();

        System.out.println("Reservation successful!");
        System.out.println(reservation);

        // Payment processing
        processPayment(reservation);
    }

    private static Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;  // Return null if parsing fails
        }
    }

    private static void processPayment(Reservation reservation) {
        System.out.println("Total amount to be paid: $" + reservation.getTotalCost());
        System.out.print("Enter your payment method (Credit/Debit): ");
        String paymentMethod = scanner.nextLine();

        if (paymentMethod.equalsIgnoreCase("Credit") || paymentMethod.equalsIgnoreCase("Debit")) {
            System.out.println("Payment processed successfully!");
            System.out.println("Thank you, " + reservation.getUserName() + ". Your reservation is confirmed.");
        } else {
            System.out.println("Invalid payment method. Please try again.");
        }
    }

    private static void viewReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations made yet.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Hotel Reservation System =====");
            System.out.println("1. Make a reservation");
            System.out.println("2. View all reservations");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    makeReservation();
                    break;
                case 2:
                    viewReservations();
                    break;
                case 3:
                    System.out.println("Exiting the system...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
