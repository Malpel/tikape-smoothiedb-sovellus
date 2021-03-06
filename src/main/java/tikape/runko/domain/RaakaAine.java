package tikape.runko.domain;

public class RaakaAine implements Comparable<RaakaAine>{

    private Integer id;
    private String nimi;

    public RaakaAine(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    @Override
    public int compareTo(RaakaAine t) {
        
        return this.nimi.compareTo(t.getNimi());
    }
    

}
