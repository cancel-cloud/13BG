package de.lukas.datastructures;

/**
 * Generische Node-Klasse für verkettete Datenstrukturen.
 * Jeder Knoten speichert einen Inhalt und eine Referenz auf den nächsten Knoten.
 *
 * @param <T> Der Typ des gespeicherten Inhalts
 */
public class Node<T> {
    private T content;
    private Node<T> next;

    /**
     * Erstellt einen neuen Knoten mit dem angegebenen Inhalt.
     *
     * @param content Der zu speichernde Inhalt
     */
    public Node(T content) {
        this.content = content;
        this.next = null;
    }

    /**
     * Gibt den Inhalt des Knotens zurück.
     *
     * @return Der gespeicherte Inhalt
     */
    public T getContent() {
        return content;
    }

    /**
     * Setzt den Inhalt des Knotens.
     *
     * @param content Der neue Inhalt
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * Gibt den nächsten Knoten zurück.
     *
     * @return Der nächste Knoten oder null, falls keiner existiert
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Setzt die Referenz auf den nächsten Knoten.
     *
     * @param next Der nächste Knoten
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }
}
