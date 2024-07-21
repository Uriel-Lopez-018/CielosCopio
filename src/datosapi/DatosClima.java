package datosapi;

public class DatosClima {
    private Main main;
    private Rain rain;
    private String dt;

    public Main getMain() { return main; }
    public Rain getRain() { return rain; }
    public String getFecha() {
        long timestamp = Long.parseLong(dt);
        java.util.Date time = new java.util.Date((long) timestamp * 1000);
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(time);
    }
    public String getHorario() {
        long timestamp = Long.parseLong(dt);
        java.util.Date time = new java.util.Date((long) timestamp * 1000);
        return new java.text.SimpleDateFormat("HH:mm").format(time);
    }

    public class Main {
        private double temp;
        private double temp_max;

        public double getTemp() { return temp; }
        public double getTemp_max() { return temp_max; }
    }

    public class Rain {
        private double _1h;

        public double get1h() { return _1h; }
    }
}
