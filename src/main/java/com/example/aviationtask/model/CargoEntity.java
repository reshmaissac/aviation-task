@Data
public class CargoEntity {

    private int flightId;
    private List<Baggage> baggage;
    private List<Cargo> cargo;
}


@Data
public class Baggage {
    private int id;
    private int weight;
    private String weightUnit;
    private int pieces;
}

@Data
public class CargoItem {
    private int id;
    private int weight;
    private String weightUnit;
    private int pieces;
}