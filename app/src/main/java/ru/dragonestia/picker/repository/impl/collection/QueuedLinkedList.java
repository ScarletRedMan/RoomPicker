package ru.dragonestia.picker.repository.impl.collection;

import java.util.HashMap;
import java.util.Map;

public class QueuedLinkedList<ITEM extends QueuedLinkedList.Item> {

    private Node<ITEM> first;
    private Node<ITEM> last;
    private Node<ITEM> cursor;
    private final Map<String, Node<ITEM>> itemMap = new HashMap<>();

    public void add(ITEM item) {
        if (itemMap.containsKey(item.getId())) return;

        Node<ITEM> node = new Node<>(item);
        if (first == null) {
            first = node;
            last = first;
        } else {
            node.prev = last;
            last.next = node;
            last = node;
        }

        itemMap.put(item.getId(), node);
    }

    public void removeById(String id) {
        if (!itemMap.containsKey(id)) return;

        remove(itemMap.get(id).object);
    }

    public void remove(ITEM item) {
        if (!itemMap.containsKey(item.getId())) return;

        var node = itemMap.get(item.getId());
        itemMap.remove(item.getId());
        node.removed = true;

        if (node.prev == null) { // first element
            first = node.next;
            if (first != null) {
                first.prev = null;
            }
        }

        if (node.next == null) { // last element
            last = node.prev;
            if (last != null) {
                last.next = null;
            }
        }

        if (node.next != null && node.prev != null) { // middle element
            var prev = node.prev;
            var next = node.next;

            prev.next = next;
            next.prev = prev;
        }
    }

    public int size() {
        return itemMap.size();
    }

    public void resetCursor() {
        cursor = null;
    }

    public ITEM pick() {
        if (first == null) {
            throw new RuntimeException("List is empty");
        }

        if (cursor == null) cursor = first;

        Node<ITEM> item = cursor;
        while (item != null && item.removed) {
            item = item.next;
        }
        if (item == null) item = first;

        cursor = item.next;
        return item.object;
    }

    private static class Node<ITEM extends Item> {

        private final ITEM object;
        private Node<ITEM> prev, next;
        private boolean removed = false;

        public Node(ITEM object) {
            this.object = object;
        }

        @Override
        public String toString() {
            return "{Node id=%s, prev=%s, next=%s, removed=%s }".formatted(
                    object.getId(),
                    prev == null? null : prev.object.getId(),
                    next == null? null : next.object.getId(),
                    removed
            );
        }
    }

    public interface Item {

        String getId();
    }
}
