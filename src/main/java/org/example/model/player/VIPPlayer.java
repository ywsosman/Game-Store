package org.example.model.player;

/**
 * A premium player with a configurable discount rate (default 20%).
 */
public class VIPPlayer extends Player {

    private static final double DEFAULT_VIP_DISCOUNT = 0.20;

    private double vipDiscountRate;

    public VIPPlayer() {
        this.vipDiscountRate = DEFAULT_VIP_DISCOUNT;
    }

    public VIPPlayer(int id, String name, String email) {
        super(id, name, email);
        this.vipDiscountRate = DEFAULT_VIP_DISCOUNT;
    }

    public VIPPlayer(int id, String name, String email, double vipDiscountRate) {
        super(id, name, email);
        this.vipDiscountRate = vipDiscountRate;
    }

    @Override
    public String getPlayerType() {
        return "VIP";
    }

    /**
     * VIP players receive a configurable discount (default 20%).
     *
     * @return the discount rate as a decimal (e.g., 0.20 = 20%)
     */
    public double getDiscount() {
        return vipDiscountRate;
    }

    public double getVipDiscountRate() {
        return vipDiscountRate;
    }

    public void setVipDiscountRate(double vipDiscountRate) {
        this.vipDiscountRate = vipDiscountRate;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-20s | %-25s | %s | %d%% discount",
                getId(), getName(), getEmail(), getPlayerType(), (int) (vipDiscountRate * 100));
    }
}
