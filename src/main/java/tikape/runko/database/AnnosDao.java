package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tikape.runko.domain.Annos;

public class AnnosDao implements Dao<Annos, Integer> {
    
    private Database database;
    
    public AnnosDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Annos findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        
        Annos o = new Annos(id, nimi);
        
        rs.close();
        stmt.close();
        connection.close();
        
        return o;
    }
    
    @Override
    public List<Annos> findAll() throws SQLException {
        
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");
        
        ResultSet rs = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            
            annokset.add(new Annos(id, nimi));
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        Collections.sort(annokset);
        
        return annokset;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Annos WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        
    }
    
    @Override
    public Annos saveOrUpdate(Annos object) throws SQLException {
        // tallenetaan vain jos saman nimisiä ei ole tallennettu jo

        Annos byName = findByName(object.getNimi());
        
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }
        
        return findByName(object.getNimi());
        
    }
    
    private Annos findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE nimi = ?");
            stmt.setString(1, name);
            
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            
            return new Annos(result.getInt(1), result.getString(2));
        }
    }
    
}
