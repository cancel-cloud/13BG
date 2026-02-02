package de.lukas.datastructures;

/**
 * Generische Queue (Warteschlange) implementiert als verkettete Liste.
 * Arbeitet nach dem FIFO-Prinzip (First In, First Out).
 *
 * @param <T> Der Typ der gespeicherten Elemente
 */
public class Queue<T> {
    private Node<T> head;
    private Node<T> tail;

    /**
     * Erstellt eine leere Warteschlange.
     */
    public Queue() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Prüft, ob die Warteschlange leer ist.
     *
     * @return true, wenn die Warteschlange leer ist, sonst false
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Fügt ein Element am Ende der Warteschlange ein.
     *
     * @param content Das einzufügende Element
     */
    public void enqueue(T content) {
        Node<T> newNode = new Node<>(content);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    /**
     * Entfernt das erste Element der Warteschlange und gibt es zurück.
     *
     * @return Das entfernte Element oder null, wenn die Warteschlange leer ist
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T content = head.getContent();
        head = head.getNext();

        if (head == null) {
            tail = null;
        }

        return content;
    }

    /**
     * Gibt das erste Element der Warteschlange zurück, ohne es zu entfernen.
     *
     * @return Das erste Element oder null, wenn die Warteschlange leer ist
     */
    public T front() {
        if (isEmpty()) {
            return null;
        }
        return head.getContent();
    }
}
