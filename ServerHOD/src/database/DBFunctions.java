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
			String sql_user = "CREATE TABLE IF NOT EXISTS public.\"user\"\r\n"
					+ "(\r\n"
					+ "    user_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),\r\n"
					+ "    username text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    password text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    CONSTRAINT user_pkey PRIMARY KEY (user_id)\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.\"user\"\r\n"
					+ "    OWNER to postgres;";
			statement_user = conn.createStatement();
			statement_user.executeUpdate(sql_user);
			
			String sql_receipts = "CREATE TABLE IF NOT EXISTS public.receipts\r\n"
					+ "(\r\n"
					+ "    receipt_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),\r\n"
					+ "    receipt_name text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    \"time\" bigint NOT NULL,\r\n"
					+ "    ingredients text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    CONSTRAINT receipts_pkey PRIMARY KEY (receipt_id)\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.receipts\r\n"
					+ "    OWNER to postgres;";
			statement_receipts = conn.createStatement();
			statement_receipts.executeUpdate(sql_receipts);
			
			String sql_ingredients = "CREATE TABLE IF NOT EXISTS public.ingredients\r\n"
					+ "(\r\n"
					+ "    ingredient_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),\r\n"
					+ "    receipt_id bigint NOT NULL,\r\n"
					+ "    ingredient text COLLATE pg_catalog.\"default\" NOT NULL,\r\n"
					+ "    CONSTRAINT ingredients_pkey PRIMARY KEY (ingredient_id),\r\n"
					+ "    CONSTRAINT receipt_id FOREIGN KEY (receipt_id)\r\n"
					+ "        REFERENCES public.receipts (receipt_id) MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ "        NOT VALID\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.ingredients\r\n"
					+ "    OWNER to postgres;";
			statement_ingredients = conn.createStatement();
			statement_ingredients.executeUpdate(sql_ingredients);
			
			String sql_saved_receipts = "CREATE TABLE IF NOT EXISTS public.saved_receipts\r\n"
					+ "(\r\n"
					+ "    user_id bigint NOT NULL,\r\n"
					+ "    receipt_id bigint NOT NULL,\r\n"
					+ "    CONSTRAINT receipt_id FOREIGN KEY (receipt_id)\r\n"
					+ "        REFERENCES public.receipts (receipt_id) MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ "        NOT VALID,\r\n"
					+ "    CONSTRAINT user_id FOREIGN KEY (user_id)\r\n"
					+ "        REFERENCES public.\"user\" (user_id) MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.saved_receipts\r\n"
					+ "    OWNER to postgres;";
			statement_saved_receipts = conn.createStatement();
			statement_saved_receipts.executeUpdate(sql_saved_receipts);
			
			String sql_permitted_ingredients = "CREATE TABLE IF NOT EXISTS public.permitted_ingredients\r\n"
					+ "(\r\n"
					+ "    user_id bigint NOT NULL,\r\n"
					+ "    ingredient_id bigint NOT NULL,\r\n"
					+ "    CONSTRAINT ingredient_id FOREIGN KEY (ingredient_id)\r\n"
					+ "        REFERENCES public.ingredients (ingredient_id) MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ "        NOT VALID,\r\n"
					+ "    CONSTRAINT user_id FOREIGN KEY (user_id)\r\n"
					+ "        REFERENCES public.\"user\" (user_id) MATCH SIMPLE\r\n"
					+ "        ON UPDATE NO ACTION\r\n"
					+ "        ON DELETE CASCADE\r\n"
					+ ")\r\n"
					+ "\r\n"
					+ "TABLESPACE pg_default;\r\n"
					+ "\r\n"
					+ "ALTER TABLE IF EXISTS public.permitted_ingredients\r\n"
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

	public void delete_receipt_from_saved(Connection conn, int rid, int uid) {
		Statement statement;
		try {
			String sql = String.format("DELETE FROM public.saved_receipts WHERE receipt_id='%s' AND user_id=%s", rid, uid);
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
			String sql = String.format("SELECT receipt_id FROM public.receipts WHERE receipt_name='%s'", name);
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
			String sql = String.format("SELECT ingredient_id FROM public.ingredients WHERE ingredient='%s'", name);
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
			String sql_into_permitted = String.format("INSERT INTO public.permitted_ingredients (user_id, ingredient_id) VALUES ('%s', '%s')", UID, IID);
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
			String sql = String.format("DELETE FROM public.permitted_ingredients WHERE ingredient_id='%s' AND user_id = '%s'", IID, UID);
			statement = conn.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void add_receipt_to_saved(Connection conn, int RID, int UID) {
		Statement statement;
		try {		
			String sql = String.format("INSERT INTO public.saved_receipts (user_id, receipt_id) VALUES ('%s', '%s')", UID, RID);
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
			String sql = String.format("SELECT receipt_id FROM public.saved_receipts WHERE user_id = %s", UID);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				receipts.add(rs.getInt(1));
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
			String sql = String.format("SELECT * FROM public.receipts WHERE receipt_id = %s", RID);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				str = str + rs.getString(1)+" "+rs.getString(2) + " " + rs.getString(3)+" "+rs.getString(4);
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
			String sql = String.format("SELECT ingredient, ingredient_id FROM public.ingredients");
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				ingredients.put(rs.getInt(2), rs.getString(1));
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
				str = str + rs.getString(1)+" "+rs.getString(2) + " " + rs.getString(3)+" "+rs.getString(4)+"\n";
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public String get_permitted_receipts(Connection conn, int UID) {
		String str = "";
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT * FROM public.receipts WHERE receipt_id IN (SELECT receipt_id FROM public.ingredients_with_receipts WHERE ingredient_id IN (SELECT ingredient_id FROM public.permitted_ingredients WHERE user_id = %s))", UID);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				str = str + rs.getString(1)+" "+rs.getString(2) + " " + rs.getString(3)+" "+rs.getString(4)+"\n";
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	public String get_permitted_ingrs(Connection conn, int UID) {
		String str = "";
		Statement statement;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT * FROM public.ingredients WHERE ingredient_id IN (SELECT ingredient_id FROM public.permitted_ingredients WHERE user_id = %s)", UID);
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				str = str + rs.getString(1) + " " + rs.getString(2)+"\n";
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
