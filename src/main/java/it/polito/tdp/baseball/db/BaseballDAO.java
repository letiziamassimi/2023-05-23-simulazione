package it.polito.tdp.baseball.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.baseball.model.Coppia;
import it.polito.tdp.baseball.model.People;
import it.polito.tdp.baseball.model.Team;


/*
	-- due vertici sono collegati se nel corso dell'anno abbiano venduto un numero di prodotti in comune >= m
	   il peso è il numero di prodotti con lo stesso idvenduto dai due rivenditori --    NON ORIENTATO E PESATO
	   
	   SELECT d1.Retailer_code, d2.Retailer_code, COUNT(distinct d1.Product_number) AS numeroProdotti
FROM go_daily_sales d1, go_daily_sales d2, go_retailers r1, go_retailers r2
WHERE d1.Product_number = d2.Product_number AND YEAR(d1.Date) = ? AND YEAR(d1.Date) = ? AND r1.Retailer_code < r2.Retailer_code
AND r1.Retailer_code = d1.Retailer_code AND r2.Retailer_code = d2.Retailer_code AND r1.Country = "?" AND r2.Country = "?"
GROUP BY d1.Retailer_code, d2.Retailer_code
HAVING numeroProdotti >= 3



-- due giocatori sono connessi da un arco se hanno militato nella stessa squadra per l'anno indicato -- NON ORIENTATO E NON PESATO

	SELECT a1.playerID, a2.playerID
FROM appearances a1, appearances a2, people p1, people p2, salaries s1, salaries s2
WHERE a1.teamID = a2.teamID AND a1.year = "?" AND a2.year = "?" AND a1.playerID < a2.playerID 
AND a1.playerID = p1.playerID AND a2.playerID = p2.playerID AND p1.playerID = s1.playerID AND p2.playerID = s2.playerID
AND s1.salary > ? AND s2.salary > ? AND s1.year = "?" AND s2.year = "?"


-- una coppia di brani è collegata da un arco se il numero distinto di playlist in cui è presente ciascuno dei 
  brani è uguale -- NON ORIENTATO E NON PESATO
  
	  SELECT COUNT(DISTINCT PlaylistId) AS numeroPlaylist
FROM playlisttrack pt
WHERE TrackId = 1

+ codice (controllo sui due vertici -> no loop)

for (Track t : listaVertici) {
		for (Track tr : listaVertici) {
			if (itunesDAO.numeroDiPlaylistInCuiIlBranoEPresente(t) == itunesDAO.numeroDiPlaylistInCuiIlBranoEPresente(tr) 
					&& !t.getTrackId().equals(tr.getTrackId())) {
				Graphs.addEdgeWithVertices(grafo, t, tr);
			}
		}
	}
	
	
-- due album sono collegati se almeno una canzone di a1 e una di a2 sono all'interno di una stessa playlist -- NON O E NON P

	SELECT distinct t1.AlbumId , t2.AlbumId
FROM playlisttrack p1, playlisttrack p2, track t1, track t2
WHERE t1.TrackId = p1.TrackId AND t2.TrackId = p2.TrackId 
AND p1.PlaylistId = p2.PlaylistId AND t1.AlbumId < t2.AlbumId 

+ codice (il controllo sui vertici era troppo difficile da fare su sql -> controllo a codice che ivertici vadano bene)

List<Coppia> coppieCheVannoBene = new LinkedList<>();
	for (Coppia c : itunesDAO.getCoppieDiAlbumCheHannoUnaTracciaNellaStessaPlaylist1(albumIdMap)) {
		if (albumCheVannoBene.contains(c.getA1()) && albumCheVannoBene.contains(c.getA2()) && !c.getA1().equals(c.getA2())) {
			coppieCheVannoBene.add(c);
		}
	}
	for (Coppia co : coppieCheVannoBene) {
		Graphs.addEdgeWithVertices(grafo, co.getA1(), co.getA2());
	}
	

-- due album sono collegati se la differenza tra i loro prezzi e diversa da 0 -- NON O E P

		SELECT SUM(t.UnitPrice) AS costoAlbum
	FROM track t
	WHERE t.AlbumId = ?
	
	+ codice
	
	for (Album a : albumCheVannoBene) {
		for (Album b : albumCheVannoBene) {
			if (!a.equals(b)) {
				double differenza = itunesDAO.prezzoDiUnAlbum(a)-itunesDAO.prezzoDiUnAlbum(b);
				if (differenza != 0) {
					Graphs.addEdgeWithVertices(grafo, a, b, Math.abs(differenza));
				}
				}
		}
	}
	
	
	-- due album sono collegati se hanno una durata differente e se la somma delle loro durate è > di 4*n -- O E P
	
	SELECT SUM(t.Milliseconds) AS durata
	FROM track t
	WHERE t.AlbumId = ?
	
	+ codice 
	
	for (Album a : albumConDurataMinima) {
		for (Album b : albumConDurataMinima) {
			if(!a.equals(b)) {
				int differenza = itunesDAO.durataAlbum(a) - itunesDAO.durataAlbum(b);
				int somma = itunesDAO.durataAlbum(a) + itunesDAO.durataAlbum(b);
				if(differenza != 0 && somma > 4*numeroInserito) {
					if (itunesDAO.durataAlbum(a)) < itunesDAO.durataAlbum(b)) {
						Graphs.addEdgeWithVertices(grafo, a, b, somma);
					}
					else 
						Graphs.addEdgeWithVertices(grafo, b, a, somma);
				}
			}
		}
	}
	
	-- due album sono collegati se la differenza tra le canzoni è diversa da zero -- O E P
 	
 	SELECT COUNT(t.TrackId) AS numeroCanzoni
	FROM track t
	WHERE t.AlbumId = ?
	
	+ codice
	
	for (Album b : albumConNumeroMinimoDiCanzoni) {
		for (Album c : albumConNumeroMinimoDiCanzoni) {
			if(!b.equals(c)) {
				int differenza = itunesDAO.numeroCanzoni(b) - itunesDAO.numeroCanzoni(c);
				if (differenza != 0) {
					if (itunesDAO.numeroCanzoni(b) < itunesDAO.numeroCanzoni(c)) {  
						Graphs.addEdge(grafo, b, c, Math.abs(differenza));
					}
					else
						Graphs.addEdge(grafo, c, b, Math.abs(differenza));
				}
			}
		}
	}
	
	
	-- due locali sono collegati da un arco se la differenza delle medie delle recensioni è diverso da 0 --
	
	SELECT AVG(stars) AS mediaRecensioni
	FROM reviews
	WHERE business_id = "go7rnFqNB--ZYarRX9vNHw" AND YEAR(review_date) = "2014"


	-- due film sono collegati se hanno un rank >= r e almeno un attore ha recitato in entrambi i film --
	
	SELECT m1.id, m2.id, COUNT(*) AS numeroAttori
FROM movies m1, movies m2, roles r1, roles r2
WHERE m1.rank IS NOT NULL AND m2.rank IS NOT NULL AND m1.rank >= 5 AND m2.rank >= 5
AND m1.id = r1.movie_id AND m2.id = r2.movie_id AND r1.actor_id = r2.actor_id AND m1.id < m2.id
GROUP BY m1.id, m2.id
HAVING numeroAttori >= 1	

	
-- due match sono collegati se esiste almeno un giocatore che abbia giocato almeno tot minuti in entrambi i match -- P E NON O
	
	SELECT a1.MatchID, a2.MatchID, COUNT(*) AS numeroGiocatori
FROM actions a1, actions a2, matches m1, matches m2
WHERE m1.MatchID = a1.MatchID AND m2.MatchID = a2.MatchID 
AND a1.PlayerID = a2.PlayerID AND a1.MatchID < a2.MatchID
AND MONTH(m1.Date) = "05" AND MONTH(m2.Date) = "05" 
AND a1.TimePlayed >= 10 AND a2.TimePlayed >= 10
GROUP BY a1.MatchID, a2.MatchID
HAVING numeroGiocatori >= 1
	
	
	
	-- due registi sono collegati se hanno diretto almeno una volta lo stesso attore nell'anno -- P E NON O
	
	SELECT mv1.director_id, mv2.director_id,COUNT(*) AS numeroVolte
FROM roles r1, roles r2, movies_directors mv1, movies_directors mv2, movies m1, movies m2
WHERE r1.movie_id = mv1.movie_id AND r2.movie_id = mv2.movie_id AND m1.id = mv1.movie_id AND m2.id = mv2.movie_id
AND mv1.director_id < mv2.director_id AND r1.actor_id = r2.actor_id AND m1.year = "2005" AND m2.year = "2005"
GROUP BY mv1.director_id, mv2.director_id
HAVING numero >= 1
*/


public class BaseballDAO {
	
	public List<Coppia> listaCoppie(int anno, double salarioSelezionato){
		
		String sql = "SELECT p1.playerID, p2.playerID "
				+ "FROM people p1, people p2, appearances a1, appearances a2, salaries s1 , salaries s2 "
				+ "WHERE p1.playerID = a1.playerID AND a1.teamID = a2.teamID AND a2.playerID = p2.playerID  "
				+ "AND p1.playerID < p2.playerID AND a1.year = a2.year AND a1.year = ? AND s1.playerID = p1.playerID "
				+ "AND s2.playerID = p2.playerID AND s2.salary > ? AND s1.salary > ? AND s1.year = ? AND s2.year = ? ";
		
		List<Coppia> result = new ArrayList<Coppia>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setDouble(2, salarioSelezionato);
			st.setDouble(3, salarioSelezionato);
			st.setInt(4, anno);
			st.setInt(5, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Coppia(rs.getString("p1.playerID"), rs.getString("p2.playerID")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	public List<People> listaGiocatoriConSalarioMinimoInQuellAnno(int anno, double salarioMinimo){
		
		String sql = "SELECT p.*"
				+ "FROM people p, salaries s "
				+ "WHERE p.playerID = s.playerID AND s.year = ? AND s.salary > ? ";
		
		List<People> result = new ArrayList<People>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setDouble(2, salarioMinimo);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new People(rs.getString("playerID"), 
						rs.getString("birthCountry"), 
						rs.getString("birthCity"), 
						rs.getString("deathCountry"), 
						rs.getString("deathCity"),
						rs.getString("nameFirst"), 
						rs.getString("nameLast"), 
						rs.getInt("weight"), 
						rs.getInt("height"), 
						rs.getString("bats"), 
						rs.getString("throws"),
						getBirthDate(rs), 
						getDebutDate(rs), 
						getFinalGameDate(rs), 
						getDeathDate(rs)) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//p.playerID, p.birthCountry, p.birthCity, p.deathCountry, p.deathCity, p.nameFirst, "
	//+ "p.nameLast, p.weight, p.height, p.bats, p.throws 
	
	public List<Integer> listaAnni(){
		
		String sql = "SELECT DISTINCT s.year "
				+ "FROM appearances s";
		
		List<Integer> result = new LinkedList<Integer>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getInt("s.year") );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	public List<People> readAllPlayers(){
		String sql = "SELECT * "
				+ "FROM people";
		List<People> result = new ArrayList<People>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new People(rs.getString("playerID"), 
						rs.getString("birthCountry"), 
						rs.getString("birthCity"), 
						rs.getString("deathCountry"), 
						rs.getString("deathCity"),
						rs.getString("nameFirst"), 
						rs.getString("nameLast"), 
						rs.getInt("weight"), 
						rs.getInt("height"), 
						rs.getString("bats"), 
						rs.getString("throws"),
						getBirthDate(rs), 
						getDebutDate(rs), 
						getFinalGameDate(rs), 
						getDeathDate(rs)) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	public List<Team> readAllTeams(){
		String sql = "SELECT * "
				+ "FROM  teams";
		List<Team> result = new ArrayList<Team>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Team( rs.getInt("iD"),
						rs.getInt("year"), 
						rs.getString("teamCode"), 
						rs.getString("divID"), 
						rs.getInt("div_ID"), 
						rs.getInt("teamRank"),
						rs.getInt("games"), 
						rs.getInt("gamesHome"), 
						rs.getInt("wins"), 
						rs.getInt("losses"), 
						rs.getString("divisionWinnner"), 
						rs.getString("leagueWinner"),
						rs.getString("worldSeriesWinnner"), 
						rs.getInt("runs"), 
						rs.getInt("hits"), 
						rs.getInt("homeruns"), 
						rs.getInt("stolenBases"),
						rs.getInt("hitsAllowed"), 
						rs.getInt("homerunsAllowed"), 
						rs.getString("name"), 
						rs.getString("park")  ) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	
	//=================================================================
	//==================== HELPER FUNCTIONS   =========================
	//=================================================================
	
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getBirthDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("birth_date") != null) {
				return rs.getTimestamp("birth_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getDebutDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("debut_date") != null) {
				return rs.getTimestamp("debut_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getFinalGameDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("finalgame_date") != null) {
				return rs.getTimestamp("finalgame_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getDeathDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("death_date") != null) {
				return rs.getTimestamp("death_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
