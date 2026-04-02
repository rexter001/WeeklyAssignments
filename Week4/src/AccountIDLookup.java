import java.util.Arrays;

public class AccountIDLookup {

    // ---------------- LINEAR SEARCH ----------------
    public static void linearSearch(String[] logs, String target) {
        int first = -1;
        int last = -1;
        int comparisons = 0;

        for (int i = 0; i < logs.length; i++) {
            comparisons++;

            if (logs[i].equals(target)) {
                if (first == -1) {
                    first = i;
                }
                last = i;
            }
        }

        System.out.println("Linear Search:");
        System.out.println("First occurrence = " + first);
        System.out.println("Last occurrence = " + last);
        System.out.println("Comparisons = " + comparisons);
    }

    // ---------------- BINARY SEARCH ----------------
    public static void binarySearch(String[] logs, String target) {
        Arrays.sort(logs);   // required for binary search

        int low = 0;
        int high = logs.length - 1;
        int comparisons = 0;
        int foundIndex = -1;

        while (low <= high) {
            comparisons++;

            int mid = (low + high) / 2;

            int cmp = logs[mid].compareTo(target);

            if (cmp == 0) {
                foundIndex = mid;
                break;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        int count = 0;

        if (foundIndex != -1) {
            for (String log : logs) {
                if (log.equals(target)) {
                    count++;
                }
            }
        }

        System.out.println("\nBinary Search:");
        System.out.println("Sorted Logs = " + Arrays.toString(logs));
        System.out.println("Found index = " + foundIndex);
        System.out.println("Count = " + count);
        System.out.println("Comparisons = " + comparisons);
    }

    public static void main(String[] args) {
        String[] logs = {"accB", "accA", "accB", "accC"};
        String target = "accB";

        linearSearch(logs.clone(), target);
        binarySearch(logs.clone(), target);
    }
}
