package de.lukas.datastructures;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Stack Demo (LIFO) ===");
        demoStack();

        System.out.println("\n=== Queue Demo (FIFO) ===");
        demoQueue();
    }

    private static void demoStack() {
        Stack<String> stack = new Stack<>();

        System.out.println("Push: A, B, C");
        stack.push("A");
        stack.push("B");
        stack.push("C");

        System.out.println("Top: " + stack.top());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Pop: " + stack.pop());
        System.out.println("isEmpty: " + stack.isEmpty());
    }

    private static void demoQueue() {
        Queue<Integer> queue = new Queue<>();

        System.out.println("Enqueue: 1, 2, 3");
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        System.out.println("Front: " + queue.front());
        System.out.println("Dequeue: " + queue.dequeue());
        System.out.println("Dequeue: " + queue.dequeue());
        System.out.println("Dequeue: " + queue.dequeue());
        System.out.println("isEmpty: " + queue.isEmpty());
    }
}
