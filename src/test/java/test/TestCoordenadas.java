package test;

import java.util.ArrayList;

import twitter4j.Query.Unit;

public class TestCoordenadas {

	public static void main(String[] args) {
		ArrayList<double[]> coords = getSquareAroundPoint(-90.0,125.0,600.0,Unit.km);

	}

	private static double[] getDestinationPoint(Double lat, Double lng, double distance, double bearing) {

		double R = 6371;
			 double latRad = lat.doubleValue() * Math.PI /180;
			 double lngRad = lng.doubleValue() * Math.PI /180;
			 double bearRad = bearing * Math.PI/180;
			 double latRadSin = Math.sin(latRad);
			 double latRadCos = Math.cos(latRad);
			 // Ratio of distance to earth's radius
			 
						 
			 double angularDistance = distance/R;
			 double angDistSin = Math.sin(angularDistance);
			 double angDistCos = Math.cos(angularDistance);
			 double xlatRad = Math.asin( latRadSin*angDistCos + latRadCos*angDistSin*Math.cos(bearRad) );
			 double xlonRad = lngRad + Math.atan2(
			            Math.sin(bearing)*angDistSin*latRadCos,
			            angDistCos-latRadSin*Math.sin(xlatRad));
			 // Return latitude and longitude as two element array in degrees
			 double xlat = xlatRad*180/Math.PI;
			 double xlon = xlonRad*180/Math.PI;
			 if(xlat>90) 
				 xlat=90;
			 if(xlat<-90) 
				 xlat=-90;

			 while(xlon > 180) 
				 xlon-=360;
			 while(xlon<=-180)
				 xlon+=360;
			 double[] coord = {xlat,xlon};
			 return coord;
			 }


			// Distance is in km, lat and lon are in degrees
	private static ArrayList<double[]> getSquareAroundPoint (Double lat, Double lon, Double radius, Unit unit){
		double R = 6371;
		double distance = radius.doubleValue();
		 if(unit == Unit.mi){
			 distance = distance * 0.621371192;
		 }

			  double north = (lat*Math.PI/180 + distance/R)*180/Math.PI;
			  if(north>90) 
				 north=90;

			  double neast = (lon +(Math.atan2(Math.sin(distance/R)*Math.cos(lat*Math.PI/180), Math.cos(distance/R) - Math.sin(lat*Math.PI/180)*Math.sin(lat*Math.PI/180 + distance/R)))*180/Math.PI);
			  double nwest = (lon +(Math.atan2(-Math.sin(distance/R)*Math.cos(lat*Math.PI/180), Math.cos(distance/R) - Math.sin(lat*Math.PI/180)*Math.sin(lat*Math.PI/180 + distance/R)))*180/Math.PI);

			  double south = (lat*Math.PI/180 - distance/R)*180/Math.PI;
			 if(south<-90) 
				 south=-90;
			  double seast = (lon +(Math.atan2(Math.sin(distance/R)*Math.cos(lat*Math.PI/180), Math.cos(distance/R) - Math.sin(lat*Math.PI/180)*Math.sin(lat*Math.PI/180 - distance/R)))*180/Math.PI);
			  double swest = (lon +(Math.atan2(-Math.sin(distance/R)*Math.cos(lat*Math.PI/180), Math.cos(distance/R) - Math.sin(lat*Math.PI/180)*Math.sin(lat*Math.PI/180 - distance/R)))*180/Math.PI);

			  double east = Math.max(seast, neast);
			  double west = Math.min(swest, nwest);
			  while(east > 180) 
					 east-=360;
			  while(east<=-180)
				 east+=360;
			  while(west > 180) 
					 east-=360;
			  while(west<=-180)
				 east+=360;
			  
			  System.out.println(north + "," + west + "-----------------------" + north + "," + east);
			  System.out.println(south + "," + west + "-----------------------" + south + "," + east);
			ArrayList<double[]> coords = new ArrayList<double[]>();
			double [] southwest = {west,south};
			double [] northeast = {east,north};
			coords.add(southwest);
			coords.add(northeast);
		return coords;
	}

}
