package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBFunctions {
	
	public void create_tables(Connection conn) {
		Statement statement_user;
		Statement statement_receipts;
		Statement statement_ingredients;
		Statement statement_saved_receipts;
		Statement statement_permitted_ingredients;
		try {
			String sql_user = "CREATE TABLE IF NOT EXISTS public.\"User\"\r\n"
					+ "(\r\n"
					+ "    \"UID\" bigint NOT NULL,\r\n"
					+ "    username text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    password text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    CONSTRAINT user_pkey PRIMARY KEY (\"UID\")\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.\"User\"\r\n"
					+ "    OWNER to postgres;";
			statement_user = conn.createStatement();
			statement_user.executeUpdate(sql_user);
			
			String sql_receipts = "CREATE TABLE IF NOT EXISTS public.\"Receipts\"\r\n"
					+ "(\r\n"
					+ "    \"RID\" bigint NOT NULL,\r\n"
					+ "    receipt_name text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    \"time\" bigint NOT NULL,\r\n"
					+ "    steps text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    ingredients text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    category text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    CONSTRAINT receipts_pkey PRIMARY KEY (\"RID\")\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.\"Receipts\"\r\n"
					+ "    OWNER to postgres;";
			statement_receipts = conn.createStatement();
			statement_receipts.executeUpdate(sql_receipts);
			
			String sql_ingredients = "CREATE TABLE IF NOT EXISTS public.\"Ingredients\"\r\n"
					+ "(\r\n"
					+ "    \"IID\" bigint NOT NULL,\r\n"
					+ "    \"RID\" bigint NOT NULL,\r\n"
					+ "    ingredient text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    CONSTRAINT ingredients_pkey PRIMARY KEY (\"IID\"),\r\n"
					+ "    CONSTRAINT receipt_id FOREIGN KEY (\"RID\")\r\n"
					+ "        REFERENCES public.\"Receipts\" (\"RID\") MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ "        NOT VALID\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.\"Ingredients\"\r\n"
					+ "    OWNER to postgres;";
			statement_ingredients = conn.createStatement();
			statement_ingredients.executeUpdate(sql_ingredients);
			
			String sql_saved_receipts = "CREATE TABLE IF NOT EXISTS public.\"SavedReceipts\"\r\n"
					+ "(\r\n"
					+ "    \"UID\" bigint NOT NULL,\r\n"
					+ "    \"RID\" bigint NOT NULL,\r\n"
					+ "    CONSTRAINT receipt_id FOREIGN KEY (\"RID\")\r\n"
					+ "        REFERENCES public.\"Receipts\" (\"RID\") MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ "        NOT VALID,\r\n"
					+ "    CONSTRAINT user_id FOREIGN KEY (\"UID\")\r\n"
					+ "        REFERENCES public.\"User\" (\"UID\") MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.\"SavedReceipts\"\r\n"
					+ "    OWNER to postgres;";
			statement_saved_receipts = conn.createStatement();
			statement_saved_receipts.executeUpdate(sql_saved_receipts);
			
			String sql_permitted_ingredients = "CREATE TABLE IF NOT EXISTS public.\"PermittedIngredients\"\r\n"
					+ "(\r\n"
					+ "    \"UID\" bigint NOT NULL,\r\n"
					+ "    \"IID\" bigint NOT NULL,\r\n"
					+ "    CONSTRAINT ingredient_id FOREIGN KEY (\"IID\")\r\n"
					+ "        REFERENCES public.\"Ingredients\" (\"IID\") MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ "        NOT VALID,\r\n"
					+ "    CONSTRAINT user_id FOREIGN KEY (\"UID\")\r\n"
					+ "        REFERENCES public.\"User\" (\"UID\") MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.\"PermittedIngredients\"\r\n"
					+ "    OWNER to postgres;";
			statement_permitted_ingredients = conn.createStatement();
			statement_permitted_ingredients.executeUpdate(sql_permitted_ingredients);
			
		}catch (Exception e){				
		}
	}
	
	public Connection connection_to_db(String dbname, String user, String pass) {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname, user, pass);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void add_user(Connection conn, String name, String password) {
		Statement statement;
		try {
			String sql = "INSERT INTO public.user (username, password) VALUES ('"+name+"', '"+password+"')";
			statement = conn.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UsersRow search_by_username(Connection conn, String name) {
		Statement statement;
		try {
			String sql = String.format("SELECT * FROM public.user WHERE username IN ('%s')", name);
			ResultSet rs = null;
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				UsersRow usr = new UsersRow(Integer.parseInt(rs.getString(1)), name, rs.getString(3));
				return usr;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete_receipt_from_saved(Connection conn, int id, int uid) {
		Statement statement;
		try {
			String sql = String.format("DELETE FROM public.saved_receipts WHERE RID='%s' AND UID=%s", id, uid);
			statement = conn.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int search_RID_by_name(Connection conn, String name) {
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT RID FROM public.receipts WHERE receipt_name='%s'", name);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int search_IID_by_name(Connection conn, String name) {
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT IID FROM public.ingredients WHERE ingredient='%s'", name);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void add_permitted_ingredient(Connection conn, int IID, int UID) {
		Statement statement_permitted_ingredient;
		try {		
			String sql_into_permitted = String.format("INSERT INTO public.permitted_ingredients (UID, IID) VALUES ('%s', '%s')", UID, IID);
			statement_permitted_ingredient = conn.createStatement();
			statement_permitted_ingredient.executeUpdate(sql_into_permitted);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void delete_permitted_ingridient(Connection conn, int IID, int UID) {
		Statement statement;
		try {
			String sql = String.format("DELETE FROM public.permitted_ingredients WHERE IID='%s' AND UID = '%s'", IID, UID);
			statement = conn.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void add_receipt_to_saved(Connection conn, int RID, int UID) {
		Statement statement;
		try {		
			String sql = String.format("INSERT INTO public.saved_receipts (UID, RID) VALUES ('%s', '%s')", UID, RID);
			statement = conn.createStatement();
			statement.executeUpdate(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, String> show_all_receipts(Connection conn){
		HashMap<Integer, String> receipts = new HashMap<>();
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT * FROM public.receipts");
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				receipts.put(rs.getInt(1), rs.getString(2));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return receipts;
	}
	
	public ArrayList<Integer> show_saved(Connection conn, int UID){
		ArrayList<Integer> receipts = new ArrayList<>();
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT RID FROM public.saved_receipts WHERE UID = %s", UID);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				receipts.add(rs.getInt(0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return receipts;
	}
	
	public String get_receipt(Connection conn, int RID) {
		String str = "";
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT * FROM public.receipts WHERE RID = %s", RID);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				str = str + rs.getString(1)+" "+rs.getString(2) + " " + rs.getString(3)+" "+rs.getString(4) + " " + rs.getString(5)+" "+rs.getString(6);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return str;		
	}
	
	public HashMap<Integer, String> show_all_ingredients(Connection conn){
		HashMap<Integer, String> ingredients = new HashMap<>();
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT ingredient, IID FROM public.ingredients");
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				ingredients.put(rs.getInt(1), rs.getString(0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ingredients;
	}
	
	public String sort_by_time (Connection conn) {
		String str = "";
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT * FROM public.receipts ORDER BY time");
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				str = str + rs.getString(1)+" "+rs.getString(2) + " " + rs.getString(3)+" "+rs.getString(4) + " " + rs.getString(5)+" "+rs.getString(6)+"\n";
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
