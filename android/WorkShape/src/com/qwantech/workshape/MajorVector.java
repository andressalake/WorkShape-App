package com.qwantech.workshape;

import java.util.List;
/* May, 20, 2014
 * 
 * This class contains all the functions necessary to deal with the vectors to
 * calculate the direction of the vector.
 * Vector are used to know if the player is running and calculate the number of steps
 * 
 */

public class MajorVector {
	
	Vector bigVectorUP = new Vector(0,0,0); // It is create the vector is going up and the vector is going down
	Vector bigVectorDOWN = new Vector(0,0,0);// to calculate the number of jumps because the player will run at the same spot
	
	public void checkListofVectors(List<Vector> list)
	{ /*
			This function need to receive the List of vector gathered during an amount of time, 1 second, 
			to define which vector is biggest one, so we can check if the player jumped. 
			we know the up vector because it will be largest in magnitude!
		*/
		//It is still need to define the amount of time to get the list of vector because a player can run
		// faster than another player
		//For example if we put 1 second for gathering the list of vector and a person jump 3 times in one second ,
		//the result will be wrong
		bigVectorUP = findLargestVector(list); //find the largestVector UP
		
		bigVectorDOWN = findLargestOpposingVector(list, bigVectorUP); //find the largestVector DOWN
			
	}
	
	private Vector findLargestVector(List<Vector> list)
	{
		double largestMagnitude = 0;
		Vector largestVector = null;
		
		//find the largest vector (which will be the largest Up Vector
		for(Vector v: list)
		{
			
			
			float mag = v.calculateVectorMag();
			if(mag > largestMagnitude)
			{
				largestMagnitude = mag;
				largestVector = v;
			}
		}
		
		return largestVector; // return the largestVector from a list of Vectors
	}
	
	private Vector findLargestOpposingVector(List<Vector> list, Vector referenceVector) //receives the List of Vector and the referenceVector
	//find the opposite vector,	the vector that is going in a opposite direction from the
	//largestVector found. It will indicate a jump or step. 
	//It calculate the angle between the two largest vectors to check if they are in an opposite direction
	{
		double largestMagnitude = 0;
		Vector largestVector = null;
		
		for(Vector v: list)
		{
			//The angle between the referenceVector and all the others vectors are checked to see if they are in an opposite direction
			double angle = referenceVector.calculateAngle(v); 
			//if angle > 135 degrees
			if(angle > Math.PI / 1.5)  // is it right? 
			{
				float mag = v.calculateVectorMag();
				if(mag > largestMagnitude) // get the largest Vector after the angle calculation
				{
					largestMagnitude = mag;
					largestVector = v;
				}
				
			}
		}
		
		return largestVector;
		
	}
	
	

}
