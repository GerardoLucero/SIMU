package com.example.topitzin.simu.objetos;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class SpeedTest {
    String fecha;
    String ping;
    String subida;

    public SpeedTest(String fecha, String bajada, String subida, String ping) {
        this.fecha = fecha;
        this.bajada = bajada;
        this.subida = subida;
        this.ping = ping;
    }

    String bajada;

    public String getBajada() {
        return bajada;
    }

    public void setBajada(String bajada) {
        this.bajada = bajada;
    }

    public String getSubida() {
        return subida;
    }

    public void setSubida(String subida) {
        this.subida = subida;
    }

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fecha", fecha);
        result.put("ping", ping);
        result.put("subida", subida);
        result.put("bajada", bajada);
        return result;
    }
}
