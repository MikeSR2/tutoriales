package com.zonadecodigo.redis;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit test for simple App.
 */
class ComandosRedisTest {
    private static RedisServer redisMockServer;
    private static ComandosRedis comandosRedis;

    @BeforeAll
    public static void prepararRedis(){
        int puertoRedis=6379; //puerto donde se ejecutara el servidor embebido
        System.out.println("Iniciando servidor Redis de prueba en localhost y puerto: "+6379);
        redisMockServer =new RedisServer(puertoRedis); //creamos el servidor con el puerto indicado
        redisMockServer.start(); //se inicia el servidor

        comandosRedis=new ComandosRedis("localhost", puertoRedis); //Creamos la instancia de nuestra clase que vamos a probar, le mandamos eln host y puerto del servidor embebido

        initDatosPrueba(); //metodo que generara algunos datos de prueba

        
    }

    /**
     * generara algunos datos de prueba
     */
    private static void initDatosPrueba(){
        try(var conexionRedis=new Jedis()){
            conexionRedis.set("string", "esto es una cadena");
            conexionRedis.lpush("lista", "esto","es","una","lista");
            conexionRedis.sadd("set", "esto","es","un","set","con","elementos","unicos");
            var mapa=new HashMap<String,String>();
            mapa.put("uno", "elemento 1");
            mapa.put("dos", "elemento 2");
            conexionRedis.hmset("hash", mapa);
            System.out.println("Datos de prueba insertados!");
        }

    }

    @Test
    @DisplayName("Prueba que exista conexion con redis")
    void pruebaConexion(){
        var conectado=comandosRedis.isConnected();
        System.out.println("Hay conexion: "+conectado);
        assertTrue(conectado);
    }

    @Test
    @DisplayName("La cadena es igual")
    void pruebaCadena(){
        var cadena=comandosRedis.getString("string");
        System.out.println("El valor de la cadena es: "+ cadena);
        assertEquals("esto es una cadena",cadena);
    }

    @Test
    @DisplayName("Obtiene el ultimo elemento de la lista")
    void pruebaLista(){
        var ultimoElementoLista=comandosRedis.getLista("lista");
        System.out.println("El ultimo elemento de la lista es: "+ultimoElementoLista);
        assertEquals("lista", ultimoElementoLista);
    }

    @Test
    @DisplayName("Es un set sin elementos repetidos")
    void pruebaSet(){
        var testSet = new HashSet<>();
        testSet.addAll(Arrays.asList("esto","es","un","set","con","elementos","unicos"));
        var set=comandosRedis.getSet("set");

        System.out.println("Los valores en el set son: "+set);
        assertEquals(testSet, set);
    }

    @Test
    @DisplayName("Obtiene un hash con dos elementos")
    void pruebaHash(){
        var map=new HashMap<String,String>();
        map.put("uno", "elemento 1");
        map.put("dos", "elemento 2");
        var hash=comandosRedis.getHash("hash");
        System.out.println("Los valores del hash son: "+hash);
        assertEquals(map, hash);
    }

    @Test
    @DisplayName("Obtiene un elemento del hash")
    void pruebaElementoHash(){
        var elemento=comandosRedis.getElementoHash("hash", "uno");
        System.out.println("El valor del elemento buscado es: "+elemento);
        assertEquals("elemento 1", elemento);
    }

    
}
