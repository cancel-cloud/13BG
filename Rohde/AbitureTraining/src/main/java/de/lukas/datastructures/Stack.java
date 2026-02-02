package de.lukas.datastructures;

/**
 * Generischer Stack (Stapel) implementiert als verkettete Liste.
 * Arbeitet nach dem LIFO-Prinzip (Last In, First Out).
 *
 * @param <T> Der Typ der gespeicherten Elemente
 */
public class Stack<T> {
    private Node<T> top;

    /**
     * Erstellt einen leeren Stapel.
     */
    public Stack() {
        this.top = null;
    }

    /**
     * Prüft, ob der Stapel leer ist.
     *
     * @return true, wenn der Stapel leer ist, sonst false
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Legt ein Element oben auf den Stapel.
     *
     * @param content Das einzufügende Element
     */
    public void push(T content) {
        Node<T> newNode = new Node<>(content);
        newNode.setNext(top);
        top = newNode;
    }

    /**
     * Entfernt das oberste Element vom Stapel und gibt es zurück.
     *
     * @return Das entfernte Element oder null, wenn der Stapel leer ist
     */
    public T pop() {
        if (isEmpty()) {
            return null;
        }

        T content = top.getContent();
        top = top.getNext();
        return content;
    }

    /**
     * Gibt das oberste Element des Stapels zurück, ohne es zu entfernen.
     *
     * @return Das oberste Element oder null, wenn der Stapel leer ist
     */
    public T top() {
        if (isEmpty()) {
            return null;
        }
        return top.getContent();
    }
}
