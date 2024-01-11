package ru.dragonestia.picker.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dragonestia.picker.repository.impl.collection.QueuedLinkedList;

import java.util.HashSet;

public class QueuedLinkedListTests {

    @Test
    void testQueuedLinkedList() {
        var list = new QueuedLinkedList<Item>();
        for (int i = 0; i < 10; i++) {
            list.add(new Item(Integer.toString(i)));
        }

        printList(list);

        var removed = new HashSet<String>();
        for (int i = 5; i < 8; i++) {
            var id = Integer.toString(i);

            list.remove(new Item(id));
            System.out.println("Removed: " + id);
            removed.add(id);
        }

        printList(list);

        for (int i = 0; i < 20; i++) {
            var item = list.pick();

            System.out.println("Picked: " + item.getId());
            Assertions.assertFalse(removed.contains(item.getId()));
        }
    }

    @Test
    void testEmptyList() {
        var list = new QueuedLinkedList<Item>();
        for (int i = 0; i < 10; i++) {
            list.add(new Item(Integer.toString(i)));
        }

        printList(list);

        for (int i = 0; i < 10; i++) {
            var id = Integer.toString(i);

            list.remove(new Item(id));
            System.out.println("Removed: " + id);
        }

        printList(list);

        Assertions.assertThrows(RuntimeException.class, list::pick);
    }

    private void printList(QueuedLinkedList<Item> list) {
        list.resetCursor();

        var sb = new StringBuilder("List(" + list.size() + "): ");
        for (int i = 0, n = list.size(); i < n; i++) {
            sb.append(i);
            if (i + 1 != n) sb.append(", ");
        }
        System.out.println(sb);
    }

    public static class Item implements QueuedLinkedList.Item {

        private final String id;

        public Item(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
