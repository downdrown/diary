package at.downdrown.diary.frontend.conversion

import com.vaadin.flow.data.binder.Result
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.converter.Converter as VaadinConverter

class Converters {
    companion object {

        fun emptyStringToNullConverter(): VaadinConverter<String, String?> {
            return object : VaadinConverter<String, String?> {
                override fun convertToModel(value: String?, context: ValueContext?): Result<String?> {
                   return Result.ok(if (value.isNullOrEmpty()) null else value)
                }

                override fun convertToPresentation(value: String?, context: ValueContext?): String {
                    return value?.let { value } ?: ""
                }
            }
        }
    }
}
