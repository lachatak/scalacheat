package de.codecentric.wittig.scala.regex

import java.util.Calendar
import scala.util.matching.Regex

/**
 * @author gunther
 */
object RegexTester extends App {

  val date = """(\d\d\d\d)-(\d\d)-(\d\d)""".r
  val datePattern = new Regex("""(\d\d\d\d)-(\d\d)-(\d\d)""", "year", "month", "day")

  val s1 = "2004-01-20" match {
    case date(year, month, day) => s"$year was a good year for PLs."
  }
  println(s"s1: $s1")

  //In a pattern match, Regex normally matches the entire input. However, an unanchored Regex finds the pattern anywhere in the input.
  val embeddedDate = date.unanchored
  val dates = "Date: 2004-01-20 17:25:18 GMT (10 years, 28 weeks, 5 days, 17 hours and 51 minutes ago)"
  val s2 = dates match {
    case embeddedDate(year, "01", "20") => year
    case _ => "nicht gematched"
  }
  val s3 = date findFirstIn dates getOrElse "nicht gematched"
  println(s"s2: $s2")
  println(s"s3: $s3")
  val s4 = for (m <- datePattern findFirstMatchIn dates) yield m group "year"
  println(s"s4: $s4")

  // Find all:
  val s5 = for (m <- date findAllMatchIn dates) yield m group 1
  s5.map { x => "s5: " + x }.foreach { println }

  val dates2 = "Date: 2004-01-20 1950-01-20 1960-01-20 "
  val mi = date findAllIn dates2
  //  mi.toList.foreach { x => println("s6: " + x) }
  val s6 = mi filter (_ => (mi group 1).toInt < 1960) map (s => s"s6: $s")
  s6.foreach { println }

  // Replacement:
  val s7 = date replaceAllIn (dates, m => m group 1)
  println(s"s7: $s7")

  // Apply a Match to a different Pattern
  val docSpree = """2004(?:-\d{2}){2}""".r
  val docView = date replaceAllIn (dates, _ match {
    case docSpree() => "Historic doc spree!"
    case _ => "Something else happened"
  })

  println(docView)
}
