package com.zonadecodigo.jedis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Hello world!
 */
public final class ConexionesJedis {

    /**
     * Crea una conexion al host y puerto defult y ejecuta
     * comandos basicos
     */
    private void conexionDefault() {
        // Crea la conexion
        try (var jedis = new Jedis()) {
            //Ejecutando comandos basicos para insertar informacion
            jedis.set("marca","Ford");
            jedis.lpush("Ford", "F150","Mustang","Explorer","F150");
            jedis.sadd("Chevy", "Camaro","Silverado","Camaro");
            var mapa=new HashMap<String,String>();
            mapa.put("marca", "Ford");
            mapa.put("modelo", "Mustang");
            jedis.hset("auto", mapa);

             //Ejecutando comandos basicos para extraer la informacion
            System.out.println("Extrayendo informacion");
            var marca=jedis.get("marca");
            var modelos=jedis.lpop("Ford", 4);
            var modelosNoRepetidos=jedis.smembers("Chevy");
            var auto=jedis.hgetAll("auto");

            System.out.println("String: "+marca);
            System.out.println("Lista: "+modelos);
            System.out.println("Set: "+modelosNoRepetidos);
            System.out.println("Hash: "+auto);
        }
    }

    /**
     * Crea un pool de conexiones con minima configuracion
     */
    private void poolDeConexiones() {
        //Configuracion del pool
        var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setBlockWhenExhausted(true);

        //Creacion del pool de conexiones 
        var jedisPool = new JedisPool(poolConfig, "localhost", 6379);

        //conexion nueva del pool
        try (var jedis = jedisPool.getResource()) {
            // operaciones con la conexion
            jedis.set("greet", "hi");
            System.out.println(jedis.get("greet"));
        }

    }

    /**
     * crea una conexion a un cluster de redis
     */
    private void conexionCluster() {
        //set con los nodos pertenecientes al cluster (host y puerto)
        Set<HostAndPort> nodosCluster = new HashSet<HostAndPort>();
        nodosCluster.add(new HostAndPort("127.0.0.1", 7000));
        nodosCluster.add(new HostAndPort("127.0.0.1", 7001));

        //Se crea la conexion
        try (var jedisCluster = new JedisCluster(nodosCluster)) {
            jedisCluster.set("Saludo","hola");
            System.out.println(jedisCluster.get("Saludo"));
        }

    }

    public static void main(String[] args) {
        var pruebas = new ConexionesJedis();
        //ejecutar los metodos creados para probar
        pruebas.conexionDefault();
    }

}
