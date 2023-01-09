package at.downdrown.diary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["at.downdrown.diary"])
class DiaryApplication

fun main(args: Array<String>) {
    runApplication<DiaryApplication>(*args)
}
