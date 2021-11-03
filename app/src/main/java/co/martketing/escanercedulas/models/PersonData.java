package co.martketing.escanercedulas.models;

public class PersonData {
    private int id;
    private String doc_id,names,lnames,birth_date,date,rh,genre,tempe,ciudad_procedencia;

    @Override
    public String toString() {
        return "PersonData{" +
                "id=" + id +
                ", doc_id='" + doc_id + '\'' +
                ", names='" + names + '\'' +
                ", lnames='" + lnames + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", date='" + date + '\'' +
                ", rh='" + rh + '\'' +
                ", genre='" + genre + '\'' +
                ", tempe='" + tempe + '\'' +
                ", ciudad_procedencia='" + ciudad_procedencia + '\'' +
                '}';
    }

    public String getCiudad_procedencia() {
        return ciudad_procedencia;
    }

    public void setCiudad_procedencia(String ciudad_procedencia) {
        this.ciudad_procedencia = ciudad_procedencia;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public void setLnames(String lnames) {
        this.lnames = lnames;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setTempe(String tempe) {
        this.tempe = tempe;
    }

    public int getId() {
        return id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public String getNames() {
        return names;
    }

    public String getLnames() {
        return lnames;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getDate() {
        return date;
    }

    public String getRh() {
        return rh;
    }

    public String getGenre() {
        return genre;
    }

    public String getTempe() {
        return tempe;
    }
}
