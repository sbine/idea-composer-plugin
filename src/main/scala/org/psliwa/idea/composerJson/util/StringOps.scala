package org.psliwa.idea.composerJson.util

class StringOps(s: String) {
  def stripQuotes = if(s.headOption.contains('"')) stripMargins("\"") else stripMargins("'")

  private def stripMargins(margin: String) = s.stripPrefix(margin).stripSuffix(margin)
}