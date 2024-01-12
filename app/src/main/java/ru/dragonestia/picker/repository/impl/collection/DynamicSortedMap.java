package ru.dragonestia.picker.repository.impl.collection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DynamicSortedMap<ITEM extends DynamicSortedMap.Item> {

    private final TreeMap<Integer, LinkedList<Node<ITEM>>> tree = new TreeMap<>();
    private final HashMap<String, Node<ITEM>> indexes = new HashMap<>();

    public void put(ITEM item) {
        var node = new Node<>(item);

        indexes.put(node.object.getId(), node);
        var list = tree.getOrDefault(node.cachedScore, new LinkedList<>());
        if (list.isEmpty()) tree.put(node.cachedScore, list);
        list.add(node);
    }

    public void removeById(String id) {
        if (!indexes.containsKey(id)) return;

        remove(indexes.get(id).object);
    }

    public void remove(ITEM item) {
        if (!indexes.containsKey(item.getId())) return;

        var node = indexes.get(item.getId());
        node.removed = true;
        indexes.remove(item.getId());

        var list = tree.get(node.cachedScore);
        list.remove(node);
        if (list.isEmpty()) tree.remove(node.cachedScore);
    }

    public int size() {
        return indexes.size();
    }

    public ITEM getMinimum() {
        return getMinimum(0);
    }

    public ITEM getMinimum(int needAppendScore) {
        if (size() == 0) {
            throw new RuntimeException("Map is empty");
        }

        ITEM result = null;
        rootLoop:
        for (var index: tree.navigableKeySet()) {
            var list = tree.get(index);
            for (var node: list) {
                if (!node.object.canBeUsed(needAppendScore)) continue;

                result = node.object;
                break rootLoop;
            }
        }

        if (result == null) {
            throw new RuntimeException("Cant get available item");
        }
        return result;
    }

    public ITEM getMaximum() {
        if (size() == 0) {
            throw new RuntimeException("Map is empty");
        }

        ITEM result = null;
        rootLoop:
        for (var index: tree.descendingKeySet()) {
            var list = tree.get(index);
            for (var node: list) {
                if (!node.object.canBeUsed(0)) continue;

                result = node.object;
                break rootLoop;
            }
        }

        if (result == null) {
            throw new RuntimeException("Cant get available item");
        }
        return result;
    }

    public Set<Node<ITEM>> getItems() {
        var set = new LinkedHashSet<Node<ITEM>>();
        for (var index: tree.navigableKeySet()) {
            set.addAll(tree.get(index));
        }
        return set;
    }

    public Node<ITEM> getNode(String id) {
        return indexes.get(id);
    }

    public void updateItem(String id, Function<Integer, Integer> setter) {
        var node = indexes.get(id);

        var prevScore = node.cachedScore;
        node.object.updateScore(setter.apply(node.cachedScore));
        var newScore = node.cachedScore;

        var prevList = tree.get(prevScore);
        prevList.remove(node);
        if (prevList.isEmpty()) {
            tree.remove(prevScore);
        }

        var newList = tree.getOrDefault(newScore, new LinkedList<>());
        if (newList.isEmpty()) tree.put(newScore, newList);
        newList.add(node);
    }

    public static class Node<ITEM extends Item> {

        private final ITEM object;
        private int cachedScore;
        private boolean removed = false;

        private Node(ITEM object) {
            this.object = object;
            cachedScore = object.getScore();

            object.setOnUpdateScore(newScore -> {
                cachedScore = newScore;
            });
        }

        public int getScore() {
            return cachedScore;
        }

        @Override
        public String toString() {
            return "{Node id=%s, score=%s, removed=%s }".formatted(
                    object.getId(),
                    getScore(),
                    removed
            );
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof DynamicSortedMap.Node<?> node) {
                return object.getId().equals(node.object.getId());
            }
            return false;
        }
    }

    public interface Item {

        String getId();

        int getScore();

        void updateScore(int value);

        void setOnUpdateScore(Consumer<Integer> setter);

        default boolean canBeUsed(int units) {
            return true;
        }
    }
}
