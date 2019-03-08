package cpdb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.UUID;

import VO.Chemicals;
import VO.Effect;
import VO.Experiments;
import VO.Models;

public class JdbcConnection {
	private static Connection connection = null;

	/**
	 * initiate mysql jdbc connection. 
	 * */
	private static Connection initiateDBconnection()	{
		if(connection != null)	{
			return connection;
		}
		else	{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cpdb?serverTimezone=UTC" , "root", "123qwe");
			} catch (SQLException se1) {
				se1.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		return connection;   
	}

	/**
	 * close connection of database
	 * */
	public static void closeConnection()	{
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	/**********************************************SQL query***************************************************/
	
	
	/**
	 * example of select statement method. 
	 * */
	public static void selectStatement(Connection connection)	{
		Statement st = null;
		try {
			st = connection.createStatement();
			String sql;

			//select statement
			sql = "select * FROM chemicals;";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String sqlRecipeProcess = rs.getString("chemicals_name");
				System.out.println(sqlRecipeProcess);
			}

			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * insert chemical information.
	 * 
	 * */
	public static int insertChemStatement(Connection conn, Chemicals chem)	{
		// the mysql insert statement
		chem.setCAS(checkCAS(conn,chem));	//change  '---' cas to uuid.
		String sql = "INSERT INTO chemicals (chemicals_name, CAS) values(\"" + chem.getChemicals_name() + "\", \"" + chem.getCAS() + "\")";
		System.out.println(sql);
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		int returnLastInsertId = 0;
		try {
			returnLastInsertId = stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		int auto_id = 0;
		if(returnLastInsertId != 0) {
			try {
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				auto_id = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return auto_id;
	}

	/**
	 * insert model information.
	 * 
	 * */
	public static int insertModelsStatement(Connection conn, Models model)	{
		// the mysql insert statement
		String sql = "INSERT INTO models (species, gender,mutagencity,strain) values(\"" + model.getSpecies() + "\", \"" + model.getGender() + "\", \"" + model.getMutagencity() + "\", \"" + model.getStrain()+ "\")";
		System.out.println(sql);
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		int returnLastInsertId = 0;
		try {
			returnLastInsertId = stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		int auto_id = 0;
		if(returnLastInsertId != 0) {
			try {
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				auto_id = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return auto_id;
	}
	
	/**
	 * file which has cpdb experiment-based have gotten simple information of experiments.  
	 * 
	 * */
	public static int insertExperimentsStatementSimpleFormat(Connection conn, Experiments experiment,int chemicalID, int modelID)	{
		// the mysql insert statement
		String sql = "INSERT INTO experiment (chemicalID, modelID,TD50,additional_information,source) values(" + chemicalID + "," + modelID +", \"" + 
										experiment.getTd50() + "\", \"" + experiment.getAdditional_information() + "\", \""  + experiment.getSource() + "\")";
		System.out.println(sql);

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLIntegrityConstraintViolationException e2) {
			System.out.println("have already exsited.");
		}	catch (SQLException e2) {
			e2.printStackTrace();
		}

		int returnLastInsertId = 0;
		try {
			returnLastInsertId = stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		int auto_id = 0;
		if(returnLastInsertId != 0) {
			try {
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				auto_id = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return auto_id;
	}
	
	/**
	 * NCINCP OR LITERATURE BASED chemical-carcinogenicity research files..
	 * that has extended information of experiments.
	 * 
	 * */
	public static int insertExperimentsStatementExtended(Connection conn, Experiments experiment,int chemID, int modelID)	{
		// the mysql insert statement
		String sql = "INSERT INTO experiment (chemicalID, modelID,TD50,route,totalexptime,exposuretime,lc,uc,source,reference,cpdb_idnum) values(" + chemID + "," + modelID +", \"" 
										+ experiment.getTd50() + "\", \"" + experiment.getRoute()  + "\", " + experiment.getTotalexptime() + ", " 
										+ experiment.getExposuretime()	+ ", " + experiment.getLc() + " , " + experiment.getUc() + ", \"" 
										+ experiment.getSource() + "\", \"" + experiment.getReference() + "\", \"" + experiment.getCpdb_idnum() + "\")";
		System.out.println(sql);

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLIntegrityConstraintViolationException e2) {
			System.out.println("have already exsited.");
		}	catch (SQLException e2) {
			e2.printStackTrace();
		}

		int returnLastInsertId = 0;
		try {
			returnLastInsertId = stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		int auto_id = 0;
		if(returnLastInsertId != 0) {
			try {
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				auto_id = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return auto_id;
	}
	
	/**
	 * insert effect data
	 * */
	private static void insertEffectStatement(Connection conn, Effect effect) {
		// the mysql insert statement
		String sql = "INSERT INTO effect (experimentID, tissue,tumor) values(" + effect.getExperimentID() + ", \"" + effect.getTissue()  + "\", \"" + effect.getTumor() + "\")";
		System.out.println(sql);
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e2) {
			System.out.println("have already exsited.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	/**********************************************preprocessing different sources***************************************************/
	
	
	private static void curateLiterature(Connection connection) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader("./data/literature_cpdb.txt"));
				preprocessingData(reader,"literature-based");
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private static void curateNCINTP(Connection connection) {
		
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/ncintp_cpdb.txt"));
			preprocessingData(reader,"ncintp");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * monkey data insertion.
	 * 
	 * */
	private static void curateMonkey(Connection connection2) {
		String line = null;
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/monkey_cpdb.txt"));
			while((line = reader.readLine()) != null)	{
//				System.out.println(line);
				 
				String metagencity = line.split("\t")[2];	//. +, - 3types
				String Rhesus_td50 = line.split("\t")[3];
				String source = "CPDBChemicals(experiment-based)";
				
				Chemicals chem = new Chemicals( line.split("\t")[0], line.split("\t")[1]);
				//TODO : insert chemicals and get key(ID);
//				int chemicalID = (int)(Math.random()*100) + 10000;
				
				//insert chemical
				int chemicalID = insertChemStatement(connection,chem);
				System.out.println(chemicalID + "\t" + chem);
				
				
				Models model = new Models("male",metagencity);
				Experiments experiment = new Experiments();
				
				if(!Rhesus_td50.equalsIgnoreCase(".") || !Rhesus_td50.equalsIgnoreCase("-"))	//has value.
				{
					experiment.setSource(source);
					experiment.setTd50(Rhesus_td50);

					//male
					model.setSpecies("Rhesus");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +1;

					//insert model
					int modelID = insertModelsStatement(connection,model);
					System.out.println(modelID + "\t" + model + "\t" + line.split("\t")[5]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment
					int experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[5]);
					insertEffectStatement(connection,effect);
				}
				
				
				String cynomolgus_td50 = line.split("\t")[4];
				if(!cynomolgus_td50.equalsIgnoreCase(".") || !cynomolgus_td50.equalsIgnoreCase("-"))	
				{
					experiment.setSource(source);
					experiment.setTd50(cynomolgus_td50);
					
					//male
					model.setSpecies("Cynomolgus");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +501;
					
					//insert model
					int modelID = insertModelsStatement(connection,model);
					System.out.println(modelID + "\t" + model + "\t" + line.split("\t")[6]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					int experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[6]);
					insertEffectStatement(connection,effect);
				}
				System.out.println("==========================================================================");				
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * hamster data insertion.
	 * 
	 * */
	private static void curateHamster(Connection connection) {
		String line = null;
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/hamster_cpdb.txt"));
			while((line = reader.readLine()) != null)	{
//				System.out.println(line);
				 
				String metagencity = line.split("\t")[2];	//. +, - 3types
				String td50 = line.split("\t")[3];
				String source = "CPDBChemicals(experiment-based)";
				
				Chemicals chem = new Chemicals( line.split("\t")[0], line.split("\t")[1]);
				//TODO : insert chemicals and get key(ID);
//				int chemicalID = (int)(Math.random()*100) + 10000;
				
				//insert chemical
				int chemicalID = insertChemStatement(connection,chem);
				System.out.println(chemicalID + "\t" + chem);
				
				
				Models model_male = new Models("male",metagencity);
				Models model_female = new Models("female",metagencity);
				Experiments experiment = new Experiments();
				
				if(!td50.equalsIgnoreCase(".") || !td50.equalsIgnoreCase("-"))	//has value.
				{
					experiment.setSource(source);
					experiment.setTd50(td50);

					//male
					model_male.setSpecies("hamster");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +1;

					//insert model
					int modelID = insertModelsStatement(connection,model_male);
					System.out.println(modelID + "\t" + model_male + "\t" + line.split("\t")[4]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment
					int experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[4]);
					insertEffectStatement(connection,effect);
					
					//female
					model_female.setSpecies("hamster");
					//TODO: insert model and get key(ID).
//					modelID = (int)(Math.random()*100) +100;
					
					//insert model
					modelID = insertModelsStatement(connection,model_female);
					System.out.println(modelID + "\t" + model_female + "\t" + line.split("\t")[5]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");

					//insert experiment					
					experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					effect = new Effect(experiment_id, line.split("\t")[5]);
					insertEffectStatement(connection,effect);
				}
			}
			
			reader.close();
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * rat & mouse data insertion.
	 * 
	 * */
	private static void curateRatandMouse(Connection connection2) {
		String line = null;
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/mouse_rat_cpdb.txt"));
			while((line = reader.readLine()) != null)	{
//				System.out.println(line);
				 
				String metagencity = line.split("\t")[2];	//. +, - 3types
				String rat_td50 = line.split("\t")[3];
				String source = "CPDBChemicals(experiment-based)";
				
				Chemicals chem = new Chemicals( line.split("\t")[0], line.split("\t")[1]);
				//TODO : insert chemicals and get key(ID);
//				int chemicalID = (int)(Math.random()*100) + 10000;
				
				//insert chemical
				int chemicalID = insertChemStatement(connection,chem);
				System.out.println(chemicalID + "\t" + chem);
				
				
				Models model_male = new Models("male",metagencity);
				Models model_female = new Models("female",metagencity);
				Experiments experiment = new Experiments();
				
				if(!rat_td50.equalsIgnoreCase(".") || !rat_td50.equalsIgnoreCase("-"))	//has value.
				{
					experiment.setSource(source);
					experiment.setTd50(rat_td50);

					//male
					model_male.setSpecies("rat");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +1;

					//insert model
					int modelID = insertModelsStatement(connection,model_male);
					System.out.println(modelID + "\t" + model_male + "\t" + line.split("\t")[5]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment
					int experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[5]);
					insertEffectStatement(connection,effect);
					
					//female
					model_female.setSpecies("rat");
					//TODO: insert model and get key(ID).
//					modelID = (int)(Math.random()*100) +100;
					
					//insert model
					modelID = insertModelsStatement(connection,model_female);
					System.out.println(modelID + "\t" + model_female + "\t" + line.split("\t")[6]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					effect = new Effect(experiment_id, line.split("\t")[6]);
					insertEffectStatement(connection,effect);
				}
				
				
				String mouse_td50 = line.split("\t")[4];
				if(!mouse_td50.equalsIgnoreCase(".") || !mouse_td50.equalsIgnoreCase("-"))	
				{
					experiment.setSource(source);
					experiment.setTd50(mouse_td50);
					
					//male
					model_male.setSpecies("mouse");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +501;
					
					//insert model
					int modelID = insertModelsStatement(connection,model_male);
					System.out.println(modelID + "\t" + model_male + "\t" + line.split("\t")[7]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					int experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[7]);
					insertEffectStatement(connection,effect);
					
					//female
					model_female.setSpecies("mouse");
					//TODO: insert model and get key(ID).
//					modelID = (int)(Math.random()*100) +800;
					
					//insert model
					modelID = insertModelsStatement(connection,model_female);
					System.out.println(modelID + "\t" + model_female + "\t" + line.split("\t")[8]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					experiment_id = insertExperimentsStatementSimpleFormat(connection,experiment,chemicalID,modelID);
					effect = new Effect(experiment_id, line.split("\t")[8]);
					insertEffectStatement(connection,effect);
				}
				System.out.println("==========================================================================");				
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	
	/***********************************************serveral methods**************************************************/

	private static void preprocessingData(BufferedReader reader, String source) {
		String line = null;
		String previousChemcode = "";
		try {

			while((line = reader.readLine()) != null)	{
				Chemicals chem = null;

				if(!previousChemcode.equalsIgnoreCase(line.split("\t")[0]) || chem == null)	{		//removing redundant iteration.
					chem = transformChemcodeToCAS(line.split("\t")[0]);	
				}

				String reference = "";
				if(source.equalsIgnoreCase("literature-based"))	{
					reference = transformReferenceInformation(line.split("\t")[1]);
				}
				else	{
					reference = line.split("\t")[1];
				}
				
				String species = transformSpecies(line.split("\t")[2]);	//m,r
				String strain = line.split("\t")[3];
				String gender = transformGender(line.split("\t")[4]);	//m,f : ncictp , m,f,b : literature.
				String route = line.split("\t")[5];
				String tissue = line.split("\t")[6];
				String tumor = line.split("\t")[7];
				int exposuretime = Integer.parseInt(line.split("\t")[8]);
				int totalexptime = Integer.parseInt(line.split("\t")[9]);
				String td50 = line.split("\t")[10];
				double lc = Double.parseDouble(line.split("\t")[11]);
				double uc = Double.parseDouble(line.split("\t")[12]);
				String cpdb_id = line.split("\t")[13];

				//insert chemical
				int chemicalID = insertChemStatement(connection,chem);
				System.out.println(chemicalID + "\t" + chem);

				Models model = new Models(species,gender,".",strain);
				Experiments experiment = new Experiments();

				if(!td50.equalsIgnoreCase(".") || !td50.equalsIgnoreCase("-"))	//has value.
				{
					//insert model
					int modelID = insertModelsStatement(connection,model);
					System.out.println(modelID + "\t" + model);

					experiment.setReference(reference);
					experiment.setLc(lc);
					experiment.setUc(uc);
					experiment.setTd50(td50);
					experiment.setTotalexptime(totalexptime);
					experiment.setExposuretime(exposuretime);
					experiment.setRoute(route);
					experiment.setSource(source);
					experiment.setTd50(td50);
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					experiment.setCpdb_idnum(cpdb_id);
					System.out.println(experiment + "\n");

					//insert experiment
					int experiment_id = insertExperimentsStatementExtended(connection,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, tissue, tumor);
					insertEffectStatement(connection,effect);
				}

				System.out.println("==========================================================================");

				previousChemcode = line.split("\t")[0];
			}

		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	/**
	 * literature based file has abbreviation of reference.
	 * it should transform and show researcher to be comfortable.
	 * */
	private static String transformReferenceInformation(String referenceAbb) {
		String line = null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader("./data/literature_cit_cpdb.txt"));
			
			while((line = reader.readLine()) != null)	{
				if(line.split("\t")[0].equalsIgnoreCase(referenceAbb))	{
					reader.close();
					return line.split("\t")[1].replaceAll("\"", "");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null)	{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return referenceAbb;
	}

	private static String transformGender(String gender) {
		if(gender.equalsIgnoreCase("m"))	{
			return "male";
		}
		else if(gender.equalsIgnoreCase("f"))	{
			return "female";
		}
		else if(gender.equalsIgnoreCase("b"))	{
			return "both";
		}
		else	{
			return "-";
		}
	}

	private static String transformSpecies(String species) {
		if(species.equalsIgnoreCase("m"))	{
			return "mouse";
		}
		else if(species.equalsIgnoreCase("r"))	{
			return "rat";
		}
		else if(species.equalsIgnoreCase("H"))	{
			return "hamsters";
		}
		else if(species.equalsIgnoreCase("D"))	{
			return "dogs";
		}
		else if(species.equalsIgnoreCase("p"))	{
			return "monkeys";
		}
		else if(species.equalsIgnoreCase("n"))	{
			return "prosimians";
		}
		else	{	
			return "not mouse or rat";
		}
	}

	private static Chemicals transformChemcodeToCAS(String chemcode) {
		Chemicals chem = null;
		String line = null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader("./data/chemname_cpdb.txt"));
			
			while((line = reader.readLine()) != null)	{
				if(line.split("\t")[0].equalsIgnoreCase(chemcode))	{
					chem = new  Chemicals(line.split("\t")[1],line.split("\t")[2]);
					reader.close();
					return chem;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null)	{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return chem;
	}

	private static String checkCAS(Connection connction,Chemicals chem)	{
		String CAS = selectChemCAS(connction,chem.getChemicals_name());
		
		if(chem.getCAS().equalsIgnoreCase("---"))	{
			CAS = UUID.randomUUID().toString();	// '---' => auto generation
			return CAS;
		}
		else if(chem.getCAS().equalsIgnoreCase("mixture"))	{
			CAS = UUID.randomUUID().toString();	// '---' => auto generation
			return CAS;
		}
		else	{
			return chem.getCAS();
		}
	}
	
	
	private static String selectChemCAS(Connection connection,String chemName) {
		Statement st = null;
		try {
			st = connection.createStatement();
			String sql;

			//select statement
			sql = "select * FROM chemicals where chemicals_name like \"" + chemName + "\";";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String sqlRecipeProcess = rs.getString("CAS");
				return sqlRecipeProcess;
			}

			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/****************************************************main*********************************************/
	

	
	private static void createTables(Connection conn) {
//		createChemicalTable(conn);
		String sql = "CREATE TABLE `chemicals` ("
				  + "`id` int(11) NOT NULL AUTO_INCREMENT, "
				  + "`chemicals_name` varchar(150) NOT NULL, "
				  + "`CAS` varchar(45) NOT NULL, "
				  + "PRIMARY KEY (`id`) "
				  + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		createTable(conn,sql);
		
//		createModelTable(conn);
		sql = "CREATE TABLE `models` ("
				+ "`ID` int(11) NOT NULL AUTO_INCREMENT, "
				+ "`species` varchar(45) NOT NULL, "
				+ "`gender` varchar(45) DEFAULT NULL, "
				+ "`mutagencity` varchar(45) DEFAULT NULL, "
				+ " `strain` varchar(45) DEFAULT NULL, "
				+ "PRIMARY KEY (`ID`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		createTable(conn,sql);
		
//		createExperimentsTable(conn);
		sql = "CREATE TABLE `experiment` ( "
				+ "`id` int(11) NOT NULL AUTO_INCREMENT, "
				  + "`chemicalID` int(11) NOT NULL, "
				  + "`modelID` int(11) NOT NULL, "
				  + "`TD50` varchar(50) NOT NULL, "
				  + "`additional_information` varchar(45) DEFAULT NULL, "
				  + "`route` varchar(45) DEFAULT NULL, "
				  + "`totalexptime` int(11) DEFAULT NULL, "
				  + "`exposuretime` int(11) DEFAULT NULL, "
				  + "`lc` double DEFAULT NULL, "
				  + "`uc` double DEFAULT NULL, "
				  + "`source` varchar(45) NOT NULL, "
				  + "`reference` varchar(150) DEFAULT NULL, "
				  + "`cpdb_idnum` varchar(45) DEFAULT NULL, "
				  + "PRIMARY KEY (`id`), "
				  + "UNIQUE KEY `id_UNIQUE` (`id`), "
				  + "KEY `chemicalID_idx` (`chemicalID`), "
				  + "KEY `modelID_idx` (`modelID`), "
				  + "CONSTRAINT `chemicalID` FOREIGN KEY (`chemicalID`) REFERENCES `chemicals` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
				  + "CONSTRAINT `modelID` FOREIGN KEY (`modelID`) REFERENCES `models` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		createTable(conn,sql);
		
//		createEffectsTable(conn,sql);
		sql = "CREATE TABLE `effect` ( "
				  + "`id` int(11) NOT NULL AUTO_INCREMENT, "
				  + "`experimentID` int(11) NOT NULL, "
				  + "`tissue` varchar(150) NOT NULL, "
				  + "`tumor` varchar(150) NOT NULL, "
				  + "PRIMARY KEY (`id`), "
				  + "KEY `experimentID_idx` (`experimentID`), "
				  + "CONSTRAINT `experimentID` FOREIGN KEY (`experimentID`) REFERENCES `experiment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8; ";
		createTable(conn,sql);
	}
	
	
	private static void createTable(Connection conn, String sql) {
		Statement st = null;
		try {
			st = connection.createStatement();

			//create statement
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private static void dropTables(Connection conn) {
		String sql = "DROP TABLE `cpdb`.`effect`;";		
		dropTable(conn,sql);
		
		sql = "DROP TABLE `cpdb`.`experiment`;";
		dropTable(conn,sql);
		
		sql = "DROP TABLE `cpdb`.`models`;";
		dropTable(conn,sql);
		
		sql = "DROP TABLE `cpdb`.`chemicals`;";
		dropTable(conn,sql);
	}
	private static void dropTable(Connection conn,String sql) {
		Statement st = null;
		try {
			st = connection.createStatement();

			//select statement
			System.out.println(sql);
			
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	/****************************************************main*********************************************/
	public static void main(String[] args) {
		Connection connection = initiateDBconnection();
		
		//drop tables & create tables
		dropTables(connection);
		createTables(connection);
		

		//summary version of data.
		curateRatandMouse(connection);
		curateHamster(connection);
		curateMonkey(connection);
		
		//extended version of data.
		curateNCINTP(connection);
		curateLiterature(connection);
		
		closeConnection();
	}


}
