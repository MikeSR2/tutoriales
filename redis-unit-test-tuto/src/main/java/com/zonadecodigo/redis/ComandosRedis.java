package com.zonadecodigo.redis;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisPool;

/**
 * Clase donde se ejecutan algunas operaciones basicas de consulta
 * a redis utilizando Jedis.
 */
public final class ComandosRedis {

    private JedisPool poolConexionesRedis;

    public ComandosRedis(String host,int puerto){
        poolConexionesRedis=new JedisPool(host,puerto);
    }

    /**
     * verifica si esta conectado a redis
     * @return
     */
    public boolean isConnected(){
         try(var conexion=poolConexionesRedis.getResource()){
            return conexion.isConnected();
         }
    }

    /**
     * consulta y devuelve dato tipo String
     * @param llave
     * @return
     */
    public String getString(String llave){
        try(var conexion=poolConexionesRedis.getResource()){
            return conexion.get(llave);
         }
    }

    /**
     * consulta y devuelve dato tipo Lista, se especifica cuantos elementos a obtener
     * @param llave
     * @param elementos
     * @return
     */
    public String getLista(String llave){
        try(var conexion=poolConexionesRedis.getResource()){
            return conexion.lpop(llave);
         }
    }

    /**
     * consulta y devuelve dato tipo Set
     * @param llave
     * @return
     */
    public Set<String> getSet(String llave){
        try(var conexion=poolConexionesRedis.getResource()){
            return conexion.smembers(llave);
         }
    }

    /**
     * consulta y devuelve dato tipo Hash, devuelve el hash  con todos sus elementos
     * @param llave
     * @return
     */
    public Map<String,String> getHash(String llave){
        try(var conexion=poolConexionesRedis.getResource()){
            return conexion.hgetAll(llave);
         }
    }

    /**
     * consulta y devuelve un elemento de un hash
     * @param llave
     * @return
     */
    public String getElementoHash(String llave,String elemento){
        try(var conexion=poolConexionesRedis.getResource()){
            return conexion.hget(llave, elemento);
         }
    }
}
