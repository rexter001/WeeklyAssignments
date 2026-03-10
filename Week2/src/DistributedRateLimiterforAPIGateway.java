import java.util.*;
        import java.util.concurrent.ConcurrentHashMap;

class TokenBucket {

    private int tokens;
    private final int maxTokens;
    private final double refillRate; // tokens per second
    private long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // refill tokens based on time
    private void refill() {

        long now = System.currentTimeMillis();
        double tokensToAdd =
                ((now - lastRefillTime) / 1000.0) * refillRate;

        if (tokensToAdd > 0) {

            tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
            lastRefillTime = now;
        }
    }

    public synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    public synchronized int getRemainingTokens() {
        refill();
        return tokens;
    }

    public int getMaxTokens() {
        return maxTokens;
    }
}

class RateLimiter {

    // clientId -> TokenBucket
    private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();

    private final int LIMIT = 1000;
    private final double REFILL_RATE = 1000.0 / 3600.0; // per second

    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(LIMIT, REFILL_RATE));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            System.out.println(
                    "Allowed (" + bucket.getRemainingTokens()
                            + " requests remaining)"
            );

            return true;

        } else {

            System.out.println(
                    "Denied (0 requests remaining, retry later)"
            );

            return false;
        }
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int remaining = bucket.getRemainingTokens();

        System.out.println(
                "{used: " + (bucket.getMaxTokens() - remaining)
                        + ", limit: " + bucket.getMaxTokens()
                        + ", remaining: " + remaining + "}"
        );
    }
}

public class DistributedRateLimiterforAPIGateway {

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        // simulate requests
        for (int i = 0; i < 5; i++) {

            limiter.checkRateLimit(client);
        }

        limiter.getRateLimitStatus(client);
    }
}