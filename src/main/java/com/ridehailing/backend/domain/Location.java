package com.ridehailing.backend.domain;

public class Location {
    private double x;
    private double y;

    public Location() {}
    public Location(double x, double y) { this.x = x; this.y = y; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
