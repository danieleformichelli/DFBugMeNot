package com.formichelli.dfbugmenot;

/**
 * A bugmenot.com result
 */
public class BugMeNotResult {
    private final String username;
    private final String password;
    private final double successRate;
    private final int votes;
    private final int eta;
    private final EtaWeight etaWeight;

    public BugMeNotResult(String username, String password, double successRate, int votes, int eta, EtaWeight etaWeight) {
        if (username == null)
            throw new NullPointerException("username cannot be null");
        if (username.isEmpty())
            throw new NullPointerException("username cannot be empty");

        if (password == null)
            throw new NullPointerException("password cannot be null");
        if (password.isEmpty())
            throw new NullPointerException("password cannot be empty");

        if (successRate < 0 || successRate > 100)
            throw new IllegalArgumentException("successRate must be in the range [0, 100]");

        if (votes < 0)
            throw new IllegalArgumentException("votes cannot be null");

        if (eta < 0)
            throw new IllegalArgumentException("eta cannot be null");

        if (etaWeight == null)
            throw new NullPointerException("etaWeight cannot be null");

        this.username = username;
        this.password = password;
        this.successRate = successRate;
        this.votes = votes;
        this.eta = eta;
        this.etaWeight = etaWeight;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public int getVotes() {
        return votes;
    }

    public int getEta() {
        return eta;
    }

    public EtaWeight getEtaWeight() {
        return etaWeight;
    }

    public enum EtaWeight {
        HOURS, DAYS, MONTHS, YEARS
    }
}
