package v2.telas;

/**
 * Interface de navegação entre telas.
 * Implementada por SalusSystem para desacoplar as telas do JFrame principal.
 */
public interface Navegador {
    void irPara(String tela);
}
