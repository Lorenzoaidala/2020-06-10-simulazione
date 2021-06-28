package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {

	public void listAllActors(Map<Integer,Actor> idMap){
		String sql = "SELECT * FROM actors";

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
							res.getString("gender"));

					idMap.put(res.getInt("id"), actor);
				}
			}
			conn.close();


		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));

				result.add(movie);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));

				result.add(director);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getGeneri(){
		String sql="SELECT DISTINCT genre "
				+ "FROM movies_genres "
				+ "ORDER BY genre";
		Connection conn = DBConnect.getConnection();
		List<String> result = new LinkedList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("genre"));
			}
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;


	}
	public List<Actor> getVertici(Map<Integer,Actor> idMap,String genere){
		String sql = "SELECT distinct actor_id "
				+ "FROM roles r, movies_genres m "
				+ "WHERE r.movie_id=m.movie_id "
				+ "AND genre =?";
		Connection conn = DBConnect.getConnection();
		List<Actor> result = new LinkedList<>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				if(idMap.containsKey(res.getInt("actor_id"))) {
					result.add(idMap.get(res.getInt("actor_id")));
				}
			}
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public List<Adiacenza> getArchi(Map<Integer,Actor>idMap,String genere){
		String sql ="SELECT r1.actor_id AS id1, r2.actor_id AS id2, COUNT(DISTINCT (r1.movie_id)) AS peso "
				+ "FROM movies_genres m, roles r1, roles r2 "
				+ "WHERE r1.movie_id=m.movie_id AND r2.movie_id=m.movie_id "
				+ "AND m.genre = ? "
				+ "AND r1.actor_id<r2.actor_id "
				+ "GROUP BY r1.actor_id,r2.actor_id";
		
		List<Adiacenza> result = new LinkedList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2"))) {
					result.add(new Adiacenza(idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")),res.getDouble("peso")));	
				}
			}
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

}
