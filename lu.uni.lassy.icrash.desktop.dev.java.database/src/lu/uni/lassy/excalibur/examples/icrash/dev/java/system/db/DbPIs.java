package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.CtAlert;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.CtCrisis;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.CtHuman;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.CtPI;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtAlertID;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtComment;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtCrisisID;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtGPSLocation;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtLatitude;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtLongitude;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtPIID;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtPITitle;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.EtAlertStatus;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.EtCrisisDomain;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.EtCrisisStatus;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.EtCrisisType;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.EtPICategory;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtDate;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtDateAndTime;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtTime;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtReal;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.utils.ICrashUtils;

/**
 * The Class DbPIs for updating and retrieving information from the table Points of Interest (PI) in the database.
 */
public class DbPIs extends DbAbstract {

	/**
	 * Count the number of PIs registered into the database.
	 *
	 */
	static public int countPIs() {

		int answer = 0; 
		try{

			conn = DriverManager.getConnection(url + dbName, userName, password);
			try{
				
				// why l in numberOfPIsl?
				String sql = "SELECT COUNT(id)  AS numberOfPIsl FROM "+ dbName + ".pis" ;


				Statement statement = conn.createStatement();
				ResultSet  res = statement.executeQuery(sql);

				if (res.next())
					answer = res.getInt("numberOfPIsl");	

			}catch (SQLException s){
				log.error("SQL statement is not executed! "+s);
			}
			conn.close();
			} catch (Exception e) {
			logException(e);
		}
		return answer;

	}



	/**
	 * Insert a PI into the database.
	 *
	 * @param aCtPI the class of the alert to insert
	 */
	static public void insertPI(CtPI aCtPI) {
		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Insert

			try {
				Statement st = conn.createStatement();

				String id = aCtPI.id.value.getValue();
				double latitude = aCtPI.location.latitude.value.getValue();
				double longitude = aCtPI.location.longitude.value.getValue();

				int year = aCtPI.instant.date.year.value.getValue();
				int month = aCtPI.instant.date.month.value.getValue();
				int day = aCtPI.instant.date.day.value.getValue();

				int hour = aCtPI.instant.time.hour.value.getValue();
				int min = aCtPI.instant.time.minute.value.getValue();
				int sec = aCtPI.instant.time.second.value.getValue();

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Calendar calendar = new GregorianCalendar(year, month, day,
						hour, min, sec);
				String instant = sdf.format(calendar.getTime());

				String title = aCtPI.title.value.getValue();
				String category = aCtPI.category.toString();
				
				log.debug("[DATABASE]-Insert PI");
				int val = st.executeUpdate("INSERT INTO " + dbName + ".pis"
						+ "(id,latitude,longitude,instant,title,category)"
						+ "VALUES(" + "'" + id + "'" + "', " + latitude + ", " + longitude + ", '"
						+ instant + "','" + title + "','" + category + "')");

				log.debug(val + " row affected");
			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}

			conn.close();
			log.debug("Disconnected from database");
		} catch (Exception e) {
			logException(e);
		}

	}

	/**
	 * Gets a PI from the database by the PI ID.
	 *
	 * @param piId The ID of the PI to retrieve from the database
	 * @return the class of the PI to retrieve
	 */
	static public CtPI getPI(String piId) {

		CtPI aCtPI = new CtPI();

		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Select

			try {
				String sql = "SELECT * FROM " + dbName + ".pis WHERE id = "
						+ piId;

				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet res = statement.executeQuery(sql);

				if (res.next()) {

					aCtPI = new CtPI();
					//alert's id
					DtPIID aId = new DtPIID(new PtString(
							res.getString("id")));

					//PI's location
					DtLatitude aDtLatitude = new DtLatitude(new PtReal(
							res.getDouble("latitude")));
					DtLongitude aDtLongitude = new DtLongitude(new PtReal(
							res.getDouble("longitude")));
					DtGPSLocation aDtGPSLocation = new DtGPSLocation(
							aDtLatitude, aDtLongitude);

					//PI's instant
					Timestamp instant = res.getTimestamp("instant");
					Calendar cal = Calendar.getInstance();
					cal.setTime(instant);

					int d = cal.get(Calendar.DATE);
					int m = cal.get(Calendar.MONTH);
					int y = cal.get(Calendar.YEAR);
					DtDate aDtDate = ICrashUtils.setDate(y, m, d);
					int h = cal.get(Calendar.HOUR_OF_DAY);
					int min = cal.get(Calendar.MINUTE);
					int sec = cal.get(Calendar.SECOND);
					DtTime aDtTime = ICrashUtils.setTime(h, min, sec);
					DtDateAndTime aInstant = new DtDateAndTime(aDtDate, aDtTime);

					//PI's title 
					DtPITitle aDtPITitle = new DtPITitle(new PtString(
							res.getString("title")));
					
					//PI's category
					String theCategory = res.getString("category");
					EtPICategory aEtPICategory = null;
					if (theCategory.equals(EtPICategory.gas.name()))
						aEtPICategory = EtPICategory.gas;
					if (theCategory.equals(EtPICategory.food.name()))
						aEtPICategory = EtPICategory.food;
					if (theCategory.equals(EtPICategory.music.name()))
						aEtPICategory = EtPICategory.music;
					if (theCategory.equals(EtPICategory.movie.name()))
						aEtPICategory = EtPICategory.movie;
					if (theCategory.equals(EtPICategory.other.name()))
						aEtPICategory = EtPICategory.other;

					aCtPI.init(aId, aDtGPSLocation, aInstant,
							aDtPITitle, aEtPICategory);

				}

			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}
			conn.close();
			log.debug("Disconnected from database");

		} catch (Exception e) {
			logException(e);
		}

		return aCtPI;

	}

	/**
	 * Gets the current highest number used for an PI ID in the database.
	 *
	 * @return the highest number for a PI ID
	 */
	static public int getMaxPIID() {

		int maxPIID = 0;

		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Select

			try {
				String sql = "SELECT MAX(CAST(id AS UNSIGNED)) FROM " + dbName
						+ ".pis";

				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet res = statement.executeQuery(sql);

				if (res.next()) {
					maxPIID = res.getInt(1);
				}

			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}
			conn.close();
			log.debug("Disconnected from database");

		} catch (Exception e) {
			logException(e);
		}

		return maxPIID;

	}

	/**
	 * Gets all of the PIs currently in the database.
	 *
	 * @return a hashtable of the PIs using the ID of the alert as a key
	 */
	static public Hashtable<String, CtPI> getSystemPIs() {

		Hashtable<String, CtPI> cmpSystemCtPI = new Hashtable<String, CtPI>();

		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Select

			try {
				String sql = "SELECT * FROM " + dbName + ".pis ";

				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet res = statement.executeQuery(sql);

				CtPI aCtPI = null;

				while (res.next()) {

					aCtPI = new CtPI();
					//alert's id
					DtPIID aId = new DtPIID(new PtString(
							res.getString("id")));

					//PI's location
					DtLatitude aDtLatitude = new DtLatitude(new PtReal(
							res.getDouble("latitude")));
					DtLongitude aDtLongitude = new DtLongitude(new PtReal(
							res.getDouble("longitude")));
					DtGPSLocation aDtGPSLocation = new DtGPSLocation(
							aDtLatitude, aDtLongitude);

					//PI's instant
					Timestamp instant = res.getTimestamp("instant");
					Calendar cal = Calendar.getInstance();
					cal.setTime(instant);

					int d = cal.get(Calendar.DATE);
					int m = cal.get(Calendar.MONTH);
					int y = cal.get(Calendar.YEAR);
					DtDate aDtDate = ICrashUtils.setDate(y, m, d);
					int h = cal.get(Calendar.HOUR_OF_DAY);
					int min = cal.get(Calendar.MINUTE);
					int sec = cal.get(Calendar.SECOND);
					DtTime aDtTime = ICrashUtils.setTime(h, min, sec);
					DtDateAndTime aInstant = new DtDateAndTime(aDtDate, aDtTime);

					//PI's title 
					DtPITitle aDtPITitle = new DtPITitle(new PtString(
							res.getString("title")));
					
					//PI's category
					String theCategory = res.getString("category");
					EtPICategory aEtPICategory = null;
					if (theCategory.equals(EtPICategory.gas.name()))
						aEtPICategory = EtPICategory.gas;
					if (theCategory.equals(EtPICategory.food.name()))
						aEtPICategory = EtPICategory.food;
					if (theCategory.equals(EtPICategory.music.name()))
						aEtPICategory = EtPICategory.music;
					if (theCategory.equals(EtPICategory.movie.name()))
						aEtPICategory = EtPICategory.movie;
					if (theCategory.equals(EtPICategory.other.name()))
						aEtPICategory = EtPICategory.other;

					//init aCtAlert instance
					aCtPI.init(aId, aDtGPSLocation, aInstant,
							aDtPITitle, aEtPICategory);

					//add instance to the hash
					cmpSystemCtPI
					.put(aCtPI.id.value.getValue(), aCtPI);

				}

			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}
			conn.close();
			log.debug("Disconnected from database");

		} catch (Exception e) {
			logException(e);
		}

		return cmpSystemCtPI;

	}

	/**
	 * Gets the PIs and their associated crises and inserts them into a hashtable, using the PI as a key.
	 *
	 * @return the hashtable of the associated crises and PIs
	 */
	static public Hashtable<CtPI, CtCrisis> getAssCtPICtCrisis() {
		
		Hashtable<CtPI, CtCrisis> assCtPICtCrisis = new Hashtable<CtPI, CtCrisis>();

		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Select

			try {
				String sql = "SELECT * FROM " + dbName + ".pis "
						+ "INNER JOIN " + dbName + ".crises ON " + dbName
						+ ".pis.crisis = " + dbName + ".crises.id";

				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet res = statement.executeQuery(sql);

				CtPI aCtPI = null;
				CtCrisis aCtCrisis = null;

				while (res.next()) {

					aCtPI = new CtPI();
					//PI's id
					DtPIID aId = new DtPIID(new PtString(
							res.getString("pis.id")));
					
					//PI's title 
					DtPITitle aDtPITitle = new DtPITitle(new PtString(
							res.getString("pis.title")));
					
					//PI's category
					String theCategory = res.getString("pis.category");
					EtPICategory aDtPICategory = null;
					if (theCategory.equals(EtPICategory.food.name()))
						aDtPICategory = EtPICategory.food;
					if (theCategory.equals(EtPICategory.gas.name()))
						aDtPICategory = EtPICategory.gas;
					if (theCategory.equals(EtPICategory.movie.name()))
						aDtPICategory = EtPICategory.movie;
					if (theCategory.equals(EtPICategory.music.name()))
						aDtPICategory = EtPICategory.music;
					if (theCategory.equals(EtPICategory.other.name()))
						aDtPICategory = EtPICategory.other;

					//PI's location
					DtLatitude aDtLatitude = new DtLatitude(new PtReal(
							res.getDouble("pis.latitude")));
					DtLongitude aDtLongitude = new DtLongitude(new PtReal(
							res.getDouble("pis.longitude")));
					DtGPSLocation aDtGPSLocation = new DtGPSLocation(
							aDtLatitude, aDtLongitude);

					//PI's instant
					Timestamp instant = res.getTimestamp("pis.instant");
					Calendar cal = Calendar.getInstance();
					cal.setTime(instant);

					int d = cal.get(Calendar.DATE);
					int m = cal.get(Calendar.MONTH);
					int y = cal.get(Calendar.YEAR);
					DtDate aDtDate = ICrashUtils.setDate(y, m, d);
					int h = cal.get(Calendar.HOUR_OF_DAY);
					int min = cal.get(Calendar.MINUTE);
					int sec = cal.get(Calendar.SECOND);
					DtTime aDtTime = ICrashUtils.setTime(h, min, sec);
					DtDateAndTime aInstant = new DtDateAndTime(aDtDate, aDtTime);

					//init aCtPI instance
					aCtPI.init(aId, aDtGPSLocation, aInstant,
							aDtPITitle, aDtPICategory);

					//*************************************
					aCtCrisis = new CtCrisis();
					//crisis's id
					DtCrisisID aCrisisId = new DtCrisisID(new PtString(
							res.getString("crises.id")));

					//crisis' type -> [small, medium, huge]
					String theCrisisType = res.getString("crises.type");
					EtCrisisType aCrisisType = null;
					if (theCrisisType.equals(EtCrisisType.small.name()))
						aCrisisType = EtCrisisType.small;
					if (theCrisisType.equals(EtCrisisType.medium.name()))
						aCrisisType = EtCrisisType.medium;
					if (theCrisisType.equals(EtCrisisType.huge.name()))
						aCrisisType = EtCrisisType.huge;

					//crisis's status -> [pending, handled, solved, closed]
					String theCrisisStatus = res.getString("crises.status");
					EtCrisisStatus aCrisisStatus = null;
					if (theCrisisStatus.equals(EtCrisisStatus.pending.name()))
						aCrisisStatus = EtCrisisStatus.pending;
					if (theCrisisStatus.equals(EtCrisisStatus.handled.name()))
						aCrisisStatus = EtCrisisStatus.handled;
					if (theCrisisStatus.equals(EtCrisisStatus.solved.name()))
						aCrisisStatus = EtCrisisStatus.solved;
					if (theCrisisStatus.equals(EtCrisisStatus.closed.name()))
						aCrisisStatus = EtCrisisStatus.closed;
					
					//crisis's domain -> [regular, fire, chemicalSubstance, naturalCase, unknownSubstance]
					String theCrisisDomain = res.getString("domain");
					EtCrisisDomain aCrisisDomain = null;
					if (theCrisisDomain.equals(EtCrisisDomain.regular.name()))
						aCrisisDomain = EtCrisisDomain.regular;
					if (theCrisisDomain.equals(EtCrisisDomain.fire.name()))
						aCrisisDomain = EtCrisisDomain.fire;
					if (theCrisisDomain.equals(EtCrisisDomain.chemicalSubstance.name()))
						aCrisisDomain = EtCrisisDomain.chemicalSubstance;
					if (theCrisisDomain.equals(EtCrisisDomain.naturalCase.name()))
						aCrisisDomain = EtCrisisDomain.naturalCase;
					if (theCrisisDomain.equals(EtCrisisDomain.unknownSubstance.name()))
						aCrisisDomain = EtCrisisDomain.unknownSubstance;

					//crisis's location
					DtLatitude aCrisisDtLatitude = new DtLatitude(new PtReal(
							res.getDouble("crises.latitude")));
					DtLongitude aCrisisDtLongitude = new DtLongitude(
							new PtReal(res.getDouble("crises.longitude")));
					DtGPSLocation aCrisisDtGPSLocation = new DtGPSLocation(
							aCrisisDtLatitude, aCrisisDtLongitude);

					//crisis's instant
					instant = res.getTimestamp("crises.instant");
					cal.setTime(instant);

					d = cal.get(Calendar.DATE);
					m = cal.get(Calendar.MONTH);
					y = cal.get(Calendar.YEAR);
					aDtDate = ICrashUtils.setDate(y, m, d);
					h = cal.get(Calendar.HOUR_OF_DAY);
					min = cal.get(Calendar.MINUTE);
					sec = cal.get(Calendar.SECOND);
					aDtTime = ICrashUtils.setTime(h, min, sec);
					DtDateAndTime aCrisisInstant = new DtDateAndTime(aDtDate,
							aDtTime);

					//crisis's comment  
					DtComment aCrisisDtComment = new DtComment(new PtString(
							res.getString("crises.comment")));

					aCtCrisis.init(aCrisisId, aCrisisType, aCrisisStatus, aCrisisDomain,
							aCrisisDtGPSLocation, aCrisisInstant,
							aCrisisDtComment);

					//add instances to the hash
					assCtPICtCrisis.put(aCtPI, aCtCrisis);

				}

			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}
			conn.close();
			log.debug("Disconnected from database");

		} catch (Exception e) {
			logException(e);
		}

		return assCtPICtCrisis;
	}
	
	/**
	 * Gets the PIs and their associated humans and inserts them into a hashtable, using the PI as a key.
	 *
	 * @return the hashtable of the associated humans and PIs
	 */
	/*
	static public Hashtable<CtPI, CtHuman> getAssCtPICtHuman() {
		// TO DO! Getting the PIs and their associated crises inserting them into a hashtable
		return null;
	}*/
	

	/**
	 * Binds a crisis onto a PI in the database.
	 *
	 * @param aCtPI The PI to bind the crisis to
	 * @param aCtCrisis The crisis to bind to to the PI
	 */
	static public void bindPICrisis(CtPI aCtPI, CtCrisis aCtCrisis) {
		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Update

			try {
				String sql = "UPDATE " + dbName
						+ ".pis SET crisis =? WHERE id = ?";
				String id = aCtPI.id.value.getValue();
				String crisiId = aCtCrisis.id.value.getValue();

				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, crisiId);
				statement.setString(2, id);
				int rows = statement.executeUpdate();
				log.debug(rows + " row affected");
			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}

			conn.close();
			log.debug("Disconnected from database");
		} catch (Exception e) {
			logException(e);
		}

	}

	/**
	 * Binds a human onto a PI in the database.
	 *
	 * @param aCtAlert The PI to bind the human to
	 * @param aCtHuman The human to bind to the PI
	 */
	/*
	static public void bindPIHuman(CtPI aCtPI, CtHuman aCtHuman) {
		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");
			
			//Update

			try {
				String sql = "UPDATE " + dbName
						+ ".pis SET human =? WHERE id = ?";
				String id = aCtPI.id.value.getValue();
				String humanPhone = aCtHuman.id.value.getValue();

				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, humanPhone);
				statement.setString(2, id);
				int rows = statement.executeUpdate();
				log.debug(rows + " row affected");
			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}

			conn.close();
			log.debug("Disconnected from database");
		} catch (Exception e) {
			logException(e);
		}

	}*/

	/**
	 * Deletes a PI from the database, it will use the ID from the CtPI to delete it.
	 *
	 * @param aCtPI The PI to delete from the database
	 */
	static public void deletePI(CtPI aCtPI) {

		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Delete

			try {
				String sql = "DELETE FROM " + dbName + ".pis WHERE id = ?";
				String id = aCtPI.id.value.getValue();

				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, id);
				int rows = statement.executeUpdate();
				log.debug(rows + " row deleted");
			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}

			conn.close();
			log.debug("Disconnected from database");
		} catch (Exception e) {
			logException(e);
		}
	}

	// TO DO! Binding of crisis and human onto PI

	/**
	 * Updates a PI in the database to have the same details as the CtPI
	 * It will update the PI with the same ID as the ID in the CtPI.
	 *
	 * @param aCtPI The PI to update
	 */
	static public void updatePI(CtPI aCtPI) {

		try {
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.debug("Connected to the database");

			/********************/
			//Update

			try {
				log.debug("[DATABASE]-Update PI");
				String sql = "UPDATE "
						+ dbName
						+ ".pis SET `status` = ?, `latitude` = ?, `longitude` = ?,"
						+ " `instant` = ?, `comment` = ? WHERE id = ?";
				String id = aCtPI.id.value.getValue();
				double latitude = aCtPI.location.latitude.value.getValue();
				double longitude = aCtPI.location.longitude.value.getValue();

				int year = aCtPI.instant.date.year.value.getValue();
				int month = aCtPI.instant.date.month.value.getValue();
				int day = aCtPI.instant.date.day.value.getValue();

				int hour = aCtPI.instant.time.hour.value.getValue();
				int min = aCtPI.instant.time.minute.value.getValue();
				int sec = aCtPI.instant.time.second.value.getValue();

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Calendar calendar = new GregorianCalendar(year, month, day,
						hour, min, sec);
				String instant = sdf.format(calendar.getTime());

				String title = aCtPI.title.value.getValue();
				
				String category = aCtPI.category.toString();

				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setDouble(1, latitude);
				statement.setDouble(2, longitude);
				statement.setString(3, instant);
				statement.setString(4, title);
				statement.setString(4, category);
				statement.setString(6, id);
				int rows = statement.executeUpdate();
				log.debug(rows + " row affected");
			} catch (SQLException s) {
				log.error("SQL statement is not executed! " + s);
			}

			conn.close();
			log.debug("Disconnected from database");
		} catch (Exception e) {
			logException(e);
		}

	}
}
