package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dragonestia.picker.api.util.ValidateIdentifier;

import java.util.UUID;

public class ValidateIdentifierTests {

    @Test
    void test_forNodes() {
        Assertions.assertFalse(ValidateIdentifier.forNode(""));
        Assertions.assertTrue(ValidateIdentifier.forNode("a"));
        Assertions.assertTrue(ValidateIdentifier.forNode("aboba123"));
        Assertions.assertFalse(ValidateIdentifier.forNode("Aboba123"));
        Assertions.assertTrue(ValidateIdentifier.forNode("node-identifier"));
        Assertions.assertFalse(ValidateIdentifier.forNode("node identifier"));
        Assertions.assertFalse(ValidateIdentifier.forNode("-"));
        Assertions.assertFalse(ValidateIdentifier.forNode("-a"));
        Assertions.assertFalse(ValidateIdentifier.forNode("a-"));
        Assertions.assertTrue(ValidateIdentifier.forNode("a".repeat(32)));
        Assertions.assertFalse(ValidateIdentifier.forNode("a".repeat(33)));
    }

    @Test
    void test_forRooms() {
        Assertions.assertFalse(ValidateIdentifier.forRoom(""));
        Assertions.assertTrue(ValidateIdentifier.forRoom("a"));
        Assertions.assertTrue(ValidateIdentifier.forRoom("aboba123"));
        Assertions.assertFalse(ValidateIdentifier.forRoom("Aboba123"));
        Assertions.assertTrue(ValidateIdentifier.forRoom("node-identifier"));
        Assertions.assertFalse(ValidateIdentifier.forRoom("node identifier"));
        Assertions.assertFalse(ValidateIdentifier.forRoom("-"));
        Assertions.assertFalse(ValidateIdentifier.forRoom("-a"));
        Assertions.assertFalse(ValidateIdentifier.forRoom("a-"));
        Assertions.assertTrue(ValidateIdentifier.forRoom("a".repeat(32)));
        Assertions.assertFalse(ValidateIdentifier.forRoom("a".repeat(33)));
    }

    @Test
    void test_forUsers() {
        Assertions.assertFalse(ValidateIdentifier.forUser(""));
        Assertions.assertTrue(ValidateIdentifier.forUser("a"));
        Assertions.assertTrue(ValidateIdentifier.forUser("a".repeat(64)));
        Assertions.assertFalse(ValidateIdentifier.forUser("a".repeat(65)));
        Assertions.assertTrue(ValidateIdentifier.forUser("aboba"));
        Assertions.assertTrue(ValidateIdentifier.forUser("AboBa-AAA @aaa:aaa_aa;aa.aaa99"));
        Assertions.assertTrue(ValidateIdentifier.forUser(UUID.randomUUID().toString()));
    }
}
