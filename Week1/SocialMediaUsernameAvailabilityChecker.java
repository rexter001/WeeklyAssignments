import java.util.*;

class UsernameChecker {

    // Stores username -> userId
    private HashMap<String, Integer> usernameMap;

    // Stores username -> number of attempts
    private HashMap<String, Integer> attemptFrequency;

    public UsernameChecker() {
        usernameMap = new HashMap<>();
        attemptFrequency = new HashMap<>();
    }

    // Register username
    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);

        if (usernameMap.containsKey(username)) {
            return false; // already taken
        }

        return true; // available
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // Append numbers
        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Replace underscore with dot
        if (username.contains("_")) {
            String dotVersion = username.replace("_", ".");
            if (!usernameMap.containsKey(dotVersion)) {
                suggestions.add(dotVersion);
            }
        }

        // Add random number
        String randomSuggestion = username + new Random().nextInt(100);
        if (!usernameMap.containsKey(randomSuggestion)) {
            suggestions.add(randomSuggestion);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + maxAttempts + " attempts)";
    }
}

public class SocialMediaUsernameAvailabilityChecker {

    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        // Existing users
        system.registerUser("john_doe", 101);
        system.registerUser("admin", 1);
        system.registerUser("alex", 102);

        // Checking availability
        System.out.println("john_doe available? " + system.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + system.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("Suggestions for john_doe: " + system.suggestAlternatives("john_doe"));

        // Simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        system.checkAvailability("guest");
        system.checkAvailability("guest");
        // Most attempted username
        System.out.println("Most attempted username: " + system.getMostAttempted());
    }
}