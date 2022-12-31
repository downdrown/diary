package at.downdrown.diary.frontend.i18n

import com.vaadin.flow.i18n.I18NProvider
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.text.MessageFormat
import java.util.*

private val log = KotlinLogging.logger {}

@Service
class TranslationProvider : I18NProvider {

    private val bundlePrefix = "translations"
    override fun getProvidedLocales(): MutableList<Locale> {
        return mutableListOf(Locale.GERMAN, Locale.ENGLISH)
    }

    override fun getTranslation(key: String?, locale: Locale?, vararg params: Any?): String {
        if (key != null && locale != null) {

            val bundle = ResourceBundle.getBundle(bundlePrefix, locale)

            return try {

                val resolvedTranslation = bundle.getString(key)

                if (params.isEmpty()) {
                    resolvedTranslation
                } else {
                    MessageFormat.format(resolvedTranslation, *params)
                }
            } catch (e: MissingResourceException) {
                log.warn("Missing resource '{}' in translations for {}", key, locale)
                "? $key ?"
            }
        }

        throw IllegalArgumentException("Required arguments missing to resolve translation")
    }
}