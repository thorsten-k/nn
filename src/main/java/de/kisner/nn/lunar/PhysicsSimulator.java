package de.kisner.nn.lunar;

import java.text.NumberFormat;

public class PhysicsSimulator
{
	public static final double GRAVITY = 3.62;
	public static final double THRUST = 10;
	public static final double TERMINAL_VELOCITY = 400;

	private int fuel;			public int getFuel() {return fuel;}
	private int seconds;		public int getSeconds() {return seconds;}
	private double altitude;	public double getAltitude() {return altitude;}
	private double velocity;	public double getVelocity() {return velocity;}

	public PhysicsSimulator()
	{
		this.fuel = 170;
		this.seconds = 0;
		this.altitude = 100000;
		this.velocity = 0;
	}
	
	public int score(){return (int) ((this.fuel * 100) + this.seconds + (this.velocity * 1000));}
	public boolean flying() {return (this.altitude > 0);}

	public void turn(boolean thrust)
	{
		this.seconds++;
		this.velocity -= GRAVITY;
		this.altitude += this.velocity;

		if (thrust && this.fuel > 0)
		{
			this.fuel--;
			this.velocity += THRUST;
		}

		this.velocity = Math.max(-TERMINAL_VELOCITY, this.velocity);
		this.velocity = Math.min(TERMINAL_VELOCITY, this.velocity);

		if (this.altitude < 0){this.altitude = 0;}
	}

	public String telemetry()
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(4);
		nf.setMaximumFractionDigits(4);
		StringBuilder sb = new StringBuilder();
		sb.append("Elapsed: ");
		sb.append(seconds);
		sb.append(" s, Fuel: ");
		sb.append(this.fuel);
		sb.append(" l, Velocity: ");
		sb.append(nf.format(velocity));
		sb.append(" m/s, ");
		sb.append((int) altitude);
		sb.append(" m");
		
		
		return sb.toString();
	}
}