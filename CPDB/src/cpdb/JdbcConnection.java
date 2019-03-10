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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
	
	
	private static void curateLiterature(Connection conn) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader("./data/literature_cpdb.txt"));
				preprocessingData(conn,reader,"literature-based");
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private static void curateNCINTP(Connection conn) {
		
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/ncintp_cpdb.txt"));
			preprocessingData(conn,reader,"ncintp");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * monkey data insertion.
	 * 
	 * */
	private static void curateMonkey(Connection conn) {
		String line = null;
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/monkey_cpdb.txt"));
			while((line = reader.readLine()) != null)	{
//				System.out.println(line);
				 
				String metagencity = line.split("\t")[2];	//. +, - 3types
				Experiments experiment = filterCPDBChemicalTD50value(line.split("\t")[3]);
				String source = "CPDBChemicals(experiment-based)";
				
				Chemicals chem = new Chemicals( line.split("\t")[0], line.split("\t")[1]);
				//TODO : insert chemicals and get key(ID);
//				int chemicalID = (int)(Math.random()*100) + 10000;
				
				//insert chemical
				int chemicalID = insertChemStatement(conn,chem);
				System.out.println(chemicalID + "\t" + chem);
				
				
				Models model = new Models("male",metagencity);
				
				{
					experiment.setSource(source);

					//male
					model.setSpecies("Rhesus");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +1;

					//insert model
					int modelID = insertModelsStatement(conn,model);
					System.out.println(modelID + "\t" + model + "\t" + line.split("\t")[5]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment
					int experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[5]);
					insertEffectStatement(conn,effect);
				}
				
				
				experiment = filterCPDBChemicalTD50value(line.split("\t")[4]);
				{
					experiment.setSource(source);
					
					//male
					model.setSpecies("Cynomolgus");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +501;
					
					//insert model
					int modelID = insertModelsStatement(conn,model);
					System.out.println(modelID + "\t" + model + "\t" + line.split("\t")[6]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					int experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[6]);
					insertEffectStatement(conn,effect);
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
	private static void curateHamster(Connection conn) {
		String line = null;
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/hamster_cpdb.txt"));
			while((line = reader.readLine()) != null)	{
//				System.out.println(line);
				 
				String metagencity = line.split("\t")[2];	//. +, - 3types
				Experiments experiment = filterCPDBChemicalTD50value(line.split("\t")[3]);
				String source = "CPDBChemicals(experiment-based)";
				
				Chemicals chem = new Chemicals( line.split("\t")[0], line.split("\t")[1]);
				//TODO : insert chemicals and get key(ID);
//				int chemicalID = (int)(Math.random()*100) + 10000;
				
				//insert chemical
				int chemicalID = insertChemStatement(conn,chem);
				System.out.println(chemicalID + "\t" + chem);
				
				
				Models model_male = new Models("male",metagencity);
				Models model_female = new Models("female",metagencity);
				
				{
					experiment.setSource(source);

					//male
					model_male.setSpecies("hamster");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +1;

					//insert model
					int modelID = insertModelsStatement(conn,model_male);
					System.out.println(modelID + "\t" + model_male + "\t" + line.split("\t")[4]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment
					int experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[4]);
					insertEffectStatement(conn,effect);
					
					//female
					model_female.setSpecies("hamster");
					//TODO: insert model and get key(ID).
//					modelID = (int)(Math.random()*100) +100;
					
					//insert model
					modelID = insertModelsStatement(conn,model_female);
					System.out.println(modelID + "\t" + model_female + "\t" + line.split("\t")[5]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");

					//insert experiment					
					experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					effect = new Effect(experiment_id, line.split("\t")[5]);
					insertEffectStatement(conn,effect);
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
	private static void curateRatandMouse(Connection conn) {
		String line = null;
		try {
			BufferedReader	reader = new BufferedReader(new FileReader("./data/mouse_rat_cpdb.txt"));
			while((line = reader.readLine()) != null)	{
//				System.out.println(line);
				 
				String metagencity = line.split("\t")[2];	//. +, - 3types
				Experiments experiment = filterCPDBChemicalTD50value(line.split("\t")[3]);
				String source = "CPDBChemicals(experiment-based)";
				
				Chemicals chem = new Chemicals( line.split("\t")[0], line.split("\t")[1]);
				//TODO : insert chemicals and get key(ID);
//				int chemicalID = (int)(Math.random()*100) + 10000;
				
				//insert chemical
				int chemicalID = insertChemStatement(conn,chem);
				System.out.println(chemicalID + "\t" + chem);
				
				
				Models model_male = new Models("male",metagencity);
				Models model_female = new Models("female",metagencity);
				
				{
					experiment.setSource(source);

					//male
					model_male.setSpecies("rat");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +1;

					//insert model
					int modelID = insertModelsStatement(conn,model_male);
					System.out.println(modelID + "\t" + model_male + "\t" + line.split("\t")[5]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment
					int experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[5]);
					insertEffectStatement(conn,effect);
					
					//female
					model_female.setSpecies("rat");
					//TODO: insert model and get key(ID).
//					modelID = (int)(Math.random()*100) +100;
					
					//insert model
					modelID = insertModelsStatement(conn,model_female);
					System.out.println(modelID + "\t" + model_female + "\t" + line.split("\t")[6]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					effect = new Effect(experiment_id, line.split("\t")[6]);
					insertEffectStatement(conn,effect);
				}
				
				
				experiment = filterCPDBChemicalTD50value(line.split("\t")[4]);
				{
					experiment.setSource(source);
					
					//male
					model_male.setSpecies("mouse");
					//TODO: insert model and get key(ID).
//					int modelID = (int)(Math.random()*100) +501;
					
					//insert model
					int modelID = insertModelsStatement(conn,model_male);
					System.out.println(modelID + "\t" + model_male + "\t" + line.split("\t")[7]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					int experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					Effect effect = new Effect(experiment_id, line.split("\t")[7]);
					insertEffectStatement(conn,effect);
					
					//female
					model_female.setSpecies("mouse");
					//TODO: insert model and get key(ID).
//					modelID = (int)(Math.random()*100) +800;
					
					//insert model
					modelID = insertModelsStatement(conn,model_female);
					System.out.println(modelID + "\t" + model_female + "\t" + line.split("\t")[8]);
					
					experiment.setModelID(modelID);
					experiment.setChemicalID(chemicalID);
					System.out.println(experiment + "\n");
					
					//insert experiment					
					experiment_id = insertExperimentsStatementSimpleFormat(conn,experiment,chemicalID,modelID);
					effect = new Effect(experiment_id, line.split("\t")[8]);
					insertEffectStatement(conn,effect);
				}
				System.out.println("==========================================================================");				
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	
	

	/***********************************************serveral methods**************************************************/

	private static Experiments filterCPDBChemicalTD50value(String td50) {
		Experiments exp = new Experiments();
		String[] items = td50.trim().split(",");
		if(items.length == 1)	{
			if(items[0].contains("-") || items[0].contains("\\."))	{
				exp.setTd50(items[0]);
			}
			else if(items[0].contains("m"))	{	//m , n , i , I, P
				exp.setTd50(items[0].substring(0,items[0].length()-1));
				exp.setAdditional_information(items[0].charAt(items[0].length()-1) + "");
			}
			else if(items[0].contains("n"))	{	//m , n , i , I, P
				exp.setTd50(items[0].substring(0,items[0].length()-1));
				exp.setAdditional_information(items[0].charAt(items[0].length()-1) + "");
			}
			else if(items[0].contains("i"))	{	//m , n , i , I, P
				exp.setTd50(items[0].substring(0,items[0].length()-1));
				exp.setAdditional_information(items[0].charAt(items[0].length()-1) + "");
			}
			else if(items[0].contains("P"))	{	//m , n , i , I, P
				exp.setTd50(items[0].substring(0,items[0].length()-1));
				exp.setAdditional_information(items[0].charAt(items[0].length()-1) + "");
			}
			else if(items[0].contains("I"))	{	//m , n , i , I, P
				exp.setTd50(".");
				exp.setAdditional_information(items[0]);
			}
			else	{
				exp.setTd50(items[0]);
			}
		}
		else	{
			int idxComma = td50.indexOf(",");
			String td = td50.substring(0,idxComma-1);
			String add_info = td50.substring(idxComma-1,td50.length());
			
			exp.setTd50(td);
			exp.setAdditional_information(add_info);
		}
		
		return exp;
	}
	
	private static void preprocessingData(Connection conn, BufferedReader reader, String source) {
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
//				String tissue = line.split("\t")[6];
//				String tumor = line.split("\t")[7];
				Map<String,String> mTumorTissue = filterTissueAndTumor(line.split("\t")[6],line.split("\t")[7]);
				
				int exposuretime = Integer.parseInt(line.split("\t")[8]);
				int totalexptime = Integer.parseInt(line.split("\t")[9]);
				String td50 = line.split("\t")[10];
				double lc = Double.parseDouble(line.split("\t")[11]);
				double uc = Double.parseDouble(line.split("\t")[12]);
				String cpdb_id = line.split("\t")[13];

				//insert chemical
				int chemicalID = insertChemStatement(conn,chem);
				System.out.println(chemicalID + "\t" + chem);

				Models model = new Models(species,gender,".",strain);
				Experiments experiment = new Experiments();

				{
					//insert model
					int modelID = insertModelsStatement(conn,model);
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
					int experiment_id = insertExperimentsStatementExtended(conn,experiment,chemicalID,modelID);
					
					for(String tumor : mTumorTissue.keySet())	{
						Effect effect = new Effect(experiment_id, mTumorTissue.get(tumor), tumor);
						insertEffectStatement(conn,effect);
					}
//					Effect effect = new Effect(experiment_id, tissue, tumor);
//					insertEffectStatement(conn,effect);
				}

				System.out.println("==========================================================================");

				previousChemcode = line.split("\t")[0];
			}

		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	private static Map<String,String> filterTissueAndTumor(String tissue, String tumor) {
//		System.out.println("[tissue: ]" + tissue + "\t[tumor: ]" + tumor);
		Map<String,String> map = new HashMap<>();
		if(tissue.length() == tumor.length())	{
			
			for(int i = 0; i < tissue.length(); i = i+3)	{
				String subTissue = tissue.substring(i,i+3);
				String subTumor = tumor.substring(i,i+3);

				if(!map.containsKey(subTissue))	{
					map.put(subTumor,subTissue);
				}
			}
		}
		
//		System.out.println(map);
		return map;
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

	/****************************************************create & delete tables*********************************************/
	/**
	 * [autoincrease] 
	 * chemical : 1 
	 * model : 100000
	 * experiment : 200000
	 * effect : 300000
	 * 
	 * */
	private static void createTables(Connection conn) {
//		createChemicalTable(conn);
		String sql = "CREATE TABLE `chemicals` ("
				  + "`id` int(11) NOT NULL AUTO_INCREMENT, "
				  + "`chemicals_name` varchar(150) NOT NULL, "
				  + "`CAS` varchar(45) NOT NULL, "
				  + "PRIMARY KEY (`id`) "
				  + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
		createTable(conn,sql);
		
//		createModelTable(conn);
		sql = "CREATE TABLE `models` ("
				+ "`ID` int(11) NOT NULL AUTO_INCREMENT, "
				+ "`species` varchar(45) NOT NULL, "
				+ "`gender` varchar(45) DEFAULT NULL, "
				+ "`mutagencity` varchar(45) DEFAULT NULL, "
				+ " `strain` varchar(10) DEFAULT NULL, "
				+ "PRIMARY KEY (`ID`) , "
				+ "KEY `strain_idx` (`strain`), "
				+ "CONSTRAINT `strain` FOREIGN KEY (`strain`) REFERENCES `cpdb_strain` (`strain_abb`) ON DELETE CASCADE ON UPDATE CASCADE "
				+ ") ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;";
		createTable(conn,sql);
		
//		createExperimentsTable(conn);
		
		sql = "CREATE TABLE `experiment` ( "
				+ "`id` int(11) NOT NULL AUTO_INCREMENT, "
				  + "`chemicalID` int(11) NOT NULL, "
				  + "`modelID` int(11) NOT NULL, "
				  + "`TD50` varchar(50) NOT NULL, "
				  + "`additional_information` varchar(45) DEFAULT NULL, "
				  + "`route` varchar(10) DEFAULT NULL, "
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
				  + "KEY `route` (`route`), "
				  + "CONSTRAINT `chemicalID` FOREIGN KEY (`chemicalID`) REFERENCES `chemicals` (`id`) ON DELETE CASCADE ON UPDATE CASCADE, "
				  + "CONSTRAINT `route` FOREIGN KEY (`route`) REFERENCES `cpdb_route` (`route_abb`) ON DELETE CASCADE ON UPDATE CASCADE, "
				  + "CONSTRAINT `modelID` FOREIGN KEY (`modelID`) REFERENCES `models` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE "
				+ ") ENGINE=InnoDB AUTO_INCREMENT=200000 DEFAULT CHARSET=utf8;";
		createTable(conn,sql);
		
//		createEffectsTable(conn,sql);
		sql = "CREATE TABLE `effect` ( "
				  + "`id` int(11) NOT NULL AUTO_INCREMENT, "
				  + "`experimentID` int(11) NOT NULL, "
				  + "`tissue` varchar(150) NOT NULL, "
				  + "`tumor` varchar(150) NOT NULL, "
				  + "PRIMARY KEY (`id`), "
				  + "KEY `experimentID_idx` (`experimentID`), "
				  + "CONSTRAINT `experimentID` FOREIGN KEY (`experimentID`) REFERENCES `experiment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE, "
				  + "CONSTRAINT `tissue` FOREIGN KEY (`tissue`) REFERENCES `cpdb_tissue` (`tissue_abb`) ON DELETE CASCADE ON UPDATE CASCADE, "
				  + "CONSTRAINT `tumor` FOREIGN KEY (`tumor`) REFERENCES `cpdb_tumor` (`tumor_abb`) ON DELETE CASCADE ON UPDATE CASCADE "
				+ ") ENGINE=InnoDB AUTO_INCREMENT=300000 DEFAULT CHARSET=utf8; ";
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

	private static void suppleTableInsert(Connection conn) {
		String sql = "CREATE TABLE `cpdb_route` ( "
				+"`route_abb` varchar(10) NOT NULL, "
				  + "`route_name` varchar(150) NOT NULL, "
				  + "PRIMARY KEY (`route_abb`), "
				  + "UNIQUE KEY `route_abb_UNIQUE` (`route_abb`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
				
		createTable(conn,sql);
		
		sql = "CREATE TABLE `cpdb_strain` ( "
				  + "`strain_abb` varchar(10) NOT NULL, "
				  + "`strain_name` varchar(150) NOT NULL, "
				  + "PRIMARY KEY (`strain_abb`), "
				  + "UNIQUE KEY `strain_abb_UNIQUE` (`strain_abb`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		createTable(conn,sql);
		
		sql = "CREATE TABLE `cpdb_tissue` ( "
				  + "`tissue_abb` varchar(10) NOT NULL, "
				  + "`tissue_name` varchar(150) NOT NULL, "
				  + "PRIMARY KEY (`tissue_abb`), "
				  + "UNIQUE KEY `tissue_abb_UNIQUE` (`tissue_abb`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

		createTable(conn,sql);
		
		sql = "CREATE TABLE `cpdb_tumor` ( "
				  + "`tumor_abb` varchar(10) NOT NULL, "
				  + "`tumor_name` varchar(150) NOT NULL, "
				  + "PRIMARY KEY (`tumor_abb`), "
				  + "UNIQUE KEY `tumor_abb_UNIQUE` (`tumor_abb`) "
				 + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

		createTable(conn,sql);
	}
	
	/****************************************************main*********************************************/
	public static void main(String[] args) {
		Connection connection = initiateDBconnection();
		
//		suppleTableInsert(connection);
		//drop tables & create tables
		dropTables(connection);
		createTables(connection);

		long st = System.currentTimeMillis();
		//summary version of data.
		curateRatandMouse(connection);
		curateHamster(connection);
		curateMonkey(connection);
		
		//extended version of data.
		curateNCINTP(connection);
		curateLiterature(connection);
		
		System.out.println((System.currentTimeMillis() - st)/1000.);
		closeConnection();
	}

}
