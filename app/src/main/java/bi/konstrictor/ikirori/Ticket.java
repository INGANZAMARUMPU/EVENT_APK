package bi.konstrictor.ikirori;

public class Ticket {
    public String id, name, autres;
    Double somme, consommable;

    public Ticket(String id, String name, String autres, double somme, double consommable) {
        this.id = id;
        this.name = name;
        this.autres = autres;
        this.somme = somme;
        this.consommable = consommable;
    }
}
