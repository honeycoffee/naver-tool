
package com.naver.pubtrans.itn.api.common;

/*
 * 좌표변환 모듈 Java Class
 * 
 * 타원체 정의 ( NULL, BESSEL, WGS84, GRS80 )
 * 시스템 정의 ( GEOGRAPHIC, TMW, TMM, TME, KATECH, UTM52, UTM51, UTMK, CONGNAMUL )
 * 
 * How to Use
 * CoordinateTrans cvt = new CoordinateTrans();
 * cvt.SetGeoEllipsSystem(소스 타원체 ,소스 좌표계,타겟 타원체,타겟 좌표계);
 * cvt.ConvCoord(소스 X좌표,소스 Y좌표);
 * cvt.GetConvX()=타겟 X좌표 ,cvt.GetConvY()=타겟 Y좌표
 * 
 * @author Photon
 * @Comment Usage - Ahn YW
 * @date 2016. 3.
 */
public class CoordinateTrans {
	
	/**
	 * 좌표체계
	 * 
	 * @author Parksw
	 *
	 */
	public enum CoordSystem {
		GEOGRAPHIC("GEOGRAPHIC"),
		TMW("TMW"),
		TMM("TMM"),
		TME("TME"),
		KATECH("KATECH"),
		UTM52("UTM52"),
		UTM51("UTM51"),
		UTMK("UTMK"),
		CONGNAMUL("CONGNAMUL");
		
		private String value;
		
		private CoordSystem(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * 타원체
	 * 
	 * @author Parksw
	 *
	 */
	public enum Ellipse {
		NULL("NULL"),
		BESSEL("BESSEL"),
		GRS80("GRS80"),
		WGS84("WGS84");
		
		private String value;
		
		private Ellipse(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	// 상수정의
	private static final double HALF_PI = Math.PI / 2;	// 아마도 1.5707963267948965
	private static final double AD_C = 1.0026000;		// Toms region 1 constant
	private static final double EPSLN = 1.0e-10;
	
	public ConvVars vars = new ConvVars();
	
	private Ellipse m_strSrcEllipse;
	private CoordSystem m_strSrcSystem;
	private Ellipse m_strDstEllipse;
	private CoordSystem m_strDstSystem;
	
	private double m_dConvX;
	private double m_dConvY;
	
	public CoordinateTrans() {}
	
	public void SetGeoEllipsSystem(Ellipse srcEllipse, CoordSystem srcSystem, Ellipse destEllipse, CoordSystem destSystem) {
		SetSrcType(srcEllipse, srcSystem);
		SetDstType(destEllipse, destSystem);

		vars.initTM(srcSystem, destSystem);
		vars.initEllipsoid(srcEllipse, destEllipse);
	}
	
	public void ConvCoord(double dInX, double dInY) {
		m_dConvX = -1;
		m_dConvY = -1;

		if (m_strSrcSystem == CoordSystem.GEOGRAPHIC || m_strDstSystem == CoordSystem.GEOGRAPHIC) {
			if (m_strSrcSystem == m_strDstSystem) {
				m_dConvX = dInX;
				m_dConvY = dInY;
			} else {

				if (m_strSrcSystem == CoordSystem.GEOGRAPHIC)
					GeoToTm(dInX, dInY);
				else
					TmToGeo(dInX, dInY);
			}

		} else {
			TmToTm(dInX, dInY);
		}
	}
	
	public double GetConvX() {
		return m_dConvX;
	}

	public double GetConvY() {
		return m_dConvY;
	}
	
	private void SetSrcType(Ellipse strEllipse, CoordSystem strSystem) {
		m_strSrcEllipse = strEllipse;
		m_strSrcSystem = strSystem;
	}

	private void SetDstType(Ellipse strEllipse, CoordSystem strSystem) {
		m_strDstEllipse = strEllipse;
		m_strDstSystem = strSystem;
	}

	private double mlfn(double e0, double e1, double e2, double e3, double phi) {
		return (e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3 * Math.sin(6.0 * phi));
	}

	// Function to return the sign of an argument
	private static int sign(double x) {
		if (x < 0.0) {
			return -1;
		} else {
			return 1;
		}
	}

	// Function to adjust longitude to -180 to 180; input in radians
	private static double adjustLon(double x) {
		if (Math.abs(x) >= Math.PI) {
			x = x - (sign(x) * Math.PI * 2);
		}
		
		return x;
	}

	// TM2Geo	직각좌표를 측지좌표로
	private GeodeticPoint tmToGeodetic(TMPoint TMPoint) {
		GeodeticPoint geodeticPoint = new GeodeticPoint();
		double x = TMPoint.getX();
		double y = TMPoint.getY();
		double lat = 0.0;
		double lon = 0.0;
		double hei = 0.0;
		double con = 0.0; // 수렴
		double phi = 0.0;
		int maxCount = 6;

		x -= vars.srcFalseEasting;
		y -= vars.srcFalseNorthing;
		con = (vars.srcMl0 + y / vars.srcScaleFactor) / vars.srcA;
		phi = con;
		
		for (int i = 0;; i++) {
			double delta_phi = ((con + vars.srcE1 * Math.sin(2.0 * phi) - vars.srcE2 * Math.sin(4.0 * phi)
					+ vars.srcE3 * Math.sin(6.0 * phi)) / vars.srcE0) - phi;
			phi += delta_phi;
			if (Math.abs(delta_phi) <= EPSLN) {
				break;
			}
			if (i >= maxCount) {
				return null;
			}
		}
		
		if (Math.abs(phi) < HALF_PI) {
			double sin_phi = Math.sin(phi);
			double cos_phi = Math.cos(phi);
			double tan_phi = Math.tan(phi);
			double c = vars.srcEC2 * Math.pow(cos_phi, 2);
			double cs = Math.pow(c, 2);
			double t = Math.pow(tan_phi, 2);
			double ts = Math.pow(t, 2);

			con = 1.0 - vars.srcEC1 * Math.pow(sin_phi, 2);
			double n = vars.srcA / Math.sqrt(con);
			double r = n * (1.0 - vars.srcEC1) / con;
			double d = x / (n * vars.srcScaleFactor);
			double ds = Math.pow(d, 2);

			lon = adjustLon(
					vars.srcPrjLon + (d
							* (1.0 - ds / 6.0
									* (1.0 + 2.0 * t + c - ds / 20.0
											* (5.0 - 2.0 * c + 28.0 * t - 3.0 * cs + 8.0 * vars.srcEC2 + 24.0 * ts)))
							/ cos_phi));
			lat = phi - (n * tan_phi * ds / r)
					* (0.5 - ds / 24.0 * (5.0 + 3.0 * t + 10.0 * c - 4.0 * cs - 9.0 * vars.srcEC2
							- ds / 30.0 * (61.0 + 90.0 * t + 298.0 * c + 45.0 * ts - 252.0 * vars.srcEC2 - 3.0 * cs)));
		} else {
			lon = vars.srcPrjLon;
			lat = HALF_PI * sign(y);
		}
		
		geodeticPoint.setLat(lat);
		geodeticPoint.setLon(lon);
		geodeticPoint.setHei(hei);
		
		return geodeticPoint;
	}
	
	// 측지좌표를 지심좌표로
	private GeocentricPoint geodeticToGeocentric(GeodeticPoint geodeticPoint) {
		GeocentricPoint geocentricPoint = new GeocentricPoint();
		
		double geodeticLat = geodeticPoint.getLat();
		double geodeticLon = geodeticPoint.getLon();
		double geodeticHei = geodeticPoint.getHei(); // maybe 0
		
		double geocentricLat = 0.0;
		double geocentricLon = 0.0;
		double geocentricHei = 0.0;
		
		if (geodeticLat < -HALF_PI && geodeticLat > -1.001 * HALF_PI) {
			geodeticLat = -HALF_PI;
		} else if (geodeticLat > HALF_PI && geodeticLat < 1.001 * HALF_PI) {
			geodeticLat = HALF_PI;
		} else if ((geodeticLat < -HALF_PI) || (geodeticLat > HALF_PI)) { // Latitude out of range
			return null;
		}

		if (geodeticLon > Math.PI) {
			geodeticLon -= (2 * Math.PI);
		}
		
		double sinLat = Math.sin(geodeticLat);
		double cosLat = Math.cos(geodeticLat);
		double sinExprLat = sinLat * sinLat;
		double rn = vars.srcA / (Math.sqrt(1.0e0 - vars.srcEC1 * sinExprLat));

		geocentricLat = (rn + geodeticHei) * cosLat * Math.sin(geodeticLon);
		geocentricLon = (rn + geodeticHei) * cosLat * Math.cos(geodeticLon);
		geocentricHei = ((rn * (1 - vars.srcEC1)) + geodeticHei) * sinLat;

		geocentricPoint.setLat(geocentricLat);
		geocentricPoint.setLon(geocentricLon);
		geocentricPoint.setHei(geocentricHei);

		return geocentricPoint;
	}
	
	// 특정 타원체의 지심좌표를 다른 타원체의 지심좌표로 변환
	private GeocentricPoint geocentricToGeocentric(GeocentricPoint srcGeocentricPoint) {
		double srcLon = srcGeocentricPoint.getLon();
		double srcLat = srcGeocentricPoint.getLat();
		double srcHei = srcGeocentricPoint.getHei();
		
		double dx = vars.datumParams[0];
		double dy = vars.datumParams[1];
		double dz = vars.datumParams[2];
		double rx = vars.datumParams[3];
		double ry = vars.datumParams[4];
		double rz = vars.datumParams[5];
		double lamda = vars.datumParams[6];

		// 회전량(rx,ry, rz)를 (초단위 레벨에서)라디안값 으로 변경. 
		rx = Math.toRadians(rx) / 3600.0;  
		ry = Math.toRadians(ry) / 3600.0;
		rz = Math.toRadians(rz) / 3600.0;
		
		// 축척변화량(lamda)
		// ppm기준이므로 1000,000으로 나눈 후 타원체 변환 공식에 따라 1을 더해줌.
		lamda = (lamda / 1000000.0) + 1.0;
		
		// 좌표변환 기준좌표
		double x0 = 0;
		double y0 = 0;
		double z0 = 0;
		
		if ((vars.srcEllipsoid == Ellipse.BESSEL) && (vars.destEllipsoid == Ellipse.GRS80)) {
			x0 = -3159521.31;
			y0 = 4068151.32;
			z0 = 3748113.85;
		} else if ((vars.srcEllipsoid == Ellipse.GRS80) && (vars.destEllipsoid == Ellipse.BESSEL)) {
			x0 = -3159666.86;
			y0 = 4068655.70;
			z0 = 3748799.65;
		} else if ((vars.srcEllipsoid == Ellipse.BESSEL) && (vars.destEllipsoid == Ellipse.WGS84)) {
			x0 = -3159521.31;
			y0 = 4068151.32;
			z0 = 3748113.85;
		} else if ((vars.srcEllipsoid == Ellipse.WGS84) && (vars.destEllipsoid == Ellipse.BESSEL)) {
			x0 = -3159666.86;
			y0 = 4068655.70;
			z0 = 3748799.65;
		} else {
			x0 = 0;
			y0 = 0;
			z0 = 0;
		}
		
		double destLat = 0.0;
		double destLon = 0.0;
		double destHei = 0.0;

		if ((vars.srcEllipsoid == Ellipse.BESSEL)
				&& ((vars.destEllipsoid == Ellipse.GRS80) || (vars.destEllipsoid == Ellipse.WGS84))) {
			double xb_x0 = srcLon - x0;
			double yb_y0 = srcLat - y0;
			double zb_z0 = srcHei - z0;
			destLon = lamda * (xb_x0 + yb_y0 * rz - zb_z0 * ry) + dx + x0;
			destLat = lamda * (-xb_x0 * rz + yb_y0 + zb_z0 * rx) + dy + y0;
			destHei = lamda * (xb_x0 * ry - yb_y0 * rx + zb_z0) + dz + z0;
		} else if (((vars.srcEllipsoid == Ellipse.GRS80) || (vars.srcEllipsoid == Ellipse.WGS84))
					&& (vars.destEllipsoid == Ellipse.BESSEL)) {
			double xg_x0_dx = srcLon - x0 - dx;
			double yg_y0_dy = srcLat - y0 - dy;
			double zg_z0_dz = srcHei - z0 - dz;
			destLon = (xg_x0_dx - yg_y0_dy * rz + zg_z0_dz * ry) / lamda + x0;
			destLat = (xg_x0_dx * rz + yg_y0_dy - zg_z0_dz * rx) / lamda + y0;
			destHei = (-xg_x0_dx * ry + yg_y0_dy * rx + zg_z0_dz) / lamda + z0;
		} else { // GRS80 or WGS84 -> GRS80 or WGS84, BESSEL -> BESSEL
			destLat = srcLat;
			destLon = srcLon;
			destHei = srcHei;
		}
		
		GeocentricPoint destGeocentricPoint = new GeocentricPoint();
		destGeocentricPoint.setLat(destLat);
		destGeocentricPoint.setLon(destLon);
		destGeocentricPoint.setHei(destHei);
		
		return destGeocentricPoint;
	}

	// 지심좌표를 측지좌표로
	private GeodeticPoint geocentricToGeodetic(GeocentricPoint geocentricPoint) {
		GeodeticPoint geodeticPoint = new GeodeticPoint();
		
		double geocentricLat = geocentricPoint.getLat();
		double geocentricLon = geocentricPoint.getLon();
		double geocentricHei = geocentricPoint.getHei();
		
		double geodeticLat = 0.0;
		double geodeticLon = 0.0;
		double geodeticHei = 0.0;
		
		double distance = 0.0; 				// distance from Z axis
		double distanceExpr = 0.0; 			// square of distance from Z axis
		double initVertical = 0.0; 			// initial estimate of vertical component
		double correctedVertical = 0.0; 	// corrected estimate of vertical component
		double initHorizontal = 0.0; 		// initial estimate of horizontal component
		double correctedHorizontal = 0.0; 	// corrected estimate of horizontal component
		double sinBowringAux = 0.0; 		// Math.sin(B0), B0 is estimate of Bowring aux variable
		double sin3BowringAux = 0.0; 		// cube of Math.sin(B0)
		double cosBowringAux;   			// Math.cos(B0)
		double sinPhi;   					// Math.sin(phi1), phi1 is estimated latitude
		double cosPhi;   					// Math.cos(phi1)
		double rn;       					// Earth radius at location
		double sum;      					// numerator of Math.cos(phi1)
		boolean atPole = false;  			// indicates location is in polar region

		if (geocentricLon != 0.0) {
			geodeticLon = Math.atan2(geocentricLat, geocentricLon);
		} else {
			if (geocentricLat > 0) {
				geodeticLon = HALF_PI;
			} else if (geocentricLat < 0) {
				geodeticLon = -HALF_PI;
			} else {
				atPole = true;
				geodeticLon = 0.0;
				if (geocentricHei > 0.0) { 			// north pole
					geodeticLat = HALF_PI;
				} else if (geocentricHei < 0.0) { 	// south pole
					geodeticLat = -HALF_PI;
				} else { // center of earth
					geodeticLat = HALF_PI;
					geodeticHei = -vars.destB;
					return null;
				}
			}
		}
		
		distanceExpr = geocentricLon * geocentricLon + geocentricLat * geocentricLat;
		distance = Math.sqrt(distanceExpr);
		initVertical = geocentricHei * AD_C;
		initHorizontal = Math.sqrt(initVertical * initVertical + distanceExpr);
		sinBowringAux = initVertical / initHorizontal;
		cosBowringAux = distance / initHorizontal;
		sin3BowringAux = sinBowringAux * sinBowringAux * sinBowringAux;
		correctedVertical = geocentricHei + vars.destB * vars.destEC2 * sin3BowringAux;
		sum = distance - vars.destA * vars.destEC1 * cosBowringAux * cosBowringAux * cosBowringAux;
		correctedHorizontal = Math.sqrt(correctedVertical * correctedVertical + sum * sum);
		sinPhi = correctedVertical / correctedHorizontal; // T1/S1
		cosPhi = sum / correctedHorizontal;
		rn = vars.destA / Math.sqrt(1.0 - vars.destEC1 * sinPhi * sinPhi);
		
		if (cosPhi >= Math.cos(67.5)) { 			// COS_67P5
			geodeticHei = distance / cosPhi - rn;
		} else if (cosPhi <= -Math.cos(67.5)) { 	// COS_67P5
			geodeticHei = distance / -cosPhi - rn;
		} else {
			geodeticHei = geocentricHei / sinPhi + rn * (vars.destEC1 - 1.0);
		}
		
		if (atPole == false) {
			geodeticLat = Math.atan(sinPhi / cosPhi);
		}
		
		geodeticPoint.setLat(geodeticLat);
		geodeticPoint.setLon(geodeticLon);
		geodeticPoint.setHei(geodeticHei);
		
		return geodeticPoint;
	}
	
	// Geo2TM	측지좌표를 직각좌표로
	private TMPoint geodeticToTm(GeodeticPoint geodeticPoint) {
		TMPoint TMPoint = new TMPoint();
		
		double x = 0.0;
		double y = 0.0;
		
		double lat = geodeticPoint.getLat();
		double lon = geodeticPoint.getLon();

		double deltaLon = adjustLon(lon - vars.destPrjLon);
		double sinPhi = Math.sin(lat);
		double cosPhi = Math.cos(lat);
		double al = cosPhi * deltaLon;
		double als = Math.pow(al, 2);
		double c = vars.destEC2 * Math.pow(cosPhi, 2);
		double tq = Math.tan(lat);
		double t = Math.pow(tq, 2);
		double con = 1.0 - vars.destEC1 * Math.pow(sinPhi, 2);
		double n = vars.destA / Math.sqrt(con);
		double ml = vars.destA * mlfn(vars.destE0, vars.destE1, vars.destE2, vars.destE3, lat);

		x = vars.destScaleFactor * n * al * (1.0 + als / 6.0
				* (1.0 - t + c + als / 20.0 * (5.0 - 18.0 * t + Math.pow(t, 2) + 72.0 * c - 58.0 * vars.destEC2)))
				+ vars.destFalseEasting;
		y = vars.destScaleFactor
				* (ml - vars.destMl0 + n * tq * (als * (0.5 + als / 24.0
						* (5.0 - t + 9.0 * c + 4.0 * Math.pow(c, 2)
								+ als / 30.0 * (61.0 - 58.0 * t + Math.pow(t, 2) + 600.0 * c - 330.0 * vars.destEC2)))))
				+ vars.destFalseNorthing;
		TMPoint.setX(x);
		TMPoint.setY(y);

		return TMPoint;
	}

	private void GeoToTm(double lon, double lat) {
		GeodeticPoint geodeticPoint = new GeodeticPoint();
		geodeticPoint.setLon(Math.toRadians(lon));
		geodeticPoint.setLat(Math.toRadians(lat));

		if ((m_strSrcEllipse == Ellipse.GRS80) && (m_strDstEllipse == Ellipse.GRS80)) {
			SetGeoEllipsSystem(Ellipse.NULL, CoordSystem.GEOGRAPHIC, Ellipse.GRS80, CoordSystem.UTMK);
		} else if (m_strSrcEllipse != m_strDstEllipse) {
			GeocentricPoint srcGeocentricPoint = geodeticToGeocentric(geodeticPoint);
			GeocentricPoint destGeocentricPoint = geocentricToGeocentric(srcGeocentricPoint);

			geodeticPoint = geocentricToGeodetic(destGeocentricPoint);
		}

		TMPoint TMPoint = geodeticToTm(geodeticPoint);

		m_dConvX = TMPoint.getX();
		m_dConvY = TMPoint.getY();
	}

	private void TmToGeo(double x, double y) {
		TMPoint TMPoint = new TMPoint();
		TMPoint.setX(x);
		TMPoint.setY(y);

		GeodeticPoint geodeticPoint = new GeodeticPoint();
		geodeticPoint = tmToGeodetic(TMPoint);

		if ((m_strSrcEllipse == Ellipse.GRS80) && (m_strDstEllipse == Ellipse.GRS80)) {
			SetGeoEllipsSystem(Ellipse.GRS80, CoordSystem.UTMK, Ellipse.NULL, CoordSystem.GEOGRAPHIC);
		} else if (m_strSrcEllipse != m_strDstEllipse) {
			GeocentricPoint srcGeocentricPoint = geodeticToGeocentric(geodeticPoint);
			GeocentricPoint destGeocentricPoint = geocentricToGeocentric(srcGeocentricPoint);
			
			geodeticPoint = geocentricToGeodetic(destGeocentricPoint);
		}

		m_dConvX = Math.toDegrees(geodeticPoint.getLon());
		m_dConvY = Math.toDegrees(geodeticPoint.getLat());
	}

	private void TmToTm(double x, double y) {
		TMPoint srcTMPoint = new TMPoint();
		srcTMPoint.setX(x);
		srcTMPoint.setY(y);

		GeodeticPoint geodeticPoint = new GeodeticPoint();
		geodeticPoint = tmToGeodetic(srcTMPoint);

		if (!m_strSrcEllipse.equals(m_strDstEllipse)) {
			GeocentricPoint srcGeocentricPoint = geodeticToGeocentric(geodeticPoint);
			GeocentricPoint destGeocentricPoint = geocentricToGeocentric(srcGeocentricPoint);

			geodeticPoint = geocentricToGeodetic(destGeocentricPoint);
		}

		TMPoint destTMPoint = new TMPoint();
		destTMPoint = geodeticToTm(geodeticPoint);

		m_dConvX = destTMPoint.getX();
		m_dConvY = destTMPoint.getY();
	}

	class GeodeticPoint {
		private double lat; // 위도
		private double lon; // 경도
		private double hei; // 표고

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public double getHei() {
			return hei;
		}

		public void setHei(double hei) {
			this.hei = hei;
		}
	}

	class GeocentricPoint extends GeodeticPoint {}

	class TMPoint {
		private double x; // 경도(lon)
		private double y; // 위도(lat)

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}
	}
	
	class ConvVars {
		public ConvVars() {
			initMolodenskyParameter();
		}

		// Source
		public Ellipse srcEllipsoid = Ellipse.NULL; 		// source 타원체 모델
		public double srcA = 0.0; 							// source 타원체의 장반경
		public double srcB = 0.0; 							// source 타원체의 단반경
		// private double srcInverseF = 0.0; 				// 편평률 역수 (cf. 편평률 = (장반경-단반경)/장반경 )
		public double srcScaleFactor = 0.0; 				// 원점 축척계수
		public double srcFalseNorthing = 0.0; 				// 원점가산값(남북)
		public double srcFalseEasting = 0.0; 				// 원점가산값(동서)
		public double srcPrjLat = 0.0;						// 투영원점 위도
		public double srcPrjLon = 0.0;						// 투영원점 경도
		public double srcMl0 = 0.0;
		public double srcE0 = 0.0;
		public double srcE1 = 0.0;
		public double srcE2 = 0.0;
		public double srcE3 = 0.0;
		public double srcEC1 = 0.0; 						// srcEC1
		public double srcEC2 = 0.0; 						// srcEC2

		public double datumParams[] = new double[7]; 		// molodensky 7 parameter

		// Dest
		public Ellipse destEllipsoid = Ellipse.NULL; 		// dest 타원체 모델
		public double destA = 0.0; 							// dest 타원체의 장반경
		public double destB = 0.0; 							// dest 타원체의 단반경
		// private double destInverseF = 0.0; 				//dest 타원체의 편평률 역수
		public double destPrjLat = 0.0;
		public double destPrjLon = 0.0;
		public double destFalseNorthing = 0.0;
		public double destFalseEasting = 0.0;
		public double destScaleFactor = 0.0;
		public double destMl0 = 0.0;
		public double destE0 = 0.0;
		public double destE1 = 0.0;
		public double destE2 = 0.0;
		public double destE3 = 0.0;
		public double destEC1 = 0.0;
		public double destEC2 = 0.0;
		
		// TM정보 초기화
		public void initTM(CoordSystem srcTM, CoordSystem destTM) {
			if (srcTM != CoordSystem.GEOGRAPHIC) {
				// source
				switch (srcTM) {
				case TMW:
					srcPrjLat = 0.663225115757845;
					srcPrjLon = 2.18171200985643;
					srcFalseNorthing = 500000.0;
					srcFalseEasting = 200000.0;
					srcScaleFactor = 1.0;
					break;
				case TMM:
					srcPrjLat = 0.663225115757845;
					srcPrjLon = 2.21661859489632;
					srcFalseNorthing = 500000.0;
					srcFalseEasting = 200000.0;
					srcScaleFactor = 1.0;
					break;
				case TME:
					srcPrjLat = 0.663225115757845;
					srcPrjLon = 2.2515251799362;
					srcFalseNorthing = 500000.0;
					srcFalseEasting = 200000.0;
					srcScaleFactor = 1.0;
					break;
				case KATECH:
					srcPrjLat = Math.toRadians(38.0);
					srcPrjLon = Math.toRadians(128.0);
					srcFalseNorthing = 600000.0;
					srcFalseEasting = 400000.0;
					srcScaleFactor = 0.9999;
					break;				
				case UTM52:
					srcPrjLat = 0.0;
					srcPrjLon = 2.25147473507269;
					srcFalseNorthing = 0.0;
					srcFalseEasting = 500000.0;
					srcScaleFactor = 0.9996;
					break;
				case UTM51:
					srcPrjLat = 0.0;
					srcPrjLon = 2.14675497995303;
					srcFalseNorthing = 0.0;
					srcFalseEasting = 500000.0;
					srcScaleFactor = 0.9996;
					break;
				case UTMK:
					srcPrjLat = Math.toRadians(38.0);
					srcPrjLon = Math.toRadians(127.5);
					srcFalseNorthing = 2000000.0;
					srcFalseEasting = 1000000.0;
					srcScaleFactor = 0.9996;
					break;
				case CONGNAMUL:
					srcPrjLat = 0.663225115757845;
					srcPrjLon = 2.21661859489632 - Math.toRadians(0.0028902777777777776);
					srcFalseNorthing = 500000.0;
					srcFalseEasting = 200000.0;
					srcScaleFactor = 1.0;
					break;
				default:
					break;
				}
			} else {
				srcScaleFactor = 1.0;
			}

			if (destTM != CoordSystem.GEOGRAPHIC) {
				switch (destTM) {
				case TMW:
					destPrjLat = 0.663225115757845;
					destPrjLon = 2.18171200985643;
					destFalseNorthing = 500000.0;
					destFalseEasting = 200000.0;
					destScaleFactor = 1.0;
					break;
				case TMM: 
					destPrjLat = 0.663225115757845;
					destPrjLon = 2.21661859489632;
					destFalseNorthing = 500000.0;
					destFalseEasting = 200000.0;
					destScaleFactor = 1.0;
					break;
				case TME:
					destPrjLat = 0.663225115757845;
					destPrjLon = 2.2515251799362;
					destFalseNorthing = 500000.0;
					destFalseEasting = 200000.0;
					destScaleFactor = 1.0;
					break;
				case KATECH:
					destPrjLat = Math.toRadians(38.0);
					destPrjLon = Math.toRadians(128.0);
					destFalseNorthing = 600000.0;
					destFalseEasting = 400000.0;
					destScaleFactor = 0.9999;
					break;
				case UTM52:
					destPrjLat = 0.0;
					destPrjLon = 2.25147473507269;
					destFalseNorthing = 0.0;
					destFalseEasting = 500000.0;
					destScaleFactor = 0.9996;
					break;
				case UTM51:
					destPrjLat = 0.0;
					destPrjLon = 2.14675497995303;
					destFalseNorthing = 0.0;
					destFalseEasting = 500000.0;
					destScaleFactor = 0.9996;
					break;
				case UTMK:
					destPrjLat = Math.toRadians(38.0);
					destPrjLon = Math.toRadians(127.5);
					destFalseNorthing = 2000000.0;
					destFalseEasting = 1000000.0;
					destScaleFactor = 0.9996;
					break;
				case CONGNAMUL:
					destPrjLat = 0.663225115757845;
					destPrjLon = 2.21661859489632 - Math.toRadians(0.0028902777777777776);
					destFalseNorthing = 500000.0;
					destFalseEasting = 200000.0;
					destScaleFactor = 1.0;
					break;
				default:
					break;
				}
				
			} else {
				destScaleFactor = 1.0;
			}
		}
		
		// 타원체정보 초기화
		private void initEllipsoid(Ellipse srcEllipse, Ellipse destEllipse) {
			if (srcEllipse != Ellipse.NULL) {
				switch (srcEllipse) {
				case BESSEL:
					srcA = 6377397.155;
					srcB = 6356078.9628181886;
					break;
				case GRS80:
					srcA = 6378137.0;
					srcB = 6356752.3141403561;
					break;
				case WGS84:
					srcA = 6378137.0;
					srcB = 6356752.3142451793;
					break;
				default:
					break;
				}

				double srcA2 = srcA * srcA;
				double srcB2 = srcB * srcB;
				srcEC1 = (srcA2 - srcB2) / srcA2;
				srcEC2 = (srcA2 - srcB2) / srcB2;
				srcE0 = e0fn(srcEC1);
				srcE1 = e1fn(srcEC1);
				srcE2 = e2fn(srcEC1);
				srcE3 = e3fn(srcEC1);
				srcMl0 = srcA * mlfn(srcE0, srcE1, srcE2, srcE3, srcPrjLat);
				srcEllipsoid = srcEllipse;

			} else {
				srcEllipsoid = Ellipse.NULL;
				srcA = 0.0;
				srcB = 0.0;
				srcScaleFactor = 0.0;
				srcFalseNorthing = 0.0;
				srcFalseEasting = 0.0;
				srcPrjLat = 0.0;
				srcPrjLon = 0.0;
				srcMl0 = 0.0;
				srcE0 = 0.0;
				srcE1 = 0.0;
				srcE2 = 0.0;
				srcE3 = 0.0;
				srcEC1 = 0.0;
				srcEC2 = 0.0;
			}

			if (destEllipse != Ellipse.NULL) {
				switch (destEllipse) {
				case BESSEL:
					destA = 6377397.155;
					destB = 6356078.9628181886;
					break;
				case GRS80:
					destA = 6378137.0;
					destB = 6356752.3141403561;
					break;
				case WGS84:
					destA = 6378137.0;
					destB = 6356752.3142451793;
					break;
				default:
					break;
				}
				
				double destA2 = destA * destA;
				double destB2 = destB * destB;
				destEC1 = (destA2 - destB2) / destA2;
				destEC2 = (destA2 - destB2) / destB2;
				destE0 = e0fn(destEC1);
				destE1 = e1fn(destEC1);
				destE2 = e2fn(destEC1);
				destE3 = e3fn(destEC1);
				destMl0 = destA * mlfn(destE0, destE1, destE2, destE3, destPrjLat);
				destEllipsoid = destEllipse;

			} else {
				destEllipsoid = Ellipse.NULL;
				destA = 0.0;
				destB = 0.0;
				destPrjLat = 0.0;
				destPrjLon = 0.0;
				destFalseNorthing = 0.0;
				destFalseEasting = 0.0;
				destScaleFactor = 0.0;
				destMl0 = 0.0;
				destE0 = 0.0;
				destE1 = 0.0;
				destE2 = 0.0;
				destE3 = 0.0;
				destEC1 = 0.0;
				destEC2 = 0.0;
			}
		}
				
		// Molodensky 7파라메터 초기화
		public void initMolodenskyParameter() {
			datumParams[0] = -145.907;
			datumParams[1] = 505.034;
			datumParams[2] = 685.756;
			datumParams[3] = -1.162;
			datumParams[4] = 2.347;
			datumParams[5] = 1.592;
			datumParams[6] = 6.342;
		}

		private double e0fn(double x) {
			return (1.0 - 0.25 * x * (1.0 + x / 16.0 * (3.0 + 1.25 * x)));
		}

		private double e1fn(double x) {
			return (0.375 * x * (1.0 + 0.25 * x * (1.0 + 0.46875 * x)));
		}

		private double e2fn(double x) {
			return (0.05859375 * x * x * (1.0 + 0.75 * x));
		}

		private double e3fn(double x) {
			return (x * x * x * (35.0 / 3072.0));
		}

		private double mlfn(double e0, double e1, double e2, double e3, double phi) {
			return (e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3 * Math.sin(6.0 * phi));
		}
	}
	
}
