package at.downdrown.diary.frontend.i18n

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class TranslationProviderTest {

    private val translationProvider = TranslationProvider()

    @Test
    fun verifyAppLocales() {
        assertEquals(mutableListOf(Locale.GERMAN, Locale.ENGLISH), translationProvider.providedLocales)
    }

    @Test
    fun getTranslation_shouldResolveTranslation() {

        var resolvedTranslation = translationProvider.getTranslation("some.translation", Locale.ENGLISH)
        assertNotNull(resolvedTranslation, "The resolved translation should not be null")
        assertFalse(resolvedTranslation.isEmpty(), "The resolved translation should not be empty")
        assertEquals("some translation", resolvedTranslation, "The resolved translation should equal 'some translation'")

        resolvedTranslation = translationProvider.getTranslation("some.translation", Locale.GERMAN)
        assertNotNull(resolvedTranslation, "The resolved translation should not be null")
        assertFalse(resolvedTranslation.isEmpty(), "The resolved translation should not be empty")
        assertEquals("Eine Übersetzung", resolvedTranslation, "The resolved translation should equal 'some translation'")
    }
    @Test
    fun getTranslation_shouldResolveInterpolatedMessage() {
        val resolvedTranslation = translationProvider.getTranslation("some.interpolated.translation", Locale.ENGLISH, "World")
        assertNotNull(resolvedTranslation, "The resolved translation should not be null")
        assertFalse(resolvedTranslation.isEmpty(), "The resolved translation should not be empty")
        assertEquals("Hello World", resolvedTranslation, "The resolved translation should equal 'Hello World'")
    }

    @Test
    fun getTranslation_shouldResolveInterpolatedMessageWithChoice() {

        var resolvedTranslation = translationProvider.getTranslation("some.interpolated.translation.with.choice", Locale.ENGLISH, 0)
        assertNotNull(resolvedTranslation, "The resolved translation should not be null")
        assertFalse(resolvedTranslation.isEmpty(), "The resolved translation should not be empty")
        assertEquals("You have no new messages.", resolvedTranslation, "The resolved translation should equal 'You have no new messages.'")

        resolvedTranslation = translationProvider.getTranslation("some.interpolated.translation.with.choice", Locale.ENGLISH, 1)
        assertNotNull(resolvedTranslation, "The resolved translation should not be null")
        assertFalse(resolvedTranslation.isEmpty(), "The resolved translation should not be empty")
        assertEquals("You have one new message.", resolvedTranslation, "The resolved translation should equal 'You have one new message.'")

        resolvedTranslation = translationProvider.getTranslation("some.interpolated.translation.with.choice", Locale.ENGLISH, 2)
        assertNotNull(resolvedTranslation, "The resolved translation should not be null")
        assertFalse(resolvedTranslation.isEmpty(), "The resolved translation should not be empty")
        assertEquals("You have 2 new messages.", resolvedTranslation, "The resolved translation should equal 'You have 2 new messages.'")
    }

    @Test
    fun getTranslation_shouldResolveNonexistentTranslation() {
        val resolvedTranslation = translationProvider.getTranslation("some.nonexistent.translation", Locale.ENGLISH)
        assertNotNull(resolvedTranslation, "The resolved translation should not be null")
        assertFalse(resolvedTranslation.isEmpty(), "The resolved translation should not be empty")
        assertEquals("? some.nonexistent.translation ?", resolvedTranslation, "The resolved translation should equal '? some.nonexistent.translation ?'")
    }

    @Test
    fun getTranslation_shouldFailForInvalidArguments() {
        val exception = assertThrows(IllegalArgumentException::class.java) { translationProvider.getTranslation(null, null) }
        assertNotNull(exception)
        assertEquals("Required arguments missing to resolve translation", exception.message)
    }
}