package org.mps.ronqi2;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.dispositivo.DispositivoSilver;

public class RonQI2SilverTest {


    /*
     * Analiza con los caminos base qué pruebas se han de realizar para comprobar que al inicializar funciona como debe ser. 
     * El funcionamiento correcto es que si es posible conectar ambos sensores y configurarlos, 
     * el método inicializar de ronQI2 o sus subclases, 
     * debería devolver true. En cualquier otro caso false. Se deja programado un ejemplo.
    */

    /*
     * Un inicializar debe configurar ambos sensores, comprueba que cuando se inicializa de forma correcta (el conectar es true), 
     * se llama una sola vez al configurar de cada sensor.
    */

    @Test
    @DisplayName("Inicializar Dispositivo Correcto Devuelve True")
    void Inicializar_DispositivoCorrecto_DevuelveTrueYConfigura(){
        // Arrange
        DispositivoSilver ds = mock(DispositivoSilver.class);
        when(ds.conectarSensorPresion()).thenReturn(true);
        when(ds.configurarSensorPresion()).thenReturn(true);
        when(ds.conectarSensorSonido()).thenReturn(true);
        when(ds.configurarSensorSonido()).thenReturn(true);
        RonQI2Silver rq2s = new RonQI2Silver();
        rq2s.anyadirDispositivo(ds);
 
        // Act + Assert
        assertTrue(rq2s.inicializar());
        verify(ds, times(1)).configurarSensorPresion();
        verify(ds, times(1)).configurarSensorSonido();
    }
 
    @Test
    @DisplayName("Inicializar Dispositivo Incorrecto Devuelve False")
    void Inicializar_DispositivoIncorrecto_DevuelveFalse(){
        // Arrange
        DispositivoSilver ds = mock(DispositivoSilver.class);
        when(ds.conectarSensorPresion()).thenReturn(true);
        when(ds.configurarSensorPresion()).thenReturn(true);
        when(ds.conectarSensorSonido()).thenReturn(true);
        when(ds.configurarSensorSonido()).thenReturn(false);
        RonQI2Silver rq2s = new RonQI2Silver();
        rq2s.anyadirDispositivo(ds);
    
        // Act + Assert
        assertFalse(rq2s.inicializar());
    }

    /*
     * Un reconectar, comprueba si el dispositivo desconectado, en ese caso, conecta ambos y devuelve true si ambos han sido conectados. 
     * Genera las pruebas que estimes oportunas para comprobar su correcto funcionamiento. 
     * Centrate en probar si todo va bien, o si no, y si se llama a los métodos que deben ser llamados.
     */

    @Test
    @DisplayName("Reconectar Dispositivo Correctamente Devuelve True")
    void Reconectar_DispositivoCorrecto_DevuelveTrue(){
        // Arrange
        DispositivoSilver ds = mock(DispositivoSilver.class);
        when(ds.estaConectado()).thenReturn(false);
        when(ds.conectarSensorPresion()).thenReturn(true);
        when(ds.conectarSensorSonido()).thenReturn(true);
        RonQI2Silver rq2s = new RonQI2Silver();
        rq2s.anyadirDispositivo(ds);

        // Act + Assert
        assertTrue(rq2s.reconectar());
        verify(ds, times(1)).conectarSensorPresion();
        verify(ds, times(1)).conectarSensorSonido();
    }

    @Test
    @DisplayName("Reconectar Dispositivo Incorrectamente Devuelve False")
    void Reconectar_DispositivoIncorrecto_DevuelveFalse(){
        // Arrange
        DispositivoSilver ds = mock(DispositivoSilver.class);
        when(ds.estaConectado()).thenReturn(false);
        when(ds.conectarSensorPresion()).thenReturn(false);
        when(ds.conectarSensorSonido()).thenReturn(false);
        RonQI2Silver rq2s = new RonQI2Silver();
        rq2s.anyadirDispositivo(ds);

        // Act + Assert
        assertFalse(rq2s.reconectar());
        verify(ds, times(1)).conectarSensorPresion();
    }
    
    /*
     * El método evaluarApneaSuenyo, evalua las últimas 5 lecturas realizadas con obtenerNuevaLectura(), 
     * y si ambos sensores superan o son iguales a sus umbrales, que son thresholdP = 20.0f y thresholdS = 30.0f;, 
     * se considera que hay una apnea en proceso. Si hay menos de 5 lecturas también debería realizar la media.
     */
     
    @Test
    @DisplayName("Evaluar Apnea Suenyo Correctamente Devuelve True")
    void EvaluarApneaSuenyo_Correctamente_DevuelveTrue(){
        // Arrange
        DispositivoSilver ds = mock(DispositivoSilver.class);
        RonQI2Silver rq2s = new RonQI2Silver();
        rq2s.anyadirDispositivo(ds);
        when(ds.leerSensorPresion()).thenReturn(25.0f);
        when(ds.leerSensorSonido()).thenReturn(35.0f);

        // Act
        for (int i = 0; i < 5; i++) {
            rq2s.obtenerNuevaLectura();
        }
        
        // Assert
        assertTrue(rq2s.evaluarApneaSuenyo());
    }

     /* Realiza un primer test para ver que funciona bien independientemente del número de lecturas.
     * Usa el ParameterizedTest para realizar un número de lecturas previas a calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
     * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-parameterized-tests
    */
}
