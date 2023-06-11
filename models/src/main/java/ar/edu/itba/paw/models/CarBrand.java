package ar.edu.itba.paw.models;

public enum CarBrand {
    VOLKSWAGEN("Volkswagen"),
    FORD("Ford"),
    CHEVROLET("Chevrolet"),
    RENAULT("Renault"),
    FIAT("Fiat"),
    PEUGEOT("Peugeot"),
    TOYOTA("Toyota"),
    HONDA("Honda"),
    CITROEN("Citroen"),
    HYUNDAI("Hyundai"),
    MERCEDES_BENZ("Mercedes Benz"),
    BMW("BMW"),
    AUDI("Audi"),
    KIA("Kia"),
    MAZDA("Mazda"),
    NISSAN("Nissan"),
    SUZUKI("Suzuki"),
    JEEP("Jeep"),
    LAND_ROVER("Land Rover"),
    VOLVO("Volvo");

    private final String formattedName;

    private CarBrand(String formattedName) {
        this.formattedName = formattedName;
    }

    @Override
    public String toString() {
        return formattedName;
    }
}
