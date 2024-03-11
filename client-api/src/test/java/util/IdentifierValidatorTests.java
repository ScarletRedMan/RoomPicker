package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dragonestia.picker.api.util.IdentifierValidator;

import java.util.UUID;

public class IdentifierValidatorTests {

    @Test
    void test_forNodes() {
        Assertions.assertFalse(IdentifierValidator.forNode(""));
        Assertions.assertTrue(IdentifierValidator.forNode("a"));
        Assertions.assertTrue(IdentifierValidator.forNode("aboba123"));
        Assertions.assertFalse(IdentifierValidator.forNode("Aboba123"));
        Assertions.assertTrue(IdentifierValidator.forNode("node-identifier"));
        Assertions.assertFalse(IdentifierValidator.forNode("node identifier"));
        Assertions.assertFalse(IdentifierValidator.forNode("-"));
        Assertions.assertFalse(IdentifierValidator.forNode("-a"));
        Assertions.assertFalse(IdentifierValidator.forNode("a-"));
        Assertions.assertTrue(IdentifierValidator.forNode("a".repeat(32)));
        Assertions.assertFalse(IdentifierValidator.forNode("a".repeat(33)));
    }

    @Test
    void test_forRooms() {
        Assertions.assertFalse(IdentifierValidator.forRoom(""));
        Assertions.assertTrue(IdentifierValidator.forRoom("a"));
        Assertions.assertTrue(IdentifierValidator.forRoom("aboba123"));
        Assertions.assertFalse(IdentifierValidator.forRoom("Aboba123"));
        Assertions.assertTrue(IdentifierValidator.forRoom("node-identifier"));
        Assertions.assertFalse(IdentifierValidator.forRoom("node identifier"));
        Assertions.assertFalse(IdentifierValidator.forRoom("-"));
        Assertions.assertFalse(IdentifierValidator.forRoom("-a"));
        Assertions.assertFalse(IdentifierValidator.forRoom("a-"));
        Assertions.assertTrue(IdentifierValidator.forRoom("a".repeat(32)));
        Assertions.assertFalse(IdentifierValidator.forRoom("a".repeat(33)));
    }

    @Test
    void test_forUsers() {
        Assertions.assertFalse(IdentifierValidator.forUser(""));
        Assertions.assertTrue(IdentifierValidator.forUser("a"));
        Assertions.assertTrue(IdentifierValidator.forUser("a".repeat(64)));
        Assertions.assertFalse(IdentifierValidator.forUser("a".repeat(65)));
        Assertions.assertTrue(IdentifierValidator.forUser("aboba"));
        Assertions.assertTrue(IdentifierValidator.forUser("AboBa-AAA @aaa:aaa_aa;aa.aaa99"));
        Assertions.assertTrue(IdentifierValidator.forUser(UUID.randomUUID().toString()));
    }
}
