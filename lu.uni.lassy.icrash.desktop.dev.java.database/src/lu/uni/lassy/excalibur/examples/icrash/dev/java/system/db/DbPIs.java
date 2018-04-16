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

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.CtPI;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtGPSLocation;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtLatitude;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtLongitude;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtPIID;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtPITitle;
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

	// TO DO! Getting the PIs and their associated crises/humans and inserts them into a hashtable

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
