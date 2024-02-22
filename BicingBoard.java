package IA.ProbIA5;

import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.*;
import java.util.HashMap;
import java.util.Map;

public class BicingBoard {
    public class Ruta {
        private Estacion estacionIni;
        private Estacion estacionFi1;
        private Estacion estacionFi2;
        private int nbicisRecogidas;
        private int nbicisDejadas1;
        private int nbicisDejadas2;
        private int costeRuta;
        private float distanciaRuta;

        public Ruta(Estacion e1, Estacion e2, Estacion e3, int n1, int n2, int n3, int c, float d) {
            this.estacionIni = e1;
            this.estacionFi1 = e2;
            this.estacionFi2 = e3;
            this.nbicisRecogidas = n1;
            this.nbicisDejadas1 = n2;
            this.nbicisDejadas2 = n3;
            costeRuta = c;
            distanciaRuta = d;
        }

        public Estacion getEstacionInicial() {
            return estacionIni;
        }
        public Estacion getEstacionFinal1() {
            return estacionFi1;
        }
        public Estacion getEstacionFinal2() {
            return estacionFi2;
        }
        public int getNBicis() {
            return nbicisRecogidas;
        }
        public int getBicisRecogidas() { 
            return nbicisRecogidas; 
        }
        public int getBicisDejadas1() {
            return nbicisDejadas1;
        }
        public int getBicisDejadas2() {
            return nbicisDejadas2;
        }
        public int getCosteRuta() {
            return costeRuta;
        }
        public float getDistanciaRuta(){
            return distanciaRuta;
        }
        public void setCosteRuta(int costeRuta) {
            this.costeRuta = costeRuta;
        }
        public void setDistanciaRuta(float distanciaRuta){
            this.distanciaRuta = distanciaRuta;
        }
        public void setEstacionIni(Estacion e1) {
            estacionIni = e1;
        }
        public void setEstacionFi1(Estacion e2) {
            estacionFi1 = e2;
        }
        public void setEstacionFi2(Estacion e3) { 
            estacionFi2 = e3; 
        }
        public void setNbicisDejadas2(int b) { 
            nbicisDejadas2 = b; 
        }
    }

    /*******************************/
    /***** ATRIBUTOS DE ESTADO *****/
    /*******************************/

    private static int nbicis;
    private static int nestaciones;
    private int nfurgos;
    private ArrayList<Ruta> Rutas;
    private Map<Estacion, Integer> estaciones;
    private ArrayList<Estacion> vectorEstaciones;
    private float coste;
    private float distancia;
    private int heuristica;

    /**************************/
    /******* CREADORAS ********/
    /**************************/

    public BicingBoard(Estaciones e, int nb, int nf, int estadoIni, int h) {
        estaciones = new HashMap<Estacion, Integer>();
        vectorEstaciones = e;
        nbicis = nb;
        nestaciones = e.size();
        nfurgos = nf;
        distancia = 0;
        for (int i = 0; i < e.size(); ++i) {
            estaciones.put(e.get(i), e.get(i).getNumBicicletasNext());
        }
        if (estadoIni == 0) {
            estadoInicial1();
        } else if (estadoIni == 1) {
            estadoInicial2(e);
        }
        coste = 0;
        heuristica = h;
    }

    public BicingBoard(Map<Estacion, Integer> e, ArrayList<Estacion> ve, int nb, int nf, ArrayList<Ruta> r, float c, float dist) {
        estaciones = new HashMap<>();
        vectorEstaciones = ve;
        for (Map.Entry<Estacion, Integer> entry : e.entrySet()) {
            Estacion key = entry.getKey();
            Integer value = entry.getValue();
            estaciones.put(key, value);
        }
        nbicis = nb;
        nestaciones = e.size();
        nfurgos = nf;

        Rutas = new ArrayList<>(r.size());
        for (int i = 0; i < r.size(); ++i) {
            Rutas.add(copiarRuta(r.get(i)));
        }
        distancia = dist;
        coste = c;
    }

    public BicingBoard(Map<Estacion, Integer> e, ArrayList<Estacion> ve, int nb, int nf, ArrayList<Ruta> r, float c, float dist, Ruta noAñadir) {
        estaciones = new HashMap<>();
        vectorEstaciones = ve;
        for (Map.Entry<Estacion, Integer> entry : e.entrySet()) {
            Estacion key = entry.getKey();
            Integer value = entry.getValue();
            estaciones.put(key, value);
        }

        nbicis = nb;
        nestaciones = e.size();
        nfurgos = nf;

        Rutas = new ArrayList<>(r.size());
        for (Ruta ruta : r) {
            if (!ruta.equals(noAñadir)) Rutas.add(copiarRuta(ruta));
        }
        distancia = dist;
        coste = c;
    }

    private Ruta copiarRuta(Ruta r) {
        return new Ruta(r.getEstacionInicial(), r.getEstacionFinal1(), r.getEstacionFinal2(),
                r.getBicisRecogidas(), r.getBicisDejadas1(), r.getBicisDejadas2(), r.getCosteRuta(), r.getCosteRuta());
    }

    /*********************/
    /****** GETTERS ******/
    /*********************/

    public int getNBicis() { return nbicis; }
    public int getNEstaciones() { return nestaciones; }
    public int getNFurgos() { return nfurgos; }
    public Map<Estacion, Integer> getEstaciones() { return estaciones; }
    public ArrayList<Estacion> getVectorEstaciones() { return vectorEstaciones; }
    public ArrayList<Ruta> getRutas(){ return Rutas; }
    public float getCoste(){ return coste; }
    public float getDistancia(){ return distancia; }
    public int getNRutas() { return Rutas.size(); }
    public Integer getBicisNext(Estacion e) { return estaciones.get(e); }
    public int getHeuristica() { return heuristica; }


    /********************************/
    /****** ESTADOS INICIALES *******/
    /********************************/

    public boolean estadoInicial1() {
        Rutas = new ArrayList<Ruta>();
        return true;
    }

    public boolean estadoInicial2(Estaciones e) {
        Rutas = new ArrayList<>();
        for (int i = 0; i < nfurgos && i < nestaciones; i += 2) {
            Estacion e1 = e.get(i);
            Estacion e2 = e.get(i+1);
            if (bicisSobrantes(e1) != 0) {
                int bR = bicisSobrantes(e1);
                System.out.println("Furgo Inicial añadida Inicial. "
                        +  "Coste " + getCoste() + ". Heuristica: " + getCoste()*getDistancia()*0.001);
                añadirFurgoneta(e1, e2, e2, bicisSobrantes(e1), bicisSobrantes(e1), 0);
            }
        }
        for (Ruta ruta : Rutas) distancia += ruta.getDistanciaRuta();
        return true;
    }

    /*************************/
    /****** OPERADORES *******/
    /*************************/

    public void añadirFurgoneta(Estacion e1, Estacion e2, Estacion e3, int bR, int bD1, int bD2) {
        Ruta rutaNueva = new Ruta(e1, e2, e3, bR, bD1, bD2, 0, 0);
        modificarCoste(rutaNueva);
        estaciones.put(e1, getBicisNext(e1) - bR);
        estaciones.put(e2, getBicisNext(e2) + bD1);
        estaciones.put(e3, getBicisNext(e3) + bD2);
        Rutas.add(rutaNueva);
    }

    public void modificarFurgoneta(Estacion e1, Estacion e2, Estacion e3, Integer bR, Integer bD1, Integer bD2, int c, float d) {
        Ruta rutaNueva = new Ruta(e1, e2, e3, bR, bD1, bD2, c, d);
        modificarCoste(rutaNueva);
        estaciones.put(e1, getBicisNext(e1) - bR);
        estaciones.put(e2, getBicisNext(e2) + bD1);
        estaciones.put(e3, getBicisNext(e3) + bD2);
        Rutas.add(rutaNueva);
    }

    /**************************/
    /****** MODIFICADORAS *****/
    /**************************/

    //Modificar coste amb l'operador canviar estacion inicial no te sentit
    public void modificarCoste(Ruta ruta) {
        coste -= ruta.getCosteRuta();
        distancia -= ruta.getDistanciaRuta();
        int d = distanciaEstaciones(ruta.getEstacionInicial(), ruta.getEstacionFinal1());
        if (ruta.getBicisDejadas2() != 0) {
            d += distanciaEstaciones(ruta.getEstacionFinal2(), ruta.getEstacionFinal1());
        }
        ruta.setDistanciaRuta(d);
        distancia += d;
        int c = 0;
        int bicicletasTransportadas = ruta.getBicisRecogidas() + ruta.getBicisDejadas1() + ruta.getBicisDejadas2();
        //if (getHeuristica() == 1) {
        //    c += (((bicicletasTransportadas + 9) / 10) * d * 0.001);
        //}
        //Nos beneficia dejar una bici en una estacion, mientras no se supere la demanda de bicis necesaria
        c -= min(ruta.getBicisDejadas1(), bicisNecesarias(ruta.getEstacionFinal1()));
        // si bicis dejadas 2 es 0 consideramos que no vamos a esa estacion
        if (ruta.getBicisDejadas2() != 0 && ruta.getEstacionFinal1() != ruta.getEstacionFinal2()) c -= min(ruta.getBicisDejadas2(), bicisNecesarias(ruta.getEstacionFinal2()));
        //Incrementa el coste por cada bici que recojamos por debajo de la demanda
        c += max(0, ruta.getBicisRecogidas() - bicisSobrantes(ruta.getEstacionInicial()));
        coste += c;
        ruta.setCosteRuta(c);
    }

    /*************************/
    /****** AUXILIARES *******/
    /*************************/

    public boolean rutaIniciaEnEstacion(Estacion estacion) {
        for (Ruta ruta : Rutas) {
            if (ruta.getEstacionInicial().equals(estacion)) {
                return true; // La ruta ya inicia en esta estación
            }
        }
        return false; // La estación no se usa como punto de inicio de ninguna ruta
    }

    //Bicis que faltan en una estacion
    public int bicisNecesarias(Estacion e) {
        return max(0, e.getDemanda() - getBicisNext(e));
    }

    //Bicis que sobran en una estacion
    public int bicisSobrantes(Estacion e) {
        return max(0, getBicisNext(e) - e.getDemanda());
    }

    public int distanciaRecorrida(final Ruta ruta) {
        int dis = distanciaEstaciones(ruta.getEstacionInicial(), ruta.getEstacionFinal1());
        if (ruta.getBicisRecogidas() - ruta.getBicisDejadas1() > 0) dis += distanciaEstaciones(ruta.getEstacionFinal1(), ruta.getEstacionFinal2());
        return dis;
    }

    //Devuelve la distancia entre las dos estaciones
    public int distanciaEstaciones(Estacion e1, Estacion e2) {
        return abs(e1.getCoordX() - e2.getCoordX()) + abs(e1.getCoordY() - e2.getCoordY());

    }
}
