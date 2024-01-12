package ru.dragonestia.picker.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dragonestia.picker.repository.impl.collection.DynamicSortedMap;

import java.util.function.Consumer;

public class DynamicSortedMapTests {

    @Test
    void testMap() {
        var map = new DynamicSortedMap<Item>();
        for (int i = 0; i < 10; i++) {
            int value = i;
            if ((i & 1) == 1) value = 10 - value;

            var item = new Item(Integer.toString(i), value);
            map.put(item);
        }

        printMap(map);

        Assertions.assertEquals(new Item("0", 0), map.getMinimum());
        Assertions.assertEquals(new Item("1", 9), map.getMaximum());

        var minId = map.getMinimum().id;
        var maxId = map.getMaximum().id;
        setFor(map, minId, 100);
        setFor(map, maxId, -100);

        printMap(map);

        Assertions.assertEquals(new Item("1", -100), map.getMinimum());
        Assertions.assertEquals(new Item("0", 100), map.getMaximum());
    }

    private void setFor(DynamicSortedMap<Item> map, String id, int score) {
        var before = map.getNode(id).toString();
        map.updateItem(id, (current) -> score);
        var after = map.getNode(id).toString();

        System.out.printf("Updated '%s' from %s to %s\n", id, before, after);
    }

    private void printMap(DynamicSortedMap<Item> map) {
        var list = map.getItems().stream().toList();
        var sb = new StringBuilder("Map(" + map.size() + "): ");
        for (int i = 0, n = map.size(); i < n; i++) {
            sb.append(list.get(i));
            if (i + 1 == n) sb.append(", ");
        }
        System.out.println(sb);
    }

    public static class Item implements DynamicSortedMap.Item {

        private final String id;
        private Consumer<Integer> setter = val -> {};
        private int score;

        public Item(String id, int score) {
            this.id = id;
            this.score = score;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public int getScore() {
            return score;
        }

        @Override
        public void updateScore(int value) {
            setter.accept(value);
            score = value;
        }

        @Override
        public void setOnUpdateScore(Consumer<Integer> setter) {
            this.setter = setter;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof Item other) {
                return id.equals(other.id) && score == other.score;
            }
            return false;
        }

        @Override
        public String toString() {
            return "{Item id='%s' score=%s}".formatted(id, score);
        }
    }
}
