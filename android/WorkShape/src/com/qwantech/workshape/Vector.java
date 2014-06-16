package com.qwantech.workshape;

public class Vector {
	
	double x, y, z;
	private float length;
	
	public Vector(double x, double y, double z)
	{
		this.x =x; 
		this.y = y;
		this.z =z;
		
	}
	
	
	public float calculateVectorMag() //calculate  the magnitude of a vector
	{
		length = (float) Math.sqrt(x*x + y*y+ z*z); 
	
		return length;
	}
	
	public boolean isOposite(double x, double y, double z)
	{
		boolean oposite = false;
		
		return oposite;
	}
	
	public double calculateAngle(Vector v)
	{
		return calculateAngle(v.x,v.y,v.z);
	}
	
	public double calculateAngle(double _x, double _y, double _z)
	{
		//calculate the angle of the vector 
		
		double angle, angleCos;
		Vector v = new Vector(_x,_y,_z);
		angleCos = ((this.x*_x) + (this.y*_y) + (this.z*_z) )/(calculateVectorMag()*v.calculateVectorMag());
		
		angle = Math.acos(angleCos);
		
		return angle; 
	}
	

}
